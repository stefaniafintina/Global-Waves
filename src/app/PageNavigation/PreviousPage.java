package app.PageNavigation;

import app.user.User;

public final class PreviousPage implements Command {
    private User user;

    public PreviousPage(final User user) {
        this.user = user;
    }

    @Override
    public String execute() {
        return user.previousPage();
    }
}
