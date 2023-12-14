package app.artistsPage;

import lombok.Getter;

public final class Event {
    @Getter
    private String name;
    @Getter
    private String date;
    @Getter
    private String description;
    public Event(final String date, final String description, final String name) {
        this.name = name;
        this.date = date;
        this.description = description;
    }
}
