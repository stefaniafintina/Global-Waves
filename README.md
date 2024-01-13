# Project GlobalWaves  - Stage 2

  The second part of the project comes with new features such as: `artists` and `hosts`(privileged users, because they can `add` and `delete` posts/items from their page: `merch`, `events` and `announcements`), adding the concept of pages: `HomePage`(prints the likedSongs and followedPlaylists for the current user), `ArtistPage`(prints the lists of the specific items that belongs only to them), `HostPage`(prints the lists of the specific items that belongs only to them), `LikedContent Page`(prints the likedSongs and followedPlaylists for the current user and the artist's name).
  
  * When an ordinary user searches for an artist or host and clicks on a search result, their current page `switches` to the one selected by the special user.
  * This stage also features a page system that adheres to the principles of `Spotify`.

  Moreover, a user can be `added` or `deleted`. The deletion can be made only in some situations:
  * if `none` of the user's audio Collections are played on the deletion timestamp
  * if `none` of the users are on user's page 

In case of success, the deletion must be made in the `global database` (the place where the information is public for all users). All the user's `songs`, `podcasts` and `playlist` will be `deleted` for all other users.
  
Also, a new audio file was added: `ALBUM`. It functions similarly to a playlist but has specific distinctions (`announcements`).

Moreover, in this stage of project we have more control over the audio collections, because we can add and delete them, of course, if they follow some `rules`:
* <ul><i>For adding</i>
* check if user exists
* check if user has another album with the same name
* check if the album has at `least 2 songs` with the same name
</ul>

* <ul><i>For deletion</i>

* check if current album is not on play
* check if a song from current album is not on play
* check if other user is on album's owner's page
</ul>

The same occurs for `removePodcast`, `removeAnnouncement`, `removeEvent`.

