import java.util.*;

public class JaccardCounter {

    public Result[] countForUser(User referenceUser, ArrayList<User> allUsers) {
        LinkedList<Result> results = new LinkedList<>();
        User smaller, higher;
        long start = System.currentTimeMillis();

        for (int i = 1; i <= allUsers.size() - 1; i++) {
            if (referenceUser.getSongsCollectionSize() == 0 || allUsers.get(i).getSongsCollectionSize() == 0) {
                continue;
            }
            if (referenceUser.getSongsCollectionSize() < allUsers.get(i).getSongsCollectionSize()) {
                smaller = referenceUser;
                higher = allUsers.get(i);
            } else {
                smaller = allUsers.get(i);
                higher = referenceUser;
            }

            int intersect = 0;
            for (int j = 0; j < smaller.getSongsListened().size(); j++) {
                if (higher.isListened(smaller.getSongsListened().get(j).getId())) {
                    intersect++;
                }
            }

            int union = smaller.getSongsCollectionSize() + higher.getSongsCollectionSize() - intersect;
            if (intersect != 0 && union != 0) {
                results.addFirst(new Result(allUsers.get(i).getId(), ((double) intersect) / ((double) union)));
            }
        }

        results.sort((o1, o2) -> {
            int jaccardCompare = Double.compare(o1.getResult(), o2.getResult());
            if (jaccardCompare == 0) {
                return -Integer.compare(o1.getUserId(), o2.getUserId());
            } else {
                return -jaccardCompare;
            }
        });
        Result[] topResults = new Result[results.size() < 100 ? results.size() : 100];
        for (int i = 0; i < topResults.length; i++) {
            topResults[i] = results.pop();
        }

        System.out.println((System.currentTimeMillis() - start) + "ms");
        return topResults;
    }
}
