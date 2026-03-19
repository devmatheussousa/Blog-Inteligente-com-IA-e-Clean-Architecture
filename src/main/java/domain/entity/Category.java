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
@Builder //Usamos builder para facilitar a construção de objetos, especialmente quando há muitos campos ou campos opcionais.
@Entity
@Table(name = "categories")
@EqualsAndHashCode(of = "id") //Gera os métodos equals e hashCode com base no campo id, garantindo que a comparação de objetos seja feita corretamente.
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank //Garante que o nome da categoria não seja nulo ou vazio, assegurando a integridade dos dados.
    @Size(max = 120) //Limita o tamanho do nome da categoria a 120 caracteres, garantindo que os dados sejam consistentes e evitando erros de armazenamento.
    @Column(nullable = false, unique = true, length = 120)
    private String name; //Campo para armazenar o nome da categoria, que deve ser único e não nulo, garantindo que cada categoria seja identificada de forma distinta.

    @NotBlank
    @Size(max = 120)
    @Column(nullable = false, unique = true, length = 120)
    private String slug; //Campo para armazenar uma versão amigável do nome da categoria, geralmente usada em URLs.

    @Column(length = 500) //Limita o tamanho da descrição a 5000 caracteres, permitindo que seja detalhada sem exceder limites razoáveis de armazenamento.
    private String description; //Campo para armazenar uma descrição opcional da categoria, permitindo fornecer mais informações sobre ela.

    @Builder.Default //Garante que a coleção de posts seja inicializada como um HashSet vazio por padrão, evitando problemas de NullPointerException ao tentar adicionar posts a uma categoria.
    @ManyToMany(mappedBy = "categories") //Indica que a relação Many-to-Many é mapeada pela coleção "categories" na entidade Post, estabelecendo a conexão entre as duas entidades.)
    private Set<Post> posts = new HashSet<>(); //Relacionamento Many-to-Many com a entidade Post, permitindo que uma categoria possa estar associada a múltiplos posts e vice-versa.

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false) //Garante que a data de criação seja registrada automaticamente e não possa ser alterada posteriormente, mantendo a integridade dos dados.
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at") //Campo para armazenar a data de atualização da categoria, permitindo rastrear quando as informações foram modificadas pela última vez.
    private LocalDateTime updatedAt;


}
