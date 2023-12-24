package project.urfu.academicPerformance.repository;

import java.util.List;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.TypedQuery;
import project.urfu.academicPerformance.model.entity.AcademicPerformance;
import project.urfu.db.JpaManagerFactory;
import project.urfu.student.model.entity.StudentEntity;

public class AcademicPerformanceRepository
{
    private final EntityManagerFactory entityManagerFactory;

    public AcademicPerformanceRepository()
    {
        this.entityManagerFactory = JpaManagerFactory.getEntityManagerFactory();
    }

    public List<AcademicPerformance> findAll()
    {
        try (EntityManager entityManager = entityManagerFactory.createEntityManager()) {
            EntityTransaction entityTransaction = entityManager.getTransaction();
            entityTransaction.begin();

            TypedQuery<AcademicPerformance> query = entityManager.createQuery(
                    "SELECT s FROM AcademicPerformance s",
                    AcademicPerformance.class);

            List<AcademicPerformance> academicPerformances = query.getResultList();

            entityTransaction.commit();
            return academicPerformances;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public void save(AcademicPerformance academicPerformance) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        EntityTransaction entityTransaction = entityManager.getTransaction();

        try {
            entityTransaction.begin();
            entityManager.persist(academicPerformance);
            entityTransaction.commit();
        } catch (Exception e) {
            if (entityTransaction != null && entityTransaction.isActive()) {
                entityTransaction.rollback();
            }
            throw new RuntimeException("Ошибка сохранения academic Performance: " + e.getMessage(), e);
        } finally {
            entityManager.close();
        }
    }

    public List<AcademicPerformance> findByStudentEntity(StudentEntity studentEntity) {
        try (EntityManager entityManager = entityManagerFactory.createEntityManager()) {
            EntityTransaction entityTransaction = entityManager.getTransaction();
            entityTransaction.begin();

            TypedQuery<AcademicPerformance> query = entityManager.createQuery(
                    "SELECT ap FROM AcademicPerformance ap WHERE ap.student = :student",
                    AcademicPerformance.class);
            query.setParameter("student", studentEntity);

            List<AcademicPerformance> academicPerformances = query.getResultList();

            entityTransaction.commit();
            return academicPerformances;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
