package project.urfu.academicPerformance.service;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import project.urfu.academicPerformance.AcademicPerformanceMaxStorage;
import project.urfu.academicPerformance.model.dto.AcademicPerformanceWithAveragePoints;
import project.urfu.academicPerformance.model.entity.AcademicPerformance;
import project.urfu.student.model.entity.StudentEntity;
import project.urfu.student.service.StudentService;

public class AcademicPerformanceService
{
    private final StudentService studentService;
    private final CrudAcademicPerformanceService crudAcademicPerformanceService;

    public AcademicPerformanceService(StudentService studentService,
            CrudAcademicPerformanceService crudAcademicPerformanceService)
    {
        this.studentService = studentService;
        this.crudAcademicPerformanceService = crudAcademicPerformanceService;
    }

    public Long getSumMaxHomeworkPoints()
    {
        return AcademicPerformanceMaxStorage.getMaxAcademicPerformances().stream()
                .mapToLong(AcademicPerformance::getHomework)
                .sum();
    }

    public Map<String, Map<String, Integer>> getGroupCategoryHomeworkCounts()
    {
        final Map<String, Map<String, Integer>> groupCategoryHomeworkCounts = new HashMap<>();
        final Long sumMaxHomeworkPoints = getSumMaxHomeworkPoints();
        List<StudentEntity> students = studentService.findAllStudents();

        for (final StudentEntity student : students)
        {
            Long sumStudentHomeworkPoints =
                    crudAcademicPerformanceService.findByStudentEntity(student)
                            .stream()
                            .mapToLong(AcademicPerformance::getHomework)
                            .sum();
            String groupName = student.getGroup();

            Map<String, Integer> homeworkCounts = groupCategoryHomeworkCounts.getOrDefault(groupName, new HashMap<>());

            double percent = (double)sumStudentHomeworkPoints / sumMaxHomeworkPoints * 100;

            if (percent >= 80)
            {
                homeworkCounts.put("80% и больше", homeworkCounts.getOrDefault("80% и больше", 0) + 1);
            }
            else if (percent >= 60)
            {
                homeworkCounts.put("60-79%", homeworkCounts.getOrDefault("60-79%", 0) + 1);
            }
            else if (percent >= 40)
            {
                homeworkCounts.put("40-59%", homeworkCounts.getOrDefault("40-59%", 0) + 1);
            }
            else
            {
                homeworkCounts.put("менее 40%", homeworkCounts.getOrDefault("менее 40%", 0) + 1);
            }

            groupCategoryHomeworkCounts.put(groupName, homeworkCounts);
        }

        return groupCategoryHomeworkCounts;
    }

    public List<AcademicPerformanceWithAveragePoints> getAveragePointsByAcademicPerformances()
    {
        List<StudentEntity> students = studentService.findAllStudents();
        Map<String, Long> sumPointsByAcademicPerformances = new HashMap<>();

        List<AcademicPerformance> academicPerformances = crudAcademicPerformanceService.findAll();

        for (AcademicPerformance academicPerformance : academicPerformances)
        {
            sumPointsByAcademicPerformances.putIfAbsent(academicPerformance.getTitle(), 0L);
            sumPointsByAcademicPerformances.put(academicPerformance.getTitle(),
                    sumPointsByAcademicPerformances.get(academicPerformance.getTitle())
                            + academicPerformance.getHomework());
        }

        return sumPointsByAcademicPerformances.entrySet().stream()
                .map(entry -> new AcademicPerformanceWithAveragePoints(
                        entry.getKey(),
                        (double) entry.getValue() / students.size())
                )
                .toList();
    }
}
