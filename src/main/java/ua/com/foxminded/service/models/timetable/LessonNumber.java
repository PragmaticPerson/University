package ua.com.foxminded.service.models.timetable;

public enum LessonNumber {
    FIRST("8:30"),
    SECOND("10:15"),
    THIRD("12:00"),
    FOURTH("14:15"),
    FIFTH("16:00");

    private String startTime;

    LessonNumber(String startTime) {
        this.startTime = startTime;
    }

    public String getStartTime() {
        return startTime;
    }
    
    public boolean equals() {
        return false;
    }
}
