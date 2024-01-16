package app.PageNavigation;

import app.user.User;

public class PreviousPage implements Command{
    User user;

    public PreviousPage(User user) {
        this.user = user;
    }

    @Override
    public String execute() {
        return user.previousPage();
    }
}
