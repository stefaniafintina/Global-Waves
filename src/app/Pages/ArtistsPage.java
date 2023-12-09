package app.Pages;

import app.audio.Collections.Album;
import app.audio.LibraryEntry;
import app.user.User;

public class ArtistsPage extends Page{

    @Override
    public String printCurrentPage() {
        return "Albums:\n\t" + getOwner().getAlbumsList() + "\n\nMerch:\n\t" + getOwner().getMerchesList() + "\n\nEvents:\n\t" + getOwner().getEventsList();
    }

    public ArtistsPage(User owner) {
        super(owner);
    }

}
