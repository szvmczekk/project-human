package pl.szvmczek.projecthuman.domain.task;

import jakarta.persistence.*;
import pl.szvmczek.projecthuman.domain.user.User;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.EAGER)
    private User user;
    private String title;
    private String description;
    private LocalDate createdDate;
    @OneToMany(mappedBy = "task",fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private Set<TaskCompletion> completions = new HashSet<>();

    public Task(String title, String description) {
        this.title = title;
        this.description = description;
        this.createdDate = LocalDate.now();
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

    @Override
    public String toString() {
        return "Task{" +
                "id=" + id +
                ", user=" + user +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", createdDate=" + createdDate +
                '}';
    }
}
