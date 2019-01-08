import java.math.BigInteger;
import java.util.*;

public class MinHashFunction {

    private static ArrayList<Integer> order;
    private HashMap<Integer, Integer> memonize;
    private long a, b, prime;
    private int size;

    public MinHashFunction(int size) {
        this.size = size;
//        createList(size);
        memonize = new HashMap<>();
        Random random = new Random();
        prime = new BigInteger(size + "").nextProbablePrime().intValue();
        a = random.nextInt((int) prime-2)+1;
        b = random.nextInt((int) prime-1);
    }

    private long hash(int x) {
        return (a * x + b) % prime;
    }

    private void createList(int size) {
        if (order == null) {
            order = new ArrayList<>();
            for (int i = 0; i < size; i++) {
                order.add(i, i);
            }
            order.trimToSize();
            shuffleOrder();
        }
    }

    private void shuffleOrder(){
        Collections.shuffle(order);
    }

    public int getMinHash(User user) {
        int minHash = Integer.MAX_VALUE;
        for (int i = 0; i < user.getSongsListened().size(); i++ ){
            int hash = 0;
            int songId = user.getSongsListened().get(i).getId();
            if (memonize.get(songId) != null) {
                hash = memonize.get(songId);
            }else {
                hash = (int) hash(songId);
                memonize.put(user.getSongsListened().get(i).getId(), hash);
            }

            if (minHash > hash){
                minHash = hash;
            }
        }
        return minHash;
    }

    @Override
    public String toString() {
        return "MinHashFunction{" +
                "a=" + a +
                ", b=" + b +
                ", prime=" + prime +
                ", size=" + size +
                '}';
    }
}
