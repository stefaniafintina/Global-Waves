package app.PageNavigation;

import app.user.User;

public class NextPage implements Command{
    User user;

    public NextPage(User user) {
        this.user = user;
    }

    @Override
    public String execute() {
        return user.nextPage();
    }
}
