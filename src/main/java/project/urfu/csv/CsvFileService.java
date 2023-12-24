package project.urfu.csv;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import com.opencsv.CSVParser;
import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.exceptions.CsvValidationException;

import project.urfu.academicPerformance.model.entity.AcademicPerformance;
import project.urfu.student.model.entity.StudentEntity;

public final class CsvFileService
{
    // АКТ УПР ДЗ СЕМ
    private static final Integer[][] performancesColumnNumberArray = {
            { 9, 10, null, 22 },
            { 23, 24, 32, 37 },
            { 38, 39, 45, 49 },
            { 50, 51, 56, 61 },
            { 62, 63, 74, 78 },
            { 79, 80, 89, 93 },
            { 94, 95, 99, 103 },
            { 104, 105, 111, 116 },
            { 117, 118, 123, 126 },
            { 127, 128, 133, 137 },
            { 138, null, 139, 144 },
            { 145, 146, 155, 160 },
            { 161, 162, 172, 176 },
            { 177, 178, 184, 188 },
            { 189, 190, 195, 198 }
    };

    private static final int[] academicPerformanceTitlesIndexArray = { 9, 23, 38, 50, 62, 79, 94, 104, 117, 127, 138,
            145, 161, 177, 189 };

    private CsvFileService()
    {
    }

    public static List<CsvFileContentInfo> getDataFromFile(Path filePathFromResource)
    {
        return parseCsvFile(filePathFromResource);
    }

    public static List<AcademicPerformance> getMaxAcademicPerformances(Path filePathFromResource)
    {
        return getMaxAcademicPerformancesPrivate(filePathFromResource);
    }

    private static List<AcademicPerformance> getMaxAcademicPerformancesPrivate(Path filePathFromResource)
    {
        try (CSVReader csvReader = getReader(filePathFromResource))
        {
            String[] line = csvReader.readNext();
            List<String> academicPerformanceTitles = Arrays.stream(academicPerformanceTitlesIndexArray)
                    .mapToObj(index -> line[index])
                    .toList();
            csvReader.readNext();
            return getAcademicPerformance(csvReader.readNext(), academicPerformanceTitles);
        }
        catch (IOException | CsvValidationException e)
        {
            e.printStackTrace();
            throw new RuntimeException();
        }
    }

    private static CSVReader getReader(Path filePathFromResource)
    {
        InputStream inputStream =
                Objects.requireNonNull(
                        CsvFileService.class.getClassLoader().getResourceAsStream(filePathFromResource.toString()));

        CSVParser parser = new CSVParserBuilder()
                .withSeparator(';')
                .withIgnoreQuotations(true)
                .build();

        return new CSVReaderBuilder(new InputStreamReader(inputStream))
                .withCSVParser(parser)
                .build();
    }

    private static List<CsvFileContentInfo> parseCsvFile(Path filePathFromResource)
    {

        List<CsvFileContentInfo> result = new ArrayList<>();
        try (CSVReader csvReader = getReader(filePathFromResource))
        {

            String[] line = csvReader.readNext();
            List<String> academicPerformanceTitles = Arrays.stream(academicPerformanceTitlesIndexArray)
                    .mapToObj(index -> line[index - 1])
                    .toList();

            csvReader.readNext();
            csvReader.readNext();
            String[] nextLine;
            while ((nextLine = csvReader.readNext()) != null)
            {
                StudentEntity studentEntity = getStudent(nextLine);
                List<AcademicPerformance> academicPerformances = getAcademicPerformance(nextLine, academicPerformanceTitles);
                CsvFileContentInfo csvFileContentInfo = new CsvFileContentInfo(studentEntity, academicPerformances);
                result.add(csvFileContentInfo);
            }
        }
        catch (IOException | CsvValidationException e)
        {
            e.printStackTrace();
            throw new RuntimeException();
        }

        return result;
    }

    private static List<AcademicPerformance> getAcademicPerformance(String[] nextLine, List<String> academicPerformanceTitles)
    {
        List<AcademicPerformance> academicPerformances = new ArrayList<>();
        int index = 0;
        for (Integer[] performancesColumns : performancesColumnNumberArray)
        {
            AcademicPerformance academicPerformance = new AcademicPerformance();
            academicPerformance.setTitle(academicPerformanceTitles.get(index));
            Integer sum = 0;
            academicPerformance.setActivityPoints(Integer.parseInt(nextLine[performancesColumns[0] - 1]));
            sum += academicPerformance.getActivityPoints();
            if (performancesColumns[1] == null)
            {
                academicPerformance.setExercises(0);
            }
            else
            {
                academicPerformance.setExercises(Integer.parseInt(nextLine[performancesColumns[1] - 1]));
                sum += academicPerformance.getExercises();
            }

            if (performancesColumns[2] == null)
            {
                academicPerformance.setHomework(0);
            }
            else
            {
                academicPerformance.setHomework(Integer.parseInt(nextLine[performancesColumns[2] - 1]));
                sum += academicPerformance.getHomework();
            }

            academicPerformance.setSeminaryPoints(Integer.parseInt(nextLine[performancesColumns[3] - 1]));
            sum += academicPerformance.getSeminaryPoints();

            academicPerformance.setTotalPoints(sum);

            academicPerformances.add(academicPerformance);
            index++;
        }

        return academicPerformances;
    }

    private static StudentEntity getStudent(String[] lineContent)
    {
        StudentEntity studentEntity = new StudentEntity();
        studentEntity.setName(lineContent[0]);
        studentEntity.setGroup(lineContent[1]);

        return studentEntity;
    }
}
