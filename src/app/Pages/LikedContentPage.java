package app.Pages;

import app.user.User;

public final class LikedContentPage extends Page {

    public LikedContentPage(final User owner) {
        super(owner);
    }

    @Override
    public String printCurrentPage() {
        return "Liked songs:\n\t" + getOwner().getLikedSongsList()
                + "\n\nFollowed playlists:\n\t" + getOwner().getFollowedPlaylistsList();
    }
}
