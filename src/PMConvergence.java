import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PMConvergence {
    private static ArrayList<Matrix> allA = new ArrayList<>();
    private static ArrayList<Matrix> allAInverse = new ArrayList<>();

    public static void main(String[] args) throws Exception {
        final int total = 1000;
        PowerPoint[] points = new PowerPoint[total];
        PowerPoint[] pointsInverse = new PowerPoint[total];
        for (int count = 0; count < total; count++) {
            Matrix A;
            double a, b, c, d, det;
            //keep regenerating A if its determinant equals 0
            do {
                A = new Matrix(2, 2);
                for (int row = 0; row < 2; row++) {
                    for (int col = 0; col < 2; col++) {
                        A.setNumber(-2.0 + 4.0 * Math.random(), row, col);
                    }
                }
                a = A.numberAt(0, 0);
                b = A.numberAt(0, 1);
                c = A.numberAt(1, 0);
                d = A.numberAt(1, 1);
                det = a * d - b * c;
            } while (det == 0);
            
            PowerReturn data1 = power_method.power_method(A,
                    new Matrix(new double[][] {{1}, {1}}), 0.00005, 100);
            //quit in failure
            if (data1 == null) {
                count--;
                continue;
            }
            Matrix AInverse = new Matrix(
                    new double[][] {{d / det, -b / det}, {-c / det, a / det}});
            PowerReturn data2 = power_method.power_method(AInverse,
                    new Matrix(new double[][] {{1}, {1}}), 0.00005, 100);
            //quit in failure
            if (data2 == null) {
                count--;
                continue;
            }
            
            double eigenvalue = data1.getEigenvalue();
            int iterations = data1.getIterations();
            double eigenvalueInverse = data2.getEigenvalue();
            int iterationsInverse = data2.getIterations();
            //det = eigenvalue * (1 / eigenvalueInverse);
            double detInverse = eigenvalueInverse * (1 / eigenvalue);
            double trace = eigenvalue + (1 / eigenvalueInverse);
            double traceInverse = eigenvalueInverse + (1 / eigenvalue);
            points[count] = new PowerPoint(det, trace, iterations);
            pointsInverse[count] = new PowerPoint(detInverse, traceInverse, iterationsInverse);
            allA.add(A);
            allAInverse.add(AInverse);
        }
        List<PowerPoint> data = new ArrayList<>(total);
        data.addAll(Arrays.asList(points));
        List<PowerPoint> dataInverse = new ArrayList<>(total);
        dataInverse.addAll(Arrays.asList(pointsInverse));

        GraphObject a = new GraphObject("A", "determinant", "trace", data);
        GraphObject aInverse = new GraphObject("A inverse", "determinant", "trace", dataInverse);

        GraphPowerMethod.plot(new GraphObject[] {a, aInverse});
    }
}