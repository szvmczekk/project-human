package pl.szvmczek.projecthuman.domain.task;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface TaskCompletionRepository extends CrudRepository<TaskCompletion, Long> {
    Optional<TaskCompletion> findByTask_IdAndDate(Long taskId, LocalDate date);
    @Modifying
    @Query("DELETE FROM TaskCompletion tc WHERE tc.task.id = :taskId AND tc.date = :date")
    void deleteByTask_IdAndDate(@Param("taskId") Long taskId,@Param("date") LocalDate date);
    boolean existsByTask_IdAndDate(Long taskId, LocalDate date);
    Optional<TaskCompletion> findTopByTask_IdAndDateLessThanOrderByDateDesc(Long taskId, LocalDate today);
    @Query("""
select tc.date
from TaskCompletion tc
where tc.task.id = :taskId and tc.date <= :endDate
order by tc.date desc
""")
    List<LocalDate> findDatesUpToDesc(Long taskId, LocalDate endDate);

}
