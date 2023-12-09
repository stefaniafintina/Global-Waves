package app.Pages;

import app.audio.LibraryEntry;
import app.user.User;

import java.util.ArrayList;
import java.util.Map;

public class HomePage extends Page {
    @Override
    public String printCurrentPage() {
        ArrayList <String> mostLikedSongs = new ArrayList<>();
        for (Map.Entry<String, Integer> entry: getOwner().getMostLikedSongs().entrySet())
            mostLikedSongs.add(entry.getKey());
        return "Liked songs:\n\t" + mostLikedSongs + "\n\nFollowed playlists:\n\t[]";
    }

    public HomePage(User owner) {
        super(owner);
    }
}
