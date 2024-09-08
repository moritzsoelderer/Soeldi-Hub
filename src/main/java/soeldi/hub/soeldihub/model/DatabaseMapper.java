package soeldi.hub.soeldihub.model;

import soeldi.hub.soeldihub.model.entities.User;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DatabaseMapper {

    private DatabaseMapper(){

    }

    public static Optional<User> mapToUser(final ResultSet resultSet) {
        try{
            return Optional.of(
                    new User(
                            Optional.of(resultSet.getInt("id")),
                            resultSet.getString("username"),
                            resultSet.getString("password")
                    )
            );
        } catch (SQLException e) {
            Logger.getAnonymousLogger().log(Level.SEVERE, "wth");
            return Optional.empty();
        }
    }
}
