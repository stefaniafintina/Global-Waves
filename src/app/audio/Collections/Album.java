package app.audio.Collections;

import app.audio.Files.AudioFile;
import app.audio.Files.Song;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;

public final class Album extends AudioCollection {
    @Getter
    @Setter
    private ArrayList<Song> songs;
    @Getter
    @Setter
    private Integer releaseYear;
    @Getter
    @Setter
    private String description;
    @Getter
    @Setter
    private Integer likes;

    /**
     */
    public Album(final String name, final String owner, final Integer releaseYear,
                 final String description) {
        super(name, owner);
        this.songs = new ArrayList<>();
        this.releaseYear = releaseYear;
        this.description = description;
        this.likes = 0;
    }
    /**
     */
    public void addSong(final Song song) {
        songs.add(song);
    }
    /**
     */
    @Override
    public int getNumberOfTracks() {
        return songs.size();
    }
    /**
     */
    @Override
    public AudioFile getTrackByIndex(final int index) {
        return songs.get(index);
    }

}
