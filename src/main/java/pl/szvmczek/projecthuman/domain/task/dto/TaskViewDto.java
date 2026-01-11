package pl.szvmczek.projecthuman.domain.task.dto;

public class TaskViewDto {
    private Long id;
    private String title;
    private String category;
    private String description;
    private boolean completed;
    private int streak;

    public TaskViewDto(Long id, String title, String description, boolean completed,int streak,String category) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.completed = completed;
        this.streak = streak;
        this.category = category;
    }

    public Long getId() {
        return id;
    }

    public int getStreak() {
        return streak;
    }

    public void setStreak(int streak) {
        this.streak = streak;
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

    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}
