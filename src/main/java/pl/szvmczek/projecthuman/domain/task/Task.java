package pl.szvmczek.projecthuman.domain.task;

import jakarta.persistence.*;
import pl.szvmczek.projecthuman.domain.category.Category;
import pl.szvmczek.projecthuman.domain.user.User;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Entity
@NamedEntityGraph(name = "Task.category", attributeNodes = @NamedAttributeNode("category"))
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    private User user;
    private String title;
    private String description;
    private LocalDate createdDate;
    @OneToMany(mappedBy = "task",fetch = FetchType.LAZY, cascade = {CascadeType.REMOVE,CascadeType.PERSIST})
    private Set<TaskCompletion> completions = new HashSet<>();
    private int currentStreak = 0;
    private LocalDate lastCompletionDate;
    @ManyToOne(fetch = FetchType.LAZY,optional = true)
    @JoinColumn(name = "category_id",referencedColumnName = "id",nullable = true)
    private Category category;

    public Task(String title, String description) {
        this.title = title;
        this.description = description;
        this.createdDate = LocalDate.now();
        this.currentStreak = 0;
    }

    public Task() {
    }


    public LocalDate getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(LocalDate createdDate) {
        this.createdDate = createdDate;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Set<TaskCompletion> getCompletions() {
        return completions;
    }

    public void setCompletions(Set<TaskCompletion> completions) {
        this.completions = completions;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public int getCurrentStreak() {
        return currentStreak;
    }

    public void setCurrentStreak(int currentStreak) {
        this.currentStreak = currentStreak;
    }

    public LocalDate getLastCompletionDate() {
        return lastCompletionDate;
    }

    public void setLastCompletionDate(LocalDate lastCompletionDate) {
        this.lastCompletionDate = lastCompletionDate;
    }
}
