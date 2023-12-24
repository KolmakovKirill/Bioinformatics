package project.urfu.academicPerformance;

import java.util.List;

import project.urfu.academicPerformance.model.entity.AcademicPerformance;
import project.urfu.data.DataService;

public class AcademicPerformanceMaxStorage
{
    private static final List<AcademicPerformance> maxAcademicPerformances = DataService.getMaxAcademicPerformances();

    public static List<AcademicPerformance> getMaxAcademicPerformances()
    {
        return maxAcademicPerformances;
    }
}
