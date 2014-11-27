public class MatrixOperations {
    // returns the identity matrix of size n x n
    public static Matrix identity(int n) {
        Matrix ret = new Matrix(n, n);

        for (int i = 0; i < ret.numCols(); i++) {
            ret.setNumber(1.0, i, i);
        }

        return ret;
    }
    
    // returns the zero matrix of size m x n
    public static Matrix zero(int m, int n) {
        return new Matrix(m, n);
    }
    
    // copy of a 2D double array
    public static double[][] copyArray(double[][] array) {
        int numRows = array.length;
        int numCols = 0;
        if (numRows > 0) {
            numCols = array[0].length;
        }

        double[][] ret = new double[numRows][numCols];

        for (int i = 0; i < numRows; i++) {
            System.arraycopy(array[i], 0, ret[i], 0, numCols);
        }
		
        return ret;
    }
    
    public static double[] concatenate(double[] array1, double[] array2) {
        int length1 = array1.length;
        int length2 = array2.length;
        double[] ret= new double[length1 + length2];
        System.arraycopy(array1, 0, ret, 0, length1);
        System.arraycopy(array2, 0, ret, length1, length2);
        return ret;
    }
    
    public static Matrix negation(Matrix A) {
        Matrix ret = new Matrix(A);
        ret.negate();
        return ret;
    }
    
    public static Matrix add(Matrix A, Matrix B) {
        Matrix ret = new Matrix(A);
        ret.add(B);
        return ret;
    }
    
    public static Matrix subtract(Matrix A, Matrix B) {
        Matrix ret = new Matrix(A);
        ret.subtract(B);
        return ret;
    }
    
    public static Matrix multiply(Matrix A, Matrix B) {
        if (A.numCols() != B.numRows()) {
            throw new MismatchedDimensionException("multiplication dimension mismatch: "
                    + A.dimensionsToString() + " * " + B.dimensionsToString());
        }

        Matrix ret = new Matrix(A.numRows(), B.numCols());

        for (int i = 0; i < ret.numRows(); i++) {
            for (int j = 0; j < ret.numCols(); j++) {
                double number = 0.0;
                for (int k = 0; k < A.numCols(); k++) {
                    number += A.numberAt(i, k) * B.numberAt(k, j);
                }
                ret.setNumber(number, i, j);
            }
        }

        return ret;
    }

    public static Matrix multiply(Matrix A, double d) {
        Matrix ret = new Matrix(A);
        ret.multiply(d);
        return ret;
    }

    public static Matrix multiply(double d, Matrix A) {
        return multiply(A, d);
    }
    
    public static Matrix divide(Matrix A, double d) {
        Matrix ret = new Matrix(A);
        ret.divide(d);
        return ret;
    }

    public static Matrix multiplyRow(Matrix A, int row, double d) {
        Matrix ret = new Matrix(A);
        ret.multiplyRow(row, d);
        return ret;
    }

    public static Matrix addToRowMultipleOfRow(Matrix A, int rowToAddTo, int rowToAddFrom, double d) {
        Matrix ret = new Matrix(A);
        ret.addToRowMultipleOfRow(rowToAddTo, rowToAddFrom, d);
        return ret;
    }

    public static Matrix switchRows(Matrix A, int row1, int row2) {
        Matrix ret = new Matrix(A);
        ret.switchRows(row1, row2);
        return ret;
    }

    public static Matrix transpose(Matrix A) {
        Matrix ret = new Matrix(A.numCols(), A.numRows());

        for (int i = 0; i < A.numRows(); i++) {
            for (int j = 0; j < A.numCols(); j++) {
                ret.setNumber(A.numberAt(i, j), j, i);
            }
        }

        return ret;
    }

    public static Matrix rref(Matrix A) {
        Matrix ret = new Matrix(A);
        ret.toRref();
        return ret;
    }

    public static Matrix concatenate(Matrix A, Matrix B) {
        Matrix ret = new Matrix(A);
        ret.concatenate(B);
        return ret;
    }

    public static Matrix replaceSection(Matrix A, int top, int left, Matrix B) {
        Matrix ret = new Matrix(A);
        ret.replaceSection(top, left, B);
        return ret;
    }
    
    public static Matrix normalize(Matrix v) {
        Matrix ret = new Matrix(v);
        ret.normalize();
        return ret;
    }
    
    public static Matrix inverse(Matrix A) {
        if (A.numCols() != A.numRows()) {
            throw new MismatchedDimensionException("finding inverse of non-square matrix: "
                    + A.dimensionsToString());
        }
        Matrix result = concatenate(A, identity(A.numRows()));
        result.toRref();
        return result.section(0, A.numRows() - 1, A.numCols(), 2 * A.numCols() - 1);
    }

    /**
     * QR factorization using Householder Reflections
     * @param A  the Matrix to be factorized
     * @return  a Matrix array with two elements, Q and R
     */
    public static Matrix[] householder(Matrix A) {
    	int m = A.numRows();
        int n = A.numCols();
        Matrix Q = identity(m);
        Matrix R = new Matrix(A);
        //Matrix[] H = new Matrix[m - 1];
        for (int k = 0; k < Math.min(m - 1, n); k++) {
            Matrix I = identity(m - k);
            Matrix a = R.section(k, m - 1, k, k);
            Matrix v = add(a, multiply(a.magnitude(), I.section(0, I.numRows() - 1, 0, 0)));
            Matrix u = divide(v, v.magnitude());
            Matrix H = identity(m);
            H.replaceSection(k, k, subtract(I, multiply(2, multiply(u, transpose(u)))));
            Q = multiply(Q, H);
            R = multiply(H, R);
        }
        Matrix[] ret = new Matrix[2];
        ret[0] = new Matrix(Q);
        ret[1] = new Matrix(R);
        return ret;
    }

    /**
     * QR factorization using Givens Rotations
     * @param A  the Matrix to be factorized
     * @return  a Matrix array with two elements, Q and R
     */
    public static Matrix[] givens(Matrix A) {
        int m = A.numRows();
        int n = A.numCols();
        Matrix Q = identity(m);
        Matrix R = new Matrix(A);
        int iterations = Math.min(m, n);
        for (int i = 0; i < iterations; i++) {
            for (int j = i + 1; j < m; j++) {
                double x = R.numberAt(i, i);
                double y = R.numberAt(j, i);
                double mag = Math.sqrt(x * x + y * y);
                double cos = x / mag;
                double sin = -y / mag;
                Matrix G = identity(m);
                G.setNumber(cos, i, i);
                G.setNumber(sin, j, i);
                G.setNumber(-sin, i, j);
                G.setNumber(cos, j, j);
                Q = multiply(Q, transpose(G));
                R = multiply(G, R);
            }
        }
        Matrix[] ret = new Matrix[2];
        ret[0] = new Matrix(Q);
        ret[1] = new Matrix(R);
        return ret;
    }

    /**
     * calculates the max eigenvalue and its corresponding eigenvector using the power method
     * @param A  find the eigenvalue/vector for this matrix
     * @param initial  initial guess for power method
     * @param tolerance  tolerance for when to stop
     * @param maxIterations  number of iterations to fail at if not within tolerance
     * @return a PowerReturn class with the results if successful, null if failed
     */
    public static PowerReturn powerMethod(Matrix A, Matrix initial, double tolerance, int maxIterations) {
        if (A.numCols() == 0 || A.numRows() == 0) {
            throw new IllegalArgumentException("powerMethod: invalid empty matrix A " + A.dimensionsToString());
        }
        if (tolerance < 0.0) {
            throw new IllegalArgumentException("powerMethod: invalid tolerance " + tolerance);
        }
        if (maxIterations <= 0) {
            throw new IllegalArgumentException("powerMethod: invalid maxIterations " + maxIterations);
        }
        if (A.numCols() != initial.numRows()) {
            throw new MismatchedDimensionException("powerMethod: A and initial do not match dimensions: "
                    + A.dimensionsToString() + " " + initial.dimensionsToString());
        }
        if (!initial.isVector()) {
            throw new IllegalArgumentException("powerMethod: initial is not a vector " + initial.dimensionsToString());
        }
        Matrix u = initial;
        double a = 0.0;
        int i;
        boolean success = false;

        for (i = 1; i <= maxIterations; i++) {
            double prev = a;
            a = u.numberAt(0, 0);
            u = multiply(multiply(A, u), 1.0 / a);
            if (Math.abs(a - prev) <= tolerance) {
                success = true;
                break;
            }
        }

        if (success) {
            return new PowerReturn(u, a, i);
        } else {
            return null;
        }
    }
}