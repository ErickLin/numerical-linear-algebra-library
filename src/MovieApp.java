import java.util.ArrayList;
import java.util.List;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.VPos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.util.Duration;

public class MovieApp extends Application {

    private List<Letter> letters;

    @Override
    public void start(Stage stage) throws Exception {
        final int totalFrames = 121;
        final double framesPerSecond = 24;

        final double spacing = 30.0;

        final GridPane gridPane = new GridPane();
        gridPane.setPadding(new Insets(spacing));
        gridPane.setHgap(spacing);
        gridPane.setVgap(spacing);

        letters = new ArrayList<>();

        LetterType[] letterTypes = {LetterType.A, LetterType.N, LetterType.Z};
        for (int i = 0; i < letterTypes.length; i++) {
            LetterType type = letterTypes[i];

            Letter letter = new Letter(type);
            GridPane.setHalignment(letter, HPos.CENTER);
            GridPane.setValignment(letter, VPos.CENTER);
            gridPane.add(letter, i, 0);

            letters.add(letter);
        }

        double rotateAngle = 3.0 * 2.0 * Math.PI / totalFrames;
        final Matrix rotateZMatrix = new Matrix(3, 3);
        rotateZMatrix.setNumber(Math.cos(rotateAngle), 0, 0);
        rotateZMatrix.setNumber(Math.sin(rotateAngle), 1, 0);
        rotateZMatrix.setNumber(-Math.sin(rotateAngle), 0, 1);
        rotateZMatrix.setNumber(Math.cos(rotateAngle), 1, 1);
        rotateZMatrix.setNumber(1.0, 2, 2);

        rotateAngle = 2.0 * 2.0 * Math.PI / totalFrames;
        final Matrix rotateYMatrix = new Matrix(3, 3);
        rotateYMatrix.setNumber(Math.cos(rotateAngle), 0, 0);
        rotateYMatrix.setNumber(Math.sin(rotateAngle), 2, 0);
        rotateYMatrix.setNumber(-Math.sin(rotateAngle), 0, 2);
        rotateYMatrix.setNumber(Math.cos(rotateAngle), 2, 2);
        rotateYMatrix.setNumber(1.0, 1, 1);

        rotateAngle = 5.0 * 2.0 * Math.PI / totalFrames;
        final Matrix rotateXMatrix = new Matrix(3, 3);
        rotateXMatrix.setNumber(Math.cos(rotateAngle), 1, 1);
        rotateXMatrix.setNumber(Math.sin(rotateAngle), 2, 1);
        rotateXMatrix.setNumber(-Math.sin(rotateAngle), 1, 2);
        rotateXMatrix.setNumber(Math.cos(rotateAngle), 2, 2);
        rotateXMatrix.setNumber(1.0, 0, 0);

        final Timeline timeLine = new Timeline();
        timeLine.setCycleCount(totalFrames);
        KeyFrame frame = new KeyFrame(Duration.seconds(1.0 / framesPerSecond), new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                letters.get(0).transform(rotateZMatrix);
                letters.get(1).transform(rotateYMatrix);
                letters.get(2).transform(rotateXMatrix);
            }
        });
        timeLine.getKeyFrames().add(frame);
        timeLine.play();

        Button button = new Button("Restart");
        button.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                for (Letter letter : letters) {
                    letter.reset();
                }
                timeLine.stop();
                timeLine.play();
            }
        });
        GridPane.setHalignment(button, HPos.CENTER);
        GridPane.setValignment(button, VPos.CENTER);
        gridPane.add(button, 1, 1);

        Scene scene = new Scene(gridPane, Letter.MAX_SIZE * letters.size() + (letters.size() + 1) * spacing,
                spacing * 3 + Letter.MAX_SIZE + button.getHeight());
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
