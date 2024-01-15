package app.Pages;

import app.user.User;
import lombok.Getter;
import lombok.Setter;

public abstract class Page {
    @Getter
    @Setter
    private User owner;
    @Getter
    @Setter
    private String type = "home";

    public Page(final User owner) {
        this.owner = owner;
    }

    /**
     * Prints the current page using
     * to required format
     */
    public abstract String printCurrentPage();
}
