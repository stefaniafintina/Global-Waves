package app.audio.Files;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
public final class Song extends AudioFile {
    private final String album;
    private final ArrayList<String> tags;
    private final String lyrics;
    private final String genre;
    private final Integer releaseYear;
    private final String artist;
    @Setter
    private Integer likes;

    /**
     */
    public Song(final String name, final Integer duration, final String album,
                final ArrayList<String> tags, final String lyrics,
                final String genre, final Integer releaseYear, final String artist) {
        super(name, duration);
        this.album = album;
        this.tags = tags;
        this.lyrics = lyrics;
        this.genre = genre;
        this.releaseYear = releaseYear;
        this.artist = artist;
        this.likes = 0;
    }
    /**
     */
    @Override
    public boolean matchesAlbum(final String newAlbum) {
        return this.getAlbum().equalsIgnoreCase(newAlbum);
    }
    /**
     */
    @Override
    public boolean matchesTags(final ArrayList<String> newTags) {
        List<String> songTags = new ArrayList<>();
        for (String tag : this.getTags()) {
            songTags.add(tag.toLowerCase());
        }

        for (String tag : newTags) {
            if (!songTags.contains(tag.toLowerCase())) {
                return false;
            }
        }
        return true;
    }
    /**
     */
    @Override
    public boolean matchesLyrics(final String newLyrics) {
        return this.getLyrics().toLowerCase().contains(newLyrics.toLowerCase());
    }
    /**
     */
    @Override
    public boolean matchesGenre(final String newGenre) {
        return this.getGenre().equalsIgnoreCase(newGenre);
    }
    /**
     */
    @Override
    public boolean matchesArtist(final String newArtist) {
        return this.getArtist().equalsIgnoreCase(newArtist);
    }
    /**
     */
    @Override
    public boolean matchesReleaseYear(final String newReleaseYear) {
        return filterByYear(this.getReleaseYear(), newReleaseYear);
    }
    /**
     */
    private static boolean filterByYear(final int year, final String query) {
        if (query.startsWith("<")) {
            return year < Integer.parseInt(query.substring(1));
        } else if (query.startsWith(">")) {
            return year > Integer.parseInt(query.substring(1));
        } else {
            return year == Integer.parseInt(query);
        }
    }

    /**
     */
    public void like() {
        likes++;
    }
    /**
     */
    public void dislike() {
        likes--;
    }
}
