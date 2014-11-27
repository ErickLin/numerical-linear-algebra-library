//import java.text.DecimalFormat;

public class Matrix {
    private double[][] matrix;

    // init Matrix with num numRows and numCols
    public Matrix(int numRows, int numCols) {
        matrix = new double[numRows][numCols];
    }
    
    // init Matrix with a copy of another 2D double array
    public Matrix(double[][] array) {
        matrix = MatrixOperations.copyArray(array);
    }

    // init Matrix as vector
    public Matrix(double[] array) {
        matrix = new double[array.length][1];
        for (int i = 0; i < array.length; i++) {
            matrix[i][0] = array[i];
        }
    }

    // init Matrix by copying another
    public Matrix(Matrix other) {
        this(other.matrix);
    }

    // number of numRows in matrix
    public int numRows() {
        return matrix.length;
    }

    // number of numCols in matrix
    public int numCols() {
        return matrix.length == 0 ? 0 : matrix[0].length;
    }

    // sets the number at matrix[row][column]
    public void setNumber(double number, int row, int column) {
        if (!isValidRowAndColumn(row, column)) {
            throw new IndexOutOfBoundsException("setting number out of bounds, row: " + row + ", column: " + column);
        }

        matrix[row][column] = number;
    }

    // returns the number at matrix[row][column]
    public double numberAt(int row, int column) {
        if (!isValidRowAndColumn(row, column)) {
            throw new IndexOutOfBoundsException("getting number out of bounds, row: " + row + ", column: " + column);
        }

        return matrix[row][column];
    }

    // returns true if row and column are within matrix dimensions
    private boolean isValidRowAndColumn(int row, int column) {
        return row >= 0 && row < numRows() && column >= 0 && column < numCols();
    }

    public double trace() {
        if (numRows() != numCols()) {
            throw new MismatchedDimensionException("not square: "
                    + dimensionsToString());
        }
        double trace = 0;
        for (int i = 0; i < numRows(); i++) {
            trace += numberAt(i, i);
        }
        return trace;
    }

    // returns a matrix that has all its elements multiplied by -1.0, same as Matrix.multiply(-1.0)
    public void negate() {
        for (int i = 0; i < numRows(); i++) {
            for (int j = 0; j < numCols(); j++) {
                setNumber(-numberAt(i, j), i, j);
            }
        }
    }
    
    // normalizes to unit vector
    public void normalize() {
        if (isVector()) {
            double mag = magnitude();
            for (int i = 0; i < numRows(); i++) {
                setNumber(numberAt(i, 0) / mag, i, 0);
            }
        }
    }

    // adds other matrix to this matrix
    public void add(Matrix other) {
        if (numRows() != other.numRows() || numCols() != other.numCols()) {
            throw new MismatchedDimensionException("addition dimension mismatch: "
                    + dimensionsToString() + " + " + other.dimensionsToString());
        }

        for (int i = 0; i < numRows(); i++) {
            for (int j = 0; j < numCols(); j++) {
                setNumber(numberAt(i, j) + other.numberAt(i, j), i, j);
            }
        }
    }


    // subtracts other matrix from this matrix
    public void subtract(Matrix other) {
        if (numRows() != other.numRows() || numCols() != other.numCols()) {
            throw new MismatchedDimensionException("subtraction dimension mismatch: "
                    + dimensionsToString() + " - " + other.dimensionsToString());
        }

        for (int i = 0; i < numRows(); i++) {
            for (int j = 0; j < numCols(); j++) {
                setNumber(numberAt(i, j) - other.numberAt(i, j), i, j);
            }
        }
    }

    // multiplies all numbers in this matrix by d
    public void multiply(double d) {
        for (int i = 0; i < numRows(); i++) {
            for (int j = 0; j < numCols(); j++) {
                setNumber(numberAt(i, j) * d, i, j);
            }
        }
    }

    // divides all numbers in this matrix by d
    public void divide(double d) {
        for (int i = 0; i < numRows(); i++) {
            for (int j = 0; j < numCols(); j++) {
                setNumber(numberAt(i, j) / d, i, j);
            }
        }
    }

    // multiplies all numbers in a row in this matrix by d
    public void multiplyRow(int row, double d) {
        for (int i = 0; i < numCols(); i++) {
            setNumber(numberAt(row, i) * d, row, i);
        }
    }

    // adds a multiple of one row to another
    public void addToRowMultipleOfRow(int rowToAddTo, int rowToAddFrom, double d) {
        for (int i = 0; i < numCols(); i++) {
            setNumber(numberAt(rowToAddTo, i) + d * numberAt(rowToAddFrom, i), rowToAddTo, i);
        }
    }

    // switches two rows
    public void switchRows(int row1, int row2) {
        if (row1 != row2) {
            double[] temp = matrix[row1];
            matrix[row1] = matrix[row2];
            matrix[row2] = temp;
        }
    }

    // reduces current matrix to reduced row echelon form
    public void toRref() {
        int iterations = Math.min(numRows(), numCols());
        for (int i = 0; i < iterations; i++) {
            int maxIndex = i;
            for (int j = i + 1; j < numRows(); j++) {
                if (Math.abs(numberAt(j, i)) > Math.abs(numberAt(maxIndex, i))) {
                    maxIndex = j;
                }
            }
            switchRows(i, maxIndex);
            if (numberAt(i, i) == 0.0) {
                continue;
            }
            multiplyRow(i, 1.0 / numberAt(i, i));
//            setNumber(1.0, i, i);
            for (int j = i + 1; j < numRows(); j++) {
                addToRowMultipleOfRow(j, i, -numberAt(j, i));
//                setNumber(0.0, j, i);
            }
        }
        for (int i = iterations - 1; i >= 0; i--) {
            for (int j = 0; j < i; j++) {
                addToRowMultipleOfRow(j, i, -numberAt(j, i));
//                setNumber(0.0, j, i);
            }
        }
    }

    // appends other matrix to the end of this matrix
    public void concatenate(Matrix m) {
        if (numRows() != m.numRows())  {
            throw new MismatchedDimensionException("append dimension mismatch: "
                    + dimensionsToString() + " <- " + m.dimensionsToString());
        }
        for (int i = 0; i < numRows(); i++) {
            matrix[i] = MatrixOperations.concatenate(matrix[i], m.matrix[i]);
        }
    }

    /**
     * returns a new Matrix that is part of the original Matrix
     * @param top  the topmost row number (inclusive)
     * @param bottom  the bottommost row number (inclusive)
     * @param left  the leftmost column number (inclusive)
     * @param right  the rightmost column number (inclusive)
     * @return  a new Matrix which is a section of A
     */
    public Matrix section(int top, int bottom, int left, int right) {
        if (left > right || top > bottom) {
            throw new IndexOutOfBoundsException("section: left must be <= right, and"
                    + "top must be <= bottom");
        }
        if (top < 0 || left < 0 || bottom >= numRows() || right >= numCols()) {
            throw new IndexOutOfBoundsException("section: invalid top/left: " + top + ", " + left);
        }

        int newNumRows = bottom - top + 1;
        int newNumCols = right - left + 1;
        Matrix ret = new Matrix(newNumRows, newNumCols);

//        double[][] newMatrix = new double[newNumRows][newNumCols];
        for (int row = 0; row < newNumRows; row++) {
            System.arraycopy(matrix[top + row], left, ret.matrix[row], 0, newNumCols);
//            for (int col = 0; col < ret.matrix[0].length; col++) {
//                ret.matrix[row][col] = numberAt(top + row, left + col);
//            }
        }

        return ret;
    }

    /**
     * replaces this Matrix with matrix B at top, left
     * @param top  the topmost column number of the section to be replaced
     * @param left  the leftmost column number of the section to be replaced
     * @param B  the smaller Matrix
     */
    public void replaceSection(int top, int left, Matrix B) {
        if (top < 0 || left < 0 || top > numRows() - B.numRows() || left > numCols() - B.numCols()) {
            throw new IndexOutOfBoundsException("invalid replaceSection: this " + dimensionsToString()
                    + ", that " + B.dimensionsToString() + ", top " + top + ", left " + left);
        }
        for (int row = 0; row < B.numRows(); row++) {
            System.arraycopy(B.matrix[row], 0, matrix[top + row], top, B.numCols());
//            for (int col = 0; col < B.numCols(); col++) {
//                matrix[top + row][left + col] = B.numberAt(row, col);
//            }
        }
    }

    // returns the dot product of this and that matrix (vector)
    public double dotProduct(Matrix other) {
        if (!isVector() || !other.isVector()) {
            throw new MismatchedDimensionException("dot product: not vectors: "
                    + dimensionsToString() + " . " + other.dimensionsToString());
        }

        double ret = 0.0;

        for (int i = 0; i < numRows(); i++) {
            ret += numberAt(i, 0) * other.numberAt(i, 0);
        }

        return ret;
    }

    // returns the magnitude of a vector
    public double magnitude() {
        if (!isVector()) {
            throw new MismatchedDimensionException("magnitude: not a vector: " + dimensionsToString());
        }

        double ret = 0.0;

        for (int i = 0; i < numRows(); i++) {
            double d = numberAt(i, 0);
            ret += d * d;
        }

        return Math.sqrt(ret);
    }

    public boolean isVector() {
        return numCols() == 1;
    }

    public String dimensionsToString() {
        return "(" + numRows() + "," + numCols() + ")";
    }

    // string representation
    @Override
    public String toString() {
        StringBuilder ret = new StringBuilder();
        //DecimalFormat df = new DecimalFormat("0.00");

        ret.append("[");
        for (int i = 0; i < numRows(); i++) {
            if (i != 0) {
                ret.append(" ");
            }
            for (int j = 0; j < numCols(); j++) {
                ret.append(numberAt(i, j));
                if (j != numCols() - 1) {
                    ret.append(", ");
                }
            }
            if (i != numRows() - 1) {
                ret.append(";\n");
            }
        }

        ret.append("]");

        return ret.toString();
    }
}