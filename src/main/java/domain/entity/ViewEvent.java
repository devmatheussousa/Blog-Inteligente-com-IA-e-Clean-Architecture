package domain.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "view_events")
@EqualsAndHashCode(of = "id")
public class ViewEvent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id", nullable = false) //Especifica a coluna de junção para a relação Many-to-One com a entidade Post, garantindo que cada evento de visualização esteja associado a um post específico.
    private Post post;

    @Column(name = "viewer_id", length = 45)
    private String viewerIp; //Campo para armazenar o endereço IP do visitante, permitindo rastrear a origem das visualizações e analisar padrões de acesso.

    @Column(name = "user_agent", length = 500)
    private String userAgent; //Campo para armazenar o user agent do visitante, permitindo identificar o tipo de dispositivo e navegador utilizado para acessar o conteúdo.

    @Column(length = 500)
    private String referrer; //Campo para armazenar a URL de referência, permitindo rastrear de onde os visitantes estão vindo antes de acessar o conteúdo.

    @CreationTimestamp
    @Column(name = "event_at", nullable = false, updatable = false)
    private LocalDateTime eventAt; //Campo para armazenar a data e hora do evento de visualização, permitindo analisar o tráfego ao longo do tempo e identificar picos de acesso.


}
