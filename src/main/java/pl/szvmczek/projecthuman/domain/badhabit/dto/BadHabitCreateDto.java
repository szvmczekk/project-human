package pl.szvmczek.projecthuman.domain.badhabit.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class BadHabitCreateDto {
    @NotBlank(message = "Title cannot be empty")
    @Size(max = 50,message = "Title must be less than 50 characters long")
    private String title;
    @Size(max = 100,message = "Description must be less than 100 characters long")
    private String description;

    public BadHabitCreateDto(String title, String description) {
        this.title = title;
        this.description = description;
    }

    public BadHabitCreateDto() {
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
