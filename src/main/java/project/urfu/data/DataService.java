package project.urfu.data;

import java.nio.file.Path;
import java.util.List;

import project.urfu.academicPerformance.model.entity.AcademicPerformance;
import project.urfu.csv.CsvFileService;

public class DataService
{
    private static final Path filePathFromResource = Path.of("content/basicprogramming_2_1.csv");

    public static List<AcademicPerformance> getMaxAcademicPerformances()
    {
        return CsvFileService.getMaxAcademicPerformances(filePathFromResource);
    }
}
