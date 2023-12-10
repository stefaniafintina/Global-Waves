package app.audio.Collections;

import app.Admin;
import app.audio.Files.AudioFile;
import app.audio.Files.Episode;
import app.audio.Files.Song;
import app.user.User;
import fileio.input.EpisodeInput;

import java.util.ArrayList;
import java.util.List;

public final class Podcast extends AudioCollection {
    private final List<Episode> episodes;

    public Podcast(String name, String owner, List<Episode> episodes) {
        super(name, owner);
        this.episodes = episodes;
    }

    public List<Episode> getEpisodes() {
        return episodes;
    }
    public void addEpisode(Episode episode) {
        episodes.add(episode);
    }

    @Override
    public int getNumberOfTracks() {
        return episodes.size();
    }

    @Override
    public AudioFile getTrackByIndex(int index) {
        return episodes.get(index);
    }

}
