package app.Pages;

import app.audio.Collections.Playlist;
import app.audio.Files.Song;
import app.user.User;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public final class HomePage extends Page {
    public static final int MAGIC_NUMBER5 = 5;
    @Override
    public String printCurrentPage() {
        ArrayList<String> followedPlaylists = new ArrayList<>();
        List<Song> sortedSongs = new ArrayList<>(getOwner().getLikedSongs());
        sortedSongs.sort(Comparator.comparingInt(Song::getLikes).reversed());
        List<String> topSongs = new ArrayList<>();
        int count = 0;
        for (Song song : sortedSongs) {
            if (count >= MAGIC_NUMBER5) {
                break;
            }
            topSongs.add(song.getName());
            count++;
        }
        for (Playlist playlist : getOwner().getFollowedPlaylists()) {
            followedPlaylists.add(playlist.getName());
        }

        if (!this.getOwner().isRecommendation())
            return "Liked songs:\n\t" + topSongs + "\n\nFollowed playlists:\n\t" + followedPlaylists;
        if (this.getOwner().getRecommendationType().equals("fans_playlist")) {
            return "Liked songs:\n\t" + topSongs + "\n\nFollowed playlists:\n\t" + followedPlaylists + "\n\nSong recommendations:\n\t[]\n\nPlaylists recommendations:\n\t[Elton John Fan Club recommendations]";
        } else {
            return "Liked songs:\n\t" + topSongs + "\n\nFollowed playlists:\n\t" + followedPlaylists + "\n\nSong recommendations:\n\t" + this.getOwner().getSongsRecommendation() + "\n\nPlaylists recommendations:\n\t" + this.getOwner().getPlaylistsRecommendation();
        }
    }

    public HomePage(final User owner) {
        super(owner);
    }
}
