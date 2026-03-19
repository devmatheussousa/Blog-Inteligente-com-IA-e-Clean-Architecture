package domain.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "posts")
@EqualsAndHashCode(of = "id")
public class Post {

    public enum PostStatus {
        DRAFT,
        PUBLISHED,
        ARCHIVED
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Size(max = 180)
    @Column(nullable = false, length = 180)
    private String title;

    @NotBlank
    @Size(max = 200)
    @Column(nullable = false, unique = true, length = 200)
    private String slug; //Campo para armazenar uma versão amigável do título, geralmente usada em URLs.

    @Size(max = 400)
    @Column(length = 400)
    private String excerpt; //Campo para armazenar um resumo ou trecho do conteúdo do post, permitindo exibir uma prévia do conteúdo em listagens ou resumos.

    @Lob  //Usamos @Lob para indicar que o campo pode armazenar grandes quantidades de texto, adequado para armazenar o conteúdo completo do post.
    @Column(nullable = false)
    private String content; //Campo para armazenar o conteúdo completo do post, permitindo que seja detalhado e formatado conforme necessário.

    @Builder.Default
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private PostStatus status = PostStatus.DRAFT; //Campo para armazenar o status do post, permitindo controlar se o post está em rascunho, publicado ou arquivado.

    @Column(name = "published_at")
    private LocalDateTime publishedAt; //Campo para armazenar a data e hora de publicação do post, permitindo rastrear quando o post foi publicado.

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id", nullable = false) //Especifica a coluna de junção para a relação Many-to-One com a entidade User, garantindo que cada post esteja associado a um autor específico.
    private User author; //Campo para armazenar a referência ao autor do post, permitindo associar o post a um usuário específico e facilitar a exibição de informações do autor.

    @Builder.Default
    @ManyToMany
    @JoinTable(
            name = "post_categories",
            joinColumns = @JoinColumn(name = "post_id"),
            inverseJoinColumns = @JoinColumn(name = "category_id")
    )
    private Set<Category> categories = new HashSet<>();

    @Builder.Default
    @ManyToMany
    @JoinTable(
            name = "post_tags",
            joinColumns = @JoinColumn(name = "post_id"),
            inverseJoinColumns = @JoinColumn(name = "tag_id")
    )
    private Set<Tag> tags = new HashSet<>();

    @Builder.Default
    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true) //Indica que a relação One-to-Many com a entidade Comment é mapeada pela propriedade "post" na entidade Comment, permitindo que um post possa ter múltiplos comentários. O uso de cascade = CascadeType.ALL garante que as operações de persistência sejam propagadas para os comentários associados, e orphanRemoval = true garante que os comentários sejam removidos quando o post for excluído, mantendo a integridade dos dados.
    private List<Comment> comments = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true) //Indica que a relação One-to-Many com a entidade PostRevision é mapeada pela propriedade "post" na entidade PostRevision, permitindo que um post possa ter múltiplas revisões. O uso de cascade = CascadeType.ALL garante que as operações de persistência sejam propagadas para as revisões associadas, e orphanRemoval = true garante que as revisões sejam removidas quando o post for excluído, mantendo a integridade dos dados.)
    private List<PostRevision> revisions = new ArrayList<>();

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    private Embedding embedding;

    @Builder.Default
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ViewEvent> viewEvents = new ArrayList<>();

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt; //Campo para armazenar a data e hora de criação do post, permitindo rastrear quando o post foi criado.

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}
