public class Pair {
    private final double first, second;
    
    public Pair(double n1, double n2) {
        first = n1;
        second = n2;
    }
    
    public double getFirst() {
        return first;
    }
    
    public double getSecond() {
        return second;
    }
    
    @Override
    public String toString() {
        return first + ", " + second;
    }
}