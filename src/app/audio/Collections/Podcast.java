package app.audio.Collections;

import app.Admin;
import app.audio.Files.AudioFile;
import app.audio.Files.Episode;
import app.audio.Files.Song;
import app.user.User;

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

    @Override
    public int getNumberOfTracks() {
        return episodes.size();
    }

    @Override
    public AudioFile getTrackByIndex(int index) {
        return episodes.get(index);
    }
    public boolean hasSomethingBelongingTo(User user) {
//        for (Song song : Admin.getSongs())
//            if (song.getArtist().equals(user.getName()))
//                return true;
        return false;
    }
}
