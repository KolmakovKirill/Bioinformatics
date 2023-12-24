package project.urfu.di;

import project.urfu.academicPerformance.repository.AcademicPerformanceRepository;
import project.urfu.academicPerformance.service.AcademicPerformanceService;
import project.urfu.academicPerformance.service.CrudAcademicPerformanceService;
import project.urfu.analytic.AnalyticService;
import project.urfu.analytic.CityHistogram;
import project.urfu.analytic.HomeworkHistogram;
import project.urfu.analytic.LinearHomeworkChart;
import project.urfu.student.repository.StudentEntityRepository;
import project.urfu.student.service.CrudStudentEntityService;
import project.urfu.student.service.StudentService;

public class DI
{
    private final CrudStudentEntityService crudStudentEntityService;
    private final CrudAcademicPerformanceService crudAcademicPerformanceService;
    private final AnalyticService analyticService;

    public DI()
    {
        final StudentEntityRepository studentEntityRepository = new StudentEntityRepository();
        final AcademicPerformanceRepository academicPerformanceRepository = new AcademicPerformanceRepository();

        this.crudStudentEntityService = new CrudStudentEntityService(studentEntityRepository);
        this.crudAcademicPerformanceService = new CrudAcademicPerformanceService(academicPerformanceRepository);

        final StudentService studentService = new StudentService(crudStudentEntityService);
        final AcademicPerformanceService academicPerformanceService = new AcademicPerformanceService(studentService,
                crudAcademicPerformanceService);

        final CityHistogram cityHistogram = new CityHistogram(studentService);
        final HomeworkHistogram homeworkHistogram = new HomeworkHistogram(academicPerformanceService);
        final LinearHomeworkChart linearHomeworkChart = new LinearHomeworkChart(academicPerformanceService);

        this.analyticService = new AnalyticService(cityHistogram, homeworkHistogram, linearHomeworkChart);
    }

    public CrudStudentEntityService getCrudStudentEntityService()
    {
        return this.crudStudentEntityService;
    }

    public CrudAcademicPerformanceService getCrudAcademicPerformanceService()
    {
        return crudAcademicPerformanceService;
    }

    public AnalyticService getAnalyticService()
    {
        return analyticService;
    }
}
