package project.urfu.csv;

import java.util.List;

import project.urfu.academicPerformance.model.entity.AcademicPerformance;
import project.urfu.student.model.entity.StudentEntity;

public record CsvFileContentInfo(StudentEntity studentEntity, List<AcademicPerformance> academicPerformances)
{
}
