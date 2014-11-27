import java.util.Scanner;
import java.util.StringTokenizer;

public class qr_fact_givens {
    public static Matrix[] qr(Matrix A) {
        return MatrixOperations.givens(A);
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int m = 0, n = 0;
        boolean valid = false;
        do {
            System.out.print("Enter the dimension m of the matrix A: ");
            try {
                m = Integer.parseInt(sc.nextLine());
                if (m > 0) {
                    valid = true;
                } else {
                    System.out.println("Please enter a positive integer for m.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Please enter a positive integer for m.");
            }
        } while (!valid);
        valid = false;
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
        
        double[][] a = new double[m][n];
        for (int i = 1; i <= m; i++) {
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
        
        Matrix A = new Matrix(a);
        
        Matrix[] QR = MatrixOperations.givens(A);
        print("Q:", QR[0]);
        print("R:", QR[1]);
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