package project.urfu.analytic;

import java.awt.*;
import java.util.List;

import javax.swing.*;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;

import project.urfu.academicPerformance.model.dto.AcademicPerformanceWithAveragePoints;
import project.urfu.academicPerformance.service.AcademicPerformanceService;

public class LinearHomeworkChart
{
    private final AcademicPerformanceService academicPerformanceService;

    public LinearHomeworkChart(
            AcademicPerformanceService academicPerformanceService)
    {
        this.academicPerformanceService = academicPerformanceService;
    }

    public void create()
    {
        // Создание набора данных для линейной диаграммы
        DefaultCategoryDataset dataset = createDataset(academicPerformanceService.getAveragePointsByAcademicPerformances());

        // Создание линейной диаграммы
        JFreeChart chart = ChartFactory.createLineChart(
                "Средний балл по темам",
                "Тема",
                "Средний балл",
                dataset,
                PlotOrientation.VERTICAL,
                true,
                false,
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

    private DefaultCategoryDataset createDataset(
            List<AcademicPerformanceWithAveragePoints> academicPerformanceWithAveragePointsList)
    {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();

        for (AcademicPerformanceWithAveragePoints academicPerformanceWithAveragePoints
                : academicPerformanceWithAveragePointsList) {
            dataset.addValue((Double) academicPerformanceWithAveragePoints.averagePoints(), "Средний балл",
                    academicPerformanceWithAveragePoints.academicPerformanceTitle());
        }

        return dataset;
    }
}
