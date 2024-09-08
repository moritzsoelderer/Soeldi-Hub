package soeldi.hub.soeldihub.model;

import soeldi.hub.soeldihub.SoeldiHubApplication;
import soeldi.hub.soeldihub.model.entities.Flow;
import soeldi.hub.soeldihub.model.entities.User;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

public class DatabaseMapper {

    private static final String RELATIVE_PATH_TO_CONTENT = "content/";

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
            return Optional.empty();
        }
    }

    public static Optional<Flow> mapToFlow(final ResultSet resultSet) {
        try{
            return Optional.of(
                    new Flow(
                            Optional.of(resultSet.getInt("id")),
                            resultSet.getString("title"),
                            Optional.of(resultSet.getTimestamp("uploaded_at").toInstant()),
                            resultSet.getInt("uploaded_by"),
                            SoeldiHubApplication.class.getResource(
                                    RELATIVE_PATH_TO_CONTENT + "flows/" + resultSet.getString("source")
                            )
                    )
            );
        } catch (SQLException e) {
            return Optional.empty();
        }
    }
}
