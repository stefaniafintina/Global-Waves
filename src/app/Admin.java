package app;

import app.Pages.ArtistsPage;
import app.Pages.HomePage;
import app.Pages.HostsPage;
import app.Pages.LikedContentPage;
import app.artistsPage.Merch;
import app.audio.Collections.Album;
import app.audio.Collections.Playlist;
import app.audio.Collections.Podcast;
import app.audio.Files.Episode;
import app.audio.Files.Song;
import app.artistsPage.Event;
import app.hostsPage.Announcement;
import app.player.Player;
import app.user.User;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import fileio.input.EpisodeInput;
import fileio.input.SongInput;
import fileio.input.UserInput;
import fileio.input.PodcastInput;
import lombok.Getter;
import lombok.Setter;

import java.util.*;

import static app.CommandRunner.objectMapper;

public final class Admin {
    public static final int MAGIC_NUMBER = 5;
    public static final int MAGIC_NUMBER3 = 3;
    public static final int MAGIC_NUMBER2 = 2;
    public static final int MAGIC_NUMBER0 = 0;
    public static final int MAGIC_NUMBER1 = 1;
    public static final int MAGIC_NUMBER4 = 4;
    public static final int MAGIC_NUMBER6 = 6;
    public static final int MAGIC_NUMBER7 = 7;
    public static final int MAGIC_NUMBER12 = 12;
    public static final int MAGIC_NUMBER31 = 31;
    public static final int MAGIC_NUMBER9 = 9;
    public static final int MAGIC_NUMBER8 = 8;
    public static final int MAGIC_NUMBER1900 = 1900;
    public static final int MAGIC_NUMBER2023 = 2023;
    public static final int MAGIC_NUMBER10 = 10;
    public static final int MAGIC_NUMBER100 = 100;
    public static final int MAGIC_NUMBER1000 = 1000;
    @Getter
    private List<User> users = new ArrayList<>();
    private List<Song> songs = new ArrayList<>();
    private List<Podcast> podcasts = new ArrayList<>();
    @Getter
    @Setter
    private List<User> artists = new ArrayList<>();
    @Getter
    @Setter
    private List<Album> allAlbums = new ArrayList<>();
    @Getter
    @Setter
    private List<User> hosts = new ArrayList<>();
    private int timestamp = 0;
    private static Admin instance = null;
    private boolean connectionStatus = true;
    public static Admin getInstance() {
        if (instance == null) {
            instance = new Admin();
        }
        return  instance;
    }
    private Admin() {
    }
    /**
     * Sets the list of users based on the provided list of user inputs.
     * Creates User objects from UserInput instances and initializes each
     * user with a HomePage.
     *
     * @param userInputList The list of UserInput instances containing user information.
     * @throws NullPointerException If the provided userInputList is null.
     */
    public void setUsers(final List<UserInput> userInputList) {
        users = new ArrayList<>();
        for (UserInput userInput : userInputList) {
            users.add(new User(userInput.getUsername(), userInput.getAge(), userInput.getCity()));
            users.get(users.size() - 1).setPage(new HomePage(users.get(users.size() - 1)));
        }
    }
    /**
     * Sets the list of songs based on the provided list of song inputs.
     * Creates Song objects from SongInput instances and initializes each song
     * with the specified attributes.
     *
     * @param songInputList The list of SongInput instances containing song information.
     * @throws NullPointerException If the provided songInputList is null.
     */
    public void setSongs(final List<SongInput> songInputList) {
        songs = new ArrayList<>();
        for (SongInput songInput : songInputList) {
            songs.add(new Song(songInput.getName(), songInput.getDuration(), songInput.getAlbum(),
                    songInput.getTags(), songInput.getLyrics(), songInput.getGenre(),
                    songInput.getReleaseYear(), songInput.getArtist()));
        }
    }
    /**
     * Sets the list of podcasts based on the provided list of podcast inputs.
     * Creates Podcast objects from PodcastInput instances and initializes
     * each podcast with episodes.
     *
     * @param podcastInputList The list of PodcastInput instances containing podcast information.
     * @throws NullPointerException If the provided podcastInputList is null.
     */
    public void setPodcasts(final List<PodcastInput> podcastInputList) {
        podcasts = new ArrayList<>();
        for (PodcastInput podcastInput : podcastInputList) {
            List<Episode> episodes = new ArrayList<>();
            for (EpisodeInput episodeInput : podcastInput.getEpisodes()) {
                episodes.add(new Episode(episodeInput.getName(), episodeInput.getDuration(),
                        episodeInput.getDescription()));
            }
            podcasts.add(new Podcast(podcastInput.getName(), podcastInput.getOwner(), episodes));
        }
    }
    /**
     */
    public List<Song> getSongs() {
        return new ArrayList<>(songs);
    }
    /**
     */
    public List<Podcast> getPodcasts() {
        return new ArrayList<>(podcasts);
    }
    /**
     */
    public List<Playlist> getPlaylists() {
        List<Playlist> playlists = new ArrayList<>();
        for (User user : users) {
            playlists.addAll(user.getPlaylists());
        }
        return playlists;
    }
    /**
     */
    public List<Album> getAlbums() {
        List<Album> albums = new ArrayList<>();
        for (User user : users) {
            if (user.getAlbums() != null) {
                albums.addAll(user.getAlbums());
            }
        }
        return albums;
    }
    /**
     */
    public User getUser(final String username) {
        for (User user : users) {
            if (user.getUsername().equals(username)) {
                return user;
            }
        }
        return null;
    }
    /**
     * The newly-added feature is updating the remained time
     * only if the user is online
     */
    public void updateTimestamp(final int newTimestamp, final String username) {

        int elapsed = newTimestamp - timestamp;
        timestamp = newTimestamp;
        if (elapsed == 0) {
            return;
        }
        for (User user : users) {
            if (user.isConnectionStatus()) {
                user.simulateTime(elapsed);
            }
        }
    }
    /**
     * Gets top 5 songs.
     *
     * @return the top 5 songs
     */
    public List<String> getTop5Songs() {

        List<Song> sortedSongs = new ArrayList<>(getSongs());
        sortedSongs.sort(Comparator.comparingInt(Song::getLikes).reversed().
                thenComparing(getSongs()::indexOf));
        List<String> topSongs = new ArrayList<>();
        int count = 0;

        for (Song song : sortedSongs) {
            if (count >= MAGIC_NUMBER) {
                break;
            }
            topSongs.add(song.getName());
            count++;
        }
        return topSongs;
    }
    /**
     * Gets top 5 playlists.
     *
     * @return the top 5 playlists
     */
    public List<String> getTop5Playlists() {
        List<Playlist> sortedPlaylists = new ArrayList<>(getPlaylists());
        sortedPlaylists.sort(Comparator.comparingInt(Playlist::getFollowers)
                .reversed()
                .thenComparing(Playlist::getTimestamp, Comparator.naturalOrder()));
        List<String> topPlaylists = new ArrayList<>();
        int count = 0;
        for (Playlist playlist : sortedPlaylists) {
            if (count >= MAGIC_NUMBER) {
                break;
            }
            topPlaylists.add(playlist.getName());
            count++;
        }
        return topPlaylists;
    }
    /**
     * the top5 albums are chosen by the following criteria:
     * the album with the most liked, based on the number of likes of each song
     * if number of likes of 2 different albums leads to comparing their names
     */
    public ArrayList<String> getTop5Albums() {
        ArrayList<Album> albumLikes = new ArrayList<>();

        for (User user: users) {
            for (Album album : user.getAlbums()) {
                int likeCounter = 0;
                for (Song song : album.getSongs()) {
                    likeCounter += song.getLikes();
                }
                album.setLikes(likeCounter);
                albumLikes.add(album);
            }
        }
        albumLikes.sort(Comparator.comparingInt(Album::getLikes).reversed().
                thenComparing(Album:: getName));

        ArrayList<String> albumsList = new ArrayList<>();
        int cnt = 0;
        for (Album album: albumLikes) {
            if (cnt < MAGIC_NUMBER) {
                albumsList.add(album.getName());
            } else {
                break;
            }
            cnt++;
        }
        return albumsList;
    }
    /**
     * Retrieves a list of the top 5 artists based on the cumulative likes received for
     * their songs across all users.
     * If artists have equal total likes, they are sorted by their names in ascending order.
     * this comparison is made by using a HashMAp that stores key artist name
     * and value their number of likes
     * @return An ArrayList containing the names of the top 5 artists.
     */
    public ArrayList<String> getTop5Artists() {
        HashMap<String, Integer> mostLikedArtists = new HashMap<>();
        for (User user: users) {
            int likeCounter = 0;
            for (Album album : user.getAlbums()) {
                for (Song song : album.getSongs()) {
                    likeCounter += song.getLikes();
                }
            }
            mostLikedArtists.put(user.getName(), likeCounter);
        }
        List<Map.Entry<String, Integer>> entryList = new ArrayList<>(mostLikedArtists.entrySet());

        entryList.sort(Map.Entry.<String, Integer>comparingByValue(Comparator.reverseOrder())
                .thenComparing(Map.Entry.comparingByKey()));
        ArrayList<String> albumsList = new ArrayList<>();
        int cnt = 0;
        for (Map.Entry<String, Integer> entry : entryList) {
            if (cnt < MAGIC_NUMBER) {
                albumsList.add(entry.getKey());
            } else {
                break;
            }
            cnt++;
        }
        return albumsList;
    }
    /**
     * Checks if there is a user with the given username in the Admin database.
     *
     * @param username The username to check for existence in the Admin database.
     * @return The User object if a user with the specified username is found,
     * otherwise returns null.
     */
    public User checkIfUserExists(final String username) {
        for (User user: users) {
            if (user.getUsername().equals(username)) {
                return user;
            }
        }
        return null;
    }
    /**
     * this method check if the user is online or offline and updates their
     * status to offline or online(respectively) and return the new status
     */
    public boolean switchConnectionStatus(final String username) {
        for (User user: users) {
            if (user.getUsername().equals(username)) {
                if (user.isConnectionStatus()) {
                    user.setConnectionStatus(false);
                    connectionStatus = false;
                    return user.isConnectionStatus();
                } else {
                    connectionStatus = true;
                    user.setConnectionStatus(true);
                    return user.isConnectionStatus();
                }
            }
        }
        return false;
    }
    /**
     * Retrieves a list of usernames for users who are currently online in the Admin database.
     * Excludes users who are marked as hosts or artists.
     *
     * @return A List of usernames for online users excluding hosts and artists.
     */
    public List<String> getOnlineUsers() {
        List<String> onlineUsers = new ArrayList<>();
        for (User user: users) {
            if (user.isConnectionStatus()) {
                if (!user.isHost() && !user.isArtist()) {
                    onlineUsers.add(user.getUsername());
                }
            }
        }
        return onlineUsers;
    }
    /**
     * check if a user is online and return it
     */
    public int isOnline(final String username) {
        for (User user: users) {
            if (user.getUsername().equals(username)) {
                if (user.isConnectionStatus()) {
                    return 1;
                }
                return 0;
            }
        }
        return 0;
    }
    /**
     * check if user's type is host
     */
    public boolean checkIfHost(final String username) {
        for (User user: users) {
            if (user.getUsername().equals(username)) {
                return user.isHost();
            }
        }
        return false;
    }
    /**
     * check if user's type is artist
     */
    public boolean checkIfArtist(final String username) {
        for (User user: users) {
            if (user.getUsername().equals(username)) {
                return user.isArtist();
            }
        }
        return false;
    }
    /**
     * if the current user doesn't exist in current database, it can successfully be added
     * artists list is updated if the current user is an artist, and the same for host
     */
    public void addUser(final User user) {
        if (checkIfUserExists(user.getUsername()) == null) {
            users.add(user);
            user.setPage(new HomePage(user));
            if (user.isArtist()) {
                artists.add(user);
                user.setPage(new ArtistsPage(user));
            } else if (user.isHost()) {
                hosts.add(user);
                user.setPage(new HostsPage(user));
            }
        }
    }
    /**
     * Adds a new album to the user's profile with the specified details and song list.
     * it also verifies if this user already has this album,
     * or if this album has the same song twice
     * @param username     The username of the artist adding the album.
     * @param name         The name of the new album.
     * @param songsList    The list of songs to be included in the album.
     * @param releaseYear  The release year of the album.
     * @param description  The description of the album.
     * @return A message indicating the result of the album addition process.
     *         Returns success message if the album is added successfully, or an error
     *         message otherwise.
     */
    public String addAlbum(final String username, final String name,
                                  final ArrayList<SongInput> songsList, final Integer releaseYear,
                                  final String description) {
        HashMap<String, Integer> numberOfAppearance = new HashMap<>();
        for (User myUser: users) {
            if (myUser.getUsername().equals(username)) {
                if (!myUser.isArtist()) {
                    return myUser.getUsername() + " is not an artist.";
                }
                if (myUser.getAlbums() != null) {
                    for (Album album : myUser.getAlbums()) {
                        if (album.getName().equals(name)) {
                            return myUser.getUsername() + " has another album with the same name.";
                        }
                    }
                }
                for (SongInput song: songsList) {
                    numberOfAppearance.put(song.getName(), numberOfAppearance.
                            getOrDefault(song.getName(), 0) + 1);
                    if (numberOfAppearance.getOrDefault(song.getName(), 0) >= 2) {
                        return myUser.getUsername() + " has the same song at least "
                                + "twice in this album.";
                    }
                }
                // Create and add the new album to the user's profile
                Album album = new Album(name, username, releaseYear, description);
                for (SongInput song: songsList) {
                    Song newSong = new Song(song.getName(), song.getDuration(), song.getAlbum(),
                            song.getTags(), song.getLyrics(), song.getGenre(),
                            song.getReleaseYear(), song.getArtist());
                    album.addSong(newSong);
                    if (!songs.contains(newSong)) {
                        songs.add(newSong);
                    }
                }
                myUser.addAlbum(album);
                // Add new songs to the global songs list if they don't already exist

                return myUser.getUsername() + " has added new album successfully.";
            }
        }
        return "The username " + username + " doesn't exist.";
    }
    /**
     * Removes the given album if it s possible
     *
     * there are several cases when the album can not be deleted:
     * ->if another user is on artist's page (artists that have
     * the album we want to delete)
     * ->if a song from this album  is now on play or the album itself it s on play
     */
    public String removeAlbum(final String username, final String name) {
        boolean existentAlbum = false;
        if (checkIfUserExists(username) == null) {
            return "The username " + username + " doesn't exist.";
        }
        if (!checkIfArtist(username)) {
            return username + " is not an artist.";
        }
        for (Album album: getUser(username).getAlbums()) {
            if (album.getName().equals(name)) {
                existentAlbum  = true;
                break;
            }
        }
        if (!existentAlbum) {
            return username + " doesn't have an album with the given name.";
        }
        for (User user1 : users) {
            if (!user1.getName().equals(username)) {
                if (user1.getPage().getOwner().getName().equals(getUser(username).
                        getPage().getOwner().getName())) {
                    return username + " can't delete this album.";
                }
                if (user1.getPlayer().getSource() != null) {
                    if (user1.getPlayer().getSource().getAudioFile().getName().equals(name)) {
                        return username + " can't delete this album.";
                    }
                    for (Album album: getAlbums()) {
                        if (album.getName().equals(name)) {
                            for (Song song: album.getSongs()) {
                                if (song.getName().equals(user1.getPlayer().getSource().
                                        getAudioFile().getName())) {
                                    return username + " can't delete this album.";
                                }
                            }
                        }
                    }
                }
            }
        }
        if (getUser(username) != null) {
            for (Album album: getUser(username).getAlbums()) {
                if (album.getName().equals(name)) {
                    for (Song song: album.getSongs()) {
                        songs.remove(song);
                    }
                    getUser(username).getAlbums().remove(album);
                    return username + " has successfully deleted the album.";
                }
            }
        }
        return null;
    }
    /**
     * Retrieves a JSON array node containing information about the albums of a specific user.
     *
     * @param username The username of the user whose albums are to be displayed.
     * @return An ArrayNode containing information about the albums, or null if the
     * username is not found.
     */
    public ArrayNode showAlbums(final String username) {
        ObjectNode objectNode = objectMapper.createObjectNode();
        for (User user: users) {
            if (user.getUsername().equals(username)) {
                ArrayNode objectNode2 = objectNode.putArray("result");
                for (Album album: user.getAlbums()) {
                    ArrayList<String> songsName = new ArrayList<>();
                    for (Song song: album.getSongs()) {
                        songsName.add(song.getName());
                    }
                    ObjectNode objectNode3 = objectMapper.createObjectNode();
                    objectNode3.put("name", album.getName());
                    objectNode3.put("songs", objectMapper.valueToTree(songsName));
                    objectNode2.add(objectNode3);
                }
                return objectNode2;
            }
        }
        return null;
    }
    /**
     * verifies the date
     */
    public int validDate(final String date) {
        if (date.charAt(MAGIC_NUMBER3) == '0' && date.charAt(MAGIC_NUMBER4) == '2') {
            if ((date.charAt(0) == MAGIC_NUMBER2 && date.charAt(MAGIC_NUMBER1) >= '9')
                    || (date.charAt(MAGIC_NUMBER0) >= '3')) {
                return 0;
            }
        } else {
            int nr = (date.charAt(MAGIC_NUMBER3) - '0') * MAGIC_NUMBER10
                    + (date.charAt(MAGIC_NUMBER4) - '0');
            if (nr > MAGIC_NUMBER12) {
                return 0;
            }
            nr = (date.charAt(MAGIC_NUMBER0) - '0') * MAGIC_NUMBER10
                    + (date.charAt(MAGIC_NUMBER1) - '0');
            if (nr > MAGIC_NUMBER31) {
                return 0;
            }
            nr = (date.charAt(MAGIC_NUMBER6) - '0') * MAGIC_NUMBER1000
                    + (date.charAt(MAGIC_NUMBER7)
                    - '0') * MAGIC_NUMBER100 + (date.charAt(MAGIC_NUMBER8) - '0')
                    * MAGIC_NUMBER10 + (date.charAt(MAGIC_NUMBER9) - '0');
            if (nr < MAGIC_NUMBER1900 || nr > MAGIC_NUMBER2023) {
                return 0;
            }
        }
        return 1;
    }
    /**
     * Adds a new event to the artist's profile with the specified details.
     *
     * @param username    The username of the artist adding the event.
     * @param name        The name of the new event.
     * @param date        The date of the event.
     * @param description The description of the event.
     * @return A message indicating the result of the event addition process.
     *         Returns success message if the event is added successfully, or
     *         an error message otherwise.
     */
    public String addEvent(final String username, final String name, final String date,
                                  final String description) {
        Event myEvent = new Event(date, description, name);
        for (User user: users) {
            if (user.getUsername().equals(username)) {
                if (!user.isArtist()) {
                    return username + " is not an artist.";
                }
                for (Event event: user.getEvents()) {
                    if (event.getName().equals(name)) {
                        return username + " has another event with the same name.";
                    }
                }
                if (validDate(date) == 0) {
                    return "Event for " + username + " does not have a valid date.";
                }
                user.addEvent(myEvent);
                return username + " has added new event successfully.";
            }
        }
        return "The username " + username + " doesn't exist.";
    }
    /**
     * Removes an event with the specified name from the artist's profile.
     *
     * @param username The username of the artist removing the event.
     * @param name     The name of the event to be removed.
     * @return A message indicating the result of the event removal process.
     *         Returns success message if the event is removed successfully,
     *         or an error message otherwise.
     */
    public String removeEvent(final String username, final String name) {
        if (checkIfUserExists(username) == null) {
            return username + " doesn't exist.";
        }
        if (!checkIfArtist(username)) {
            return username + " is not an artist.";
        }
        if (getUser(username) != null) {
            for (Event event: getUser(username).getEvents()) {
                if (event.getName().equals(name)) {
                    getUser(username).getEvents().remove(event);
                    return username + " deleted the event successfully.";
                }
            }
            return username + " doesn't have an event with the given name.";
        }
        return null;
    }
    /**
     * Adds new merchandise to the artist's profile with the specified details.
     *
     * @param username    The username of the artist adding the merchandise.
     * @param name        The name of the new merchandise.
     * @param price       The price of the merchandise.
     * @param description The description of the merchandise.
     * @return A message indicating the result of the merchandise addition process.
     *         Returns success message if the merchandise is added successfully, or an
     *         error message otherwise.
     */
    public String addMerch(final String username, final String name,
                                  final double price, final String description) {
        Merch myMerch = new Merch(price, description, name);
        for (User user: users) {
            if (user.getUsername().equals(username)) {
                if (!user.isArtist()) {
                    return username + " is not an artist.";
                }
                for (Merch merch: user.getMerches()) {
                    if (merch.getName().equals(name)) {
                        return username + " has merchandise with the same name.";
                    }
                }
                if (price < 0) {
                    return "Price for merchandise can not be negative.";
                }
                user.addMerch(myMerch);
                return username + " has added new merchandise successfully.";
            }
        }
        return "The username " + username + " doesn't exist.";
    }
    /**
     * Deletes a user if none of its audio file is on play or
     * other users are on its page in case the user is a host or an artist
     * if the user is not an artist nor a host it can be deleted no matter what
     * in case of success all information about the user should be
     * deleted from the global database
     */
    public String deleteUser(final String username) {
        for (User user:  users) {
            if (user.getName().equals(username)) {
                for (User user1 : users) {
                    if (!user1.getName().equals(username)) {
                        if (user1.getPage().getOwner().getName().equals(user.getPage().
                                getOwner().getName())) {
                            return username + " can't be deleted.";
                        }
                        if (user1.getPlayer().getSource() != null) {
                            for (Song song : songs) {
                                if (user1.getPlayer().getSource().getAudioFile().getName().
                                        equals(song.getName())) {
                                    if (song.getArtist().equals(username)) {
                                        return username + " can't be deleted.";
                                    }
                                }
                            }
                            for (Album album : getAlbums()) {
                                for (Song song: album.getSongs()) {
                                    if (user1.getPlayer().getSource().getAudioFile().
                                            getName().equals(song.getName())) {
                                        if (album.getOwner().equals(username)) {
                                            return username + " can't be deleted.";
                                        }
                                    }
                                }
                            }
                            for (Album album : getAlbums()) {
                                if (user1.getPlayer().getSource().getAudioFile().getName().
                                        equals(album.getName())) {
                                    if (album.getOwner().equals(username)) {
                                        return username + " can't be deleted.";
                                    }
                                }
                            }
                            for (Playlist playlist : getPlaylists()) {
                                for (Song song: playlist.getSongs()) {
                                    if (user1.getPlayer().getSource().getAudioFile().getName().
                                            equals(song.getName())) {
                                        if (playlist.getOwner().equals(username)) {
                                            return username + " can't be deleted.";
                                        }
                                    }
                                }
                            }
                            for (Playlist playlist : getPlaylists()) {
                                if (user1.getPlayer().getSource().getAudioFile().getName().
                                        equals(playlist.getName())) {
                                    if (playlist.getOwner().equals(username)) {
                                        return username + " can't be deleted.";
                                    }
                                }
                            }
                            for (Podcast podcast : getPodcasts()) {
                                for (Episode episode: podcast.getEpisodes()) {
                                    if (user1.getPlayer().getSource().getAudioFile().getName().
                                            equals(episode.getName())) {
                                        if (podcast.getOwner().equals(username)) {
                                            return username + " can't be deleted.";
                                        }
                                    }
                                }
                            }
                            for (Podcast podcast : getPodcasts()) {
                                if (user1.getPlayer().getSource().getAudioFile().getName().
                                        equals(podcast.getName())) {
                                    if (podcast.getOwner().equals(username)) {
                                        return username + " can't be deleted.";
                                    }
                                }
                            }
                        }
                    }
                }
                for (Playlist playlist : getPlaylists()) {
                    for (Song song : songs) {
                        if (song.getArtist().equals(username)) {
                            playlist.removeSong(song);
                            break;
                        }
                    }
                }

                for (Playlist playlist : getUser(username).getFollowedPlaylists()) {
                    playlist.setFollowers(playlist.getFollowers() - 1);
                }

                for (Song song: getUser(username).getLikedSongs()) {
                    song.setLikes(song.getLikes() - 1);
                }

                getAlbums().removeIf(album -> album.getOwner().equals(username));
                for (User user1 : users) {
                    user1.getLikedSongs().removeIf(song -> song.getArtist().equals(username));

                    for (Song song : songs) {
                        if (song.getArtist().equals(username)) {
                            user1.getMostLikedSongs().remove(song.getName());
                        }
                    }

                    user1.getFollowedPlaylists().removeIf(playlist ->
                            playlist.getOwner().equals(username));

                    for (Playlist playlist : user1.getFollowedPlaylists()) {
                        for (Song song : playlist.getSongs()) {
                            if (song.getArtist().equals(username)) {
                                playlist.removeSong(song);
                            }
                        }
                        if (playlist.getSongs().isEmpty()) {
                            user1.getFollowedPlaylists().remove(playlist);
                        }
                    }
                }

                songs.removeIf(song -> song.getArtist().equals(username));
                users.remove(user);
                return username + " was successfully deleted.";
            }
        }
        return username + " doesn't exist";
    }
    /**
     * Retrieves a list of all users in the global database, categorized by user type.
     *
     * @return An ArrayList of usernames, including regular users, artists, and hosts.
     */
    public ArrayList<String> getAllUsers() {
        ArrayList<String> allUsers = new ArrayList<>();
        for (User user: users) {
            if (!user.isArtist() && !user.isHost()) {
                allUsers.add(user.getUsername());
            }
        }
        for (User user: users) {
            if (user.isArtist()) {
                allUsers.add(user.getUsername());
            }
        }
        for (User user: users) {
            if (user.isHost()) {
                allUsers.add(user.getUsername());
            }
        }
        return allUsers;
    }
    /**
     * Adds a new podcast to the global database under the specified host's profile.
     * Each podcast has a unique name, and each episode within the podcast also has a unique name.
     * The method checks if the host exists, is a host, and if there are no conflicts with
     * existing podcasts and episodes.
     *
     * @param username      The username of the host adding the podcast.
     * @param episodesList  A list of EpisodeInput objects representing episodes of the podcast.
     * @param name          The name of the new podcast.
     * @return A message indicating the result of the podcast addition process.
     *         Returns success message if the podcast is added successfully, or an error message
     *         otherwise.
     */
    public String addPodcast(final String username, final ArrayList<EpisodeInput>
            episodesList, final String name) {
        if (checkIfUserExists(username) == null) {
            return username + " doesn't exist.";
        }
        if (!checkIfHost(username)) {
            return username + " is not a host.";
        }
        for (User user:  users) {
            if (user.getName().equals(username)) {
                HashMap<String, Integer> numberOfAppearance = new HashMap<>();
                for (Podcast podcast : user.getPodcasts()) {
                    numberOfAppearance.put(podcast.getName(), 1);
                }
                if (numberOfAppearance.containsKey(name)) {
                    return user.getUsername() + " has another podcast with the same name.";
                }
                for (EpisodeInput episode : episodesList) {
                    if (numberOfAppearance.containsKey(episode.getName())) {
                        return user.getUsername() + " has another podcast with the same name.";
                    }
                    numberOfAppearance.put(episode.getName(), numberOfAppearance.
                            getOrDefault(episode.getName(), 0) + 1);
                }
                ArrayList<Episode> listOfEpisodes = new ArrayList<>();
                for (EpisodeInput episode : episodesList) {
                    listOfEpisodes.add(new Episode(episode.getName(), episode.getDuration(),
                            episode.getDescription()));
                }

                Podcast podcast = new Podcast(name, username, listOfEpisodes);
                podcasts.add(podcast);
                user.addPodcast(podcast);
                return username + " has added new podcast successfully.";
            }
        }
        return null;
    }
    /**
     * Removes a podcast from the global database under the specified host's profile.
     * The method checks if the host exists, is a host, and ensures that the podcast
     * and its episodes are not currently in use.
     * The removal process includes checking if any user is currently playing or has
     * the podcast in their playlist.
     *
     * @param username The username of the host removing the podcast.
     * @param name     The name of the podcast to be removed.
     * @return A message indicating the result of the podcast removal process.
     *         Returns success message if the podcast is removed successfully,
     *         or an error message otherwise.
     */
    public String removePodcast(final String username, final String name) {
        if (checkIfUserExists(username) == null) {
            return username + " doesn't exist.";
        }
        if (!checkIfHost(username)) {
            return username + " is not an artist.";
        }
        for (User user1 : users) {
            if (!user1.getName().equals(username)) {
                if (user1.getPlayer().getSource() != null) {
                    if (user1.getPlayer().getSource().getAudioFile().getName().
                            equals(name)) {
                        return username + " can't delete this podcast.";
                    }
                    for (Podcast podcast: getPodcasts()) {
                        if (podcast.getName().equals(name)) {
                            for (Episode episode: podcast.getEpisodes()) {
                                if (user1.getPlayer().getSource().getAudioFile().
                                        getName().equals(episode.getName())) {
                                    return username + " can't delete this podcast.";
                                }
                            }
                        }
                    }
                }
            }
        }
        if (getUser(username) != null) {
            for (Podcast podcast: getUser(username).getPodcasts()) {
                if (podcast.getName().equals(name)) {
                    getUser(username).getPodcasts().remove(podcast);
                    return username + " deleted the podcast successfully.";
                }
            }
            return username + " doesn't have a podcast with the given name.";
        }
        return null;
    }
    /**
     * Adds a new announcement under the specified host's profile.
     * Announcements are created with a unique name and a description.
     * The method checks if the host exists, is a host, and ensures that
     * the announcement name is unique.
     *
     * @param username    The username of the host adding the announcement.
     * @param name        The name of the new announcement.
     * @param description The description of the new announcement.
     * @return A message indicating the result of the announcement addition process.
     *         Returns success message if the announcement is added successfully, or
     *         an error message otherwise.
     */
    public String addAnnouncement(final String username, final String name,
                                         final String description) {
        Announcement myAnnouncement = new Announcement(name, description);
        if (checkIfUserExists(username) == null) {
            return username + " doesn't exist.";
        }
        if (!checkIfHost(username)) {
            return username + " is not a host.";
        }
        if (getUser(username) != null) {
            for (Announcement announcement : getUser(username).getAnnouncements()) {
                if (announcement.getName().equals(name)) {
                    return username + " has already added an announcement with this name.";
                }
            }
        }
        getUser(username).addAnnouncement(myAnnouncement);
        return username + " has successfully added new announcement.";
    }
    /**
     * Retrieves a list of podcasts associated with the specified user.
     * The method creates a JSON representation of the user's podcasts and their episodes.
     *
     * @param username The username of the user for whom podcasts are to be retrieved.
     * @return An ArrayNode containing a JSON representation of the user's podcasts and episodes.
     *         Returns null if the user does not exist or has no podcasts.
     */
    public ArrayNode showPodcasts(final String username) {
        ObjectNode objectNode = objectMapper.createObjectNode();
        for (User user: users) {
            if (user.getUsername().equals(username)) {
                ArrayNode objectNode2 = objectNode.putArray("result");
                for (Podcast podcast: user.getPodcasts()) {
                    ArrayList<String> episodesName = new ArrayList<>();
                    for (Episode episode: podcast.getEpisodes()) {
                        episodesName.add(episode.getName());
                    }
                    ObjectNode objectNode3 = objectMapper.createObjectNode();
                    objectNode3.put("name", podcast.getName());
                    objectNode3.put("episodes", objectMapper.valueToTree(episodesName));
                    objectNode2.add(objectNode3);
                }
                return objectNode2;
            }
        }
        return null;
    }
    /**
     * Removes a previously added announcement from the specified host's profile.
     * The method checks if the host exists, is a host, and attempts to find and
     * remove the announcement.
     *
     * @param username The username of the host removing the announcement.
     * @param name     The name of the announcement to be removed.
     * @return A message indicating the result of the announcement removal process.
     *         Returns success message if the announcement is removed successfully,
     *         or an error message if the user doesn't exist, is not a host, or has
     *         no such announcement.
     */
    public String removeAnnouncement(final String username, final String name) {
        if (checkIfUserExists(username) == null) {
            return username + " doesn't exist.";
        }
        if (!checkIfHost(username)) {
            return username + " is not a host.";
        }
        if (getUser(username) != null) {
           for (Announcement announcement: getUser(username).getAnnouncements()) {
               if (announcement.getName().equals(name)) {
                   getUser(username).getAnnouncements().remove(announcement);
                   return username + " has successfully deleted the announcement.";
               }
           }
           return username + " has no announcement with the given name.";
        }
        return null;
    }
    /**
     * Changes the current page for the specified user based on the provided page identifier.
     * The method accepts different page identifiers and sets the user's page accordingly.
     *
     * @param username  The username of the user whose page is to be changed.
     * @param nextPage  The identifier of the next page to be accessed.
     * @return A message indicating the result of the page change process.
     *         Returns a success message if the page is changed successfully,
     *         or an error message if the specified page is non-existent.
     */
    public String changePage(final String username, final String nextPage) {
        if (nextPage.equals("HomePage") || nextPage.equals("Home")) {
            getUser(username).setPage(new HomePage(getUser(username)));
            return username + " accessed " + nextPage + " successfully.";
        }
        if (nextPage.equals("LikedContent")) {
            getUser(username).setPage(new LikedContentPage(getUser(username)));
            return username + " accessed " + nextPage + " successfully.";
        }
        if (nextPage.equals("Artist page")) {
            getUser(username).setPage(new ArtistsPage(getUser(username)));
            return username + " accessed " + nextPage + " successfully.";
        }
        if (nextPage.equals("Host page")) {
            getUser(username).setPage(new HostsPage(getUser(username)));
            return username + " accessed " + nextPage + " successfully.";
        }
        return username + " is trying to access a non-existent page.";
    }


    public ObjectNode wrappedUser(String username) {

        User user = getUser(username);
        if (user != null) {
            user.orderByNumOfListen();
            ObjectNode objectNode = objectMapper.createObjectNode();

            ObjectNode objectNodeResult = objectMapper.createObjectNode();

            int cnt = 0;
            ObjectNode objectNodeS = objectMapper.createObjectNode();
            for (Map.Entry<String, Integer> entry : user.getListenedSongs().entrySet()) {
                if (cnt < 5) {
                    objectNodeS.put(entry.getKey(), entry.getValue());
                }
                cnt++;
            }

            cnt = 0;
            ObjectNode objectNodeA = objectMapper.createObjectNode();
            for (Map.Entry<String, Integer> entry : user.getMostListenedArtists().entrySet()) {
                if (cnt < 5) {
                    objectNodeA.put(entry.getKey(), entry.getValue());
                }
                cnt++;
            }

            cnt = 0;
            ObjectNode objectNodeG = objectMapper.createObjectNode();
            for (Map.Entry<String, Integer> entry : user.getMostListenedGenres().entrySet()) {
                if (cnt < 5) {
                    objectNodeG.put(entry.getKey(), entry.getValue());
                }
                cnt++;
            }

            cnt = 0;
            ObjectNode objectNodeAlb = objectMapper.createObjectNode();
            for (Map.Entry<String, Integer> entry : user.getListenedAlbums().entrySet()) {
                if (cnt < 5) {
                    objectNodeAlb.put(entry.getKey(), entry.getValue());
                }
                cnt++;
            }

            cnt = 0;
            ObjectNode objectNodeEp = objectMapper.createObjectNode();
            for (Map.Entry<String, Integer> entry : user.getListenedEpisodes().entrySet()) {
                if (cnt < 5) {
                    objectNodeEp.put(entry.getKey(), entry.getValue());
                }
                cnt++;
            }

            objectNodeResult.set("topArtists",objectNodeA);
            objectNodeResult.set("topGenres",objectNodeG);
            objectNodeResult.set("topSongs", objectNodeS);
            objectNodeResult.set("topAlbums", objectNodeAlb);
            objectNodeResult.set("topEpisodes",objectNodeEp);
            return objectNodeResult;
        }
        return null;
    }

    public ObjectNode wrappedArtist(String username) {
        User user = getUser(username);
        if (user != null) {
            user.orderByNumOfListen();
            ObjectNode objectNodeResult = objectMapper.createObjectNode();

            int cnt = 0;
            ObjectNode objectNodeS = objectMapper.createObjectNode();
            for (Map.Entry<String, Integer> entry : user.getListenedSongs().entrySet()) {
                if (cnt < 5) {
                    objectNodeS.put(entry.getKey(), entry.getValue());
                }
                cnt++;
            }

            cnt = 0;
            ObjectNode objectNodeAlb = objectMapper.createObjectNode();
            for (Map.Entry<String, Integer> entry : user.getListenedAlbums().entrySet()) {
                if (cnt < 5) {
                    objectNodeAlb.put(entry.getKey(), entry.getValue());
                }
                cnt++;
            }

            cnt = 0;
            ArrayNode objectNodeFans = objectMapper.createArrayNode();
            for (Map.Entry<String, Integer> entry : user.getFans().entrySet()) {
                if (cnt < 5) {
                    objectNodeFans.add(entry.getKey());
                }
                cnt++;
            }

            objectNodeResult.set("topAlbums", objectNodeAlb);
            objectNodeResult.set("topSongs", objectNodeS);
            objectNodeResult.set("topFans", objectNodeFans);
            objectNodeResult.put("listeners", user.getFans().size());
            return objectNodeResult;
        }
        return null;
    }

    public String getPremiumSubscription(final String username) {
        User user = getUser(username);
        if (user != null) {
            if (user.isPremium()) {
                return username + " is already a premium user.";
            } else {
                user.setPremium(true);
                return username + " bought the subscription successfully.";
            }
        }
        return "The username " + username + " doesn't exist.";
    }

    public String cancelSubscription(final String username) {
        User user = getUser(username);
        if (user != null) {
            if (user.isPremium()) {
                user.setPremium(false);
                return username + " cancelled the subscription successfully.";
            } else {
                return username + " is not a premium user.";
            }
        }
        return "The username " + username + " doesn't exist.";
    }
    /**
     * reset
     */
    public void reset() {
        connectionStatus = true;
        users = new ArrayList<>();
        songs = new ArrayList<>();
        podcasts = new ArrayList<>();
        timestamp = 0;
    }
}
