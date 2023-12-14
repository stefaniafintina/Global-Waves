package app.audio.Collections;

import app.audio.Files.AudioFile;
import app.audio.LibraryEntry;
import lombok.Getter;

@Getter
public abstract class AudioCollection extends LibraryEntry {
    private final String owner;

    public AudioCollection(final String name, final String owner) {
        super(name);
        this.owner = owner;
    }
    /**
     * Retrieves the number of tracks in the audio collection.
     *
     * @return The number of tracks in the audio collection.
     */
    public abstract int getNumberOfTracks();
    /**
     * Retrieves the audio file (track) from the audio collection based on the provided index.
     *
     * @param index The index of the track in the audio collection.
     * @return The AudioFile representing the track at the specified index.
     * @throws IndexOutOfBoundsException If the provided index is outside the valid
     * range of track indices.
     */
    public abstract AudioFile getTrackByIndex(int index);
    /**
     * Checks if the owner of the object matches the specified user.
     *
     * @param user The user to compare against the owner of the object.
     * @return {@code true} if the owner matches the specified user, {@code false} otherwise.
     */
    @Override
    public boolean matchesOwner(final String user) {
        return this.getOwner().equals(user);
    }
}
