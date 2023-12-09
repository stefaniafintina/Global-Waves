package app.Pages;

import app.user.User;
import lombok.Getter;
import lombok.Setter;

public abstract class Page {
    @Getter
    @Setter
    private User owner;

    public Page(User owner) {
        this.owner = owner;
    }


    public abstract String printCurrentPage();
}
