import javafx.util.Pair;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

public class Main {

    private static final int NUMBER_OF_FIRST_USERS = 100;
    private static final int NUMBER_OF_HASH_FUNCTIONS = 100;

    public static void main(String[] args) {
        long startTime = System.currentTimeMillis();
        Pair<Integer, ArrayList<User>> pairUser = new FileManager().readDataFromFile("facts-nns.csv");
        System.out.println("Create "+(System.currentTimeMillis()-startTime) + "ms");


        long startJaccard = System.currentTimeMillis();
        LinkedList<Pair<Integer, Result[]>> jaccardResults = getNNFirstUsers(NUMBER_OF_FIRST_USERS, pairUser.getValue());
        System.out.println("Jaccard = " + (System.currentTimeMillis() - startJaccard) + "ms");

        HashMap<Integer, Result[]> interestHashMap = new HashMap<>();
        for (Pair<Integer, Result[]> pair : jaccardResults){
            interestHashMap.put(pair.getKey(), pair.getValue());
        }

        long startMinHash = System.currentTimeMillis();
        LinkedList<Pair<Integer, Result[]>> minHashResults = getMinHashNN(NUMBER_OF_FIRST_USERS, pairUser.getValue(), NUMBER_OF_HASH_FUNCTIONS, pairUser.getKey(), interestHashMap);
        System.out.println("MinHash = " + (System.currentTimeMillis() - startMinHash) + "ms");

        countRMSE(jaccardResults, minHashResults);

        System.out.println("Time = " + (System.currentTimeMillis() - startTime) + "ms");
    }

    private static LinkedList<Pair<Integer, Result[]>> getNNFirstUsers(int numberOfFirstUsers, ArrayList<User> users) {
        JaccardCounter jaccardCounter = new JaccardCounter();
        LinkedList<Pair<Integer, Result[]>> collectResults = new LinkedList<>();
        for (int i = 1; i <= numberOfFirstUsers; i++) {
            collectResults.addLast(new Pair<>(i, jaccardCounter.countForUser(users.get(i), users)));
        }
        new FileManager().saveResultToFile("result_first_"+numberOfFirstUsers+"_users_jaccard.txt", collectResults);

        return collectResults;
    }

    private static LinkedList<Pair<Integer, Result[]>> getMinHashNN(int numberOfFirstUsers, ArrayList<User> users, int numberOfHashFunction, int songsNumber, HashMap<Integer, Result[]> results){
        LinkedList<Pair<Integer, Result[]>> collectResults = new LinkedList<>();
        MinHashSimilarity minHashSimilarity = new MinHashSimilarity(numberOfHashFunction, songsNumber);
        for (int i = 1; i<=numberOfFirstUsers; i++){
            collectResults.addLast(new Pair<>(i, minHashSimilarity.countSimilarity(users.get(i), users, getIndexes(results.get(i)))));
        }
        new FileManager().saveResultToFile("result_first_"+numberOfFirstUsers+"_users_minhash.txt", collectResults);

        return collectResults;
    }

    private static int[] getIndexes(Result[] results){
        int[] indexes = new int[results.length];
        for (int i = 0; i<results.length; i++){
            indexes[i] = results[i].getUserId();
        }
        return indexes;
    }

    private static void countRMSE(LinkedList<Pair<Integer, Result[]>> jaccard, LinkedList<Pair<Integer, Result[]>> minHash){
        double sumMSE = 0.0;
        double sumRMSE = 0.0;
        for (int i = 0; i<jaccard.size(); i++){
            double MSE = 0;
            for (int j = 0; j<jaccard.get(i).getValue().length; j++){
                double error = Math.pow(jaccard.get(i).getValue()[j].getResult() - minHash.get(i).getValue()[j].getResult(), 2)/jaccard.get(i).getValue().length;
                sumMSE += error;
                MSE += error;
            }
            System.out.println((i+1) + ". MSE = " + MSE + ", RMSE = " + (Math.sqrt(MSE)));
            sumRMSE += Math.sqrt(MSE);
        }
        double RMSE = Math.sqrt(sumMSE/jaccard.size());
        System.out.println("AVG_MSE = " + (sumMSE/jaccard.size()) + ", AVG_RMSE = " + RMSE);
        System.out.println("Sum_AVGs_RMSE = " + sumRMSE + ", AVG_RMSEv2 = " + (sumRMSE/jaccard.size()));
    }
}

//AVG 1 per 338.94ms
//Users = 1014071
//Result for all = 338.94 * 1014071 [ms] = 343,70922474×10⁶ ms = 95,47478465 h



