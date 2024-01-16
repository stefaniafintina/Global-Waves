package app.player;

import app.Admin;
import app.audio.Collections.Album;
import app.audio.Collections.AudioCollection;
import app.audio.Collections.Podcast;
import app.audio.Files.AudioFile;
import app.audio.Files.Episode;
import app.audio.Files.Song;
import app.audio.LibraryEntry;
import app.user.User;
import app.utils.Enums;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * The type Player.
 */
public final class Player {

    public static final int MAGIC_NUMBER90 = 90;
    private Enums.RepeatMode repeatMode;
    private boolean shuffle;
    private boolean paused;
    @Getter
    private PlayerSource source;
    @Getter
    private String type;
    private ArrayList<PodcastBookmark> bookmarks = new ArrayList<>();
    /**
     * Instantiates a new Player.
     */
    public Player() {
        this.repeatMode = Enums.RepeatMode.NO_REPEAT;
        this.paused = true;
    }
    /**
     * Stop.
     */
    public void stop() {
        if ("podcast".equals(this.type)) {
            bookmarkPodcast();
        }

        repeatMode = Enums.RepeatMode.NO_REPEAT;
        paused = true;
        source = null;
        shuffle = false;
    }
/**
 * utility class used to resume the podcast whenever the user drops it
 * and after a given period of time, they load it again
 */
    private void bookmarkPodcast() {
        if (source != null && source.getAudioFile() != null) {
            PodcastBookmark currentBookmark =
                    new PodcastBookmark(source.getAudioCollection().getName(), source.getIndex(),
                            source.getDuration());
            bookmarks.removeIf(bookmark -> bookmark.getName().equals(currentBookmark.getName()));
            bookmarks.add(currentBookmark);
        }
    }
    /**
     * Create source player source.
     *
     * @param type      the type
     * @param entry     the entry
     * @param bookmarks the bookmarks
     * @return the player source
     * added new statement for album
     */
    public static PlayerSource createSource(final String type, final LibraryEntry entry,
                                            final List<PodcastBookmark> bookmarks) {
        if ("song".equals(type)) {
            return new PlayerSource(Enums.PlayerSourceType.LIBRARY, (AudioFile) entry);
        } else if ("playlist".equals(type)) {
            return new PlayerSource(Enums.PlayerSourceType.PLAYLIST, (AudioCollection) entry);
        } else if ("podcast".equals(type)) {
            return createPodcastSource((AudioCollection) entry, bookmarks);
        } else if ("album".equals(type)) {
            return new PlayerSource(Enums.PlayerSourceType.ALBUM, (AudioCollection) entry);
        }
        return null;
    }
    /**
     * Creates a PlayerSource for a podcast with the specified AudioCollection and
     * PodcastBookmarks.
     * If a matching bookmark is found in the provided list of bookmarks,
     * it is used to initialize the PlayerSource.
     * If no matching bookmark is found, a default PlayerSource is
     * created with the provided AudioCollection.
     *
     * @param collection The AudioCollection representing the podcast
     *                   for which the PlayerSource is being created.
     * @param bookmarks  A list of PodcastBookmarks containing saved
     *                  positions within various podcasts.
     * @return A PlayerSource instance representing the podcast with
     * the provided AudioCollection and optional bookmark.
     * @throws NullPointerException If the collection parameter is null.
     */
    private static PlayerSource createPodcastSource(final AudioCollection collection,
                                                    final List<PodcastBookmark> bookmarks) {
        for (PodcastBookmark bookmark : bookmarks) {
            if (bookmark.getName().equals(collection.getName())) {
                return new PlayerSource(Enums.PlayerSourceType.PODCAST, collection, bookmark);
            }
        }
        return new PlayerSource(Enums.PlayerSourceType.PODCAST, collection);
    }
    /**
     * Sets the source of the player based on the provided LibraryEntry
     * and new source type.
     * If the previous source type was a podcast, it bookmarks the
     * current position before changing the source.
     * After setting the new source, the repeat mode is reset to
     * NO_REPEAT, shuffle is set to false,
     * and the player is paused by default.
     *
     * @param entry    The LibraryEntry representing the media content
     *                 to set as the new source.
     * @param newType  The new source type (e.g., "podcast", "music").
     * @throws NullPointerException If the entry or newType parameter
     * is null.
     */
    public void setSource(final LibraryEntry entry, final String newType) {
        if ("podcast".equals(this.type)) {
            bookmarkPodcast();
        }

        this.type = newType;
        this.source = createSource(type, entry, bookmarks);
        this.repeatMode = Enums.RepeatMode.NO_REPEAT;
        this.shuffle = false;
        this.paused = true;
    }
    /**
     * Toggles the playback pause state of the player.
     * If the player is currently playing, invoking this method
     * will pause playback,
     * and if the player is paused, invoking this method will resume
     * playback.
     * The state is toggled by negating the current value of the
     * 'paused' attribute.
     */
    public void pause() {
        paused = !paused;
    }
    /**
     * Toggles the shuffle mode of the player, and optionally sets
     * a seed for shuffling.
     * If a seed is provided, the shuffle order is generated based
     * on the seed.
     * For playlists and albums, shuffling is toggled, and the
     * shuffle index is updated accordingly.
     *
     * @param seed The seed used for generating a consistent
     *             shuffle order. Can be {@code null} to disable seeding.
     */
    public void shuffle(final Integer seed) {
        if (seed != null) {
            source.generateShuffleOrder(seed);
        }

        if (source.getType() == Enums.PlayerSourceType.PLAYLIST) {
            shuffle = !shuffle;
            if (shuffle) {
                source.updateShuffleIndex();
            }
        }
        if (source.getType() == Enums.PlayerSourceType.ALBUM) {
            shuffle = !shuffle;
            if (shuffle) {
                source.updateShuffleIndex();
            }
        }
    }
    /**
     * Toggles and cycles through different repeat modes for the player.
     * The method returns the updated repeat mode after toggling.
     * - If the current repeat mode is NO_REPEAT:
     *   - For library sources, it sets the repeat mode to REPEAT_ONCE.
     *   - For other sources, it sets the repeat mode to REPEAT_ALL.
     * - If the current repeat mode is REPEAT_ONCE:
     *   - It sets the repeat mode to REPEAT_INFINITE.
     * - If the current repeat mode is REPEAT_ALL:
     *   - It sets the repeat mode to REPEAT_CURRENT_SONG.
     * - If the current repeat mode is REPEAT_INFINITE:
     *   - It sets the repeat mode to NO_REPEAT.
     * - If the current repeat mode is REPEAT_CURRENT_SONG:
     *   - It sets the repeat mode to NO_REPEAT.
     *
     * @return The updated Enums.RepeatMode after toggling.
     */
    public Enums.RepeatMode repeat() {
        if (repeatMode == Enums.RepeatMode.NO_REPEAT) {
            if (source.getType() == Enums.PlayerSourceType.LIBRARY) {
                repeatMode = Enums.RepeatMode.REPEAT_ONCE;
            } else {
                repeatMode = Enums.RepeatMode.REPEAT_ALL;
            }
        } else {
            if (repeatMode == Enums.RepeatMode.REPEAT_ONCE) {
                repeatMode = Enums.RepeatMode.REPEAT_INFINITE;
            } else {
                if (repeatMode == Enums.RepeatMode.REPEAT_ALL) {
                    repeatMode = Enums.RepeatMode.REPEAT_CURRENT_SONG;
                } else {
                    repeatMode = Enums.RepeatMode.NO_REPEAT;
                }
            }
        }

        return repeatMode;
    }
    /**
     * Simulates the player's progress through time, advancing playback
     * within the current source.
     * If the player is not paused and a valid source is set, it
     * simulates the player's progress
     * by repeatedly skipping to the next source until the specified
     * time is within the duration
     * of the current source. It then adjusts the playback position
     * within the source.
     *
     * @param time The simulated time in milliseconds to advance
     *             the player within the current source.
     *             If the time exceeds the duration of the current
     *             source, the player may advance to the next source.
     *             Must be a non-negative value.
     * @throws IllegalStateException If the player is paused or if
     * the current source is null.
     * @throws IllegalArgumentException If the provided time is negative.
     */
    public void simulatePlayer(int time, final User user) {
        User artist = null;

        if (source != null && source.getDuration() > 0) {
            user.setPlayerType(this.type);
        } else {
            user.setPlayerType(null);
        }
        if (!paused && source != null) {
            int ok = 0;
            while (time >= source.getDuration()) {
                time -= source.getDuration();
                next();

                if (user.isBreakStatus()) {
                    int songTotal = 0;
                    for (Map.Entry<String, Integer> entry : user.
                            getListenedSongsBreak().entrySet()) {
                        songTotal += entry.getValue();
                    }
                    for (Map.Entry<String, Integer> entry : user.
                            getMostListenedArtistsBreak().entrySet()) {
                        User artistBreak =  Admin.getInstance().getUser(entry.getKey());
                        double songRevenue = artistBreak.getSongRevenue();
                        songRevenue = songRevenue + (user.getBreakValue() * entry.getValue())
                                / songTotal;

                        artistBreak.setSongRevenue(songRevenue);
                    }
                    for (Map.Entry<String, Integer> songEntry : user.
                            getListenedSongsBreak().entrySet()) {
                        User foundArtist = Admin.getInstance().getArtistBySong(songEntry.getKey());
                        if (foundArtist != null) {
                            double revenueForASong = user.getBreakValue()
                                    * songEntry.getValue() / songTotal;
                            double currentCountPremium = foundArtist.getMostProfitableSong().
                                    getOrDefault(songEntry.getKey(), 0.0);
                            foundArtist.getMostProfitableSong().put(songEntry.getKey(),
                                    currentCountPremium + revenueForASong);
                        }
                    }
                    user.getListenedSongsBreak().clear();
                    user.getMostListenedArtistsBreak().clear();
                    user.setBreakStatus(false);
                }
                if (this.getCurrentAudioFile() != null && this.type.equals("album")) {
                    Integer currentCount = user.getListenedSongs().getOrDefault(this.
                            getCurrentAudioFile().getName(), 0);
                    Integer currCnt = user.getListenedAlbums().getOrDefault(source.
                            getAudioCollection().getName(), 0);
                    user.getListenedAlbums().put(source.getAudioCollection().
                            getName(), currCnt + 1);
                    user.getListenedSongs().put(this.getCurrentAudioFile().getName(),
                            currentCount + 1);
                    if (user.isPremium()) {
                        Integer currentCountPremium = user.getListenedSongsPremium().
                                getOrDefault(this.getCurrentAudioFile().getName(), 0);
                        user.getListenedSongsPremium().put(this.getCurrentAudioFile().
                                getName(), currentCountPremium + 1);
                    }
                    if (!user.isPremium()) {
                        Integer currentCountBreak = user.getListenedSongsBreak().
                                getOrDefault(this.getCurrentAudioFile().getName(), 0);
                        user.getListenedSongsBreak().put(this.getCurrentAudioFile().getName(),
                                currentCountBreak + 1);
                        user.addListenedArtistBreak(((Song) this.getCurrentAudioFile()).getArtist());
                    }

                    user.addListenedGenre(((Song) this.getCurrentAudioFile()).getGenre());
                    user.addListenedArtist(((Song) this.getCurrentAudioFile()).getArtist());

                    if (user.isPremium()) {
                        user.addListenedArtistPremium(((Song) this.
                                getCurrentAudioFile()).getArtist());
                    }
                    for (Album album: Admin.getInstance().getAlbums()) {
                        if (album.getName().equals(source.getAudioCollection().getName())
                                && album.getSongs().contains((Song) this.getCurrentAudioFile())) {
                            artist = Admin.getInstance().getUser(album.getOwner());
                            if (artist != null) {
                                Integer countArtist = artist.getListenedSongs().getOrDefault(this.
                                        getCurrentAudioFile().getName(), 0);
                                Integer countArtistsAlbum = artist.getListenedAlbums().
                                        getOrDefault(album.getName(), 0);
                                Integer fansCount = artist.getFans().
                                        getOrDefault(user.getName(), 0);

                                artist.getListenedSongs().put(this.getCurrentAudioFile().getName(),
                                        countArtist + 1);
                                artist.getListenedAlbums().put(album.getName(),
                                        countArtistsAlbum + 1);
                                artist.getFans().put(user.getName(), fansCount + 1);
                            }
                            break;
                        }
                    }
                } else if (this.getCurrentAudioFile() != null && this.type.equals("podcast")) {
                    Integer currentCount = user.getListenedEpisodes().
                            getOrDefault(this.getCurrentAudioFile().getName(), 0);
                    Integer currCnt = user.getListenedPodcasts().
                            getOrDefault(source.getAudioCollection().getName(), 0);
                    user.getListenedPodcasts().put(source.getAudioCollection().getName(), currCnt + 1);
                    user.getListenedEpisodes().put(this.getCurrentAudioFile().getName(),
                            currentCount + 1);
                    if (user.isPremium()) {
                        Integer currentCountPremium = user.getListenedSongsPremium().
                                getOrDefault(this.getCurrentAudioFile().getName(), 0);
                        user.getListenedEpisodesPremium().put(this.getCurrentAudioFile().
                                getName(), currentCountPremium + 1);
                    }
                    if (!user.isPremium()) {
                        Integer currentCountBreak = user.getListenedSongsBreak().
                                getOrDefault(this.getCurrentAudioFile().getName(), 0);
                        user.getListenedEpisodesBreak().put(this.getCurrentAudioFile().
                                getName(), currentCountBreak + 1);
                        user.addListenedHost((source.getAudioCollection()).getOwner());
                    }

                    user.addListenedHost((source.getAudioCollection()).getOwner());

                    if (user.isPremium()) {
                        user.addListenedHostPremium((source.getAudioCollection()).getOwner());
                    }
                    for (Podcast podcast: Admin.getInstance().getPodcasts()) {
                        if (podcast.getName().equals(source.getAudioCollection().getName())
                                && podcast.getEpisodes().
                                contains((Episode) this.getCurrentAudioFile())) {
                            artist = Admin.getInstance().getUser(podcast.getOwner());
                            if (artist != null) {
                                Integer countArtist = artist.getListenedEpisodes().
                                        getOrDefault(this.getCurrentAudioFile().getName(), 0);
                                Integer countArtistsPodcasts = artist.getListenedAlbums().
                                        getOrDefault(podcast.getName(), 0);
                                Integer fansCount = artist.getFans().getOrDefault(user.getName(), 0);

                                artist.getListenedEpisodes().put(this.getCurrentAudioFile().
                                        getName(), countArtist + 1);
                                artist.getListenedAlbums().put(podcast.getName(), countArtistsPodcasts + 1);
                                artist.getFans().put(user.getName(), fansCount + 1);
                            }
                            break;
                        }
                    }
                }
                if (paused) {
                    break;
                }
            }

            if (!paused) {
                source.skip(-time);
            }

            if (source != null && source.getDuration() < 11 && user.isBreakStatus()) {
                int songTotal = 0;
                for (Map.Entry<String, Integer> entry : user.getListenedSongsBreak().entrySet()) {
                    songTotal += entry.getValue();
                }
                for (Map.Entry<String, Integer> entry : user.getMostListenedArtistsBreak().entrySet()) {
                    User artistBreak =  Admin.getInstance().getUser(entry.getKey());
                    double songRevenue = artistBreak.getSongRevenue();

                    songRevenue = songRevenue + (user.getBreakValue() * entry.getValue()) / songTotal;

                    artistBreak.setSongRevenue(songRevenue);
                }
                for (Map.Entry<String, Integer> songEntry : user.getListenedSongsBreak().entrySet()) {
                    User foundArtist = Admin.getInstance().getArtistBySong(songEntry.getKey());
                    if (foundArtist != null) {
                        double revenueForASong = user.getBreakValue() * songEntry.getValue() / songTotal;
                        double currentCountPremium = foundArtist.getMostProfitableSong().getOrDefault(songEntry.getKey(), 0.0);
                        foundArtist.getMostProfitableSong().put(songEntry.getKey(),
                                currentCountPremium + revenueForASong);
                    }
                }

                user.getListenedSongsBreak().clear();
                user.getMostListenedArtistsBreak().clear();
                user.setBreakStatus(false);
            }

        }
    }
    /**
     * Moves the player to the next audio file in the current source,
     * considering repeat and shuffle settings.
     * If the player is set to repeat once, it disables repeat mode
     * after playing the next audio file.
     * If the current source has a duration of 0 (indicating no
     * playable content) and the player is paused,
     * it stops the player.
     */
    public void next() {
        paused = source.setNextAudioFile(repeatMode, shuffle);
        if (repeatMode == Enums.RepeatMode.REPEAT_ONCE) {
            repeatMode = Enums.RepeatMode.NO_REPEAT;
        }

        if (source.getDuration() == 0 && paused) {
            stop();
        }
    }
    /**
     * Moves the player to the previous audio file in the current source,
     * considering shuffle settings.
     * Resets the pause state to false after moving to the previous audio
     * file.
     */
    public void prev() {
        source.setPrevAudioFile(shuffle);
        paused = false;
    }
    /**
     * Skips the playback position within the current source by the
     * specified duration.
     * Resets the pause state to false after skipping.
     *
     * @param duration The duration in milliseconds by which to skip
     *                 the playback position.
     *                 A positive value will advance the playback,
     *                 while a negative value will rewind it.
     */
    private void skip(final int duration) {
        source.skip(duration);
        paused = false;
    }
    /**
     * Skips to the next episode in a podcast source by rewinding
     * the playback position by a predefined duration.
     * This method is specifically designed for podcast sources.
     * If the current source is not a podcast, this method has no effect.
     */

    public void skipNext() {
        if (source.getType() == Enums.PlayerSourceType.PODCAST) {
            skip(-MAGIC_NUMBER90);
        }
    }
    /**
     * Skips to the previous episode in a podcast source by advancing
     * the playback position by a predefined duration.
     * This method is specifically designed for podcast sources.
     * If the current source is not a podcast, this method has no effect.
     */
    public void skipPrev() {
        if (source.getType() == Enums.PlayerSourceType.PODCAST) {
            skip(MAGIC_NUMBER90);
        }
    }
    /**
     * Retrieves the currently playing or paused AudioFile from the
     * current player source.
     *
     * @return The AudioFile representing the current playback state,
     * or null if no source is set.
     */
    public AudioFile getCurrentAudioFile() {
        if (source == null) {
            return null;
        }
        return source.getAudioFile();
    }
    /**
     * Retrieves the current playback pause state of the player.
     *
     * @return {@code true} if the player is currently paused,
     * {@code false} if it is playing.
     */
    public boolean getPaused() {
        return paused;
    }
    /**
     * Retrieves the current shuffle mode of the player.
     *
     * @return {@code true} if the shuffle mode is enabled,
     * {@code false} otherwise.
     */
    public boolean getShuffle() {
        return shuffle;
    }
    /**
     * Retrieves the current statistics and status of the player,
     * including the filename, duration,
     * repeat mode, shuffle mode, and playback pause state.
     *
     * @return A PlayerStats object containing information about the
     * current player state.
     */
    public PlayerStats getStats() {
        String filename = "";
        int duration = 0;
        if (source != null && source.getAudioFile() != null) {
            filename = source.getAudioFile().getName();
            duration = source.getDuration();
        } else {
            stop();
        }

        return new PlayerStats(filename, duration, repeatMode, shuffle, paused);
    }
}
