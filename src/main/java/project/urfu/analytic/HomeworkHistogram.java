package project.urfu.analytic;

import java.awt.*;
import java.util.Map;

import javax.swing.*;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;

import project.urfu.academicPerformance.service.AcademicPerformanceService;

public class HomeworkHistogram
{
    private final AcademicPerformanceService academicPerformanceService;

    public HomeworkHistogram(AcademicPerformanceService academicPerformanceService)
    {
        this.academicPerformanceService = academicPerformanceService;
    }

    public void create()
    {
        // Подготовка данных
        Map<String, Map<String, Integer>> groupTaskCounts = academicPerformanceService.getGroupCategoryHomeworkCounts();

        // Создание набора данных для гистограммы
        DefaultCategoryDataset dataset = createDataset(groupTaskCounts);

        // Создание гистограммы
        JFreeChart chart = ChartFactory.createBarChart(
                "Количество студентов по уровням выполнения заданий",
                "Группа",
                "Количество студентов",
                dataset,
                PlotOrientation.VERTICAL,
                true,
                true,
                false
        );

        // Настройка внешнего вида графика
        chart.setBackgroundPaint(Color.white);

        // Создание панели для графика
        ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setPreferredSize(new Dimension(800, 600));

        JFrame jFrame = new JFrame();
        jFrame.setContentPane(chartPanel);
        jFrame.setSize(800, 600);
        jFrame.setLocationRelativeTo(null);
        jFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        jFrame.setVisible(true);
    }

    private DefaultCategoryDataset createDataset(Map<String, Map<String, Integer>> groupCategoryHomeworkCounts) {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();

        for (Map.Entry<String, Map<String, Integer>> entry : groupCategoryHomeworkCounts.entrySet()) {
            String groupName = entry.getKey();
            Map<String, Integer> taskCounts = entry.getValue();

            for (Map.Entry<String, Integer> taskEntry : taskCounts.entrySet()) {
                String taskLevel = taskEntry.getKey();
                int count = taskEntry.getValue();

                dataset.addValue(count, taskLevel, groupName);
            }
        }

        return dataset;
    }
}
