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
        User user=new User(name,mobile);
        users.add(user);
        return user;
    }


    public Artist createArtist(String name) {
        Artist artist=new Artist(name);
        artists.add(artist);
        return artist;

    }

    public Album createAlbum(String title, String artistName) {
        Artist artist=new Artist(artistName);
        if(!artists.contains(artist)){
           // Artist artist=new Artist(artistName);
            artists.add(artist);
            Album album=new Album(title);
            albums.add(album);
            List<Album> albums1=new ArrayList<>();
            if(artistAlbumMap.containsKey(artist)) {
                albums1 = artistAlbumMap.get(artist);
             }
           albums1.add(album);
           artistAlbumMap.put(artist,albums1);
        }else{
            List<Album> albums1=artistAlbumMap.get(artists);
            Album album=new Album(title);
            albums1.add(album);
            artistAlbumMap.put(artist,albums1);
        }
         Album album=new Album(title);
         return album;
    }

    public Song createSong(String title, String albumName, int length) throws Exception{
        Album album=new Album(albumName);
        Song song =new Song (title,length);
        if(!albums.contains(album)){
            throw new Exception("Album does not exist");
        }else{
            albums.add(album);
            songs.add(song);
            List<Song> songs1=new ArrayList<>();
            if(albumSongMap.containsKey(album)){
                songs1=albumSongMap.get(album);
            }
            songs1.add(song);
            albumSongMap.put(album,songs1);

        }
        return song;
    }

    public Playlist createPlaylistOnLength(String mobile, String title, int length) throws Exception {
                 Playlist playlist=new Playlist(title);
                 boolean flag=false;
                 for(User user: users){
                     String mobileNo=user.getMobile();
                     if(mobileNo.equals(mobileNo)){
                         creatorPlaylistMap.put(user,playlist);
                         List<Playlist> playlists=new ArrayList<>();
                         if(userPlaylistMap.containsKey(user)){
                             playlists=userPlaylistMap.get(user);
                         }
                         playlists.add(playlist);
                         userPlaylistMap.put(user,playlists);
                         flag=true;
                        for(Song song: songs){
                            int len=song.getLength();
                            if(len==length){
                                List<Song> songs1=new ArrayList<>();
                                if(playlistSongMap.containsKey(playlist)){
                                    songs1=playlistSongMap.get(playlist);
                                }
                                songs1.add(song);
                                playlistSongMap.put(playlist,songs1);
                            }

                        }
                     }
                 }
                 if(flag) return playlist;
                 else  throw new Exception("User does not exist");


    }

    public Playlist createPlaylistOnName(String mobile, String title, List<String> songTitles) throws Exception {
        Playlist playlist=new Playlist(title);
        boolean flag=false;
        for(User user: users){


            String mobileNo=user.getMobile(); // mobile no ofuser

            if(mobileNo.equals(mobile)){ // if mobile no matches given no i.e user exist
                flag=true; // user exist


                creatorPlaylistMap.put(user,playlist);

                List<Playlist> playlists=new ArrayList<>();

                if(userPlaylistMap.containsKey(user)){
                    playlists=userPlaylistMap.get(user);
                }

                playlists.add(playlist);
                userPlaylistMap.put(user,playlists);



                for(Song song: songs ){
                    for(String name: songTitles){
                        if(song.getTitle().equals(name)){
                            List<Song> songs1=new ArrayList<>();
                            if(playlistSongMap.containsKey(playlist)){
                                songs1=playlistSongMap.get(playlist);
                            }
                            songs1.add(song);
                            playlistSongMap.put(playlist,songs1);
                        }
                    }
                }

            }
        }
        if(flag) return  playlist;
        else throw new Exception("User does not exist");

    }

    public Playlist findPlaylist(String mobile, String playlistTitle) throws Exception {
        Playlist playlist=null;
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
                if(playlistListenerMap.containsKey(playlist)||creatorPlaylistMap.containsKey(user)){
                    continue;
                }else{
                    List<User> users1=new ArrayList<>();
                    users1.add(user);
                    if(!playlistListenerMap.containsKey(playlist))
                        playlistListenerMap.put(playlist,users1);

                    if(!creatorPlaylistMap.containsKey(user)){
                        creatorPlaylistMap.put(user,playlist);
                    }
                }
            }
        }
        if(!flag2){
            throw  new Exception("Playlist does not exist");
        }
      if(flag1==false){
          throw  new Exception("User does not exist");
      }

      return playlist;
    }

    public Song likeSong(String mobile, String songTitle) throws Exception {
        Song song2 = null;
        Playlist playlist;
        User user1=null;
        boolean flag=false;
        for(User user: users){
            String mblNo=user.getMobile();
            if(mblNo.equals(mobile)){
                flag=true;
                user1=user;
                playlist=creatorPlaylistMap.get(user);
                break;
            }
        }
        if(!flag){
            throw new Exception("User does not exist");
        }
      boolean flag2=false;
         for(Song song: songs){
             if(song.getTitle().equals(songTitle)){
                  song2=song;
                  flag2=true;
                 List<User> userList=new ArrayList<>();
                 if(songLikeMap.containsKey(song)){
                    userList=songLikeMap.get(song);
                 }
                 if(!songLikeMap.containsKey(song)) {
                     int songLike = song.getLikes();
                     songLike++;
                     song.setLikes(songLike);
                     userList.add(user1);
                     songLikeMap.put(song,userList);



                 }

             }
         }
         if(!flag2) throw new Exception("Song does not exist");


         Album album2=null;
         for(Album album:albumSongMap.keySet()){
             if(albumSongMap.get(album).contains(song2)){
                 album2=album;
             }
         }
         Artist artist=null;
         for(Artist artist1: artistAlbumMap.keySet()){
             if(artistAlbumMap.get(artist1).contains(album2)){
                 artist=artist1;
                 int likes=artist.getLikes();
                 likes++;
                 artist.setLikes(likes);
             }
         }

         return  song2;
    }

    public String mostPopularArtist() {
        String name="";
        int ans=0;
        for(Artist artist: artists){
            int temp=artist.getLikes();
            if(temp>ans){
                ans=temp;
                name=artist.getName();
            }
        }
        return name;
    }

    public String mostPopularSong() {
        String songName="";
        int ans=0;
        for(Song song:songs){
            int temp=song.getLikes();
            if(temp>ans){
                ans=temp;
                songName=song.getTitle();
            }
        }
        return songName;
    }
}
