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

            Album album=new Album(title);

            albums.add(album);

            List<Album> albums1=new ArrayList<>();

            if(artistAlbumMap.containsKey(artist)) {
                albums1 = artistAlbumMap.get(artist);
             }

             albums1.add(album);

             artistAlbumMap.put(artist,albums1);

             return album;
        }else{
            Album album=new Album(title);

            List<Album> albums1=new ArrayList<>();


            if(artistAlbumMap.containsKey(artist)) {
                albums1 = artistAlbumMap.get(artists);
            }

            albums1.add(album);

            artistAlbumMap.put(artist,albums1);


            return album;
        }
    }

    public Song createSong(String title, String albumName, int length) throws Exception{
        Album album=new Album(albumName);
        Song song =new Song (title,length);
        if(!albums.contains(album)){
            throw new Exception("Album does not exist");
        }else{
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
                 /*for(User user: users){
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
                 else  throw new Exception("User does not exist");*/
                 User user1=null;
                for(User user: users){
                    if(user.getMobile().equals(mobile)){
                        user1=user;
                        flag=true;
                    }
                }
                if(!flag) throw new Exception("User does not exist");
                else {
                    List<Song> songList=new ArrayList<>();
                    for (Song song : songs) {

                        int lengthOfSong = song.getLength();

                        if (lengthOfSong == length) {
                            songList.add(song);
                        }

                    }
                    playlistSongMap.put(playlist,songList);

                    List<User> usersList=new ArrayList<>();

                    usersList.add(user1);

                    playlistListenerMap.put(playlist,usersList);

                    creatorPlaylistMap.put(user1,playlist);

                    List<Playlist> playlistList=new ArrayList<>();

                    if(userPlaylistMap.containsKey(user1)){
                        playlistList=userPlaylistMap.get(user1);
                    }

                    playlistList.add(playlist);

                    userPlaylistMap.put(user1,playlistList);

                    return playlist;
                }

    }

    public Playlist createPlaylistOnName(String mobile, String title, List<String> songTitles) throws Exception {
       /* Playlist playlist=new Playlist(title);
        boolean flag=false;
        for(User user: users){


            String mobileNo=user.getMobile(); // mobile no of user

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
        else throw new Exception("User does not exist");*/

        User user=null;
        boolean flag=false;

        for (User user1: users){
            if(user1.getMobile().equals(mobile)){
                flag=true;
                user=user1;
                break;
            }
        }
        if(!flag) throw new Exception("User does not exist");
        else {
            Playlist playlist=new Playlist(title);

            playlists.add(playlist);

            List<Song> songList=new ArrayList<>();

            for(Song song: songs){
                if(songTitles.contains(song.getTitle())){
                    songList.add(song);
                }
            }

            playlistSongMap.put(playlist,songList);

            creatorPlaylistMap.put(user,playlist);

            List<Playlist> playlistList=new ArrayList<>();

            if(userPlaylistMap.containsKey(user)){
                playlistList=userPlaylistMap.get(user);
            }

            playlistList.add(playlist);

            userPlaylistMap.put(user,playlistList);

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
             }
         }
         if(!flag2) throw new Exception("Song does not exist");

         if(songLikeMap.containsKey(song2)){
             List<User> userList=songLikeMap.get(song2);
             if(userList.contains(user1)){
                 return song2;
             }else {
                 int likes=song2.getLikes();
                 likes++;
                 song2.setLikes(likes);
                 userList.add(user1);
                 songLikeMap.put(song2,userList);

                 Album album=null;

                 for (Album album1 :albumSongMap.keySet()){

                     List<Song> songList=new ArrayList<>();
                     songList=albumSongMap.get(album1);
                     if(songList.contains(song2)){
                         album=album1;
                         break;
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
             for (Album album1 :albumSongMap.keySet()){

                 List<Song> songList=new ArrayList<>();
                 songList=albumSongMap.get(album1);
                 if(songList.contains(song2)){
                     album=album1;
                     break;
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
        String name="";
        int ans=0;
        for(Artist artist: artists){
            int temp=artist.getLikes();
            if(temp>=ans){
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
