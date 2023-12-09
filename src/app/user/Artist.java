package app.user;

import app.artistsPage.Event;
import app.artistsPage.Merch;
import app.audio.Collections.Album;
import app.audio.Collections.Playlist;
import app.audio.Files.Song;
import app.audio.LibraryEntry;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.HashMap;

public class Artist extends User {
    public Artist(String name, int age, String city) {
        super(name, age, city);
    }
}
