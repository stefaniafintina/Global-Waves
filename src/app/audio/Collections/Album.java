package app.audio.Collections;

import app.Admin;
import app.audio.Files.AudioFile;
import app.audio.Files.Song;
import app.user.User;
import fileio.input.SongInput;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;

@Getter
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

    public Album(String name, String owner, Integer releaseYear, String description) {
        super(name, owner);
        this.songs = new ArrayList<>();
        this.releaseYear = releaseYear;
        this.description = description;
    }

    public void addSong(Song song) {
        songs.add(song);
    }
    @Override
    public int getNumberOfTracks() {
        return songs.size();
    }
    @Override
    public AudioFile getTrackByIndex(int index) {
        return songs.get(index);
    }

}
