package soeldi.hub.soeldihub.model;

import soeldi.hub.soeldihub.model.entities.User;

import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

public class DatabaseRepository {

    private static DatabaseRepository instance;
    private final static String databaseUser = "my_user";
    private final static String databasePassword = "my_password";
    private final static String databaseName = "soeldihub";
    private final static int databasePort = 3306;
    private final static String databaseUrl = "jdbc:mysql://localhost:" + databasePort + "/" + databaseName;

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

    private static boolean nextOrFalse(final ResultSet resultSet) {
        try{
            return resultSet.next();
        }
        catch (Exception e) {
            return false;
        }
    }
}
