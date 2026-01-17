package pl.szvmczek.projecthuman.domain.habit;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface HabitCompletionRepository extends CrudRepository<HabitCompletion, Long> {
    Optional<HabitCompletion> findByHabit_IdAndDate(Long habitId, LocalDate date);
    @Modifying
    @Query("DELETE FROM HabitCompletion tc WHERE tc.habit.id = :habitId AND tc.date = :date")
    void deleteByTask_IdAndDate(@Param("habitId") Long taskId,@Param("date") LocalDate date);
    boolean existsByHabit_IdAndDate(Long habitId, LocalDate date);
    Optional<HabitCompletion> findTopByHabit_IdAndDateLessThanOrderByDateDesc(Long habitId, LocalDate today);
    @Query("""
select tc.date
from HabitCompletion tc
where tc.habit.id = :habitId and tc.date <= :endDate
order by tc.date desc
""")
    List<LocalDate> findDatesUpToDesc(Long habitId, LocalDate endDate);

}
