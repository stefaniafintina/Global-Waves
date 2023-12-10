package app.Pages;

import app.user.User;

public class HostsPage extends Page {
    public HostsPage(User owner) {
        super(owner);
    }

    @Override
    public String printCurrentPage() {
        return "Podcasts:\n\t" + getOwner().getPodcastsList() + "\n\nAnnouncements:\n\t" + getOwner().getAnnouncementsList();
    }
}
