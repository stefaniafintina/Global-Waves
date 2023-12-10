package app.user;

import app.Admin;
import app.Pages.ArtistsPage;
import app.Pages.Page;
import app.artistsPage.Merch;
import app.audio.Collections.*;
import app.audio.Files.AudioFile;
import app.audio.Files.Episode;
import app.audio.Files.Song;
import app.audio.LibraryEntry;
import app.artistsPage.Event;
import app.hostsPage.Announcement;
import app.player.Player;
import app.player.PlayerStats;
import app.searchBar.Filters;
import app.searchBar.SearchBar;
import app.utils.Enums;
import fileio.input.EpisodeInput;
import fileio.input.SongInput;
import javassist.expr.NewArray;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Delegate;
import org.checkerframework.checker.units.qual.A;

import java.util.*;
import java.util.concurrent.atomic.AtomicReferenceArray;

public class User extends LibraryEntry{
    @Getter
    private String username;
    @Getter
    @Setter
    private int age;
    @Getter
    private String city;
    @Getter
    private ArrayList<Playlist> playlists;
    @Getter
    private ArrayList<Album> albums;
    @Getter
    private ArrayList<Event> events;
    @Getter
    private ArrayList<Merch> merches;
    @Getter
    private ArrayList<Announcement> announcements;
    @Getter
    private ArrayList<Song> likedSongs;
    @Getter
    private ArrayList<Playlist> followedPlaylists;
    @Getter
    private HashMap<String, Integer> mostLikedSongs;
    @Getter
    @Setter
    private boolean connectionStatus;
    @Getter
    @Setter
    private boolean artist;
    @Getter
    @Setter
    private boolean host;
    @Getter
    private final Player player;
    private final SearchBar searchBar;
    private boolean lastSearched;
    @Getter
    @Setter
    private ArrayList<String> results;
    @Getter
    private int artistSelected;
    @Getter
    @Setter
    private Page page;
    @Getter
    private Page artistPage;
    @Getter
    @Setter
    private boolean isUsed;
    private String selectedType;
    @Getter
    @Setter
    private ArrayList<Podcast> podcasts;
    public User(String username, int age, String city) {
        super(username);
        this.username = username;
        this.age = age;
        this.city = city;
        playlists = new ArrayList<>();
        likedSongs = new ArrayList<>();
        followedPlaylists = new ArrayList<>();
        player = new Player();
        searchBar = new SearchBar(username);
        lastSearched = false;
        connectionStatus = true;
        artist = false;
        host = false;
        albums = new ArrayList<>();
        mostLikedSongs = new HashMap<>();
        events = new ArrayList<>();
        merches = new ArrayList<>();
        results = new ArrayList<>();
        artistPage = new ArtistsPage(this);
        isUsed = false;
        selectedType = null;
        podcasts = new ArrayList<>();
        announcements = new ArrayList<>();
    }

    public ArrayList<String> search(Filters filters, String type) {
        if (!this.connectionStatus) {
            ArrayList<String> message = new ArrayList<>();
            message.add(" is offline.");
            return message;
        }
        searchBar.clearSelection();
        player.stop();
        artistPage = new ArtistsPage(this);
        lastSearched = true;
        ArrayList<String> results = new ArrayList<>();
        List<LibraryEntry> libraryEntries = searchBar.search(filters, type);
        artistSelected = 0;
        for (LibraryEntry libraryEntry : libraryEntries) {
            results.add(libraryEntry.getName());
        }
        isUsed = false;
        return results;
    }

    public String select(int itemNumber) {
        if (!lastSearched)
            return "Please conduct a search before making a selection.";

        lastSearched = false;

        LibraryEntry selected = searchBar.select(itemNumber);

        if (selected == null)
            return "The selected ID is too high.";
        if (searchBar.getSearchType() == 1) {
            page = ((User)selected).getPage();
            return "Successfully selected %s's page.".formatted(selected.getName());
        }

        return "Successfully selected %s.".formatted(selected.getName());
    }

    public String load() {
        if (searchBar.getLastSelected() == null)
            return "Please select a source before attempting to load.";

        if (!searchBar.getLastSearchType().equals("song") && ((AudioCollection)searchBar.getLastSelected()).getNumberOfTracks() == 0) {
            return "You can't load an empty audio collection!";
        }

        player.setSource(searchBar.getLastSelected(), searchBar.getLastSearchType());
        searchBar.clearSelection();

        player.pause();

        return "Playback loaded successfully.";
    }

    public String playPause() {
        if (player.getCurrentAudioFile() == null)
            return "Please load a source before attempting to pause or resume playback.";
        player.pause();

        if (player.getPaused())
            return "Playback paused successfully.";
        else
            return "Playback resumed successfully.";
    }

    public String repeat() {
        if (player.getCurrentAudioFile() == null)
            return "Please load a source before setting the repeat status.";

        Enums.RepeatMode repeatMode = player.repeat();
        String repeatStatus = "";

        switch(repeatMode) {
            case NO_REPEAT -> repeatStatus = "no repeat";
            case REPEAT_ONCE -> repeatStatus = "repeat once";
            case REPEAT_ALL -> repeatStatus = "repeat all";
            case REPEAT_INFINITE -> repeatStatus = "repeat infinite";
            case REPEAT_CURRENT_SONG -> repeatStatus = "repeat current song";
        }

        return "Repeat mode changed to %s.".formatted(repeatStatus);
    }

    public String shuffle(Integer seed) {
        if (player.getCurrentAudioFile() == null)
            return "Please load a source before using the shuffle function.";

        if (!player.getType().equals("playlist") && !player.getType().equals("album"))
            return "The loaded source is not a playlist or an album.";

        player.shuffle(seed);

        if (player.getShuffle())
            return "Shuffle function activated successfully.";
        return "Shuffle function deactivated successfully.";
    }

    public String forward() {
        if (player.getCurrentAudioFile() == null)
            return "Please load a source before attempting to forward.";

        if (!player.getType().equals("podcast"))
            return "The loaded source is not a podcast.";

        player.skipNext();

        return "Skipped forward successfully.";
    }

    public String backward() {
        if (player.getCurrentAudioFile() == null)
            return "Please select a source before rewinding.";

        if (!player.getType().equals("podcast"))
            return "The loaded source is not a podcast.";

        player.skipPrev();

        return "Rewound successfully.";
    }

    public String like() {
        if (!this.connectionStatus)
            return this.username + " is offline.";
        if (player.getCurrentAudioFile() == null)
            return "Please load a source before liking or unliking.";
        if (!player.getType().equals("song") && !player.getType().equals("playlist") && !player.getType().equals("album"))
            return "Loaded source is not a song.";

        Song song = (Song) player.getCurrentAudioFile();

        if (likedSongs.contains(song)) {
            likedSongs.remove(song);
            song.dislike();
            mostLikedSongs.put(song.getName(), mostLikedSongs.getOrDefault(song.getName(), 0) - 1);
            return "Unlike registered successfully.";
        }
        mostLikedSongs.put(song.getName(), mostLikedSongs.getOrDefault(song.getName(), 0) + 1);
        likedSongs.add(song);
        song.like();
        return "Like registered successfully.";
    }

    public String next() {
        if (player.getCurrentAudioFile() == null)
            return "Please load a source before skipping to the next track.";

        player.next();

        if (player.getCurrentAudioFile() == null)
            return "Please load a source before skipping to the next track.";

        return "Skipped to next track successfully. The current track is %s.".formatted(player.getCurrentAudioFile().getName());
    }

    public String prev() {
        if (player.getCurrentAudioFile() == null)
            return "Please load a source before returning to the previous track.";

        player.prev();
        if (selectedType != null && selectedType.equals("song")) {
            for (Song song: Admin.getSongs()) {
                if (song.getName().equals(player.getCurrentAudioFile().getName())) {
                    for (User user: Admin.getArtists()) {
                        if (user.getName().equals(song.getArtist())) {
                            artistPage = user.getPage();
                        }
                    }
                }

            }
        } else if (selectedType != null && selectedType.equals("album")) {
            for (Album album: Admin.getAlbums()) {
                if (album.getName().equals(player.getCurrentAudioFile().getName())) {
                    for (User user: Admin.getArtists()) {
                        if (user.getName().equals(album.getOwner())) {
                            artistPage = user.getPage();
                        }
                    }
                }

            }
        }
        return "Returned to previous track successfully. The current track is %s.".formatted(player.getCurrentAudioFile().getName());
    }

    public String createPlaylist(String name, int timestamp) {
        if (playlists.stream().anyMatch(playlist -> playlist.getName().equals(name)))
            return "A playlist with the same name already exists.";

        playlists.add(new Playlist(name, username, timestamp));

        return "Playlist created successfully.";
    }

    public String addRemoveInPlaylist(int Id) {
        if (player.getCurrentAudioFile() == null)
            return "Please load a source before adding to or removing from the playlist.";

        if (player.getType().equals("podcast"))
            return "The loaded source is not a song.";

        if (Id > playlists.size())
            return "The specified playlist does not exist.";

        Playlist playlist = playlists.get(Id - 1);

        if (playlist.containsSong((Song)player.getCurrentAudioFile())) {
            playlist.removeSong((Song)player.getCurrentAudioFile());
            return "Successfully removed from playlist.";
        }

        playlist.addSong((Song)player.getCurrentAudioFile());
        return "Successfully added to playlist.";
    }

    public String switchPlaylistVisibility(Integer playlistId) {
        if (playlistId > playlists.size())
            return "The specified playlist ID is too high.";

        Playlist playlist = playlists.get(playlistId - 1);
        playlist.switchVisibility();

        if (playlist.getVisibility() == Enums.Visibility.PUBLIC) {
            return "Visibility status updated successfully to public.";
        }

        return "Visibility status updated successfully to private.";
    }

    public ArrayList<PlaylistOutput> showPlaylists() {
        ArrayList<PlaylistOutput> playlistOutputs = new ArrayList<>();
        for (Playlist playlist : playlists) {
            playlistOutputs.add(new PlaylistOutput(playlist));
        }

        return playlistOutputs;
    }

    public String follow() {
        LibraryEntry selection = searchBar.getLastSelected();
        String type = searchBar.getLastSearchType();

        if (selection == null)
            return "Please select a source before following or unfollowing.";

        if (!type.equals("playlist"))
            return "The selected source is not a playlist.";

        Playlist playlist = (Playlist)selection;

        if (playlist.getOwner().equals(username))
            return "You cannot follow or unfollow your own playlist.";

        if (followedPlaylists.contains(playlist)) {
            followedPlaylists.remove(playlist);
            playlist.decreaseFollowers();

            return "Playlist unfollowed successfully.";
        }

        followedPlaylists.add(playlist);
        playlist.increaseFollowers();


        return "Playlist followed successfully.";
    }

    public PlayerStats getPlayerStats() {
        return player.getStats();
    }

    public ArrayList<String> showPreferredSongs() {
        ArrayList<String> results = new ArrayList<>();
        for (AudioFile audioFile : likedSongs) {
            results.add(audioFile.getName());
        }

        return results;
    }

    public String getPreferredGenre() {
        String[] genres = {"pop", "rock", "rap"};
        int[] counts = new int[genres.length];
        int mostLikedIndex = -1;
        int mostLikedCount = 0;

        for (Song song : likedSongs) {
            for (int i = 0; i < genres.length; i++) {
                if (song.getGenre().equals(genres[i])) {
                    counts[i]++;
                    if (counts[i] > mostLikedCount) {
                        mostLikedCount = counts[i];
                        mostLikedIndex = i;
                    }
                    break;
                }
            }
        }

        String preferredGenre = mostLikedIndex != -1 ? genres[mostLikedIndex] : "unknown";
        return "This user's preferred genre is %s.".formatted(preferredGenre);
    }
    public Album addAlbum(Album album) {
        if (albums.contains(album.getName())) {
            return null;
        } else {
            albums.add(album);
            return album;
        }
    }
    public Podcast addPodcast(Podcast podcast) {
        if (podcasts.contains(podcast.getName())) {
            return null;
        } else {
            podcasts.add(podcast);
            return podcast;
        }
    }

    public Event addEvent(Event event) {
        for (Event event1: events) {
            if (event1.getName().equals(event.getName())) {
                return null;
            }
        }
        events.add(event);
        return event;
    }

    public Merch addMerch(Merch merch) {
        for (Merch merch1: merches) {
            if (merch1.getName().equals(merch.getName()))
                return null;
        }
        merches.add(merch);
        return merch;
    }
    public Announcement addAnnouncement(Announcement announcement) {
        for (Announcement announcement1: announcements) {
            if (announcement1.getName().equals(announcement.getName()))
                return null;
        }
        announcements.add(announcement);
        return announcement;
    }
    public ArrayList<String> getAlbumsList() {
        ArrayList<String> albumList = new ArrayList<>();
        for (Album album: this.albums) {
            String message;
            message = album.getName();
            albumList.add(message);
        }
        return albumList;
    }

    public ArrayList<String> getMerchesList() {
        ArrayList<String> merchList = new ArrayList<>();
        for (Merch merch: this.merches) {
            String message;
            message = merch.getName() +  " - " + (int)(merch.getPrice()) + ":\n\t" + merch.getDescription();
            merchList.add(message);
        }
        return merchList;
    }
    public ArrayList<String> getEventsList() {
        ArrayList<String> eventList = new ArrayList<>();
        for (Event event: this.events) {
            String message;
            message = event.getName() +  " - " + event.getDate() + ":\n\t" + event.getDescription();
            eventList.add(message);
        }
        return eventList;
    }
    public ArrayList<String> getAnnouncementsList() {
        ArrayList<String> announcementsList = new ArrayList<>();
        for (Announcement announcement: this.announcements) {
            String message;
            message = announcement.getName() + ":\n\t" + announcement.getDescription() + "\n";
            announcementsList.add(message);
        }
        return announcementsList;
    }
    public ArrayList<String> getPodcastsList() {
        ArrayList<String> podcastsList = new ArrayList<>();
        for (Podcast podcast: this.podcasts) {
            String message1;
            ArrayList<String> message2 = new ArrayList<>();
            String message;
            message1 = podcast.getName() + ":\n\t";
//            System.out.println(" PODCASTUL " + podcast.getName());
            for (Episode episode: podcast.getEpisodes()) {
//                System.out.println(episode.getName() + " - " + episode.getDescription());
                message2.add(episode.getName() + " - " + episode.getDescription());
            }
            message = message1 + message2 + "\n";
            podcastsList.add(message);
        }
        return podcastsList;
    }
    public ArrayList<String> getLikedSongsList() {
        ArrayList<String> likedSongsList = new ArrayList<>();
        for (Song song: this.getLikedSongs()) {
            String message;
            message = song.getName() + " - " + song.getArtist();
            likedSongsList.add(message);
        }
        return likedSongsList;
    }
    public ArrayList<String> getLikePageList() {
        ArrayList<String> likedSongsList = new ArrayList<>();
        for (Song song: this.getLikedSongs()) {
            String message;
            message = song.getName();
            likedSongsList.add(message);
        }
        return likedSongsList;
    }
    public ArrayList<String> getFollowedPlaylistsList() {
        ArrayList<String> followedPlaylistsList = new ArrayList<>();
        for (Playlist playlist: this.getFollowedPlaylists()) {
            String message;
            message = playlist.getName() + " - " + playlist.getOwner();
            followedPlaylistsList.add(message);
        }
        return followedPlaylistsList;
    }

    public void simulateTime(int time) {
        player.simulatePlayer(time);
    }
}
