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



# Project GlobalWaves  - Stage 3

The third part of the project comes with the following features:
* `wrapped`
* `page navigation`
* `notifications`
* `monetization`

The `wrapped` feature is implemented for artists, hosts, and regular users.
* To implement these `statistics`, I utilized `linkedHashMaps` to maintain data regarding the frequency of listens 
for each type of `audio file` (such as songs and episodes) and `audio collections` (including albums, podcasts, and playlists).
Additionally, for `sorting` this information, I employed `streams`.
* The hashmaps get `refreshed` not only upon `loading new content` but also through the `player class`, which actively counts
`songs` and `episodes` from the audio collection being `played at the moment`.
* For example, when a user `loads` a new source into their player, the application `updates` the count for the `song`, `album`, and the `associated artist`, 
thereby reflecting an increase in the user's `total listens` for that artist.

`Monetization` is managed in `two` different ways:
* Income from `premium subscriptions` 
  * When a `regular user` holds a premium status, we utilize a hashMap to keep track of the 
  songs they `listen` to and their `frequency` of listening throughout the duration of their
  `premium subscription`. In the event of the user `cancelling` their subscription or at the 
  end of the program, the `revenue for each song` is determined using a specific `formula` and 
  the data from these hashMaps. This allocation of revenue is dependent on how `often` the 
  user listened to each song, as recorded in the `hashMap`. Following this calculation, the
  hashMap is `reset`.
* Income from `ads`
  * When a regular user has a `free account`, `ads` can be inserted during their normal playback
  of `audio files`. We simulate this `interaction` by extending the duration of the currently 
  playing audio file by the `length` of the ad and creating new hashmaps that stores the information
  about number of `listening` of each audio file or audio collection, but only when the user does
  not have a premium subscription and setting a corresponding `flag`.
  * If the flag is on and the player `switches` to another source or the current one 
  is almost over (less than `10` seconds left), we calculate earnings for all songs
  played between two ads or from the program's start to the first ad. We use a similar 
  method to how we handle `earning money` with premium accounts.

`Notifications` are set up as a way to share `updates` between `artists`, `hosts` and `playlists` and `users`:
* The system for notifications uses the Observer design pattern.
* When a regular user (acting as an `Observer`) subscribes to or unsubscribes from an artist or host (both acting as `Subjects`),
or follows or unfollows a playlist (also a `Subject`), they are added to or removed from the `Subject's list of Observers`.
* For instance, if an artist `releases` a new album, `all Observers` in their list get `notified` about this new release,
and the notification is added to an internal ArrayList that keeps `all notifications`.
* When a user `checks` their notifications, these are `displayed` in the json file and 
then `removed` from the internal list.

`Page navigation` is designed for quick and easy `movement` between `recent pages`, implemented using the
`Command` design pattern:
* The `history` of visited pages is maintained in a `Deque<Page>` (a double-ended queue), 
which acts as a flexible data structure allowing for efficient `addition` or `removal` of 
page states from either end.
* When a user `navigates` to a new page, a command representing this navigation is executed.
This command `saves` the new page state to the `pageHistory` deque, treating it as a stack for
backward navigation.
* For `forward` navigation, if the user wants to go forward to a page they navigated back 
from, a `next page` command can be used.



