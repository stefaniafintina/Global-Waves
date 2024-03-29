package app;

import app.PageNavigation.NextPage;
import app.PageNavigation.PreviousPage;
import app.audio.Collections.PlaylistOutput;
import app.player.PlayerStats;
import app.searchBar.Filters;
import app.user.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import fileio.input.CommandInput;

import java.util.ArrayList;
import java.util.Map;
import java.util.List;
import java.util.Comparator;

import java.util.stream.Collectors;

/**
 * The type Command runner.
 */
public final class CommandRunner {
    public static final double MAGIC_NUMBER_MILION = 1000000.0;
    public static final double MAGIC_NUMBER100 = 100.0;
    private CommandRunner() {
    }
    /**
     * The Object mapper.
     */
    public static final ObjectMapper objectMapper = new ObjectMapper();
    /**
     * Search object node.
     *
     * @param commandInput the command input
     * @return the object node
     */
    public static ObjectNode search(final CommandInput commandInput) {
        User user = Admin.getInstance().getUser(commandInput.getUsername());
        Filters filters = new Filters(commandInput.getFilters());
        String type = commandInput.getType();

        ArrayList<String> results = user.search(filters, type);
        if (results.contains(" is offline.")) {
            ObjectNode objectNode = objectMapper.createObjectNode();
            objectNode.put("command", commandInput.getCommand());
            objectNode.put("user", commandInput.getUsername());
            objectNode.put("timestamp", commandInput.getTimestamp());
            objectNode.put("message", commandInput.getUsername() + " is offline.");
            results.clear();
            objectNode.put("results", objectMapper.valueToTree(results));
            return objectNode;
        }

        String message = "Search returned " + results.size() + " results";

        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("user", commandInput.getUsername());
        objectNode.put("timestamp", commandInput.getTimestamp());
        objectNode.put("message", message);
        objectNode.put("results", objectMapper.valueToTree(results));
        return objectNode;
    }
    /**
     * Select object node.
     *
     * @param commandInput the command input
     * @return the object node
     */
    public static ObjectNode select(final CommandInput commandInput) {
        User user = Admin.getInstance().getUser(commandInput.getUsername());
        String message = user.select(commandInput.getItemNumber());

        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("user", commandInput.getUsername());
        objectNode.put("timestamp", commandInput.getTimestamp());
        objectNode.put("message", message);

        return objectNode;
    }
    /**
     * Load object node.
     *
     * @param commandInput the command input
     * @return the object node
     */
    public static ObjectNode load(final CommandInput commandInput) {
        User user = Admin.getInstance().getUser(commandInput.getUsername());
        String message = user.load();

        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("user", commandInput.getUsername());
        objectNode.put("timestamp", commandInput.getTimestamp());
        objectNode.put("message", message);

        return objectNode;
    }
    /**
     * Play pause object node.
     *
     * @param commandInput the command input
     * @return the object node
     */
    public static ObjectNode playPause(final CommandInput commandInput) {
        User user = Admin.getInstance().getUser(commandInput.getUsername());
        String message = user.playPause();

        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("user", commandInput.getUsername());
        objectNode.put("timestamp", commandInput.getTimestamp());
        objectNode.put("message", message);

        return objectNode;
    }
    /**
     * Repeat object node.
     *
     * @param commandInput the command input
     * @return the object node
     */
    public static ObjectNode repeat(final CommandInput commandInput) {
        User user = Admin.getInstance().getUser(commandInput.getUsername());
        String message = user.repeat();

        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("user", commandInput.getUsername());
        objectNode.put("timestamp", commandInput.getTimestamp());
        objectNode.put("message", message);

        return objectNode;
    }
    /**
     * Shuffle object node.
     *
     * @param commandInput the command input
     * @return the object node
     */
    public static ObjectNode shuffle(final CommandInput commandInput) {
        User user = Admin.getInstance().getUser(commandInput.getUsername());
        Integer seed = commandInput.getSeed();
        String message = user.shuffle(seed);

        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("user", commandInput.getUsername());
        objectNode.put("timestamp", commandInput.getTimestamp());
        objectNode.put("message", message);

        return objectNode;
    }
    /**
     * Forward object node.
     *
     * @param commandInput the command input
     * @return the object node
     */
    public static ObjectNode forward(final CommandInput commandInput) {
        User user = Admin.getInstance().getUser(commandInput.getUsername());
        String message = user.forward();

        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("user", commandInput.getUsername());
        objectNode.put("timestamp", commandInput.getTimestamp());
        objectNode.put("message", message);

        return objectNode;
    }
    /**
     * Backward object node.
     *
     * @param commandInput the command input
     * @return the object node
     */
    public static ObjectNode backward(final CommandInput commandInput) {
        User user = Admin.getInstance().getUser(commandInput.getUsername());
        String message = user.backward();

        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("user", commandInput.getUsername());
        objectNode.put("timestamp", commandInput.getTimestamp());
        objectNode.put("message", message);

        return objectNode;
    }
    /**
     * Like object node.
     *
     * @param commandInput the command input
     * @return the object node
     */
    public static ObjectNode like(final CommandInput commandInput) {
        User user = Admin.getInstance().getUser(commandInput.getUsername());
        String message = user.like();

        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("user", commandInput.getUsername());
        objectNode.put("timestamp", commandInput.getTimestamp());
        objectNode.put("message", message);

        return objectNode;
    }
    /**
     * Next object node.
     *
     * @param commandInput the command input
     * @return the object node
     */
    public static ObjectNode next(final CommandInput commandInput) {
        User user = Admin.getInstance().getUser(commandInput.getUsername());
        String message = user.next();

        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("user", commandInput.getUsername());
        objectNode.put("timestamp", commandInput.getTimestamp());
        objectNode.put("message", message);

        return objectNode;
    }
    /**
     * Prev object node.
     *
     * @param commandInput the command input
     * @return the object node
     */
    public static ObjectNode prev(final CommandInput commandInput) {
        User user = Admin.getInstance().getUser(commandInput.getUsername());
        String message = user.prev();

        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("user", commandInput.getUsername());
        objectNode.put("timestamp", commandInput.getTimestamp());
        objectNode.put("message", message);

        return objectNode;
    }
    /**
     * Create playlist object node.
     *
     * @param commandInput the command input
     * @return the object node
     */
    public static ObjectNode createPlaylist(final CommandInput commandInput) {
        User user = Admin.getInstance().getUser(commandInput.getUsername());
        String message = user.createPlaylist(commandInput.getPlaylistName(),
                commandInput.getTimestamp());

        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("user", commandInput.getUsername());
        objectNode.put("timestamp", commandInput.getTimestamp());
        objectNode.put("message", message);

        return objectNode;
    }
    /**
     * Add remove in playlist object node.
     *
     * @param commandInput the command input
     * @return the object node
     */
    public static ObjectNode addRemoveInPlaylist(final CommandInput commandInput) {
        User user = Admin.getInstance().getUser(commandInput.getUsername());
        String message = user.addRemoveInPlaylist(commandInput.getPlaylistId());

        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("user", commandInput.getUsername());
        objectNode.put("timestamp", commandInput.getTimestamp());
        objectNode.put("message", message);

        return objectNode;
    }
    /**
     * Switch visibility object node.
     *
     * @param commandInput the command input
     * @return the object node
     */
    public static ObjectNode switchVisibility(final CommandInput commandInput) {
        User user = Admin.getInstance().getUser(commandInput.getUsername());
        String message = user.switchPlaylistVisibility(commandInput.getPlaylistId());

        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("user", commandInput.getUsername());
        objectNode.put("timestamp", commandInput.getTimestamp());
        objectNode.put("message", message);

        return objectNode;
    }
    /**
     * Show playlists object node.
     *
     * @param commandInput the command input
     * @return the object node
     */
    public static ObjectNode showPlaylists(final CommandInput commandInput) {
        User user = Admin.getInstance().getUser(commandInput.getUsername());
        ArrayList<PlaylistOutput> playlists = user.showPlaylists();

        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("user", commandInput.getUsername());
        objectNode.put("timestamp", commandInput.getTimestamp());
        objectNode.put("result", objectMapper.valueToTree(playlists));

        return objectNode;
    }
    /**
     * Follow object node.
     *
     * @param commandInput the command input
     * @return the object node
     */
    public static ObjectNode follow(final CommandInput commandInput) {
        User user = Admin.getInstance().getUser(commandInput.getUsername());
        String message = user.follow();

        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("user", commandInput.getUsername());
        objectNode.put("timestamp", commandInput.getTimestamp());
        objectNode.put("message", message);

        return objectNode;
    }
    /**
     * Status object node.
     *
     * @param commandInput the command input
     * @return the object node
     */
    public static ObjectNode status(final CommandInput commandInput) {
        User user = Admin.getInstance().getUser(commandInput.getUsername());
        PlayerStats stats = user.getPlayerStats();
        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("user", commandInput.getUsername());
        objectNode.put("timestamp", commandInput.getTimestamp());
        objectNode.put("stats", objectMapper.valueToTree(stats));

        return objectNode;
    }
    /**
     * Show liked songs object node.
     *
     * @param commandInput the command input
     * @return the object node
     */
    public static ObjectNode showLikedSongs(final CommandInput commandInput) {
        User user = Admin.getInstance().getUser(commandInput.getUsername());
        ArrayList<String> songs = user.showPreferredSongs();

        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("user", commandInput.getUsername());
        objectNode.put("timestamp", commandInput.getTimestamp());
        objectNode.put("result", objectMapper.valueToTree(songs));

        return objectNode;
    }
    /**
     * Gets preferred genre.
     *
     * @param commandInput the command input
     * @return the preferred genre
     */
    public static ObjectNode getPreferredGenre(final CommandInput commandInput) {
        User user = Admin.getInstance().getUser(commandInput.getUsername());
        String preferredGenre = user.getPreferredGenre();

        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("user", commandInput.getUsername());
        objectNode.put("timestamp", commandInput.getTimestamp());
        objectNode.put("result", objectMapper.valueToTree(preferredGenre));

        return objectNode;
    }
    /**
     * Gets top 5 songs.
     *
     * @param commandInput the command input
     * @return the top 5 songs
     */
    public static ObjectNode getTop5Songs(final CommandInput commandInput) {
        List<String> songs = Admin.getInstance().getTop5Songs();

        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("timestamp", commandInput.getTimestamp());
        objectNode.put("result", objectMapper.valueToTree(songs));

        return objectNode;
    }
    /**
     * Gets top 5 playlists.
     *
     * @param commandInput the command input
     * @return the top 5 playlists
     */
    public static ObjectNode getTop5Playlists(final CommandInput commandInput) {
        List<String> playlists = Admin.getInstance().getTop5Playlists();

        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("timestamp", commandInput.getTimestamp());
        objectNode.put("result", objectMapper.valueToTree(playlists));

        return objectNode;
    }
    /**
     *This method updates the connection status of the specified user and
     * returns an objectNode, containing information about the operation, including the
     * command, user, timestamp, and a message
     * indicating the result of the operation.
     */
    public static ObjectNode switchConnectionStatus(final CommandInput commandInput) {
       User checkIfUserExists = Admin.getInstance().checkIfUserExists(commandInput.getUsername());
        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("user", commandInput.getUsername());
        objectNode.put("timestamp", commandInput.getTimestamp());
        if (checkIfUserExists == null) {
            objectNode.put("message", "The username "
                    + commandInput.getUsername() + " doesn't exist.");
        } else {
            if (checkIfUserExists.isHost() || checkIfUserExists.isArtist()) {
                objectNode.put("message", commandInput.getUsername() + " is not a normal user.");
            } else {
                objectNode.put("message", commandInput.getUsername()
                        + " has changed status successfully.");
            }
        }
        return objectNode;
    }
    /**
     *This method is used for getting all the online user
     * this operation is possible after determining
     * the status of each user in Admin class
     */
    public static ObjectNode getOnlineUsers(final CommandInput commandInput) {
        List<String> onlineUsers = Admin.getInstance().getOnlineUsers();
        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("timestamp", commandInput.getTimestamp());
        objectNode.put("result", objectMapper.valueToTree(onlineUsers));

        return objectNode;
    }
    /**
     *This method is used to add a user and to check
     * if the new user will be a host, an artist or a normal user
     * This command will print different messages if a user already exists
     * or if it can be added
     */
    public static ObjectNode addUser(final CommandInput commandInput) {
        User user = new User(commandInput.getUsername(), commandInput.getAge(),
                commandInput.getCity());
        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("user", commandInput.getUsername());
        objectNode.put("timestamp", commandInput.getTimestamp());
        if (Admin.getInstance().checkIfUserExists(commandInput.getUsername()) == null) {
            if (commandInput.getType().equals("host")) {
                user.setHost(true);
            } else if (commandInput.getType().equals("artist")) {
                user.setArtist(true);
            }
            Admin.getInstance().addUser(user);
            objectNode.put("message", "The username " +  commandInput.getUsername()
                    + " has been added successfully.");
        } else {
            objectNode.put("message", "The username " +  commandInput.getUsername()
                    + " is already taken.");
        }
        return objectNode;
    }
    /**
     * Adds a new album
     * @param commandInput the command input
     */
    public static ObjectNode addAlbum(final CommandInput commandInput) {
        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("user", commandInput.getUsername());
        objectNode.put("timestamp", commandInput.getTimestamp());
        objectNode.put("message", Admin.getInstance().addAlbum(commandInput.getUsername(),
                commandInput.getName(), commandInput.getSongs(), commandInput.getReleaseYear(),
                commandInput.getDescription()));
        return objectNode;
    }
    /**
     * Removes an Album from a user's list of albums
     * and also deletes it from the list which contains all the albums
     */
    public static ObjectNode removeAlbum(final CommandInput commandInput) {
        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("user", commandInput.getUsername());
        objectNode.put("timestamp", commandInput.getTimestamp());
        objectNode.put("message", Admin.getInstance().removeAlbum(commandInput.getUsername(),
                commandInput.getName()));
        return objectNode;
    }
    /**
     * Shows the list of albums for the given artist
     */
    public static ObjectNode showAlbums(final CommandInput commandInput) {
        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("user", commandInput.getUsername());
        objectNode.put("timestamp", commandInput.getTimestamp());
        objectNode.put("result", Admin.getInstance().showAlbums(commandInput.getUsername()));
        return objectNode;
    }
    /**
     * Prints the current page of a user if the user is online
     */
    public static ObjectNode printCurrentPage(final CommandInput commandInput) {
        User user = Admin.getInstance().getUser(commandInput.getUsername());
        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("user", commandInput.getUsername());
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("timestamp", commandInput.getTimestamp());
        if (Admin.getInstance().isOnline(commandInput.getUsername()) == 0) {
            objectNode.put("message", commandInput.getUsername() + " is offline.");
        } else {
            if (user != null) {
                objectNode.put("message", user.getPage().printCurrentPage());
            }
        }
            return objectNode;
    }
    /**
     *Adds a new event on an artist page
     */
    public static ObjectNode addEvent(final CommandInput commandInput) {
        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("user", commandInput.getUsername());
        objectNode.put("timestamp", commandInput.getTimestamp());
        objectNode.put("message", Admin.getInstance().addEvent(commandInput.getUsername(),
                commandInput.getName(), commandInput.getDate(), commandInput.getDescription()));
        return objectNode;
    }
    /**
     *Removes a new event from an artist page
     */
    public static ObjectNode removeEvent(final CommandInput commandInput) {
        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("user", commandInput.getUsername());
        objectNode.put("timestamp", commandInput.getTimestamp());
        objectNode.put("message", Admin.getInstance().removeEvent(commandInput.getUsername(),
                commandInput.getName()));
        return objectNode;
    }
    /**
     * Adds a merch on an artist page
     */
    public static ObjectNode addMerch(final CommandInput commandInput) {
        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("user", commandInput.getUsername());
        objectNode.put("timestamp", commandInput.getTimestamp());
        objectNode.put("message", Admin.getInstance().addMerch(commandInput.getUsername(),
                commandInput.getName(), commandInput.getPrice(), commandInput.getDescription()));
        return objectNode;
    }
    /**
     * Deletes a user if it's possible
     */
    public static ObjectNode deleteUser(final CommandInput commandInput) {
        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("user", commandInput.getUsername());
        objectNode.put("timestamp", commandInput.getTimestamp());
        objectNode.put("message", Admin.getInstance().deleteUser(commandInput.getUsername()));
        return objectNode;
    }
    /**
     * Gets a list with all users, including artists, hosts, online
     * and offline users
     */
    public static ObjectNode getAllUsers(final CommandInput commandInput) {
        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("timestamp", commandInput.getTimestamp());
        ArrayList<String> resultList = Admin.getInstance().getAllUsers();
        objectNode.put("result", objectMapper.valueToTree(resultList));
        return objectNode;
    }
    /**
     * Adds a new podcast
     */
    public static ObjectNode addPodcast(final CommandInput commandInput) {
        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("user", commandInput.getUsername());
        objectNode.put("timestamp", commandInput.getTimestamp());
        objectNode.put("message", Admin.getInstance().addPodcast(commandInput.getUsername(),
                commandInput.getEpisodes(), commandInput.getName()));
        return objectNode;
    }
    /**
     * Removes a podcast
     */
    public static ObjectNode removePodcast(final CommandInput commandInput) {
        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("user", commandInput.getUsername());
        objectNode.put("timestamp", commandInput.getTimestamp());
        objectNode.put("message", Admin.getInstance().removePodcast(commandInput.getUsername(),
                commandInput.getName()));
        return objectNode;
    }
    /**
     * Shows a list of podcasts
     */
    public static ObjectNode showPodcasts(final CommandInput commandInput) {
        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("user", commandInput.getUsername());
        objectNode.put("timestamp", commandInput.getTimestamp());
        objectNode.put("result", Admin.getInstance().showPodcasts(commandInput.getUsername()));
        return objectNode;
    }
    /**
     * Adds a new Announcement on a host page
     */
    public static ObjectNode addAnnouncement(final CommandInput commandInput) {
        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("user", commandInput.getUsername());
        objectNode.put("timestamp", commandInput.getTimestamp());
        objectNode.put("message", Admin.getInstance().addAnnouncement(commandInput.getUsername(),
                commandInput.getName(), commandInput.getDescription()));
        return objectNode;
    }
    /**
     * Removes an Announcement
     */
    public static ObjectNode removeAnnouncement(final CommandInput commandInput) {
        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("user", commandInput.getUsername());
        objectNode.put("timestamp", commandInput.getTimestamp());
        objectNode.put("message", Admin.getInstance().removeAnnouncement(commandInput.getUsername(),
                commandInput.getName()));
        return objectNode;
    }
    /**
     * Changes the page of a specific user
     */
    public static ObjectNode changePage(final CommandInput commandInput) {
        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("user", commandInput.getUsername());
        objectNode.put("timestamp", commandInput.getTimestamp());
        objectNode.put("message", Admin.getInstance().changePage(commandInput.getUsername(),
                commandInput.getNextPage()));
        return objectNode;
    }
    /**
     * Gets top 5 albums.
     *
     * @param commandInput the command input
     * @return the top 5 albums
     */
    public static ObjectNode getTop5Albums(final CommandInput commandInput) {
        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("timestamp", commandInput.getTimestamp());
        objectNode.put("result", objectMapper.valueToTree(Admin.getInstance().getTop5Albums()));
        return objectNode;
    }
    /**
     * Gets top 5 artists.
     *
     * @param commandInput the command input
     * @return the top 5 artists
     */
    public static ObjectNode getTop5Artists(final CommandInput commandInput) {
        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("timestamp", commandInput.getTimestamp());
        objectNode.put("result", objectMapper.valueToTree(Admin.getInstance().getTop5Artists()));
        return objectNode;
    }
    /**
     * Wraps the command input into a JSON ObjectNode with additional user-related information.
     * The method takes command input and creates a JSON node that
     * includes the command, username, and timestamp.
     * It then fetches user information and determines the type
     * of user (artist, host, or regular user)
     * to provide specific data.
     * If there's no data available for the user, it returns a message indicating so.
     *
     * @param commandInput The CommandInput object containing the command, username, and timestamp.
     * @return ObjectNode A JSON object node containing the wrapped information.
     * This includes:
     *         - The command, user, and timestamp from the CommandInput.
     *         - A result node with detailed information based on the user's
     *         type and available data.
     *         - A message node if there's no data to show.
     */
    public static ObjectNode wrapped(final CommandInput commandInput) {
        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("user", commandInput.getUsername());
        objectNode.put("timestamp", commandInput.getTimestamp());
        User user = Admin.getInstance().getUser(commandInput.getUsername());
        if (user.getListenedSongs().isEmpty() && user.getListenedAlbums().isEmpty()
                && user.getMostListenedGenres().isEmpty()
                && user.getMostListenedArtists().isEmpty()
                && user.getListenedEpisodes().isEmpty()) {
            if (user.isArtist()) {
                objectNode.put("message", "No data to show for artist "
                        + user.getName() + ".");
            } else {
                objectNode.put("message", "No data to show for user "
                        + user.getName() + ".");
            }
            return objectNode;
        }
        if (!Admin.getInstance().checkIfHost(commandInput.getUsername())
                && !Admin.getInstance().checkIfArtist(commandInput.getUsername())) {
            objectNode.put("result", objectMapper.valueToTree(Admin.getInstance().
                    wrappedUser(commandInput.getUsername())));
        }
        if (Admin.getInstance().checkIfArtist(commandInput.getUsername())) {
            objectNode.put("result", objectMapper.valueToTree(Admin.getInstance().
                    wrappedArtist(commandInput.getUsername())));
        }
        if (Admin.getInstance().checkIfHost(commandInput.getUsername())) {
            objectNode.put("result", objectMapper.valueToTree(Admin.getInstance().
                    wrappedHost(commandInput.getUsername())));
        }
        return objectNode;
    }

    /**
     * Buy Premium
     * */
    public static ObjectNode buyPremium(final CommandInput commandInput) {
        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("user", commandInput.getUsername());
        objectNode.put("timestamp", commandInput.getTimestamp());
        objectNode.put("message", Admin.getInstance().
                getPremiumSubscription(commandInput.getUsername()));
        return objectNode;
    }

    /**
     * Cancel Premium
     * */
    public static ObjectNode cancelPremium(final CommandInput commandInput) {
        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("user", commandInput.getUsername());
        objectNode.put("timestamp", commandInput.getTimestamp());
        objectNode.put("message", Admin.getInstance().
                cancelSubscription(commandInput.getUsername()));
        return objectNode;
    }

    /**
     * Subscribe
     */
    public static ObjectNode subscribe(final CommandInput commandInput) {
        User user = Admin.getInstance().getUser(commandInput.getUsername());
        String message = user.subscribe(commandInput.getUsername());

        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("user", commandInput.getUsername());
        objectNode.put("timestamp", commandInput.getTimestamp());
        objectNode.put("message", message);

        return objectNode;
    }

    /**
     * Get notifications
     * @param commandInput
     * @return
     */
    public static ObjectNode getNotifications(final CommandInput commandInput) {
        User user = Admin.getInstance().getUser(commandInput.getUsername());

        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("user", commandInput.getUsername());
        objectNode.put("timestamp", commandInput.getTimestamp());
        objectNode.put("notifications", objectMapper.
                valueToTree(Admin.getInstance().getNotifications(commandInput.
                        getUsername())));

        return objectNode;
    }

    /**
     * Buy merch
     * @param commandInput
     * @return
     */
    public static ObjectNode buyMerch(final CommandInput commandInput) {
        User user = Admin.getInstance().getUser(commandInput.getUsername());

        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("user", commandInput.getUsername());
        objectNode.put("timestamp", commandInput.getTimestamp());
        objectNode.put("message", user.buyMerch(commandInput.
                getUsername(), commandInput.getName()));

        return objectNode;
    }

    /**
     * Add merch
     * @param commandInput
     * @return
     */
    public static ObjectNode seeMerch(final CommandInput commandInput) {
        User user = Admin.getInstance().getUser(commandInput.getUsername());

        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("user", commandInput.getUsername());
        objectNode.put("timestamp", commandInput.getTimestamp());
        ArrayNode merchArrayNode = objectMapper.createArrayNode();
        for (String merch : user.getMyMerches()) {
            merchArrayNode.add(merch);
        }

        objectNode.put("result", merchArrayNode);

        return objectNode;
    }

    /**
     * Next Page
     * @param commandInput
     * @return
     */
    public static ObjectNode nextPage(final CommandInput commandInput) {
        User user = Admin.getInstance().getUser(commandInput.getUsername());

        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("user", commandInput.getUsername());
        objectNode.put("timestamp", commandInput.getTimestamp());
        NextPage nextPage = new NextPage(Admin.getInstance().
                getUser(commandInput.getUsername()));
        objectNode.put("message", nextPage.execute());

        return objectNode;
    }

    /**
     * Previous Page
     * @param commandInput
     * @return
     */
    public static ObjectNode previousPage(final CommandInput commandInput) {
        User user = Admin.getInstance().getUser(commandInput.getUsername());

        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("user", commandInput.getUsername());
        objectNode.put("timestamp", commandInput.getTimestamp());
        PreviousPage previousPage = new PreviousPage(Admin.getInstance().
                getUser(commandInput.getUsername()));
        objectNode.put("message", previousPage.execute());

        return objectNode;
    }

    /**
     * Update Recommendations
     * @param commandInput
     * @return
     */
    public static ObjectNode updateRecommendations(final CommandInput commandInput) {
        User user = Admin.getInstance().getUser(commandInput.getUsername());

        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("user", commandInput.getUsername());
        objectNode.put("timestamp", commandInput.getTimestamp());
        objectNode.put("message", Admin.getInstance().
                updateRecommendations(commandInput.getUsername(),
                        commandInput.getRecommendationType()));

        return objectNode;
    }

    /**
     * Load Recommendations
     * @param commandInput
     * @return
     */
    public static ObjectNode loadRecommendations(final CommandInput commandInput) {
        User user = Admin.getInstance().getUser(commandInput.getUsername());

        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("user", commandInput.getUsername());
        objectNode.put("timestamp", commandInput.getTimestamp());
        objectNode.put("message", "Playback loaded successfully.");

        return objectNode;
    }

    /**
     * Generates a summary report at the end of the program execution.
     * This method processes all users, particularly premium users,
     * to calculate and update song revenues.
     * It then sorts artists based on their revenues and other criteria,
     * and generates a detailed report
     * including each artist's merch revenue, song revenue, and their ranking.
     * The method creates and returns an ObjectNode containing this information.
     *
     * @return ObjectNode A JSON object node containing the end-of-program summary.
     *         The node includes a 'command' field set to "endProgram" and a 'result' node
     *         with detailed information about each artist.
     */
    public static ObjectNode endProgram() {
        ObjectNode root = objectMapper.createObjectNode();
        for (User user: Admin.getInstance().getUsers()) {
            if (user.isPremium()) {
                int songTotal = 0;
                for (Map.Entry<String, Integer> entry : user.
                        getListenedSongsPremium().entrySet()) {
                    songTotal += entry.getValue();
                }
                for (Map.Entry<String, Integer> entry : user.
                        getMostListenedArtistsPremium().entrySet()) {
                    User artist =  Admin.getInstance().getUser(entry.getKey());
                    double songRevenue = artist.getSongRevenue();
                    songRevenue = songRevenue + (MAGIC_NUMBER_MILION * entry.getValue())
                            / songTotal;
                    artist.setSongRevenue(songRevenue);
                }
                for (Map.Entry<String, Integer> songEntry : user.
                        getListenedSongsPremium().entrySet()) {
                    User foundArtist = Admin.getInstance().getArtistBySong(songEntry.
                            getKey());
                    if (foundArtist != null) {
                        double revenueForASong = MAGIC_NUMBER_MILION * songEntry.getValue()
                                / songTotal;
                        double currentCountPremium = foundArtist.getMostProfitableSong().
                                getOrDefault(songEntry.getKey(), 0.0);
                        foundArtist.getMostProfitableSong().put(songEntry.getKey(),
                                currentCountPremium + revenueForASong);
                    }
                }
            }
        }
        ObjectNode resultNode = objectMapper.createObjectNode();

        List<User> artists = new ArrayList<>(Admin.getInstance().getArtists());

        List<User> sortedArtists = artists.stream()
                .sorted(Comparator
                        .comparing(User::getSongRevenue, Comparator.reverseOrder())
                        .thenComparing(User::getMerchRevenue, Comparator.reverseOrder())
                        .thenComparing(User::getName))
                .collect(Collectors.toList());
        int index = 0;
        for (User artist : sortedArtists) {
            artist.orderByNumOfListen();
            if (!artist.getFans().isEmpty() || artist.getMerchRevenue() > 0.0) {
                index++;
                ObjectNode artistNode = objectMapper.createObjectNode();
                artistNode.put("merchRevenue", artist.getMerchRevenue());
                artistNode.put("songRevenue", Math.round(artist.
                        getSongRevenue() * MAGIC_NUMBER100) / MAGIC_NUMBER100);
                artistNode.put("ranking", index);
                if (artist.getSongRevenue() == 0) {
                    artistNode.put("mostProfitableSong", "N/A");
                } else {
                    for (Map.Entry<String, Double> entry : artist.
                            getMostProfitableSong().entrySet()) {
                        artistNode.put("mostProfitableSong", entry.getKey());
                        break;
                    }
                }
                resultNode.set(artist.getName(), artistNode);
            }
        }

        root.put("command", "endProgram");
        root.set("result", resultNode);
        return root;
    }

    /**
     * AdBreak
     * @param commandInput
     * @return
     */
    public static ObjectNode adBreak(final CommandInput commandInput) {
        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("user", commandInput.getUsername());
        objectNode.put("timestamp", commandInput.getTimestamp());
        objectNode.put("message", Admin.getInstance().
                adBreak(commandInput.getUsername(), commandInput.getPrice()));
        return objectNode;
    }
}
