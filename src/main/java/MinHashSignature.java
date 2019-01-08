import java.util.Arrays;

public class MinHashSignature {

    private int[] signature;

    public MinHashSignature(int size) {
        signature = new int[size];
    }

    public void setSignatureOnPosition(int position, int value){
        signature[position] = value;
    }

    public int[] getSignature() {
        return signature;
    }

    public int getSignatureValueOnPosition(int position){
        return signature[position];
    }

    @Override
    public String toString() {
        return "MinHashSignature{" +
                "signature=" + Arrays.toString(signature) +
                '}';
    }
}
