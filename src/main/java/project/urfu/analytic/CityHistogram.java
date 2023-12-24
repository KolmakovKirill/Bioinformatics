package project.urfu.analytic;

import java.awt.*;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.swing.*;

import org.apache.commons.lang3.StringUtils;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.labels.StandardPieSectionLabelGenerator;
import org.jfree.chart.plot.PiePlot;
import org.jfree.data.general.DefaultPieDataset;

import project.urfu.student.model.dto.StudentCityInfoDto;
import project.urfu.student.service.StudentService;

public class CityHistogram
{
    private final StudentService studentService;

    public CityHistogram(StudentService studentService)
    {
        this.studentService = studentService;
    }

    public void create()
    {
        final List<StudentCityInfoDto> studentCityInfoDtoList = studentService.getStudentCitiesDto();

        final Map<String, Integer> cityCounts = countStudentCities(studentCityInfoDtoList);

        // Создание набора данных для гистограммы
        DefaultPieDataset dataset = createDataset(cityCounts);

        // Создание круговой диаграммы
        JFreeChart chart = ChartFactory.createPieChart(
                "Количество студентов из разных городов",
                dataset,
                true,
                true,
                false
        );

        // Настройка внешнего вида графика
        PiePlot plot = (PiePlot) chart.getPlot();
        plot.setLabelFont(new Font("SansSerif", Font.PLAIN, 12));
        plot.setNoDataMessage("Нет данных для отображения");
        plot.setCircular(true);

        // Отображение процентов на сегментах
        NumberFormat nf = NumberFormat.getPercentInstance(Locale.getDefault());
        nf.setMaximumFractionDigits(1);
        plot.setLabelGenerator(new StandardPieSectionLabelGenerator("{0} ({2})", new DecimalFormat("0"), nf));

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

    private Map<String, Integer> countStudentCities(List<StudentCityInfoDto> studentCities)
    {
        Map<String, Integer> cityCounts = new HashMap<>();

        for (StudentCityInfoDto studentCity : studentCities)
        {
            String city = studentCity.getCity();
            if (StringUtils.isNotBlank(city))
            {
                cityCounts.put(city, cityCounts.getOrDefault(city, 0) + 1);
            }
        }

        return cityCounts;
    }

    private DefaultPieDataset createDataset(Map<String, Integer> cityCounts)
    {
        DefaultPieDataset dataset = new DefaultPieDataset();

        for (Map.Entry<String, Integer> entry : cityCounts.entrySet()) {
            dataset.setValue(entry.getKey(), entry.getValue());
        }

        return dataset;
    }
}
