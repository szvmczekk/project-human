package pl.szvmczek.projecthuman.domain.habitdrop.dto;

import java.time.LocalDateTime;

public class HabitDropViewDto {
    private Long id;
    private String title;
    private String description;
    private boolean status;
    private LocalDateTime startDate;
    private int resetCount;

    public HabitDropViewDto(Long id, String title, String description, boolean status, LocalDateTime startDate, int resetCount) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.status = status;
        this.startDate = startDate;
        this.resetCount = resetCount;
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

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public LocalDateTime getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDateTime startDate) {
        this.startDate = startDate;
    }

    public int getResetCount() {
        return resetCount;
    }

    public void setResetCount(int resetCount) {
        this.resetCount = resetCount;
    }
}
