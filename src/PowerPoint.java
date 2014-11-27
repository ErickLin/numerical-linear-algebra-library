public class PowerPoint {

    final double determinant;
    final double trace;
    final int iterations;

    public PowerPoint(double determinant, double trace, int iterations) {
        this.determinant = determinant;
        this.trace = trace;
        this.iterations = iterations;
    }
    
    public double getDeterminant() {
        return determinant;
    }
    
    public double getTrace() {
        return trace;
    }
    
    public int getIterations() {
        return iterations;
    }
    
    @Override
    public String toString() {
        return determinant + "," + trace + "," + iterations;
    }
}