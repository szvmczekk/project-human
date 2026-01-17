package pl.szvmczek.projecthuman.domain.habitdrop.dto;

public class HabitDropCreateDto {
    private String title;
    private String description;

    public HabitDropCreateDto(String title, String description) {
        this.title = title;
        this.description = description;
    }

    public HabitDropCreateDto() {
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
}
