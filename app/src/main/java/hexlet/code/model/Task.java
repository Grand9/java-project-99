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
import jakarta.persistence.ManyToMany;
import jakarta.persistence.JoinTable;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Set;

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

    @ManyToMany
    @JoinTable(
            name = "task_labels",
            joinColumns = @JoinColumn(name = "task_id"),
            inverseJoinColumns = @JoinColumn(name = "label_id")
    )
    private Set<Label> labels;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    /**
     * Gets the title of the task.
     *
     * @return the title of the task
     */
    public String getTitle() {
        return name;
    }

    /**
     * Gets the status of the task.
     *
     * @return the status of the task
     */
    public TaskStatus getStatus() {
        return taskStatus;
    }

    /**
     * Sets the status of the task.
     *
     * @param taskStatus the new status of the task
     */
    public void setStatus(TaskStatus taskStatus) {
        this.taskStatus = taskStatus;
    }

    /**
     * Sets the creation time of the task before it is persisted.
     */
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }
}
