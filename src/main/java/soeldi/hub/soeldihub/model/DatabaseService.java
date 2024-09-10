package soeldi.hub.soeldihub.model;

import soeldi.hub.soeldihub.model.entities.Flow;
import soeldi.hub.soeldihub.model.entities.Like;
import soeldi.hub.soeldihub.model.entities.User;

import java.util.List;
import java.util.Optional;

public class DatabaseService {

    private static DatabaseService instance;
    private static DatabaseRepository repository;


    private DatabaseService(){
    }

    public static DatabaseService getInstance(){
        if(instance == null){
            instance = new DatabaseService();
            repository = DatabaseRepository.getInstance();
            return instance;
        }
        return instance;
    }

    public Optional<User> findUser(final String username, final String password) {
            return Optional.of(username)
                    .flatMap(user -> repository.fetchUser(user, password));
    }

    public Optional<Boolean> createUser(final User user) {
        return Optional.of(user)
                .filter(u -> findUser(u.username(), u.password()).isEmpty())
                .flatMap(u -> repository.insertUser(u.username(), u.password()))
                .map(i -> i == 1);
    }

    public Optional<Flow> findFlow(final int id) {
        return Optional.of(id)
                .flatMap(repository::fetchFlow);
    }

    public Optional<List<Optional<Flow>>> findLatestFlows() {
        return repository.fetchLatestFlows();
    }

    public Optional<Like> findLike(final int id) {
        return repository.fetchLike(id);
    }

}
