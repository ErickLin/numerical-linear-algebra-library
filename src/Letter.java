import javafx.scene.layout.Pane;
import javafx.scene.shape.Line;

import java.util.ArrayList;
import java.util.List;

public class Letter extends Pane {

    public static double MAX_SIZE = 150.0;
    private List<Line> lines;
    private Matrix lineMatrices;
    private LetterType letterType;

    public Letter(LetterType type) {
        letterType = type;
        setUp();
        setPrefSize(MAX_SIZE, MAX_SIZE);
    }

    public LetterType getLetterType() {
        return letterType;
    }

    public void reset() {
        getChildren().removeAll(lines);
        setUp();
    }

    private void setUp() {
        lineMatrices = new Matrix(3, 0);
        lines = new ArrayList<>();
        switch (letterType) {
            case A:
                addLine(0.0, -50.0, -50.0, 50.0);
                addLine(0.0, -50.0, 50.0, 50.0);
                addLine(-25.0, 0.0, 25.0, 00.0);
                break;

            case N:
                addLine(-50.0, -50.0, -50.0, 50.0);
                addLine(-50.0, -50.0, 50.0, 50.0);
                addLine(50.0, -50.0, 50.0, 50.0);
                break;

            case Z:
                addLine(-50.0, -50.0, 50.0, -50.0);
                addLine(50.0, -50.0, -50.0, 50.0);
                addLine(-50.0, 50.0, 50.0, 50.0);
                break;
        }
    }

    public void transform(Matrix matrix) {
        lineMatrices = MatrixOperations.multiply(matrix, lineMatrices);
        updateLines();
    }

    private void updateLines() {
        for (int i = 0; i < lineMatrices.numCols(); i += 3) {
            Line line = lines.get(i / 3);
            line.setLayoutX(50.0);
            line.setLayoutY(50.0);
            line.setStartX(lineMatrices.numberAt(0, i));
            line.setStartY(lineMatrices.numberAt(1, i));
            line.setEndX(lineMatrices.numberAt(0, i + 1));
            line.setEndY(lineMatrices.numberAt(1, i + 1));
        }
    }

    private void addLine(double startX, double startY, double endX, double endY) {
        Line line = new Line(startX, startY, endX, endY);
        line.setStrokeWidth(2.0);
        lines.add(line);
        getChildren().add(line);

        Matrix matrix = new Matrix(3, 3);
        matrix.setNumber(startX, 0, 0);
        matrix.setNumber(startY, 1, 0);
        matrix.setNumber(endX, 0, 1);
        matrix.setNumber(endY, 1, 1);
        matrix.setNumber(1.0, 2, 2);
        lineMatrices.concatenate(matrix);
    }
}
