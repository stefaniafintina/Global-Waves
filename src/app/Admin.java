package app;

import app.Pages.ArtistsPage;
import app.Pages.HomePage;
import app.artistsPage.Merch;
import app.audio.Collections.Album;
import app.audio.Collections.Playlist;
import app.audio.Collections.Podcast;
import app.audio.Files.Episode;
import app.audio.Files.Song;
import app.artistsPage.Event;
import app.player.Player;
import app.user.Artist;
import app.user.User;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import fileio.input.*;
import lombok.Getter;
import lombok.Setter;
import net.sf.saxon.ma.map.MapFunctionSet;
import org.checkerframework.checker.formatter.FormatUtil;

import java.util.*;

import static app.CommandRunner.objectMapper;

public class Admin {
    private static List<User> users = new ArrayList<>();
    private static List<Song> songs = new ArrayList<>();
    private static List<Podcast> podcasts = new ArrayList<>();
    private static List<UserInput> userInputList = new ArrayList<>();
    @Getter
    @Setter
    private static List<User> artists = new ArrayList<>();
    @Getter
    @Setter
    private static List<Album> allAlbums = new ArrayList<>();
    private static List<User> hosts = new ArrayList<>();
    private static int timestamp = 0;
    private static boolean connectionStatus = true;


    public static void setUsers(List<UserInput> userInputList) {
        users = new ArrayList<>();
        for (UserInput userInput : userInputList) {
            users.add(new User(userInput.getUsername(), userInput.getAge(), userInput.getCity()));
            users.get(users.size() - 1).setPage(new HomePage(users.get(users.size() - 1)));
        }
    }

    public static void setSongs(List<SongInput> songInputList) {
        songs = new ArrayList<>();
        for (SongInput songInput : songInputList) {
            songs.add(new Song(songInput.getName(), songInput.getDuration(), songInput.getAlbum(),
                    songInput.getTags(), songInput.getLyrics(), songInput.getGenre(),
                    songInput.getReleaseYear(), songInput.getArtist()));
        }
    }

    public static void setPodcasts(List<PodcastInput> podcastInputList) {
        podcasts = new ArrayList<>();
        for (PodcastInput podcastInput : podcastInputList) {
            List<Episode> episodes = new ArrayList<>();
            for (EpisodeInput episodeInput : podcastInput.getEpisodes()) {
                episodes.add(new Episode(episodeInput.getName(), episodeInput.getDuration(), episodeInput.getDescription()));
            }
            podcasts.add(new Podcast(podcastInput.getName(), podcastInput.getOwner(), episodes));
        }
    }

    public static List<Song> getSongs() {
        return new ArrayList<>(songs);
    }

    public static List<Podcast> getPodcasts() {
        return new ArrayList<>(podcasts);
    }

    public static List<Playlist> getPlaylists() {
        List<Playlist> playlists = new ArrayList<>();
        for (User user : users) {
            playlists.addAll(user.getPlaylists());
        }
        return playlists;
    }
    public static List<Album> getAlbums() {
        List<Album> albums = new ArrayList<>();
        for (User user : users) {
            if (user.getAlbums() != null)
                albums.addAll(user.getAlbums());
        }
        return albums;
    }
    public static User getUser(String username) {
        for (User user : users) {
            if (user.getUsername().equals(username)) {
                return user;
            }
        }
        return null;
    }
    public static void updateTimestamp(int newTimestamp) {
        if (connectionStatus) {
            int elapsed = newTimestamp - timestamp;
            timestamp = newTimestamp;
            if (elapsed == 0) {
                return;
            }
            for (User user : users) {
                user.simulateTime(elapsed);
            }
        }
        timestamp = newTimestamp;
    }

    public static List<String> getTop5Songs() {
        List<Song> sortedSongs = new ArrayList<>(songs);
        sortedSongs.sort(Comparator.comparingInt(Song::getLikes).reversed());
        List<String> topSongs = new ArrayList<>();
        int count = 0;
        for (Song song : sortedSongs) {
            if (count >= 5) break;
            topSongs.add(song.getName());
            count++;
        }
        return topSongs;
    }

    public static List<String> getTop5Playlists() {
        List<Playlist> sortedPlaylists = new ArrayList<>(getPlaylists());
        sortedPlaylists.sort(Comparator.comparingInt(Playlist::getFollowers)
                .reversed()
                .thenComparing(Playlist::getTimestamp, Comparator.naturalOrder()));
        List<String> topPlaylists = new ArrayList<>();
        int count = 0;
        for (Playlist playlist : sortedPlaylists) {
            if (count >= 5) break;
            topPlaylists.add(playlist.getName());
            count++;
        }
        return topPlaylists;
    }

    public static User checkIfUserExists(String username) {
        for (User user: users) {
            if (user.getUsername().equals(username))
                return user;
        }
        return null;
    }
    public static boolean switchConnectionStatus(String username) {
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

    public static List<String> getOnlineUsers() {
        List<String> onlineUsers = new ArrayList<>();
        for(User user: users) {
            if (user.isConnectionStatus()) {
                if (!user.isHost() && !user.isArtist())
                    onlineUsers.add(user.getUsername());
            }
        }
        return onlineUsers;
    }
    public static int isOnline(String username) {
        for(User user: users) {
            if (user.getUsername().equals(username)) {
                if (user.isConnectionStatus())
                    return 1;
                return 0;
            }
        }
        return 0;
    }
    public static void addUser(User user) {
        if (checkIfUserExists(user.getUsername()) == null) {
            users.add(user);
            user.setPage(new HomePage(user));
            if (user.isArtist()) {
                artists.add(user);
                user.setPage(new ArtistsPage(user));
            }
            else if (user.isHost()) {
                hosts.add(user);
            }
        }
    }
    public static String addAlbum(String username, String name, ArrayList<SongInput> songsList, Integer releaseYear, String description) {
        int ok = 0;
        HashMap<String, Integer> numberOfAppearance= new HashMap<>();
        for (User myUser: users) {
            if (myUser.getUsername().equals(username)) {
                ok = 1;
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
                    numberOfAppearance.put(song.getName(), numberOfAppearance.getOrDefault(song.getName(), 0) + 1);
                    if (numberOfAppearance.getOrDefault(song.getName(), 0) >= 2)
                        return  myUser.getUsername() + " has the same song at least twice in this album.";
                }
                Album album = new Album(name, username, releaseYear, description);
                for (SongInput song: songsList) {
                    album.addSong(new Song(song.getName(), song.getDuration(), song.getAlbum(),
                            song.getTags(), song.getLyrics(), song.getGenre(),
                            song.getReleaseYear(), song.getArtist()));
                }
                myUser.addAlbum(album);
                for (SongInput song: songsList) {
                    if (!songs.contains(song)) {
                        songs.add(new Song(song.getName(), song.getDuration(), song.getAlbum(),
                                song.getTags(), song.getLyrics(), song.getGenre(),
                                song.getReleaseYear(), song.getArtist()));
                    }
                }
                return myUser.getUsername() + " has added new album successfully.";
            }
        }
        return "The username " + username + " doesn't exist.";
    }
    public static ArrayNode showAlbums(String username) {
        ObjectNode objectNode = objectMapper.createObjectNode();
        for (User user: users) {
            if (user.getUsername().equals(username)) {
                ArrayNode objectNode2 = objectNode.putArray("result");
                for (Album album: user.getAlbums()) {
                    ArrayList<String> songsName = new ArrayList<>();
                    for (Song song: album.getSongs())
                        songsName.add(song.getName());
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
    public static ArrayList<String> getMostLikedSongs(String username) {
        for (User user: users) {
            if (user.getUsername().equals(username)) {
                return user.mostLikedSongs();
            }
        }
        return null;
    }

    public static int validDate(String date) {
        if (date.charAt(3) == '0' && date.charAt(4) == '2') {
            if ((date.charAt(0) == 2 && date.charAt(1) >= '9') || (date.charAt(0) >= '3'))
                return 0;
        } else {
            int nr = (date.charAt(3) - '0') * 10 + (date.charAt(4) - '0');
            if (nr > 12)
                return 0;
            nr = (date.charAt(0) - '0') * 10 + (date.charAt(1) - '0');
            if (nr > 31)
                return 0;
            nr = (date.charAt(6) - '0') * 1000 + (date.charAt(7) - '0') * 100 + (date.charAt(8) - '0') * 10 + (date.charAt(9) - '0');
            if (nr < 1900 || nr > 2023)
                return 0;
        }
        return 1;
    }
    public static String addEvent(String username, String name, String date, String description) {
        Event myEvent = new Event(date, description, name);
        for (User user: users) {
            if (user.getUsername().equals(username)) {
                if (!user.isArtist())
                    return username + " is not an artist.";
                for (Event event: user.getEvents()) {
                    if (event.getName().equals(name)) {
                        return username + " has another event with the same name.";
                    }
                }
                if (validDate(date) == 0)
                    return "Event for " + username + " does not have a valid date.";
                user.addEvent(myEvent);
                return username + " has added new event successfully.";
            }
        }
        return "The username " + username + " doesn't exist.";
    }
    public static String addMerch(String username, String name, double price, String description) {
        Merch myMerch = new Merch(price, description, name);
        for (User user: users) {
            if (user.getUsername().equals(username)) {
                if (!user.isArtist())
                    return username + " is not an artist.";
                for (Merch merch: user.getMerches()) {
                    if (merch.getName().equals(name)) {
                        return username + " has merchandise with the same name.";
                    }
                }
                if (price < 0)
                    return "Price for merchandise can not be negative.";
                user.addMerch(myMerch);
                return username + " has added new merchandise successfully.";
            }
        }
        return "The username " + username + " doesn't exist.";
    }

    public static String deleteUser(String username) {
        for (User user:  users) {
            if (user.getName().equals(username)) {
                if (!user.isArtist() && !user.isHost())
                    return username + " was successfully deleted.";
                for (User user1 : users) {
                    if (!user1.getName().equals(username)) {
                        if (user1.getPage().getOwner().getName().equals(user.getPage().getOwner().getName())) {
                            return username + " can't be deleted.";
                        }
                        if (user1.getPlayer().getSource() != null) {
                            for (Song song : songs) {
                                if (user1.getPlayer().getSource().getAudioFile().getName().equals(song.getName())) {
                                    if (song.getArtist().equals(username)) {
                                        return username + " can't be deleted.";
                                    }
                                }
                            }
                            for (Album album : getAlbums()) {
                                if (user1.getPlayer().getSource().getAudioFile().getName().equals(album.getName())) {
                                    if (album.getOwner().equals(username))
                                        return username + " can't be deleted.";
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

                getAlbums().removeIf(album -> album.getOwner().equals(username));
                for (User user1 : users) {
                    user1.getLikedSongs().removeIf(song -> song.getArtist().equals(username));
                    for (Song song : songs) {
                        if (song.getArtist().equals(username)) {
                            user1.getMostLikedSongs().remove(song.getName());
                            System.out.println(song.getName() + " " + song.getArtist());
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

    public static ArrayList<String> getAllUsers() {
        ArrayList<String> allUsers = new ArrayList<>();
        for (User user: users)
            if (!user.isArtist() && !user.isHost())
                allUsers.add(user.getUsername());
        for (User user: users)
            if (user.isArtist())
                allUsers.add(user.getUsername());
        for (User user: users)
            if (user.isHost())
                allUsers.add(user.getUsername());
        return allUsers;
    }
    public static void reset() {
        connectionStatus = true;
        users = new ArrayList<>();
        songs = new ArrayList<>();
        podcasts = new ArrayList<>();
        timestamp = 0;
    }
}
