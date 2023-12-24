package project.urfu.analytic;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class AnalyticService
{
    private final CityHistogram cityHistogram;
    private final HomeworkHistogram homeworkHistogram;
    private final LinearHomeworkChart linearHomeworkChart;

    public AnalyticService(
            CityHistogram cityHistogram,
            HomeworkHistogram homeworkHistogram,
            LinearHomeworkChart linearHomeworkChart)
    {
        this.cityHistogram = cityHistogram;
        this.homeworkHistogram = homeworkHistogram;
        this.linearHomeworkChart = linearHomeworkChart;
    }

    public void analyticDbData()
    {
        ExecutorService executor = Executors.newFixedThreadPool(3);

        // Выполнение методов параллельно
        executor.submit(cityHistogram::create);
        executor.submit(homeworkHistogram::create);
        executor.submit(linearHomeworkChart::create);

        executor.shutdown();
    }
}
