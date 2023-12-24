package project.urfu.student.service;

import java.util.List;

import project.urfu.student.mapper.StudentMapper;
import project.urfu.student.model.dto.StudentCityInfoDto;
import project.urfu.student.model.entity.StudentEntity;

public class StudentService
{
    private final CrudStudentEntityService crudStudentEntityService;

    public StudentService(CrudStudentEntityService crudStudentEntityService)
    {
        this.crudStudentEntityService = crudStudentEntityService;
    }

    public List<StudentCityInfoDto> getStudentCitiesDto()
    {
        return crudStudentEntityService.findAllStudents().stream()
                .map(StudentMapper::toStudentCityInfoDto)
                .toList();
    }

    public List<StudentEntity> findAllStudents()
    {
        return crudStudentEntityService.findAllStudents();
    }
}
