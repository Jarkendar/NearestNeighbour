import java.util.*;

public class MinHashSimilarity {

    private MinHashFunction[] minHashFunctions;
    private int songsNumber;

    public MinHashSimilarity(int numberOfFunction, int songsNumber) {
        this.minHashFunctions = new MinHashFunction[numberOfFunction];
        this.songsNumber = songsNumber;
        createMinHashFunctions(numberOfFunction, songsNumber);
    }

    private void createMinHashFunctions(int numberHashFunction, int songsNumber) {
        minHashFunctions = new MinHashFunction[numberHashFunction];
        for (int i = 0; i < minHashFunctions.length; i++) {
            minHashFunctions[i] = new MinHashFunction(songsNumber);
        }
    }

    public Result[] countSimilarity(User referenceUser, ArrayList<User> users, int[] indexes) {
        LinkedList<Result> results = new LinkedList<>();
        long start = System.currentTimeMillis();
        MinHashSignature referenceSignature = countSignature(referenceUser);
        for (int i = 0; i<indexes.length;  i++) {
            User user = getUserWithId(users, indexes[i]);
            if (user == null){
                System.out.println("null");
            }
            MinHashSignature minHashSignature = countSignature(user);
            int identical = countIdenticalHash(minHashSignature, referenceSignature);
            if (identical >= 0){
                results.addFirst(new Result(user.getId(), ((double)identical)/((double) minHashFunctions.length)));
            }
        }

        System.out.println((System.currentTimeMillis() - start) + "ms");
        Collections.reverse(results);
        return results.toArray(new Result[0]);
    }

    private User getUserWithId(ArrayList<User> users, int id){
        int index = Collections.binarySearch(users, new User(id), new Comparator<User>() {
            @Override
            public int compare(User user1, User user2) {
                return Integer.compare(user1.getId(), user2.getId());
            }
        });
        if (index >= 0){
            return users.get(index);
        }else {
            return null;
        }
    }

    private MinHashSignature countSignature(User user) {
        MinHashSignature minHashSignature = new MinHashSignature(minHashFunctions.length);
        for (int i = 0; i < minHashFunctions.length; i++) {
            minHashSignature.setSignatureOnPosition(i, minHashFunctions[i].getMinHash(user));
        }
        return minHashSignature;
    }

    private int countIdenticalHash(MinHashSignature minHashSignature, MinHashSignature referenceSignature){
        int identical = 0;
        for (int i = 0; i <referenceSignature.getSignature().length; i++){
            if (minHashSignature.getSignatureValueOnPosition(i) == referenceSignature.getSignatureValueOnPosition(i) ){
                identical++;
            }
        }
        return identical;
    }

    @Override
    public String toString() {
        return "MinHashSimilarity{" +
                "minHashFunctions=" + Arrays.toString(minHashFunctions) +
                ", songsNumber=" + songsNumber +
                '}';
    }

    //todo 1. macierz charakterystyczna (wiersz = 1 unikalny elemnen, kolumna konkretny użytkownik)
    //todo pattern x/(x+y)
    //todo 1.a 10 -> y, 01 -> y, 11 -> x
    //todo 2. wylosować permutacje liczb
    //todo 3. pierwsza jedynka która wystąpi w permutacji jest naszym MINHASHem yeah
    //todo 4. sygnatura MINHASH złożona jest z k takich MINHASHy
    //todo 5. similarity = sum(mhA == mhB)/ countOfMinHashFUnction


}
