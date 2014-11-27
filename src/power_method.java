import java.util.Scanner;
import java.util.StringTokenizer;

// Estimates the dominant eigenvalue and eigenvector of a Matrix
// Returns null if failed
public class power_method {
    public static PowerReturn power_method(Matrix A, Matrix initial, double tolerance, int maxIterations) {
        return MatrixOperations.powerMethod(A, initial, tolerance, maxIterations);
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int n = 0;
        boolean valid = false;
        do {
            System.out.print("Enter the dimension n of the matrix A: ");
            try {
                n = Integer.parseInt(sc.nextLine());
                if (n > 0) {
                    valid = true;
                } else {
                    System.out.println("Please enter a positive integer for n.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Please enter a positive integer for n.");
            }
        } while (!valid);
        
        double[][] a = new double[n][n];
        for (int i = 1; i <= n; i++) {
            valid = false;
            do {
                System.out.print("Enter the elements of row " + i + " of A, separated by spaces: ");
                StringTokenizer row = new StringTokenizer(sc.nextLine());
                try {
                    if (row.countTokens() == n) {
                        for (int j = 1; j <= n; j++) {
                            a[i - 1][j - 1] = Double.parseDouble(row.nextToken());
                        }
                        valid = true;
                    } else {
                        System.out.println("Please enter an n number of elements.");
                    }
                } catch (NumberFormatException e) {
                    System.out.println("Please enter real numbers only.");
                }
            } while (!valid);
        }
        
        double[][] v = new double[n][1];
        valid = false;
        do {
            System.out.print("Enter the elements of v, separated by spaces: ");
            StringTokenizer row = new StringTokenizer(sc.nextLine());
            try {
                if (row.countTokens() == n) {
                    for (int i = 1; i <= n; i++) {
                        v[i - 1][0] = Double.parseDouble(row.nextToken());
                    }
                    valid = true;
                } else {
                    System.out.println("Please enter an n number of elements.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Please enter real numbers only.");
            }
        } while (!valid);

        Matrix A = new Matrix(a);
        Matrix I = new Matrix(v);

        PowerReturn powerResult = MatrixOperations.powerMethod(A, I, .0001, 100);
        if (powerResult != null) {
            print("Dominant eigenvalue of A:", powerResult.getEigenvalue());
            print("Associated eigenvector:", powerResult.getEigenvector());
        }
        else {
            print("Power method for A failed");
        }
    }
    
    public static void print(String message) {
        System.out.println();
        System.out.println(message);
    }

    public static void print(String message, Object o) {
        System.out.println();
        System.out.println(message);
        System.out.println(o);
    }
}