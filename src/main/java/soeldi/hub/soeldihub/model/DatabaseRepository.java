package soeldi.hub.soeldihub.model;

import soeldi.hub.soeldihub.model.entities.Comment;
import soeldi.hub.soeldihub.model.entities.Flow;
import soeldi.hub.soeldihub.model.entities.Like;
import soeldi.hub.soeldihub.model.entities.User;

import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.logging.Level;
import java.util.logging.Logger;

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

    public Optional<User> fetchUser(final int id) {
        return fetchById("user", id, DatabaseMapper::mapToUser);
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
        final Optional<Integer> likes = fetchCountLikes(id);
        return fetchById("flow", id, resultSet -> DatabaseMapper.mapToFlow(resultSet, likes));
    }

    /**
    Returns the latest 25 flows
     **/
    public Optional<List<Optional<Flow>>> fetchLatestFlows() {
        final String flowQuery = "SELECT * FROM flow LEFT JOIN (SELECT flow_id, COUNT(*) AS count FROM user_likes_flow GROUP BY flow_id) AS likes ON likes.flow_id = flow.id ORDER BY flow.uploaded_at DESC LIMIT 25" ;
        try(final PreparedStatement pstmt = DriverManager.getConnection(databaseUrl, databaseUser, databasePassword)
                .prepareStatement(flowQuery)) {

            return Optional.of(pstmt.executeQuery())
                    .map(resultSet -> whileHasNextDo(resultSet, DatabaseMapper::mapToFlow));
        }
        catch (SQLException e) {
            return Optional.empty();
        }
    }

    public Optional<Like> fetchLike(final int id) {
        return fetchById("user_likes_flow", id, DatabaseMapper::mapToLike);
    }

    public Optional<Like> fetchLike(final int userId, final int flowId) {
        final String userQuery = "SELECT * FROM user_likes_flow WHERE user_id = ? AND flow_id = ?;";
        try(final PreparedStatement pstmt = DriverManager.getConnection(databaseUrl, databaseUser, databasePassword).prepareStatement(userQuery)) {
            pstmt.setInt(1, userId);
            pstmt.setInt(2, flowId);

            return Optional.of(pstmt.executeQuery())
                    .filter(DatabaseRepository::nextOrFalse)
                    .flatMap(DatabaseMapper::mapToLike);
        }
        catch (SQLException e) {
            return Optional.empty();
        }
    }

    public Optional<Integer> fetchCountLikes(final int flowId) {
        final String flowLikeQuery = "SELECT COUNT(*) AS count FROM user_likes_flow WHERE flow_id = ?" ;
        try(final PreparedStatement pstmt = DriverManager.getConnection(databaseUrl, databaseUser, databasePassword)
                .prepareStatement(flowLikeQuery)) {
            pstmt.setInt(1, flowId);

            return Optional.of(pstmt.executeQuery())
                    .filter(DatabaseRepository::nextOrFalse)
                    .flatMap(DatabaseMapper::mapToCount);
        }
        catch (SQLException e) {
            return Optional.empty();
        }
    }

    public Optional<Integer> insertLike(final int userId, final int flowId) {
        final String userQuery = "INSERT INTO user_likes_flow (user_id, flow_id) VALUES(?,?);";
        try(final PreparedStatement pstmt = DriverManager.getConnection(databaseUrl, databaseUser, databasePassword).prepareStatement(userQuery)){
            pstmt.setInt(1, userId);
            pstmt.setInt(2, flowId);

            return Optional.of(pstmt.executeUpdate());
        } catch (SQLException e) {
            return Optional.empty();
        }
    }

    public Optional<Comment> fetchComment(final int id) {
        return Optional.of(id)
                .flatMap(i -> fetchById("COMMENT", i, DatabaseMapper::mapToComment));
    }

    public Optional<Comment> fetchCommentByFlowId(final int flowId) {
        final String query = "SELECT * FROM comment AS t WHERE t.flow_id = ?" ;
        try(final PreparedStatement pstmt = DriverManager.getConnection(databaseUrl, databaseUser, databasePassword)
                .prepareStatement(query)) {
            pstmt.setInt(1, flowId);

            return Optional.of(pstmt.executeQuery())
                    .filter(DatabaseRepository::nextOrFalse)
                    .flatMap(DatabaseMapper::mapToComment);
        }
        catch (SQLException e) {
            Logger.getAnonymousLogger().log(Level.SEVERE, e.getMessage());
            return Optional.empty();
        }
    }

    public Optional<List<Optional<Comment>>> fetchLatestCommentsByFlowId(final int flowId, final int limit) {
        final String query = "SELECT * FROM comment AS t WHERE t.flow_id = ? ORDER BY created_at DESC LIMIT ?" ;
        try(final PreparedStatement pstmt = DriverManager.getConnection(databaseUrl, databaseUser, databasePassword)
                .prepareStatement(query)) {
            pstmt.setInt(1, flowId);
            pstmt.setInt(2, limit);

            return Optional.of(pstmt.executeQuery())
                    .map(resultSet -> whileHasNextDo(resultSet, DatabaseMapper::mapToComment));
        }
        catch (SQLException e) {
            Logger.getAnonymousLogger().log(Level.SEVERE, e.getMessage());
            return Optional.empty();
        }
    }

    private <T> Optional<T> fetchById(final String tableName, final int id, final Function<ResultSet, Optional<T>> mappingFunction) {
        final String query = "SELECT * FROM " + tableName + " AS t WHERE t.id = ?" ;
        try(final PreparedStatement pstmt = DriverManager.getConnection(databaseUrl, databaseUser, databasePassword)
                .prepareStatement(query)) {
            pstmt.setInt(1, id);

            return Optional.of(pstmt.executeQuery())
                    .filter(DatabaseRepository::nextOrFalse)
                    .flatMap(mappingFunction);
        }
        catch (SQLException e) {
            Logger.getAnonymousLogger().log(Level.SEVERE, e.getMessage());
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

    private static <T> List<T> whileHasNextDo(final ResultSet resultSet, final Function<ResultSet, T> toDo) {
        final List<T> list = new ArrayList<>();
        while(nextOrFalse(resultSet)){
            list.add(toDo.apply(resultSet));
        }
        return list;
    }
}
