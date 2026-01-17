package pl.szvmczek.projecthuman.domain.habit;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
public class HabitCompletion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private LocalDate date;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "habit_id",referencedColumnName = "id",nullable = false)
    private Habit habit;

    public HabitCompletion(Habit habit, LocalDate date) {
        this.date = date;
        this.habit = habit;
    }

    public HabitCompletion() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public Habit getHabit() {
        return habit;
    }

    public void setHabit(Habit habit) {
        this.habit = habit;
    }
}
