package app;

import lombok.Getter;
import lombok.Setter;

@Getter
public class Notifications {
    @Setter
    private String name;
    @Setter
    private String description;

    public Notifications(final String name, final String description) {
        this.name = name;
        this.description = description;
    }
}
