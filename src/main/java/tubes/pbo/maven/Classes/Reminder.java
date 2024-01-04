package tubes.pbo.maven.Classes;

public class Reminder {
    private String title;
    private int hour;
    private int minute;

    public Reminder(String title, int hour, int minute) {
        this.title = title;
        this.hour = hour;
        this.minute = minute;
    }

    public String getTitle() {
        return title;
    }

    public int getHour() {
        return hour;
    }

    public int getMinute() {
        return minute;
    }
}
