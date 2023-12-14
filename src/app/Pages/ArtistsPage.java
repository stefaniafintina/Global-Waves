package app.Pages;

import app.user.User;

public final class ArtistsPage extends Page {

    @Override
    public String printCurrentPage() {
        return "Albums:\n\t" + getOwner().getAlbumsList()
                + "\n\nMerch:\n\t" + getOwner().getMerchesList() + "\n\nEvents:\n\t"
                + getOwner().getEventsList();
    }

    public ArtistsPage(final User owner) {
        super(owner);
    }

}
