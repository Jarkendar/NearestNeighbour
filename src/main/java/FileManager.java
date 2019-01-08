import javafx.util.Pair;

import java.io.*;
import java.util.ArrayList;
import java.util.LinkedList;

public class FileManager {

    public Pair<Integer, ArrayList<User>> readDataFromFile(String filename) {
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(new File(filename)))) {
            ArrayList<User> users = new ArrayList<>(1_500_000);
            users.add(new User(0));
            String line;
            int i = 0;
            int maxSongId = 0;
            while ((line = bufferedReader.readLine()) != null) {
                if (i%1000000==0){
                    System.out.println(i);
                }
                String[] cells = line.split(",");
                if (cells.length != 2 || !cells[0].matches("[0-9]+")) {
                    continue;
                }
                int userId = Integer.parseInt(cells[0]);
                int songId = Integer.parseInt(cells[1]);
                if (maxSongId < songId){
                    maxSongId = songId;
                }
                if (users.size()-1 == userId) {
                    users.get(userId).addSong(songId);
                } else {
                    users.add(userId, new User(userId));
                    users.get(userId).addSong(songId);
                }
                i++;
            }
            System.out.println(i);
            users.trimToSize();
            return new Pair(maxSongId, users);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void saveResultToFile(String filename, LinkedList<Pair<Integer, Result[]>> collect){
        try(BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(new File(filename)))){
            for (Pair<Integer, Result[]> pair : collect){
                bufferedWriter.write("User = "+pair.getKey()+"\n");
                for (int i = 0; i<pair.getValue().length; i++){
                    bufferedWriter.write(pair.getValue()[i].toString()+"\n");
                }
                bufferedWriter.write("\n");
            }
            bufferedWriter.flush();
        }catch (IOException e){
            e.printStackTrace();
        }
    }
}
