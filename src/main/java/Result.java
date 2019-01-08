public class Result {
    private int userId;
    private double result;

    public Result(int userId, double result) {
        this.userId = userId;
        this.result = result;
    }

    public int getUserId() {
        return userId;
    }

    public double getResult() {
        return result;
    }

    @Override
    public String toString() {
        return String.format("%7d %.5f", userId, result);
    }
}
