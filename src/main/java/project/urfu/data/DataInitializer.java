package project.urfu.data;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import project.urfu.academicPerformance.model.entity.AcademicPerformance;
import project.urfu.academicPerformance.service.CrudAcademicPerformanceService;
import project.urfu.csv.CsvFileContentInfo;
import project.urfu.csv.CsvFileService;
import project.urfu.di.DI;
import project.urfu.student.model.entity.StudentEntity;
import project.urfu.student.service.CrudStudentEntityService;
import project.urfu.vk.service.VkMemberResponse;
import project.urfu.vk.service.VkService;

public class DataInitializer
{
    private final CrudStudentEntityService crudStudentEntityService;
    private final CrudAcademicPerformanceService crudAcademicPerformanceService;

    public DataInitializer(DI di)
    {
        this.crudStudentEntityService = di.getCrudStudentEntityService();
        this.crudAcademicPerformanceService = di.getCrudAcademicPerformanceService();
    }

    public void initializeDataFromCsvFileIfDBEmpty()
    {
        if (crudStudentEntityService.findAllStudents().isEmpty())
        {
            initializeDataFromCsvFile();
        }
    }

    public void initializeDataFromCsvFile()
    {
        Path filePathFromResource = Path.of("content/basicprogramming_2_1.csv");
        List<CsvFileContentInfo> dataFromCsvFile = CsvFileService.getDataFromFile(filePathFromResource);

        initializeData(dataFromCsvFile);
    }

    private void initializeData(List<CsvFileContentInfo> dataFromCsvFile)
    {
        Map<String, StudentEntity> map = new HashMap<>();
        for(CsvFileContentInfo csvFileContentInfo : dataFromCsvFile)
        {
            map.put(csvFileContentInfo.studentEntity().getName(), csvFileContentInfo.studentEntity());
        }

        VkService vkService = new VkService();

        List<VkMemberResponse> vkMemberResponsesList = new ArrayList<>();
        try
        {
            vkMemberResponsesList = vkService.getGroupByName("project__it");
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        vkMemberResponsesList.forEach(vkMemberResponse -> {
            String key = "%s %s".formatted(vkMemberResponse.getFirst_name(), vkMemberResponse.getLast_name());
            if (map.containsKey(key))
            {
                StudentEntity studentEntity = map.get(key);
                if (vkMemberResponse.getCity() != null)
                {
                    studentEntity.setCity(vkMemberResponse.getCity().getTitle());
                }
                studentEntity.setAge(vkMemberResponse.getAge());
                return;
            }

            key = "%s %s".formatted(vkMemberResponse.getLast_name(), vkMemberResponse.getFirst_name());
            if (map.containsKey(key))
            {
                StudentEntity studentEntity = map.get(key);
                if (vkMemberResponse.getCity() != null)
                {
                    studentEntity.setCity(vkMemberResponse.getCity().getTitle());
                }
                studentEntity.setAge(vkMemberResponse.getAge());
            }
        });

        for(CsvFileContentInfo csvFileContentInfo : dataFromCsvFile)
        {
            createAcademicPerformances(csvFileContentInfo.academicPerformances(), createStudent(csvFileContentInfo.studentEntity()));
        }
    }

    private StudentEntity createStudent(StudentEntity studentEntity)
    {
        crudStudentEntityService.add(studentEntity);
        StudentEntity studentEntityFromDb = crudStudentEntityService.findById(studentEntity.getId());

        System.out.println(studentEntityFromDb);

        return studentEntityFromDb;
    }

    private List<AcademicPerformance> createAcademicPerformances(List<AcademicPerformance> academicPerformances,
            StudentEntity studentEntity)
    {
        academicPerformances.forEach(academicPerformance -> {
            academicPerformance.setStudent(studentEntity);
            crudAcademicPerformanceService.add(academicPerformance);
            System.out.println(academicPerformance);
        });


        return academicPerformances;
    }
}
