import java.util.ArrayList;

public class GaussNewton {
    /**
     * performs the Gauss-Newton method on an input vector
     * @param pairs Ordered pairs represented in an ArrayList
     * @param beta0 the initial values of beta represented by a vector of doubles
     * @param N the number of iterations
     * @param funcType the function index (0 = qua, 1 = exp, 2 = log, 3 = rat)
     * @return the calculated beta
     */
    public static Matrix gn(ArrayList<Pair> pairs, double[] beta0, int N, FunctionType funcType) {
        int n = pairs.size();
        Matrix beta = new Matrix(beta0);
        for (int iteration = 0; iteration < N; iteration++) {
            double[] rBacking = new double[n];
            for (int i = 0; i < n; i++) {
                if (funcType == FunctionType.QUADRATIC) {
                    rBacking[i] = pairs.get(i).getSecond() - qua(pairs.get(i).getFirst(),
                            beta.numberAt(0, 0), beta.numberAt(1, 0), beta.numberAt(2, 0));
                } else if (funcType == FunctionType.EXPONENTIAL) {
                    rBacking[i] = pairs.get(i).getSecond() - exp(pairs.get(i).getFirst(),
                            beta.numberAt(0, 0), beta.numberAt(1, 0), beta.numberAt(2, 0));
                } else if (funcType == FunctionType.LOGARITHM) {
                    rBacking[i] = pairs.get(i).getSecond() - log(pairs.get(i).getFirst(),
                            beta.numberAt(0, 0), beta.numberAt(1, 0), beta.numberAt(2, 0));
                } else if (funcType == FunctionType.RATIONAL) {
                    rBacking[i] = pairs.get(i).getSecond() - rat(pairs.get(i).getFirst(),
                        beta.numberAt(0, 0), beta.numberAt(1, 0), beta.numberAt(2, 0));
                }
            }
            Matrix r = new Matrix(rBacking);
            //Jacobian matrix
            Matrix J = new Matrix(n, 3);
            for (int row = 0; row < n; row++) {
                double a = beta.numberAt(0, 0);
                double b = beta.numberAt(1, 0);
                double x = pairs.get(row).getFirst();
                if (funcType == FunctionType.QUADRATIC) {
                    J.setNumber(-x * x, row, 0);
                    J.setNumber(-x, row, 1);
                    J.setNumber(-1, row, 2);
                } else if (funcType == FunctionType.EXPONENTIAL) {
                    J.setNumber(-Math.exp(b * x), row, 0);
                    J.setNumber(-a * x * Math.exp(b * x), row, 1);
                    J.setNumber(-1, row, 2);
                } else if (funcType == FunctionType.LOGARITHM) {
                    J.setNumber(-Math.log(x + b), row, 0);
                    J.setNumber(-a / (x + b), row, 1);
                    J.setNumber(-1, row, 2);
                } else if (funcType == FunctionType.RATIONAL) {
                    J.setNumber(-x / (x + b), row, 0);
                    J.setNumber(a * x / Math.pow(x + b, 2), row, 1);
                    J.setNumber(-1, row, 2);
                }
            }
            //adds conditioning error by using operations on J
//            Matrix JPseudoInverse =
//                    MatrixOperations.multiply(
//                        MatrixOperations.inverse(
//                            MatrixOperations.multiply(
//                                MatrixOperations.transpose(J)
//                                , J))
//                        , MatrixOperations.transpose(J));
            Matrix[] QR = qr_fact_givens.qr(J);
            Matrix Q = QR[0];
            Matrix R = QR[1];
            //better way is to solve Rx = (Q transpose)r = b using back-substitution
            Matrix b = MatrixOperations.multiply(MatrixOperations.transpose(Q), r);
            Matrix x = new Matrix(3, 1);
            //solve for x using back-substitution
            for (int i = x.numRows() - 1; i >= 0; i--) {
                double temp = b.numberAt(i, 0);
                for (int j = x.numRows() - 1; j > i; j--)
                    temp -= R.numberAt(i, j) * x.numberAt(j, 0);
                x.setNumber(temp / R.numberAt(i, i), i, 0);
            }
            beta = MatrixOperations.subtract(beta, x);
        }
        return beta;
    }
    
    //quadratic function
    public static double qua(double x, double a, double b, double c) {
        return (a * x * x) + (b * x) + c;
    }
    
    //exponential function
    public static double exp(double x, double a, double b, double c) {
        return a * Math.exp(b * x) + c;
    }
    
    //logarithmic function
    public static double log(double x, double a, double b, double c) {
        return a * Math.log(x + b) + c;
    }
    
    //rational function
    public static double rat(double x, double a, double b, double c) {
        return (a * x) / (x + b) + c;
    }
}