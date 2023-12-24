package project.urfu.student.service;

import java.util.List;

import project.urfu.student.model.entity.StudentEntity;
import project.urfu.student.repository.StudentEntityRepository;

public class CrudStudentEntityService
{
    private final StudentEntityRepository studentEntityRepository;

    public CrudStudentEntityService(StudentEntityRepository studentEntityRepository)
    {
        this.studentEntityRepository = studentEntityRepository;
    }

    public StudentEntity add(StudentEntity entity)
    {
        studentEntityRepository.save(entity);
        return entity;
    }

    public StudentEntity findById(int id)
    {
        return studentEntityRepository.findById(id);
    }

    public List<StudentEntity> findAllStudents()
    {
        return studentEntityRepository.findAllStudents();
    }
}
