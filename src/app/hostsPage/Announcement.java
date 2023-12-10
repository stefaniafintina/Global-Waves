package app.hostsPage;

import lombok.Getter;

import java.util.PrimitiveIterator;

public class Announcement {
    @Getter
    private String name;
    @Getter
    private String description;

    public Announcement(String name, String description) {
        this.name = name;
        this.description = description;
    }
}
