package project.urfu.academicPerformance.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import project.urfu.student.model.entity.StudentEntity;

@Entity
@Table
public class AcademicPerformance
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String title;

    @Column
    private int activityPoints; // АКТ

    @Column
    private int exercises; // упр

    @Column
    private int homework; // ДЗ

    @Column
    private int seminaryPoints; // СЕМ

    @Column
    private int totalPoints;

    @ManyToOne
    @JoinColumn(name = "student_id")
    private StudentEntity student;

    public Long getId()
    {
        return id;
    }

    public void setId(Long id)
    {
        this.id = id;
    }

    public String getTitle()
    {
        return title;
    }

    public void setTitle(String title)
    {
        this.title = title;
    }

    public int getExercises()
    {
        return exercises;
    }

    public void setExercises(int exercises)
    {
        this.exercises = exercises;
    }

    public int getHomework()
    {
        return homework;
    }

    public void setHomework(int homework)
    {
        this.homework = homework;
    }

    public int getActivityPoints()
    {
        return activityPoints;
    }

    public void setActivityPoints(int activityPoints)
    {
        this.activityPoints = activityPoints;
    }

    public int getSeminaryPoints()
    {
        return seminaryPoints;
    }

    public void setSeminaryPoints(int seminaryPoints)
    {
        this.seminaryPoints = seminaryPoints;
    }

    public int getTotalPoints()
    {
        return totalPoints;
    }

    public void setTotalPoints(int totalPoints)
    {
        this.totalPoints = totalPoints;
    }

    public StudentEntity getStudent()
    {
        return student;
    }

    public void setStudent(StudentEntity student)
    {
        this.student = student;
    }

    @Override
    public String toString()
    {
        return "AcademicPerformance{" +
                "id=" + id +
                ", activityPoints=" + activityPoints +
                ", exercises=" + exercises +
                ", homework=" + homework +
                ", seminaryPoints=" + seminaryPoints +
                ", totalPoints=" + totalPoints +
                ", student=" + student +
                '}';
    }
}
