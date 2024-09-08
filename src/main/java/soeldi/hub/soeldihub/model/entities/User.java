package soeldi.hub.soeldihub.model.entities;

import java.util.Optional;

public record User(
        Optional<Integer> id,
        String username,
        String password
) {

    public static User newUser(final String username, final String password) {
        return new User(Optional.empty(), username, password);
    }
}
