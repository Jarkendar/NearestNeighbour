public class Song {
    private int id;
    private int count = 0;

    public Song(int id) {
        this.id = id;
        count = 1;
    }

    public int getId() {
        return id;
    }

    public int getCount() {
        return count;
    }

    public void increaseCount() {
        count++;
    }

    @Override
    public String toString() {
        return "Song{" +
                "id=" + id +
                ", count=" + count +
                '}';
    }
}
