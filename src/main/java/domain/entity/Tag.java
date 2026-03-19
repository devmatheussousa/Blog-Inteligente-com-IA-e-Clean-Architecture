package domain.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "tags")
@EqualsAndHashCode(of = "id")
public class Tag {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Size(max = 100)
    @Column(nullable = false, unique = true, length = 100)
    private String name; //Campo para armazenar o nome da tag, permitindo categorizar os posts de forma flexível e facilitar a busca por temas específicos.

    @NotBlank
    @Size(max = 120)
    @Column(nullable = false, unique = true, length = 120)
    private String slug; //Campo para armazenar uma versão amigável do nome da tag, geralmente usada em URLs, permitindo que as tags sejam referenciadas de forma mais legível e SEO-friendly.

    @Builder.Default
    @ManyToMany(mappedBy = "tags") //Indica que a relação Many-to-Many é mapeada pela coleção "tags" na entidade Post, estabelecendo a conexão entre as duas entidades.)
    private Set<Post> posts = new HashSet<>();

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt; //Campo para armazenar a data e hora de criação da tag, permitindo rastrear quando cada tag foi criada e facilitar a organização temporal das tags.

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}
