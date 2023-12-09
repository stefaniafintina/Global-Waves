package app.audio.Collections;

import app.audio.Files.AudioFile;
import app.audio.LibraryEntry;
import app.user.User;
import lombok.Getter;

@Getter
public abstract class AudioCollection extends LibraryEntry {
    private final String owner;

    public AudioCollection(String name, String owner) {
        super(name);
        this.owner = owner;
    }

    public abstract int getNumberOfTracks();

    public abstract AudioFile getTrackByIndex(int index);

    @Override
    public boolean matchesOwner(String user) {
        return this.getOwner().equals(user);
    }
    public abstract boolean hasSomethingBelongingTo(User user);
}
