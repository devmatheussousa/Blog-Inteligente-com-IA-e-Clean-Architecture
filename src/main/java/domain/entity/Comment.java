package domain.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "comments")
@EqualsAndHashCode(of = "id")
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Size(max = 2000)
    @Column(name = "author_name", nullable = false, length = 2000)
    private String content;

    @NotBlank
    @Size(max = 120)
    @Column(name = "author_name", nullable = false, length = 120)
    private String authorName;

    @Email
    @Size(max = 255)
    @Column(name = "author_email", length = 255)
    private String authorEmail;

    @Builder.Default //Usamos @Builder.Default para garantir que o valor padrão seja aplicado mesmo quando o objeto for criado usando o builder, evitando que o campo seja nulo.
    @Column(nullable = false)
    private Boolean approved = Boolean.FALSE; //Campo para indicar se o comentário foi aprovado, permitindo controle sobre a moderação dos comentários.

    @ManyToOne(fetch = FetchType.LAZY) //Indica que a relação Many-to-One com a entidade Post deve ser carregada de forma preguiçosa, ou seja, os dados do post só serão carregados quando forem acessados, melhorando o desempenho.
    @JoinColumn(name = "post_id", nullable = false) //Especifica a coluna de junção para a relação Many-to-One, garantindo que cada comentário esteja associado a um post específico.
    private Post post;

    @ManyToOne(fetch = FetchType.LAZY) //Indica que a relação Many-to-One com o próprio Comment deve ser carregada de forma preguiçosa, permitindo que um comentário possa ter um comentário pai, facilitando a criação de threads de comentários.
    @JoinColumn(name = "parent_id") //Especifica a coluna de junção para a relação Many-to-One com o próprio Comment, permitindo que um comentário possa ter um comentário pai, facilitando a criação de threads de comentários.
    private Comment parent; //Campo para armazenar a referência ao comentário pai, permitindo a criação de threads de comentários e respostas aninhadas.

    @Builder.Default
    @OneToMany(mappedBy = "parent", cascade = CascadeType.ALL, orphanRemoval = true) //Indica que a relação One-to-Many com o próprio Comment é mapeada pela propriedade "parent", permitindo que um comentário possa ter múltiplos comentários filhos (respostas). O uso de cascade = CascadeType.ALL garante que as operações de persistência sejam propagadas para os comentários filhos, e orphanRemoval = true garante que os comentários filhos sejam removidos quando o comentário pai for excluído.)
    private List<Comment> replies; //Campo para armazenar a lista de comentários filhos (respostas), permitindo a criação de threads de comentários e respostas aninhadas.

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt; //Campo para armazenar a data de criação do comentário, permitindo rastrear quando o comentário foi feito.

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt; //Campo para armazenar a data de atualização do comentário, permitindo rastrear quando o comentário foi modificado pela última vez.
}
