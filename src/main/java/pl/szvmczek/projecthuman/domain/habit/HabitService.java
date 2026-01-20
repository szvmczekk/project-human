package pl.szvmczek.projecthuman.domain.habit;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.szvmczek.projecthuman.domain.category.Category;
import pl.szvmczek.projecthuman.domain.category.CategoryService;
import pl.szvmczek.projecthuman.domain.habit.dto.HabitCreateDto;
import pl.szvmczek.projecthuman.domain.habit.dto.HabitUpdateDto;
import pl.szvmczek.projecthuman.domain.habit.dto.HabitViewDto;
import pl.szvmczek.projecthuman.domain.user.User;
import pl.szvmczek.projecthuman.domain.user.UserService;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class HabitService {
    private final HabitRepository habitRepository;
    private final HabitCompletionRepository habitCompletionRepository;
    private final UserService userService;
    private final CategoryService categoryService;

    public HabitService(HabitRepository habitRepository,
                        HabitCompletionRepository habitCompletionRepository,
                        UserService userService,
                        CategoryService categoryService) {
        this.habitRepository = habitRepository;
        this.habitCompletionRepository = habitCompletionRepository;
        this.userService = userService;
        this.categoryService = categoryService;
    }

    @Transactional
    public void saveHabit(HabitCreateDto dto, Long userId) {
        Habit habit = HabitDtoMapper.map(dto);
        User userReference = userService.getReferenceById(userId);
        habit.setUser(userReference);
        habit.setTitle(habit.getTitle().trim());
        if (dto.getCategoryId() != null) {
            Category category = categoryService.getCategoryByIdAndUserId(dto.getCategoryId(), userId)
                    .orElseThrow(() -> new EntityNotFoundException("Category not found"));
            habit.setCategory(category);
        }
        habitRepository.save(habit);
    }

    @Transactional(readOnly = true)
    public List<HabitViewDto> getHabitsForUser(Long userId) {
        List<Habit> tasksByUser = habitRepository.findAllByUserId(userId);
        return tasksByUser.stream()
                .map(HabitDtoMapper::map)
                .toList();
    }

    @Transactional
    public void updateHabitForUser(HabitUpdateDto dto, Long userId) {
        Habit originalHabit = getHabitOrThrow(dto.getId(), userId);
        originalHabit.setTitle(dto.getTitle());
        originalHabit.setDescription(dto.getDescription());
        categoryService.getCategoryByIdAndUserId(dto.getCategoryId(), userId).ifPresent(originalHabit::setCategory);
    }

    @Transactional
    public void deleteHabit(Long habitId, Long userId) {
        Habit habit = getHabitOrThrow(habitId, userId);
        habitRepository.delete(habit);
    }

    @Transactional
    public void toggleCompletion(Long habitId, Long userId) {
        Habit habit = getHabitOrThrow(habitId, userId);
        LocalDate today = LocalDate.now();
        boolean doneToday = habitCompletionRepository.existsByHabit_IdAndDate(habit.getId(), today);
        if (doneToday)
            unCompleteHabit(habit, today);
        else
            completeHabit(habit, today);
    }

    public HabitUpdateDto getHabitForEdit(Long habitId, Long userId) {
        Habit habit = getHabitOrThrow(habitId, userId);
        return new HabitUpdateDto(habit.getId(), habit.getTitle(), habit.getDescription());
    }

    private Habit getHabitOrThrow(Long habitId, Long userId) {
        return habitRepository.findByIdAndUserId(habitId, userId)
                .orElseThrow(() -> new EntityNotFoundException("Habit not found!"));
    }

    private void completeHabit(Habit habit, LocalDate today) {
        habitCompletionRepository.save(new HabitCompletion(habit, today));
        LocalDate last = habit.getLastCompletionDate();
        if (last != null && last.isEqual(today.minusDays(1))) {
            habit.setCurrentStreak(habit.getCurrentStreak() + 1);
        } else {
            habit.setCurrentStreak(1);
        }
        habit.setLastCompletionDate(today);
    }

    private void unCompleteHabit(Habit habit, LocalDate today) {
        habitCompletionRepository.deleteByTask_IdAndDate(habit.getId(), today);
        Optional<HabitCompletion> prev =
                habitCompletionRepository.findTopByHabit_IdAndDateLessThanOrderByDateDesc(habit.getId(), today);
        if (prev.isEmpty()) {
            habit.setLastCompletionDate(null);
            habit.setCurrentStreak(0);
        } else {
            LocalDate prevDate = prev.get().getDate();
            habit.setLastCompletionDate(prevDate);
            habit.setCurrentStreak(recalculateStreakEndingAt(habit.getId(), prevDate));
        }
    }

    private int recalculateStreakEndingAt(Long habitId, LocalDate endDate) {
        List<LocalDate> dates = habitCompletionRepository.findDatesUpToDesc(habitId, endDate);
        if (dates.isEmpty()) return 0;

        int streak = 0;
        LocalDate expected = endDate;

        for (LocalDate d : dates) {
            if (!d.equals(expected)) break;
            streak++;
            expected = expected.minusDays(1);
        }
        return streak;
    }

}
