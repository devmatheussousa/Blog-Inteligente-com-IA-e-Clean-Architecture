package application.usecase.post;

import domain.entity.Post;
import domain.entity.PostRevision;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneOffset;

public class UpdatePostUseCaseTest {
    void shouldCreateAuditRevisionBeforeUpdatingPost() {
        String fixedNow = String.valueOf(Instant.parse("2026-01-15T10:00:00Z"));
        Clock fixedClock = Clock.fixed(Instant.parse(fixedNow), ZoneOffset.UTC);

        Post post = new Post(
                "post-123",
                "Título antigo",
                "Conteúdo antigo",
                "autor@blog.com",
                Instant.parse("2026-01-10T09:00:00Z")
        );

        UpdatePostUseCase useCase = new UpdatePostUseCase(fixedClock);

        PostRevision revision = useCase.update(
                post,
                "Título novo",
                "Conteúdo novo",
                "editor@blog.com",
                "Ajuste de SEO"
        );

        assertEquals("Título antigo", revision.getPrevisousTitle());
        assertEquals("Conteúdo antigo", revision.getPreviousContent());
        assertEquals("editor@blog.com", revision.getChangedBy());
        assertEquals("Ajuste de SEO", revision.getChangeReason());
        assertEquals(fixedNow, String.valueOf(revision.getChangedAt()));

        assertEquals("Título novo", post.getTitle());
        assertEquals("Conteúdo novo", post.getContent());
        assertEquals("editor@blog.com", post.getLastModifiedBy());
        assertEquals(fixedNow, post.getUpdatedAt());
    }

    private void assertEquals(String títuloAntigo, String previsousTitle) {
    }

}
