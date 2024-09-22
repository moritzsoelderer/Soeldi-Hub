package soeldi.hub.soeldihub.model.entities;

import java.net.URL;
import java.time.Instant;
import java.util.Optional;

public record Flow(
        Optional<Integer> id,
        String title,
        Optional<Instant> uploadedAt,
        int uploadedBy,
        String caption,
        URL source,
        Optional<Integer> likes,
        Optional<Integer> comments
) {
    public static Flow newFlow(final String title, final int uploadedById, final URL source){
        return new Flow(
                Optional.empty(),
                title,
                Optional.empty(),
                uploadedById,
                "",
                source,
                Optional.empty(),
                Optional.empty()
        );
    }

    public static Flow newFlow(final String title, final int uploadedById, final URL source, final String caption){
        return new Flow(
                Optional.empty(),
                title,
                Optional.empty(),
                uploadedById,
                caption,
                source,
                Optional.empty(),
                Optional.empty()
        );
    }
}
