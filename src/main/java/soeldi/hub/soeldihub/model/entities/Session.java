package soeldi.hub.soeldihub.model.entities;

import java.time.Instant;
import java.util.Optional;

public class Session {
    private final String username;
    private final Instant sessionBegin;
    private final Optional<Instant> sessionEnd;

    private static Session instance;

    private Session(final String username) {
        this.username = username;
        this.sessionBegin = Instant.now();
        this.sessionEnd = Optional.empty();
    }

    public static Session initialize(final String username) throws IllegalStateException{
        if(instance == null) {
            instance = new Session(username);
            return instance;
        }
        else if(!instance.username.equals(username)){
            throw new IllegalStateException("Only one session for one user is allowed");
        }
        else {
            return instance;
        }
    }

    public static Optional<Session> getInstance() {
        return Optional.ofNullable(instance);
    }

    public String getUsername() {
        return this.username;
    }
}
