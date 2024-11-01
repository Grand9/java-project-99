package hexlet.code.repository;

import hexlet.code.model.Label;
import hexlet.code.model.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {
    List<Task> findByLabels(Label label);

    @Query("SELECT t FROM Task t "
            + "LEFT JOIN t.labels l "
            + "WHERE (:titleCont IS NULL OR t.name LIKE %:titleCont%) "
            + "AND (:assigneeId IS NULL OR t.assignee.id = :assigneeId) "
            + "AND (:status IS NULL OR t.taskStatus.slug = :status) "
            + "AND (:labelId IS NULL OR l.id = :labelId)")
    List<Task> findTasksByFilters(@Param("titleCont") String titleCont,
                                  @Param("assigneeId") Long assigneeId,
                                  @Param("status") String status,
                                  @Param("labelId") Long labelId);

}
