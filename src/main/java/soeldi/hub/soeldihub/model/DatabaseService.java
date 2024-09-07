package soeldi.hub.soeldihub.model;

import soeldi.hub.soeldihub.model.entities.User;

import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

public class DatabaseService {

    private static DatabaseService instance;

    private final String databaseUser = "my_user";
    private final String databasePassword = "my_password";
    private final String databaseName = "soeldihub";
    private final int databasePort = 3306;
    private final String databaseUrl = "jdbc:mysql://localhost:" + databasePort + "/" + databaseName;

    private DatabaseService() throws SQLException {
    }

    public static Optional<DatabaseService> getInstance(){
        if(instance == null){
            try{
                instance = new DatabaseService();
                return Optional.of(instance);
            }
            catch (SQLException e) {
                return Optional.empty();
            }
        }
        return Optional.of(instance);
    }

    public Optional<User> findUser(final String username, final String password) {
        final String userQuery = "SELECT * FROM user WHERE username = ? AND password = ?;";
        try(final PreparedStatement pstmt = DriverManager.getConnection(databaseUrl, databaseUser, databasePassword).prepareStatement(userQuery)){
            pstmt.setString(1, username);
            pstmt.setString(2, password);
            final ResultSet resultSet = pstmt.executeQuery();
            if(resultSet.next()){
                final User foundUser = new User(resultSet.getString("username"), resultSet.getString("password"));
                return Optional.of(foundUser);
            }
        } catch (SQLException e) {
            return Optional.empty();
        }
        return Optional.empty();
    }

    public Optional<Boolean> createUser(final String username, final String password) {
        if(this.findUser(username, password).isPresent()) {
            return Optional.of(false);
        }
        final String userQuery = "INSERT INTO user (username, password) VALUES(?,?);";
        try(final PreparedStatement pstmt = DriverManager.getConnection(databaseUrl, databaseUser, databasePassword).prepareStatement(userQuery)){
            pstmt.setString(1, username);
            pstmt.setString(2, password);
            final int affectedRows = pstmt.executeUpdate();
            return Optional.of(affectedRows == 1);
        } catch (SQLException e) {
            return Optional.empty();
        }
    }

}
