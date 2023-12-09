package app.artistsPage;

import lombok.Getter;

public class Event {
    @Getter
    private String name;
    @Getter
    private String date;
    @Getter
    private String description;
    public Event(String date, String description, String name) {
        this.name = name;
        this.date = date;
        this.description = description;
    }
}
