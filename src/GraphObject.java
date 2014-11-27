
import java.util.List;

public class GraphObject {
    public final String title;
    public final String xAxisTitle;
    public final String yAxisTitle;
    public final List<PowerPoint> data;

    public GraphObject(String title, String xAxisTitle, String yAxisTitle, List<PowerPoint> data) {
        this.title = title;
        this.xAxisTitle = xAxisTitle;
        this.yAxisTitle = yAxisTitle;
        this.data = data;
    }
}