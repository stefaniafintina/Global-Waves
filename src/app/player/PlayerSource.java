package app.player;

import app.audio.Collections.AudioCollection;
import app.audio.Files.AudioFile;
import app.utils.Enums;
import lombok.Getter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public final class PlayerSource {
    @Getter
    private Enums.PlayerSourceType type;
    @Getter
    private AudioCollection audioCollection;
    @Getter
    private AudioFile audioFile;
    @Getter
    private int index;
    private int indexShuffled;
    private int remainedDuration;
    private final List<Integer> indices = new ArrayList<>();

    public PlayerSource(final Enums.PlayerSourceType type, final AudioFile audioFile) {
        this.type = type;
        this.audioFile = audioFile;
        this.remainedDuration = audioFile.getDuration();
    }

    public PlayerSource(final Enums.PlayerSourceType type, final AudioCollection audioCollection) {
        this.type = type;
        this.audioCollection = audioCollection;
        this.audioFile = audioCollection.getTrackByIndex(0);
        this.index = 0;
        this.indexShuffled = 0;
        this.remainedDuration = audioFile.getDuration();
    }

    public PlayerSource(final Enums.PlayerSourceType type, final AudioCollection audioCollection,
                        final PodcastBookmark bookmark) {
        this.type = type;
        this.audioCollection = audioCollection;
        this.index = bookmark.getId();
        this.remainedDuration = bookmark.getTimestamp();
        this.audioFile = audioCollection.getTrackByIndex(index);
    }

    public int getDuration() {
        return remainedDuration;
    }
    /**
     * Sets the next audio file in the player source based on the provided
     * repeat mode and shuffle settings.
     *
     * @param repeatMode The repeat mode indicating how the player should
     *                  handle repeated playback.
     * @param shuffle    {@code true} if shuffle mode is enabled,
     * {@code false} otherwise.
     * @return {@code true} if the player is paused after setting
     * the next audio file, {@code false} otherwise.
     */
    public boolean setNextAudioFile(final Enums.RepeatMode repeatMode, final boolean shuffle) {
        boolean isPaused = false;

        if (type == Enums.PlayerSourceType.LIBRARY) {
            if (repeatMode != Enums.RepeatMode.NO_REPEAT) {
                remainedDuration = audioFile.getDuration();
            } else {
                remainedDuration = 0;
                isPaused = true;
            }
        } else {
            if (repeatMode == Enums.RepeatMode.REPEAT_ONCE
                    || repeatMode == Enums.RepeatMode.REPEAT_CURRENT_SONG
                    || repeatMode == Enums.RepeatMode.REPEAT_INFINITE) {
                remainedDuration = audioFile.getDuration();
            } else if (repeatMode == Enums.RepeatMode.NO_REPEAT) {
                if (shuffle) {
                    if (indexShuffled == indices.size() - 1) {
                        remainedDuration = 0;
                        isPaused = true;
                    } else {
                        indexShuffled++;

                        index = indices.get(indexShuffled);
                        updateAudioFile();
                        remainedDuration = audioFile.getDuration();
                    }
                } else {
                    if (index == audioCollection.getNumberOfTracks() - 1) {
                        remainedDuration = 0;
                        isPaused = true;
                    } else {
                        index++;
                        updateAudioFile();
                        remainedDuration = audioFile.getDuration();
                    }
                }
            } else if (repeatMode == Enums.RepeatMode.REPEAT_ALL) {
                if (shuffle) {
                    indexShuffled = (indexShuffled + 1) % indices.size();
                    index = indices.get(indexShuffled);
                } else {
                    index = (index + 1) % audioCollection.getNumberOfTracks();
                }
                updateAudioFile();
                remainedDuration = audioFile.getDuration();
            }
        }

        return isPaused;
    }
    /**
     * Sets the previous audio file in the player source based
     * on the provided shuffle setting.
     *
     * @param shuffle {@code true} if shuffle mode is enabled,
     * {@code false} otherwise.
     */
    public void setPrevAudioFile(final boolean shuffle) {
        if (type == Enums.PlayerSourceType.LIBRARY) {
            remainedDuration = audioFile.getDuration();
        } else {
            if (remainedDuration != audioFile.getDuration()) {
                remainedDuration = audioFile.getDuration();
            } else {
                if (shuffle) {
                    if (indexShuffled > 0) {
                        indexShuffled--;
                    }
                    index = indices.get(indexShuffled);
                    updateAudioFile();
                    remainedDuration = audioFile.getDuration();
                } else {
                    if (index > 0) {
                        index--;
                    }
                    updateAudioFile();
                    remainedDuration = audioFile.getDuration();
                }
            }
        }
    }
    /**
     * Generates a shuffled order of indices for the audio tracks
     * in the player source using the provided seed.
     * Clears the existing indices and populates the list with a
     * shuffled order.
     *
     * @param seed The seed for the random number generator to
     *            ensure consistent shuffling.
     *             Can be {@code null} for non-seeded shuffling.
     */
    public void generateShuffleOrder(final Integer seed) {
        indices.clear();
        Random random = new Random(seed);
        if (audioCollection != null) {
            for (int i = 0; i < audioCollection.getNumberOfTracks(); i++) {
                indices.add(i);
            }
        }
        Collections.shuffle(indices, random);
    }
    /**
     * Updates the shuffle index based on the current index in the
     * shuffled order.
     * This method is useful when transitioning between non-shuffle
     * and shuffle modes.
     */
    public void updateShuffleIndex() {
        for (int i = 0; i < indices.size(); i++) {
            if (indices.get(i) == index) {
                indexShuffled = i;
                break;
            }
        }
    }
    /**
     * Skips the playback position within the current audio file by
     * the specified duration.
     * Adjusts the remained duration, and advances to the next audio
     * file if needed.
     *
     * @param duration The duration in milliseconds by which to skip
     *                 the playback position.
     *                 A positive value will advance the playback,
     *                 while a negative value will rewind it.
     */
    public void skip(final int duration) {
        remainedDuration += duration;
        if (remainedDuration > audioFile.getDuration()) {
            remainedDuration = 0;
            index++;
            updateAudioFile();
        } else if (remainedDuration < 0) {
            remainedDuration = 0;
        }
    }
    /**
     * Updates the current audio file in the player source based
     * on the current index.
     * Retrieves the audio file from the associated audio collection
     * and sets it as the current audio file.
     */
    private void updateAudioFile() {
        setAudioFile(audioCollection.getTrackByIndex(index));
    }
    /**
     * Updates the current audio file in the player source based on the current index.
     * Retrieves the audio file from the associated audio collection and sets it as the c
     * urrent audio file.
     */
    public void setAudioFile(final AudioFile audioFile) {
        this.audioFile = audioFile;
    }

}
