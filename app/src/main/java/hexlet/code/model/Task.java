package hexlet.code.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Column;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.PrePersist;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * Represents a Task entity.
 */
@Getter
@Setter
@Entity
@Table(name = "tasks")
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    private Integer index;

    private String description;

    @ManyToOne
    @JoinColumn(name = "task_status_id", nullable = false)
    private TaskStatus taskStatus;

    @ManyToOne
    @JoinColumn(name = "assignee_id")
    private User assignee;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    /**
     * Sets the creation time of the task before it is persisted.
     */
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }
}
