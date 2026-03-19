package domain.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "embeddings")
@EqualsAndHashCode(of = "id")
public class Embedding {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(name = "model_name", nullable = false, length = 100)
    private String modelName; //Campo para armazenar o nome do modelo de embedding utilizado, permitindo identificar qual modelo foi usado para gerar o embedding.

    @Lob //Usamos @Lob para indicar que o campo pode armazenar grandes quantidades de texto, adequado para armazenar o vetor de embedding em formato JSON.
    @Column(name = "vector_json", nullable = false)
    private String vectorJson; //Campo para armazenar o vetor de embedding em formato JSON, permitindo armazenar a representação vetorial do conteúdo de forma estruturada e fácil de acessar.

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true) //Indica que a relação One-to-One com a entidade Post deve ser gerenciada de forma cascata, ou seja, as operações de persistência (como salvar ou excluir) serão propagadas para o post associado. O uso de orphanRemoval = true garante que o post associado seja removido quando o embedding for excluído, mantendo a integridade dos dados.
    @JoinColumn(name = "post_id", nullable = false, unique = true) //Especifica a coluna de junção para a relação Many-to-One com a entidade Post, garantindo que cada embedding esteja associado a um post específico.
    private Post post; //Relacionamento Many-to-One com a entidade Post, permitindo associar o embedding a um post específico, facilitando a recuperação do embedding para um post quando necessário.

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;


}
