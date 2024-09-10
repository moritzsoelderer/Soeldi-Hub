package soeldi.hub.soeldihub.model;

import soeldi.hub.soeldihub.SoeldiHubApplication;
import soeldi.hub.soeldihub.model.entities.Flow;
import soeldi.hub.soeldihub.model.entities.Like;
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

    public static Optional<Flow> mapToFlow(final ResultSet resultSet, final Optional<Integer> numberOflikes) {
        try{
            return Optional.of(
                    new Flow(
                            Optional.of(resultSet.getInt("id")),
                            resultSet.getString("title"),
                            Optional.of(resultSet.getTimestamp("uploaded_at").toInstant()),
                            resultSet.getInt("uploaded_by"),
                            SoeldiHubApplication.class.getResource(
                                    RELATIVE_PATH_TO_CONTENT + "flows/" + resultSet.getString("source")
                            ),
                            numberOflikes
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
                                    ),
                            Optional.of(resultSet.getInt("count"))
                    )
            );
        } catch (SQLException e) {
            return Optional.empty();
        }
    }

    public static Optional<Like> mapToLike(final ResultSet resultSet) {
        try{
            return Optional.of(
                    new Like(
                            Optional.of(resultSet.getInt("id")),
                            resultSet.getInt("user_id"),
                            resultSet.getInt("flow_id"),
                            Optional.of(resultSet.getTimestamp("created_at").toInstant())
                    )
            );
        } catch (SQLException e) {
            return Optional.empty();
        }
    }

    public static Optional<Integer> mapToCount(final ResultSet resultSet) {
        try{
            return Optional.of(resultSet.getInt("count"));
        } catch (SQLException e) {
            return Optional.empty();
        }
    }
}
