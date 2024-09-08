package soeldi.hub.soeldihub.model;

import soeldi.hub.soeldihub.model.entities.Flow;
import soeldi.hub.soeldihub.model.entities.User;

import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

/**
 * Database Repository for db
 */

public class DatabaseRepository {

    private static DatabaseRepository instance;
    private static final String databaseUser = "my_user";
    private static final String databasePassword = "my_password";
    private static final String databaseName = "soeldihub";
    private static final int databasePort = 3306;
    private static final String databaseUrl = "jdbc:mysql://localhost:" + databasePort + "/" + databaseName;

    private DatabaseRepository() {

    }

    public static DatabaseRepository getInstance(){
        if(instance == null){
            instance = new DatabaseRepository();
            return instance;
        }
        return instance;
    }

    public Optional<User> fetchUser(final String username, final String password) {
        final String userQuery = "SELECT * FROM user WHERE username = ? AND password = ?;";
        try(final PreparedStatement pstmt = DriverManager.getConnection(databaseUrl, databaseUser, databasePassword).prepareStatement(userQuery)) {
            pstmt.setString(1, username);
            pstmt.setString(2, password);

            return Optional.of(pstmt.executeQuery())
                    .filter(DatabaseRepository::nextOrFalse)
                    .flatMap(DatabaseMapper::mapToUser);
        }
        catch (SQLException e) {
            return Optional.empty();
        }
    }

    public Optional<Integer> insertUser(final String username, final String password) {
        final String userQuery = "INSERT INTO user (username, password) VALUES(?,?);";
        try(final PreparedStatement pstmt = DriverManager.getConnection(databaseUrl, databaseUser, databasePassword).prepareStatement(userQuery)){
            pstmt.setString(1, username);
            pstmt.setString(2, password);
            return Optional.of(pstmt.executeUpdate());
        } catch (SQLException e) {
            return Optional.empty();
        }
    }

    public Optional<Flow> fetchFlow(final int id) {
        final String flowQuery = "SELECT * FROM flow WHERE id = ?";
        try(final PreparedStatement pstmt = DriverManager.getConnection(databaseUrl, databaseUser, databasePassword)
                .prepareStatement(flowQuery)) {
            pstmt.setInt(1, id);

            return Optional.of(pstmt.executeQuery())
                    .filter(DatabaseRepository::nextOrFalse)
                    .flatMap(DatabaseMapper::mapToFlow);
        }
        catch (SQLException e) {
            return Optional.empty();
        }
    }

    /**
    Returns the latest 25 flows
     **/
    public Optional<List<Optional<Flow>>> fetchLatestFlows() {
        final String flowQuery = "SELECT * FROM flow ORDER BY flow.uploaded_at DESC LIMIT 25" ;
        try(final PreparedStatement pstmt = DriverManager.getConnection(databaseUrl, databaseUser, databasePassword)
                .prepareStatement(flowQuery)) {

            return Optional.of(pstmt.executeQuery())
                    .map(resultSet -> whileHasNextDo(resultSet, DatabaseMapper::mapToFlow));
        }
        catch (SQLException e) {
            return Optional.empty();
        }
    }

    private static boolean nextOrFalse(final ResultSet resultSet) {
        try{
            return resultSet.next();
        }
        catch (Exception e) {
            return false;
        }
    }

    private static <T> List<T> whileHasNextDo(final ResultSet resultSet, Function<ResultSet, T> toDo) {
        final List<T> list = new ArrayList<>();
        while(nextOrFalse(resultSet)){
            list.add(toDo.apply(resultSet));
        }
        return list;
    }
}
