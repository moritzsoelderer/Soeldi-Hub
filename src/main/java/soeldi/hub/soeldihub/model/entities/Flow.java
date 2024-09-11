package soeldi.hub.soeldihub.model.entities;

import java.net.URL;
import java.time.Instant;
import java.util.Optional;

public record Flow(
        Optional<Integer> id,
        String title,
        Optional<Instant> uploadedAt,
        int uploadedBy,
        URL source,
        Optional<Integer> likes
) {
    public static Flow newFlow(final String title, final int uploadedById, final URL source){
        return new Flow(
                Optional.empty(),
                title,
                Optional.empty(),
                uploadedById,
                source,
                Optional.empty()
        );
    }
}
