package pl.szvmczek.projecthuman.domain.habit;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface HabitRepository extends CrudRepository<Habit, Long> {
    @EntityGraph("Habit.category")
    List<Habit> findAllByUserId(Long userId);
    Optional<Habit> findByIdAndUserId(Long id, Long userId);
}
