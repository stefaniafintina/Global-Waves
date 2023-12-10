package app.Pages;

import app.user.User;

public class LikedContentPage extends Page {

    public LikedContentPage(User owner) {
        super(owner);
    }

    @Override
    public String printCurrentPage() {
        return "Liked songs:\n\t" + getOwner().getLikedSongsList() + "\n\nFollowed playlists:\n\t" + getOwner().getFollowedPlaylistsList();
    }
}
