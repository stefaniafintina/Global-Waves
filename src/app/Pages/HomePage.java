package app.Pages;

import app.audio.Collections.Playlist;
import app.audio.Files.Song;
import app.audio.LibraryEntry;
import app.user.User;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

public class HomePage extends Page {
    @Override
    public String printCurrentPage() {
        ArrayList<String> followedPlaylists = new ArrayList<>();
        List<Song> sortedSongs = new ArrayList<>(getOwner().getLikedSongs());
        sortedSongs.sort(Comparator.comparingInt(Song::getLikes).reversed());
        List<String> topSongs = new ArrayList<>();
        int count = 0;
        for (Song song : sortedSongs) {
            if (count >= 5) break;
            topSongs.add(song.getName());
            count++;
        }
        for (Playlist playlist : getOwner().getFollowedPlaylists())
            followedPlaylists.add(playlist.getName());
        return "Liked songs:\n\t" + topSongs + "\n\nFollowed playlists:\n\t" + followedPlaylists;
    }

    public HomePage(User owner) {
        super(owner);
    }
}
