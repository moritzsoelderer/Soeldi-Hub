package soeldi.hub.soeldihub.model.entities;

import java.time.Instant;
import java.util.Optional;

public record Comment(
        Optional<Integer> id,
        int commentedBy,
        int flowId,
        String text,
        Optional<Instant> createdAt

) {
    public static Comment newComment(final int commentedBy, final int flowId, final String text){
        return new Comment(
                Optional.empty(),
                commentedBy,
                flowId,
                text,
                Optional.empty()
        );
    }
}
