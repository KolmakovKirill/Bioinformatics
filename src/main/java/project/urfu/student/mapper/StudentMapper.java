package project.urfu.student.mapper;

import org.dozer.DozerBeanMapper;
import org.dozer.loader.api.BeanMappingBuilder;

import project.urfu.student.model.dto.StudentCityInfoDto;
import project.urfu.student.model.entity.StudentEntity;

public class StudentMapper
{
    private static final DozerBeanMapper MAPPER = new DozerBeanMapper();

    static
    {
        BeanMappingBuilder studentCityInfoBuilder = new BeanMappingBuilder()
        {
            @Override
            protected void configure()
            {
                mapping(StudentCityInfoDto.class, StudentEntity.class);
            }
        };

        MAPPER.addMapping(studentCityInfoBuilder);
    }

    public static StudentCityInfoDto toStudentCityInfoDto(StudentEntity entity)
    {
        return MAPPER.map(entity, StudentCityInfoDto.class);
    }
}
