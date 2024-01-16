package app.PageNavigation;

import app.user.User;

public final class NextPage implements Command {
    private User user;

    public NextPage(final User user) {
        this.user = user;
    }

    @Override
    public String execute() {
        return user.nextPage();
    }
}
