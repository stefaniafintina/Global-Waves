package app.Pages;

import app.audio.Collections.Playlist;
import app.audio.LibraryEntry;
import app.user.User;

import java.util.ArrayList;
import java.util.Map;

public class HomePage extends Page {
    @Override
    public String printCurrentPage() {
        ArrayList<String> mostLikedSongs = new ArrayList<>();
        ArrayList<String> followedPlaylists = new ArrayList<>();
        for (Map.Entry<String, Integer> entry: getOwner().getMostLikedSongs().entrySet())
            mostLikedSongs.add(entry.getKey());
        for (Playlist playlist : getOwner().getFollowedPlaylists())
            followedPlaylists.add(playlist.getName());
        return "Liked songs:\n\t" + mostLikedSongs + "\n\nFollowed playlists:\n\t" + followedPlaylists;
    }

    public HomePage(User owner) {
        super(owner);
    }
}
