import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class User {

    private int id;
    private ArrayList<Song> songsListened;
    private int songsCount;
    private boolean sorted = false;

    public User(int id) {
        this.id = id;
        songsListened = new ArrayList<>();
        songsCount = 0;
    }

    public int getId() {
        return id;
    }

    public void addSong(int songId) {
        int index = containsKey(songId);
        if (index >= 0) {
            songsListened.get(index).increaseCount();
        } else {
            songsListened.add(new Song(songId));
            sorted = false;
            songsListened.trimToSize();
        }
        songsCount++;
    }

    public int containsKey(int songId){
        if (!sorted){
            Collections.sort(songsListened, Comparator.comparingInt(Song::getId));
            sorted = true;
        }
        return Collections.binarySearch(songsListened, new Song(songId), Comparator.comparingInt(Song::getId));
    }

    public int getSongsCollectionSize() {
        return songsListened.size();
    }

    public int getCountOfSongsTimes(int songId) {
        if (songsListened.get(songId) != null) {
            return songsListened.get(songId).getCount();
        } else {
            return 0;
        }
    }

    public boolean isListened(int songId) {
        return containsKey(songId) >= 0;
    }

    public ArrayList<Song> getSongsListened() {
        return songsListened;
    }

    public int getSongsCount() {
        return songsCount;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", songsListened=" + songsListened.size() +
                ", songsCount=" + songsCount +
                "}";
    }
}
