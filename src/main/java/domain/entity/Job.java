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
@Table(name = "jobs")
@EqualsAndHashCode(of = "id")
public class Job {

    public enum JobStatus {
        PENDING,
        RUNNING,
        COMPLETED,
        FAILED
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(nullable = false, length = 100)
    private String type; //Campo para armazenar o tipo do job, permitindo categorizar os diferentes tipos de tarefas que podem ser executadas.

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private JobStatus status;

    @Lob
    @Column(name = "payload_json")
    private String payloadJson; //Campo para armazenar os dados necessários para a execução do job em formato JSON, permitindo flexibilidade na definição dos parâmetros de cada job.1

    @Column(name = "error_message", length = 1000)
    private String errorMessage; //Campo para armazenar mensagens de erro caso o job falhe, permitindo rastrear e diagnosticar problemas durante a execução do job.

    @Builder.Default //Usamos @Builder.Default para garantir que o valor padrão seja aplicado mesmo quando o objeto for criado usando o builder, evitando que o campo seja nulo.
    @Column(nullable = false)
    private Integer attempts; //Campo para armazenar o número de tentativas de execução do job, permitindo implementar lógica de retry em caso de falhas.

    @Column(name = "scheduled_at")
    private LocalDateTime scheduledAt; //Campo para armazenar a data e hora programada para a execução do job, permitindo agendar tarefas para serem executadas em momentos específicos.

    @Column(name = "started_at")
    private LocalDateTime startedAt; //Campo para armazenar a data e hora de início da execução do job, permitindo rastrear quando o job começou a ser processado.

    @Column(name = "finished_at")
    private LocalDateTime finishedAt; //Campo para armazenar a data e hora de término da execução do job, permitindo rastrear quando o job foi concluído, seja com sucesso ou falha.

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}
