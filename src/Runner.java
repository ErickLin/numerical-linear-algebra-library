public class Runner {
    public static void main(String[] args) {
        double[][] a = {{1.0, 2.0, 2.0},
                       {3.0, 2.0, 1.0},
                       {4.0, 2.0, 2.0},
                       {1.0, 2.0, 3.0}};
        double[][] b = {{1.0, 2.0, 2.0, 4.0},
                       {3.0, 2.0, 1.0, 3.0},
                       {4.0, 2.0, 2.0, 5.0}};
        Matrix A = new Matrix(a);
        Matrix B = new Matrix(b);

        Matrix M = A;

        print("A", M);

        Matrix[] QR = MatrixOperations.householder(M);
        print("Householder Reflection");
        print("Q", QR[0]);
        print("R", QR[1]);
        print("A = QR", MatrixOperations.multiply(QR[0], QR[1]));

        QR = MatrixOperations.givens(M);
        print("Givens Rotation");
        print("Q", QR[0]);
        print("R", QR[1]);
        print("A = QR", MatrixOperations.multiply(QR[0], QR[1]));

        double[][] h = {{4.0, 1.0},
                        {6.0, 3.0}};
        double[][] i = {{1}, {1}};

        Matrix H = new Matrix(h);
        Matrix I = new Matrix(i);

        print("H", H);

        PowerReturn powerResult = MatrixOperations.powerMethod(H, I, .0001, 100);
        if (powerResult != null) {
            print("eigenvector of H", powerResult.getEigenvector());
            print("eigenvalue of H", powerResult.getEigenvalue());
        }
        else {
            print("power method for H failed");
        }
    }

    public static void print(String message) {
        System.out.println(message);
        System.out.println();
    }

    public static void print(String message, Object o) {
        System.out.println(message);
        System.out.println(o);
        System.out.println();
    }
}
