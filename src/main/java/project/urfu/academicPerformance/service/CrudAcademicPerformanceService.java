package project.urfu.academicPerformance.service;

import java.util.List;

import project.urfu.academicPerformance.model.entity.AcademicPerformance;
import project.urfu.academicPerformance.repository.AcademicPerformanceRepository;
import project.urfu.student.model.entity.StudentEntity;

public class CrudAcademicPerformanceService
{
    private final AcademicPerformanceRepository academicPerformanceRepository;

    public CrudAcademicPerformanceService(AcademicPerformanceRepository academicPerformanceRepository)
    {
        this.academicPerformanceRepository = academicPerformanceRepository;
    }

    public AcademicPerformance add(AcademicPerformance entity)
    {
        academicPerformanceRepository.save(entity);
        return entity;
    }

    public List<AcademicPerformance> findByStudentEntity(StudentEntity studentEntity)
    {
        return academicPerformanceRepository.findByStudentEntity(studentEntity);
    }

    public List<AcademicPerformance> findAll()
    {
        return academicPerformanceRepository.findAll();
    }
}
