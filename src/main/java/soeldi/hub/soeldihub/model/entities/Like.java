package soeldi.hub.soeldihub.model.entities;

import java.time.Instant;
import java.util.Optional;

public record Like(
        Optional<Integer> id,
        int userId,
        int flowId,
        Optional<Instant> createdAt
) {

    public static Like newLike(final int userId, final int flowId) {
        return new Like(
                Optional.empty(),
                userId,
                flowId,
                Optional.empty()
        );
    }
}
