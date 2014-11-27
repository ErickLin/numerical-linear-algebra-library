public class PowerReturn {
    private final Matrix eigenvector;
    private final double eigenvalue;
    private final int iterations;
    
    public PowerReturn(Matrix eigenvector, double eigenvalue, int iterations) {
        this.eigenvector = eigenvector;
        this.eigenvalue = eigenvalue;
        this.iterations = iterations;
    }

    public Matrix getEigenvector() {
        return eigenvector;
    }
    
    public double getEigenvalue() {
        return eigenvalue;
    }
    
    public int getIterations() {
        return iterations;
    }
}