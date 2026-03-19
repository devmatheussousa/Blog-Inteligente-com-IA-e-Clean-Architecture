package domain.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.Instant;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "post_revisions")
@EqualsAndHashCode(of = "id")
public class PostRevision {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "version_number", nullable = false)
    private Integer versionNumber; //Campo para armazenar o número da versão da revisão, permitindo rastrear a sequência de revisões feitas em um post.

    @NotBlank
    @Column(nullable = false, length = 180)
    private String title; //Campo para armazenar o título do post na revisão, permitindo que cada revisão capture o estado do título em um determinado momento.

    @Lob
    @Column(nullable = false)
    private String content;

    @Column(length = 500)
    private String summary; //Campo para armazenar um resumo ou descrição da revisão, permitindo que os usuários entendam rapidamente as mudanças feitas em cada revisão.

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id", nullable = false) //Especifica a coluna de junção para a relação Many-to-One com a entidade Post, garantindo que cada revisão esteja associada a um post específico.
    private Post post; //Relacionamento Many-to-One com a entidade Post, permitindo associar cada revisão a um post específico, facilitando a recuperação das revisões para um post quando necessário.

    @ManyToOne(fetch = FetchType.LAZY) //Indica que a relação Many-to-One com a entidade User deve ser carregada de forma preguiçosa, ou seja, os dados do usuário só serão carregados quando forem acessados, melhorando o desempenho.
    @JoinColumn(name = "revised_by_user_id", nullable = false) //Especifica a coluna de junção para a relação Many-to-One com a entidade User, garantindo que cada revisão esteja associada a um usuário específico que realizou a revisão.
    private User revisedBy; //Campo para armazenar a referência ao usuário que fez a revisão, permitindo rastrear quem realizou cada revisão e atribuir crédito ou responsabilidade pelas mudanças feitas.

    @CreationTimestamp
    private LocalDateTime createdAt; //Campo para armazenar a data e hora de criação da revisão, permitindo rastrear quando cada revisão foi feita.
}
