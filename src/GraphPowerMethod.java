import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.ScatterChart;
import javafx.scene.chart.XYChart;
import javafx.scene.layout.TilePane;
import javafx.stage.Stage;

public class GraphPowerMethod extends Application {

    private static final String tempDir = "pm_data.txt";
    private static final double chartHeight = 400.0;
    private static final double chartWidth = 600.0;

    public static void plot(GraphObject[] graphObjects) throws FileNotFoundException {
        PrintStream out = new PrintStream(new File(tempDir));
        for (GraphObject go : graphObjects) {
            out.println(go.title);
            out.println(go.xAxisTitle);
            out.println(go.yAxisTitle);
            for (PowerPoint p : go.data) {
                out.println(p);
            }
        }
        out.close();

        launch();
    }

    @Override
    public void start(Stage stage) throws Exception {
        BufferedReader in = new BufferedReader(new InputStreamReader(new FileInputStream(new File(tempDir))));

        TilePane tilePane = new TilePane();
        tilePane.setPrefColumns(2);

        List<PowerPoint> data = new ArrayList<>();
        String title = null;
        String xAxisTitle = null;
        String yAxisTitle = null;
        while (in.ready()) {
            String line = in.readLine();
            String[] split = line.split("\\,");
            if (split.length == 3) {
                double x = Double.parseDouble(split[0]);
                double y = Double.parseDouble(split[1]);
                int z = Integer.parseInt(split[2]);
                data.add(new PowerPoint(x, y, z));
            } else {
                if (title == null) {
                    title = line;
                } else if (xAxisTitle == null) {
                    xAxisTitle = line;
                } else if (yAxisTitle == null) {
                    yAxisTitle = line;
                } else {
                    XYChart<Number, Number> chart = chartFromData(title, xAxisTitle, yAxisTitle, data);
                    XYChart.Series<Number, Number> series = new XYChart.Series<>();
                    series.getData().add(new XYChart.Data<Number, Number>(1, 1));
                    series.getData().add(new XYChart.Data<Number, Number>(2, 2));
                    series.getData().add(new XYChart.Data<Number, Number>(-1, -1));
                    chart.getData().add(series);
                    chart.setLegendVisible(false);
                    tilePane.getChildren().add(chart);
                    title = line;
                    xAxisTitle = null;
                    yAxisTitle = null;
                    data = new ArrayList<>();
                }
            }
        }

        tilePane.getChildren().add(chartFromData(title, xAxisTitle, yAxisTitle, data));

        in.close();


        Scene scene = new Scene(tilePane, chartWidth * tilePane.getChildren().size(), chartHeight);
        stage.setScene(scene);
        stage.show();
    }

    private static XYChart<Number, Number> chartFromData(String title, String xAxisTitle, String yAxisTitle, List<PowerPoint> data) {
        NumberAxis xAxis = new NumberAxis();
        NumberAxis yAxis = new NumberAxis();
        xAxis.setLabel(xAxisTitle);
        yAxis.setLabel(yAxisTitle);

        ScatterChart<Number, Number> chart = new ScatterChart<>(xAxis, yAxis);
        chart.setTitle(title);
        XYChart.Series<Number, Number> series = new XYChart.Series<>();
        chart.getData().add(series);
        for (PowerPoint p : data) {
            Number x = p.getDeterminant();
            Number y = p.getTrace();
            series.getData().add(new XYChart.Data<>(x, y));
        }

        chart.setPrefSize(chartWidth, chartHeight);

        return chart;
    }

}