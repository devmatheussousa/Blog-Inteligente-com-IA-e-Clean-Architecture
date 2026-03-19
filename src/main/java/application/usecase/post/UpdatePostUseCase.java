package application.usecase.post;

import domain.entity.Post;
import domain.entity.PostRevision;
import lombok.RequiredArgsConstructor;

import java.time.Clock;
import java.time.Instant;
import java.util.Objects;

@RequiredArgsConstructor
public class UpdatePostUseCase {

    private final Clock clock; //serve para obter a data e hora atual, útil para definir o updatedAt do post

    public PostRevision update(Post post, String newTitle, String newContent, String actor, String reason){
        Objects.requireNonNull(post, "Post cannot be null");

        Instant now = clock.instant(); //obtem a data e hora atual usando o clock
        PostRevision revision = new PostRevision(
                post.getId(), //id do post que está sendo revisado
                post.getTitle(), //título anterior do post
                post.getContent(), //conteúdo anterior do post
               actor, //nome do usuário que fez a atualização
                reason, //motivo da atualização
                now //data e hora da revisão
        ); //cria uma nova revisão do post

        post.applyUpdate(newTitle, newContent, actor, String.valueOf(now));
        return revision;
    }
}
