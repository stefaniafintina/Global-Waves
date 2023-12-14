package app.Pages;

import app.user.User;

public final class HostsPage extends Page {
    public HostsPage(final User owner) {
        super(owner);
    }

    @Override
    public String printCurrentPage() {
        return "Podcasts:\n\t" + getOwner().getPodcastsList()
                + "\n\nAnnouncements:\n\t" + getOwner().getAnnouncementsList();
    }
}
