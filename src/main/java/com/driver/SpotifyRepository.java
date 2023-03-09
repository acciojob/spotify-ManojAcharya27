package com.driver;

import java.util.*;

import org.springframework.stereotype.Repository;

@Repository
public class SpotifyRepository {
    public HashMap<Artist, List<Album>> artistAlbumMap;
    public HashMap<Album, List<Song>> albumSongMap;
    public HashMap<Playlist, List<Song>> playlistSongMap;
    public HashMap<Playlist, List<User>> playlistListenerMap;
    public HashMap<User, Playlist> creatorPlaylistMap;
    public HashMap<User, List<Playlist>> userPlaylistMap;
    public HashMap<Song, List<User>> songLikeMap;

    public List<User> users;
    public List<Song> songs;
    public List<Playlist> playlists;
    public List<Album> albums;
    public List<Artist> artists;

    public SpotifyRepository(){
        //To avoid hitting apis multiple times, initialize all the hashmaps here with some dummy data
        artistAlbumMap = new HashMap<>();
        albumSongMap = new HashMap<>();
        playlistSongMap = new HashMap<>();
        playlistListenerMap = new HashMap<>();
        creatorPlaylistMap = new HashMap<>();
        userPlaylistMap = new HashMap<>();
        songLikeMap = new HashMap<>();

        users = new ArrayList<>();
        songs = new ArrayList<>();
        playlists = new ArrayList<>();
        albums = new ArrayList<>();
        artists = new ArrayList<>();
    }

    public User createUser(String name, String mobile) {
        User user=new User();
        user.setName(name);
        user.setMobile(mobile);
        users.add(user);
        return user;
    }


    public Artist createArtist(String name) {
        Artist artist=new Artist();
        artist.setName(name);
        artists.add(artist);
        return artist;

    }

    public Album createAlbum(String title, String artistName) {

        Artist artist=null;
        for(Artist artist1 :artists){
            if(artist1.getName()==artistName){
                artist=artist1;
            }
        }

        if(artist==null){
           // Artist artist=new Artist(artistName);
            artist = createArtist(artistName);

            Album album = new Album();

            album.setTitle(title);
            album.setReleaseDate(new Date());

            albums.add(album);

            List<Album> list= new ArrayList<>();
            list.add(album);
            artistAlbumMap.put(artist,list);

            return album;
        }else{
            Album album = new Album();

            album.setTitle(title);
            album.setReleaseDate(new Date());

            albums.add(album);

            List<Album> list = artistAlbumMap.get(artist);
            if(list == null){
                list = new ArrayList<>();
            }
            list.add(album);
            artistAlbumMap.put(artist,list);

            return album;
        }
    }

    public Song createSong(String title, String albumName, int length) throws Exception{
        Album album=null;
        // finding Album present or not
        for (Album album2:albums){
            if(album2.getTitle()==albumName){
                album=album2;
                break;
            }
        }
        // if not throw exception
        if(album==null){
            throw new Exception("Album does not exist");
        }else{
            Song song=new Song(title,length);  // create song add that song to list
            songs.add(song);
            if(albumSongMap.containsKey(album)){   //  find the album is already created or not if created then get the list of all songs
                List<Song> songList=new ArrayList<>();
                songList=albumSongMap.get(album);
                songList.add(song);              // then add that song to that list
                albumSongMap.put(album,songList); // then update song album map
            }else {
                List<Song> songList=new ArrayList<>();  // if not created, create new list and then add that song then update
                songList.add(song);
                albumSongMap.put(album,songList);
            }
            return song;
        }

    }

    public Playlist createPlaylistOnLength(String mobile, String title, int length) throws Exception {

                 User user1=null;   // first find the given user is already registered or not
                for(User user: users){
                    if(user.getMobile()==mobile){
                        user1=user;
                    }
                }
                if(user1==null) { // if not throw exception
                    throw new Exception("User does not exist");
                }
                else {
                    Playlist playlist = new Playlist(); // then create a new play list with  given title
                    playlist.setTitle(title);
                    playlists.add(playlist);

                    List<Song> list = new ArrayList<>();   // then create a list of song which is equal to given length of song
                    for(Song song:songs){
                        if(song.getLength()==length){
                            list.add(song);
                        }
                    }
                    playlistSongMap.put(playlist,list); // then update the list of song with playlist

                    List<User> uList = new ArrayList<>();
                    uList.add(user1);      // add the given to user list

                    playlistListenerMap.put(playlist,uList);  // update in userListenerdb with created playlist

                    creatorPlaylistMap.put(user1,playlist); // and also update in creator db with user and play list

                   if(userPlaylistMap.containsKey(user1)){ // find the playlist of given user if user already using some other playlist
                       List<Playlist> playlistList=new ArrayList<>(); // get that playlist and update it
                       playlistList=userPlaylistMap.get(user1);
                       playlistList.add(playlist);
                       userPlaylistMap.put(user1,playlistList);
                   }else {
                       List<Playlist> playlistList=new ArrayList<>(); // if not create new one then update
                       playlistList.add(playlist);
                       userPlaylistMap.put(user1,playlistList);
                   }

                    return playlist;
                }
    }

    public Playlist createPlaylistOnName(String mobile, String title, List<String> songTitles) throws Exception {
        User user = null;    // same thing but add that song  according to song name
        for(User u: users){
            if(u.getMobile().equals(mobile)){
                user = u;
                break;
            }
        }
        if(user == null){
            throw  new Exception("User does not exist");
        }
        else{
            Playlist playlist = new Playlist(title);
            playlists.add(playlist);
            List<Song> song_list = new ArrayList<>();
            for (Song s : songs) {
                if (songTitles.contains(s.getTitle())) {
                    song_list.add(s);
                }
            }
            playlistSongMap.put(playlist, song_list);
            creatorPlaylistMap.put(user, playlist);
            List<User> userList = new ArrayList<>();
            if(playlistListenerMap.containsKey(playlist)){
                userList = playlistListenerMap.get(playlist);
            }
            userList.add(user);
            playlistListenerMap.put(playlist,userList);
            List<Playlist> playlistList = new ArrayList<>();
            if (userPlaylistMap.containsKey(user)) {
                playlistList = userPlaylistMap.get(user);
            }
            playlistList.add(playlist);
            userPlaylistMap.put(user, playlistList);
            return playlist;

        }
    }

    public Playlist findPlaylist(String mobile, String playlistTitle) throws Exception {

        Playlist playlist=null;
        User user1=null;
        boolean flag1=false;
        boolean flag2=false;
        for(Playlist playlist1: playlists){
            if(playlist1.getTitle().equals(playlistTitle)){
                playlist=playlist1;
                flag2=true;
                break;
            }
        }
        for(User user: users){
            String mobilNo=user.getMobile();
            if(mobilNo.equals(mobile)){
                flag1=true;
                user1=user;
            }
        }
        if(flag1==false){
            throw  new Exception("User does not exist");
        }

        if(!flag2){
            throw  new Exception("Playlist does not exist");
        }

        if (creatorPlaylistMap.containsKey(user1)){
            return playlist;
        }

        List<User> listenerList=playlistListenerMap.get(user1);

        for(User user: listenerList){
            if(user==user1){
                return playlist;
            }
        }

        listenerList.add(user1);

        playlistListenerMap.put(playlist,listenerList);

        List<Playlist> playlistList=new ArrayList<>();
             if(userPlaylistMap.containsKey(user1)) {
                 playlistList = userPlaylistMap.get(user1);
             }
             playlistList.add(playlist);
             userPlaylistMap.put(user1,playlistList);

             return playlist;

    }

    public Song likeSong(String mobile, String songTitle) throws Exception {

        Song song2 = null;
        Playlist playlist;
        User user1=null;
        boolean flag=false;
        if(users.size()!=0) {
            for (User user : users) {
                String mblNo = user.getMobile();
                if (mblNo.equals(mobile)) {
                    flag = true;
                    user1 = user;
                    playlist = creatorPlaylistMap.get(user);
                    break;
                }
            }
        }
        if(!flag){
            throw new Exception("User does not exist");
        }
      boolean flag2=false;
        if(songs.size()!=0) {
            for (Song song : songs) {
                if (song.getTitle().equals(songTitle)) {
                    song2 = song;
                    flag2 = true;
                }
            }
        }
         if(!flag2) throw new Exception("Song does not exist");

         if(songLikeMap.containsKey(song2)){
             List<User> userList=songLikeMap.get(song2);
             if(userList.size()!=0&&userList.contains(user1)){
                 return song2;
             }else {
                 int likes=song2.getLikes();
                 likes++;
                 song2.setLikes(likes);
                 userList.add(user1);
                 songLikeMap.put(song2,userList);

                 Album album=null;
                if(artistAlbumMap.size()!=0) {
                    for (Album album1 : albumSongMap.keySet()) {

                        List<Song> songList = new ArrayList<>();
                        if(albumSongMap.containsKey(album1)) {
                            songList = albumSongMap.get(album1);
                        }
                        if (songList.contains(song2)) {
                            album = album1;
                            break;
                        }

                    }
                }
                 Artist artist=null;
                 for (Artist artist1: artistAlbumMap.keySet()){
                     List<Album> albumList=artistAlbumMap.get(artist1);
                     if(albumList.contains(album)){
                         artist=artist1;
                          break;
                     }
                 }
                 int likesForArtist=artist.getLikes();
                 likesForArtist++;
                 artist.setLikes(likesForArtist);
                 artists.add(artist);
                 return  song2;
             }
         }else {
             int likes=song2.getLikes();
             likes++;
             song2.setLikes(likes);

             List<User> userList=new ArrayList<>();
             userList.add(user1);
             songLikeMap.put(song2,userList);




             Album album=null;
             if(albumSongMap.size()!=0) {
                 for (Album album1 : albumSongMap.keySet()) {

                     List<Song> songList = new ArrayList<>();
                     if(albumSongMap.containsKey(album1)) {
                         songList = albumSongMap.get(album1);
                     }
                     if (songList.contains(song2)) {
                         album = album1;
                         break;
                     }
                 }
             }

             Artist artist=null;
             for (Artist artist1: artistAlbumMap.keySet()){
                 List<Album> albumList=artistAlbumMap.get(artist1);
                 if(albumList.contains(album)){
                     artist=artist1;
                     break;
                 }
             }
             int likesForArtist=artist.getLikes();
             likesForArtist++;
             artist.setLikes(likesForArtist);
             artists.add(artist);
         }

         return  song2;
    }

    public String mostPopularArtist() {
       Artist artist=null;
       int max=0;
       if(artists.size()!=0) {
           for (Artist artist1 : artists) {
               if (artist1.getLikes() >= max) {
                   max = artist1.getLikes();
                   artist = artist1;
               }
           }
       }
       if(artist==null) return null;
       else return artist.getName();
    }

    public String mostPopularSong() {
      Song song=null;
      int max=0;
      for (Song song1: songs){
          if(song1.getLikes()>=max){
              max=song1.getLikes();
              song=song1;
          }
      }
      if(song==null) return  null;
      return song.getTitle();
    }
}
