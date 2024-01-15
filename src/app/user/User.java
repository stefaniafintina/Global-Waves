package app.user;

import app.Subject;
import app.Observer;
import app.Admin;
import app.Notifications;
import app.Pages.ArtistsPage;
import app.Pages.Page;
import app.artistsPage.Merch;
import app.audio.Collections.Album;
import app.audio.Collections.Podcast;
import app.audio.Collections.Playlist;
import app.audio.Collections.AudioCollection;
import app.audio.Collections.PlaylistOutput;
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
import lombok.Getter;
import lombok.Setter;
import java.util.*;
import java.util.stream.Collectors;

/**
 * The type User.
 */
public final class User extends LibraryEntry implements Observer, Subject{
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

    @Getter
    @Setter
    private ArrayList<Podcast> podcasts;
    @Getter
    @Setter
    private LinkedHashMap<String, Integer> listenedSongs;
    @Getter
    @Setter
    private LinkedHashMap<String, Integer> artistListenedSongs;
    @Getter
    @Setter
    private LinkedHashMap<String, Integer> listenedSongsPremium;
    @Getter
    @Setter
    private LinkedHashMap<String, Integer> listenedAlbums;
    @Getter
    @Setter
    private LinkedHashMap<String, Integer> listenedEpisodes;
    @Getter
    @Setter
    private LinkedHashMap<String, Integer> mostListenedArtists;
    @Getter
    @Setter
    private LinkedHashMap<String, Integer> mostListenedArtistsPremium;
    @Getter
    @Setter
    private LinkedHashMap<String, Integer> mostListenedGenres;
    @Getter
    private LinkedHashMap<String, Integer> fans;
    @Getter
    @Setter
    private boolean premium;
    @Getter
    @Setter
    private double songRevenue;
    @Getter
    @Setter
    private LinkedHashMap<String, Double> mostProfitableSong;
    @Getter
    @Setter
    private String playerType;
    @Getter
    @Setter
    private LinkedHashMap<String, Integer> mostListenedArtistsBreak;
    @Getter
    @Setter
    private LinkedHashMap<String, Integer> listenedSongsBreak;
    @Getter
    @Setter
    private boolean breakStatus = false;
    @Getter
    @Setter
    private double breakValue;
    @Getter
    @Setter
    private ArrayList<Observer> subscriptions = new ArrayList<>();
    @Getter
    @Setter
    private int subscribers = 0;
    @Getter
    @Setter
    private ArrayList<Notifications> notif;
    @Getter
    @Setter
    private Double merchRevenue;
    @Getter
    @Setter
    private ArrayList<String> myMerches;

    /**
     * Instantiates a new User.
     *
     * @param username the username
     * @param age      the age
     * @param city     the city
     */
    public User(final String username, final int age, final String city) {
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
        podcasts = new ArrayList<>();
        announcements = new ArrayList<>();
        listenedEpisodes = new LinkedHashMap<>();
        listenedSongsPremium = new LinkedHashMap<>();
        listenedSongs = new LinkedHashMap<>();
        mostListenedArtists = new LinkedHashMap<>();
        mostListenedArtistsPremium = new LinkedHashMap<>();
        mostListenedGenres = new LinkedHashMap<>();
        listenedAlbums = new LinkedHashMap<>();
        fans = new LinkedHashMap<>();
        artistListenedSongs = new LinkedHashMap<>();
        premium = false;
        mostProfitableSong = new LinkedHashMap<>();
        playerType = null;
        listenedSongsBreak = new LinkedHashMap<>();
        mostListenedArtistsBreak = new LinkedHashMap<>();
        breakStatus = false;
        notif = new ArrayList<>();
        myMerches = new ArrayList<>();
        merchRevenue = 0.0;
    }
    /**
     * Search array list.
     * This method is used for searching artists, songs, playlists, podcasts and hosts
     * None of the searches types can be done if the current user is offline
     * @param filters the filters
     * @param type    the type
     * @return the array list
     */
    public ArrayList<String> search(final Filters filters, final String type) {
        if (!this.connectionStatus) {
            ArrayList<String> message = new ArrayList<>();
            message.add(" is offline.");
            return message;
        }
        searchBar.clearSelection();
        player.stop();
        lastSearched = true;
        ArrayList<String> newResults = new ArrayList<>();
        List<LibraryEntry> libraryEntries = searchBar.search(filters, type);
        artistSelected = 0;
        for (LibraryEntry libraryEntry : libraryEntries) {
            newResults.add(libraryEntry.getName());
        }
        isUsed = false;
        return newResults;
    }
    /**
     * Select string.
     *
     * @param itemNumber the item number
     * @return the string
     */
    public String select(final int itemNumber) {
        if (!lastSearched) {
            return "Please conduct a search before making a selection.";
        }
        lastSearched = false;
        LibraryEntry selected = searchBar.select(itemNumber);

        if (selected == null) {
            return "The selected ID is too high.";
        }
        /* if searchBar.getType() is 1, it means that the user
            searched for an artist or a host, and the user will be on the artist's or host's page*/
        if (searchBar.getSearchType() == 1) {
            page = ((User) selected).getPage();
            return "Successfully selected %s's page.".formatted(selected.getName());
        }

        return "Successfully selected %s.".formatted(selected.getName());
    }

    /**
     * Load string.
     *
     * @return the string
     */
    public String load() {
        breakStatus = false;
        if (searchBar.getLastSelected() == null) {
            return "Please select a source before attempting to load.";
        }
        if (!searchBar.getLastSearchType().equals("song")
                && ((AudioCollection) searchBar.getLastSelected()).getNumberOfTracks() == 0) {
            return "You can't load an empty audio collection!";
        }

        player.setSource(searchBar.getLastSelected(), searchBar.getLastSearchType());
        if (searchBar.getLastSearchType().equals("song")) {
            Integer currentCount = this.listenedSongs.getOrDefault(searchBar.getLastSelected().getName(), 0);
            this.listenedSongs.put(searchBar.getLastSelected().getName(), currentCount + 1);
            if (!isPremium()) {
                Integer currentCountBreak = this.listenedSongsBreak.getOrDefault(searchBar.getLastSelected().getName(), 0);
                this.listenedSongsBreak.put(searchBar.getLastSelected().getName(), currentCountBreak + 1);
            }
            if (Admin.getInstance().getUser(username).isPremium()) {
                Integer currentCountPremium = this.listenedSongsPremium.getOrDefault(searchBar.getLastSelected().getName(), 0);
                this.listenedSongsPremium.put(searchBar.getLastSelected().getName(), currentCountPremium + 1);
            }
            for (Album album: Admin.getInstance().getAlbums()) {
                if (album.getSongs().contains((Song)searchBar.getLastSelected())) {
                    Song song = (Song) searchBar.getLastSelected();
                    Integer currCnt = listenedAlbums.getOrDefault(album.getName(), 0);
                    this.listenedAlbums.put(album.getName(), currCnt + 1);
                    this.addListenedArtist(song.getArtist());
                    if (Admin.getInstance().getUser(username).isPremium()) {
                        this.addListenedArtistPremium(song.getArtist());
                    }
                    if (!isPremium()) {
                        this.addListenedArtistBreak(song.getArtist());
                    }

                    this.addListenedGenre(song.getGenre());
                    User artist = Admin.getInstance().getUser(song.getArtist());
                    if (artist != null) {
                        Integer countArtist = artist.getListenedSongs().getOrDefault(song.getName(), 0);
                        Integer countArtistsAlbum = artist.getListenedAlbums().getOrDefault(album.getName(), 0);
                        Integer fansCount = artist.getFans().getOrDefault(username, 0);

                        artist.getListenedSongs().put(song.getName(), countArtist + 1);
                        artist.getListenedAlbums().put(album.getName(), countArtistsAlbum + 1);

                        artist.getFans().put(username, fansCount + 1);
                    }
                }
            }
        } else if (searchBar.getLastSearchType().equals("album")) {
            Song firstSongInAlbum = ((Album) searchBar.getLastSelected()).getSongs().get(0);
            Integer currentCount = this.getListenedSongs().getOrDefault(firstSongInAlbum.getName(), 0);
            Integer currCnt = this.getListenedAlbums().getOrDefault(((Album) searchBar.getLastSelected()).getName(), 0);
            this.getListenedAlbums().put(((Album) searchBar.getLastSelected()).getName(), currCnt + 1);
            this.getListenedSongs().put(firstSongInAlbum.getName(), currentCount + 1);
            if (Admin.getInstance().getUser(username).isPremium()) {
                Integer currentCountPremium = this.listenedSongsPremium.getOrDefault(firstSongInAlbum.getName(), 0);
                this.listenedSongsPremium.put(firstSongInAlbum.getName(), currentCountPremium + 1);
            }
            if (!isPremium()) {
                Integer currentCountBreak = this.listenedSongsBreak.getOrDefault(firstSongInAlbum.getName(), 0);
                this.listenedSongsBreak.put(firstSongInAlbum.getName(), currentCountBreak + 1);
                this.addListenedArtistBreak((firstSongInAlbum).getArtist());
            }

            this.addListenedGenre((firstSongInAlbum).getGenre());
            this.addListenedArtist((firstSongInAlbum).getArtist());

            if (Admin.getInstance().getUser(username).isPremium()) {
                this.addListenedArtistPremium((firstSongInAlbum).getArtist());
            }
            for (Album album: Admin.getInstance().getAlbums()) {
                if (album.getName().equals(((Album) searchBar.getLastSelected()).getName()) && album.getSongs().contains(firstSongInAlbum)) {
                    User artist = Admin.getInstance().getUser(album.getOwner());
                    if (artist != null) {
                        Integer countArtist = artist.getListenedSongs().getOrDefault(firstSongInAlbum.getName(), 0);
                        Integer countArtistsAlbum = artist.getListenedAlbums().getOrDefault(album.getName(), 0);
                        Integer fansCount = artist.getFans().getOrDefault(this.getName(), 0);

                        artist.getListenedSongs().put(firstSongInAlbum.getName(), countArtist + 1);
                        artist.getListenedAlbums().put(album.getName(), countArtistsAlbum + 1);
                        artist.getFans().put(this.getName(), fansCount + 1);
                    }
                    break;
                }
            }
        }
        searchBar.clearSelection();

        player.pause();

        return "Playback loaded successfully.";
    }
    /**
     * Play pause string.
     *
     * @return the string
     */
    public String playPause() {
        if (player.getCurrentAudioFile() == null) {
            return "Please load a source before attempting to pause or resume playback.";
        }
        player.pause();

        if (player.getPaused()) {
            return "Playback paused successfully.";
        } else {
            return "Playback resumed successfully.";
        }
    }
    /**
     * Repeat string.
     *
     * @return the string
     */
    public String repeat() {

        if (player.getCurrentAudioFile() == null) {
            return "Please load a source before setting the repeat status.";
        }
        Enums.RepeatMode repeatMode = player.repeat();
        String repeatStatus = "";

        switch (repeatMode) {
            case NO_REPEAT:
                repeatStatus = "no repeat";
                break;
            case REPEAT_ONCE:
                repeatStatus = "repeat once";
                break;
            case REPEAT_ALL:
                repeatStatus = "repeat all";
                break;
            case REPEAT_INFINITE:
                repeatStatus = "repeat infinite";
                break;
            case REPEAT_CURRENT_SONG:
                repeatStatus = "repeat current song";
                break;
            default:
                return null;
        }
        return "Repeat mode changed to %s.".formatted(repeatStatus);
    }
    /**
     * Shuffle string.
     *
     * @param seed the seed
     * @return the string
     */
    public String shuffle(final Integer seed) {
        if (player.getCurrentAudioFile() == null) {
            return "Please load a source before using the shuffle function.";
        }
        if (!player.getType().equals("playlist") && !player.getType().equals("album")) {
            return "The loaded source is not a playlist or an album.";
        }

        player.shuffle(seed);

        if (player.getShuffle()) {
            return "Shuffle function activated successfully.";
        }
        return "Shuffle function deactivated successfully.";
    }
    /**
     * Forward string.
     *
     * @return the string
     */
    public String forward() {
        if (player.getCurrentAudioFile() == null) {
            return "Please load a source before attempting to forward.";
        }

        if (!player.getType().equals("podcast")) {
            return "The loaded source is not a podcast.";
        }

        player.skipNext();

        return "Skipped forward successfully.";
    }
    /**
     * Backward string.
     *
     * @return the string
     */
    public String backward() {
        if (player.getCurrentAudioFile() == null) {
            return "Please select a source before rewinding.";
        }

        if (!player.getType().equals("podcast")) {
            return "The loaded source is not a podcast.";
        }

        player.skipPrev();

        return "Rewound successfully.";
    }
    /**
     * Like string.
     *
     * @return the string
     */
    public String like() {
        if (!this.connectionStatus) {
            return this.username + " is offline.";
        }
        if (player.getCurrentAudioFile() == null) {
            return "Please load a source before liking or unliking.";
        }
        if (!player.getType().equals("song") && !player.getType().equals("playlist")
                && !player.getType().equals("album")) {
            return "Loaded source is not a song.";
    }

        Song song = (Song) player.getCurrentAudioFile();

        if (likedSongs.contains(song)) {
            likedSongs.remove(song);
            song.dislike();
            for (Song song1: Admin.getInstance().getSongs()) {
                if (song1.getName().equals(song.getName())) {
                    if (!song1.getLikes().equals(song.getLikes())) {
                        song1.setLikes(song.getLikes());
                    }
                }
            }
            return "Unlike registered successfully.";
        }
        likedSongs.add(song);
        song.like();
        for (Song song1: Admin.getInstance().getSongs()) {
            if (song1.getName().equals(song.getName())) {
                if (!song1.getLikes().equals(song.getLikes())) {
                    song1.setLikes(song.getLikes());
                }
            }
        }
        return "Like registered successfully.";
    }
    /**
     * Next string.
     *
     * @return the string
     */
    public String next() {
        if (player.getCurrentAudioFile() == null) {
            return "Please load a source before skipping to the next track.";
        }

        player.next();

        if (player.getCurrentAudioFile() == null) {
            return "Please load a source before skipping to the next track.";
        }

        return ("Skipped to next track successfully. "
                + "The current track is %s.").formatted(player.getCurrentAudioFile().getName());
    }
    /**
     * Prev string.
     *
     * @return the string
     */
    public String prev() {
        if (player.getCurrentAudioFile() == null) {
            return "Please load a source before returning to the previous track.";
        }

        player.prev();

        return ("Returned to previous track successfully. "
                + "The current track is %s.").formatted(player.getCurrentAudioFile().getName());
    }
    /**
     * Create playlist string.
     *
     * @param name      the name
     * @param timestamp the timestamp
     * @return the string
     */
    public String createPlaylist(final String name, final int timestamp) {
        if (playlists.stream().anyMatch(playlist -> playlist.getName().
                equals(name))) {
            return "A playlist with the same name already exists.";
    }
        Playlist playlist = new Playlist(name, username, timestamp);
        playlists.add(playlist);
        playlist.registerObserver(this);
        return "Playlist created successfully.";
    }
    /**
     * Add remove in playlist string.
     *
     * @param id the id
     * @return the string
     */
    public String addRemoveInPlaylist(final int id) {
        if (player.getCurrentAudioFile() == null) {
            return "Please load a source before adding to "
                   + "or removing from the playlist.";
        }

        if (player.getType().equals("podcast")) {
            return "The loaded source is not a song.";
        }

        if (id > playlists.size()) {
            return "The specified playlist does not exist.";
        }

        Playlist playlist = playlists.get(id - 1);

        if (playlist.containsSong((Song) player.getCurrentAudioFile())) {
            playlist.removeSong((Song) player.getCurrentAudioFile());
            return "Successfully removed from playlist.";
        }

        playlist.addSong((Song) player.getCurrentAudioFile());
        return "Successfully added to playlist.";
    }
    /**
     * Switch playlist visibility string.
     *
     * @param playlistId the playlist id
     * @return the string
     */
    public String switchPlaylistVisibility(final Integer playlistId) {
        if (playlistId > playlists.size()) {
            return "The specified playlist ID is too high.";
        }

        Playlist playlist = playlists.get(playlistId - 1);
        playlist.switchVisibility();

        if (playlist.getVisibility() == Enums.Visibility.PUBLIC) {
            return "Visibility status updated successfully to public.";
        }

        return "Visibility status updated successfully to private.";
    }
    /**
     * Show playlists array list.
     *
     * @return the array list
     */
    public ArrayList<PlaylistOutput> showPlaylists() {
        ArrayList<PlaylistOutput> playlistOutputs = new ArrayList<>();
        for (Playlist playlist : playlists) {
            playlistOutputs.add(new PlaylistOutput(playlist));
        }

        return playlistOutputs;
    }
    /**
     * Follow string.
     *
     * @return the string
     */
    public String follow() {
        LibraryEntry selection = searchBar.getLastSelected();
        String type = searchBar.getLastSearchType();

        if (selection == null) {
            return "Please select a source before following or unfollowing.";
        }

        if (!type.equals("playlist")) {
            return "The selected source is not a playlist.";
        }

        Playlist playlist = (Playlist) selection;

        if (playlist.getOwner().equals(username)) {
            return "You cannot follow or unfollow your own playlist.";
        }

        if (followedPlaylists.contains(playlist)) {
            followedPlaylists.remove(playlist);
            playlist.decreaseFollowers();

            return "Playlist unfollowed successfully.";
        }

        followedPlaylists.add(playlist);
        playlist.increaseFollowers();

        Notifications notification = new Notifications("New follow", "New follow from " + username + ".");
        playlist.notifyObservers(notification);
        return "Playlist followed successfully.";
    }
    /**
     * Gets player stats.
     *
     * @return the player stats
     */
    public PlayerStats getPlayerStats() {
        return player.getStats();
    }
    /**
     * Show preferred songs array list.
     *
     * @return the array list
     */
    public ArrayList<String> showPreferredSongs() {
        ArrayList<String> newResults = new ArrayList<>();
        for (AudioFile audioFile : likedSongs) {
            newResults.add(audioFile.getName());
        }

        return newResults;
    }
    /**
     * Gets preferred genre.
     *
     * @return the preferred genre
     */
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
    /**
     *This method verifies if a specific user has an album with given name
     * if the album doesn t exist it can be added in user's album list
     */
    public Album addAlbum(final Album album) {
        if (albums.contains(album.getName())) {
            return null;
        } else {
            Notifications notification = new Notifications("New Album", "New Album from " + this.getName() + ".");
            notifyObservers(notification);
            albums.add(album);
            return album;
        }
    }
    /**
     * Adds new podcast if it's  possible
     */
    public Podcast addPodcast(final Podcast podcast) {
        if (podcasts.contains(podcast.getName())) {
            return null;
        } else {
            Notifications notification = new Notifications("New Podcast", "New Podcast from " + this.getName() + ".");
            notifyObservers(notification);
            podcasts.add(podcast);
            return podcast;
        }
    }
    /**
     * Adds new event if it's possible
     */
    public Event addEvent(final Event event) {
        for (Event event1: events) {
            if (event1.getName().equals(event.getName())) {
                return null;
            }
        }
        Notifications notification = new Notifications("New Event", "New Event from " + this.getName() + ".");
        notifyObservers(notification);
        events.add(event);
        return event;
    }
    /**
     * Adds new merch if it's possible
     */
    public Merch addMerch(final Merch merch) {
        for (Merch merch1: merches) {
            if (merch1.getName().equals(merch.getName())) {
                return null;
            }
        }
        Notifications notification = new Notifications("New Merchandise", "New Merchandise from " + this.getName() + ".");
        notifyObservers(notification);
        merches.add(merch);
        return merch;
    }
    /**
     * Adds new announcement if it's possible
     */
    public Announcement addAnnouncement(final Announcement announcement) {
        for (Announcement announcement1: announcements) {
            if (announcement1.getName().equals(announcement.getName())) {
                return null;
            }
        }
        Notifications notification = new Notifications("New Announcement",
                "New Announcement from " + this.getName()  + ".");
        notifyObservers(notification);
        announcements.add(announcement);
        return announcement;
    }
    /**
     * gets the name of all albums of a user
     */
    public ArrayList<String> getAlbumsList() {
        ArrayList<String> albumList = new ArrayList<>();
        for (Album album: this.albums) {
            String message;
            message = album.getName();
            albumList.add(message);
        }
        return albumList;
    }
    /**
     * takes the name, price and description from the merch list of the current user
     */
    public ArrayList<String> getMerchesList() {
        ArrayList<String> merchList = new ArrayList<>();
        for (Merch merch: this.merches) {
            String message;
            message = merch.getName() +  " - " + (int) (merch.getPrice())
                    + ":\n\t" + merch.getDescription();
            merchList.add(message);
        }
        return merchList;
    }
    /**
     * takes the name, date and description from the event list of the current user
     */
    public ArrayList<String> getEventsList() {
        ArrayList<String> eventList = new ArrayList<>();
        for (Event event: this.events) {
            String message;
            message = event.getName() +  " - " + event.getDate()
                    + ":\n\t" + event.getDescription();
            eventList.add(message);
        }
        return eventList;
    }
    /**
     * takes the name, price and description from the announcement list of the current user
     */
    public ArrayList<String> getAnnouncementsList() {
        ArrayList<String> announcementsList = new ArrayList<>();
        for (Announcement announcement: this.announcements) {
            String message;
            message = announcement.getName() + ":\n\t"
                    + announcement.getDescription() + "\n";
            announcementsList.add(message);
        }
        return announcementsList;
    }
    /**
     *takes the name, price and description from the podcast list of the current user
     */
    public ArrayList<String> getPodcastsList() {
        ArrayList<String> podcastsList = new ArrayList<>();
        for (Podcast podcast: this.podcasts) {
            String message1;
            ArrayList<String> message2 = new ArrayList<>();
            String message;
            message1 = podcast.getName() + ":\n\t";
            for (Episode episode: podcast.getEpisodes()) {
                message2.add(episode.getName() + " - " + episode.getDescription());
            }
            message = message1 + message2 + "\n";
            podcastsList.add(message);
        }
        return podcastsList;
    }
    /**
     * takes the name, price and description from the likeSongs list of the current user
     */
    public ArrayList<String> getLikedSongsList() {
        ArrayList<String> likedSongsList = new ArrayList<>();
        for (Song song: this.getLikedSongs()) {
            String message;
            message = song.getName() + " - " + song.getArtist();
            likedSongsList.add(message);
        }
        return likedSongsList;
    }
    /**
     * takes the name, price and description from the followedPlaylist list of the current user
     */
    public ArrayList<String> getFollowedPlaylistsList() {
        ArrayList<String> followedPlaylistsList = new ArrayList<>();
        for (Playlist playlist: this.getFollowedPlaylists()) {
            String message;
            message = playlist.getName() + " - " + playlist.getOwner();
            followedPlaylistsList.add(message);
        }
        return followedPlaylistsList;
    }

    public void addListenedArtist(String artist) {
        Integer currentCount = this.mostListenedArtists.getOrDefault(artist, 0);
        this.mostListenedArtists.put(artist, currentCount + 1);
    }

    public void addListenedArtistPremium(String artist) {
        Integer currentCount = this.mostListenedArtistsPremium.getOrDefault(artist, 0);
        this.mostListenedArtistsPremium.put(artist, currentCount + 1);
    }

    public void addListenedArtistBreak(String artist) {
        Integer currentCount = this.mostListenedArtistsBreak.getOrDefault(artist, 0);
        this.mostListenedArtistsBreak.put(artist, currentCount + 1);
    }

    public void addListenedGenre(String genre) {
        Integer currentCount = this.mostListenedGenres.getOrDefault(genre, 0);
        this.mostListenedGenres.put(genre, currentCount + 1);
    }

    public void orderByNumOfListen() {
        LinkedHashMap<String, Integer> sortedMap = listenedSongs.entrySet()
                .stream()
                .sorted(Map.Entry.<String, Integer>comparingByValue(Comparator.reverseOrder())
                        .thenComparing(Map.Entry.comparingByKey()))
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (e1, e2) -> e1,
                        LinkedHashMap::new
                ));

        listenedSongs.clear();
        listenedSongs.putAll(sortedMap);

        sortedMap.clear();
        sortedMap = mostListenedArtists.entrySet()
                .stream()
                .sorted(Map.Entry.<String, Integer>comparingByValue(Comparator.reverseOrder())
                        .thenComparing(Map.Entry.comparingByKey()))
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (e1, e2) -> e1,
                        LinkedHashMap::new
                ));

        mostListenedArtists.clear();
        mostListenedArtists.putAll(sortedMap);

        sortedMap.clear();
        sortedMap = mostListenedGenres.entrySet()
                .stream()
                .sorted(Map.Entry.<String, Integer>comparingByValue(Comparator.reverseOrder())
                        .thenComparing(Map.Entry.comparingByKey()))
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (e1, e2) -> e1,
                        LinkedHashMap::new
                ));

        mostListenedGenres.clear();
        mostListenedGenres.putAll(sortedMap);

        sortedMap.clear();
        sortedMap = listenedAlbums.entrySet()
                .stream()
                .sorted(Map.Entry.<String, Integer>comparingByValue(Comparator.reverseOrder())
                        .thenComparing(Map.Entry.comparingByKey()))
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (e1, e2) -> e1,
                        LinkedHashMap::new
                ));

        listenedAlbums.clear();
        listenedAlbums.putAll(sortedMap);

        sortedMap.clear();
        sortedMap = fans.entrySet()
                .stream()
                .sorted(Map.Entry.<String, Integer>comparingByValue(Comparator.reverseOrder())
                        .thenComparing(Map.Entry.comparingByKey()))
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (e1, e2) -> e1,
                        LinkedHashMap::new
                ));

        fans.clear();
        fans.putAll(sortedMap);

        LinkedHashMap<String, Double> newSortedMap = new LinkedHashMap<>();
        newSortedMap = mostProfitableSong.entrySet()
                .stream()
                .sorted(Map.Entry.<String, Double>comparingByValue(Comparator.reverseOrder())
                        .thenComparing(Map.Entry.comparingByKey()))
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (e1, e2) -> e1,
                        LinkedHashMap::new
                ));

        mostProfitableSong.clear();
        mostProfitableSong.putAll(newSortedMap);
    }

    public String subscribe(String username) {
        User user = Admin.getInstance().getUser(username);
        if (user == null)
            return "The username " + username + "doesn't exist.";
        if (!user.getPage().getType().equals("artist") && !user.getPage().getType().equals("host")){
            return "To subscribe you need to be on the page of an artist or host.";
        }

        String ownerName = user.getPage().getOwner().getName();
        if (Admin.getInstance().getUser(ownerName).getSubscriptions().contains(Admin.getInstance().getUser(username))) {
            Admin.getInstance().getUser(ownerName).removeObserver(Admin.getInstance().getUser(username));
            return username + " unsubscribed from " + ownerName + " successfully.";
        }
        Admin.getInstance().getUser(ownerName).registerObserver(Admin.getInstance().getUser(username));

        return username + " subscribed to " + ownerName + " successfully.";
    }

    public String buyMerch(String username, String merchName) {
        User user = Admin.getInstance().getUser(username);
        if (user== null)
            return "The username " + username + " doesn't exist.";
        if (!user.getPage().getType().equals("artist"))
            return "Cannot buy merch from this page.";
        for (User artist: Admin.getInstance().getArtists()) {
            for (Merch merch: artist.merches)
                if (merch.getName().equals(merchName)) {
                    user.getMyMerches().add(merchName);
                    artist.setMerchRevenue(artist.getMerchRevenue() + merch.getPrice());
                    return username + " has added new merch successfully.";
                }
        }
        return "The merch " + merchName + " doesn't exist.";
    }


    /**
     * Simulates the passage of time in the player by advancing the playback position.
     * Invokes the player's simulatePlayer method with the provided time.
     *
     * @param time The simulated time in milliseconds to advance the player's
     *             playback position.
     * @see Player#simulatePlayer(int, User)
     */
    public void simulateTime(final int time) {
        player.simulatePlayer(time, this);
    }

    @Override
    public void receiveUpdate(Notifications notification) {
        if (!isArtist() && !isHost())
            notif.add(notification);
    }

    @Override
    public void registerObserver(Observer o) {
        subscriptions.add(o);
        this.subscribers++;
    }

    @Override
    public void removeObserver(Observer o) {
        subscriptions.remove(o);
        this.subscribers--;
    }

    @Override
    public void notifyObservers(Notifications notification) {
        if (isArtist()) {
            for (Observer subscription : subscriptions) {
                subscription.receiveUpdate(notification);
            }
        }
    }
}
