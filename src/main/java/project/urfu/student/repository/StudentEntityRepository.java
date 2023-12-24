package project.urfu.student.repository;

import java.util.List;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.TypedQuery;
import project.urfu.db.JpaManagerFactory;
import project.urfu.student.model.entity.StudentEntity;

public class StudentEntityRepository
{
    private final EntityManagerFactory entityManagerFactory;

    public StudentEntityRepository()
    {
        this.entityManagerFactory = JpaManagerFactory.getEntityManagerFactory();
    }

    public void save(StudentEntity studentEntity) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        EntityTransaction entityTransaction = entityManager.getTransaction();

        try {
            entityTransaction.begin();
            entityManager.persist(studentEntity);
            entityTransaction.commit();
        } catch (Exception e) {
            if (entityTransaction != null && entityTransaction.isActive()) {
                entityTransaction.rollback();
            }
            // Можно обработать исключение или просто передать его дальше
            throw new RuntimeException("Ошибка сохранения студента: " + e.getMessage(), e);
        } finally {
            entityManager.close();
        }
    }

    public StudentEntity findById(int id)
    {
        try(EntityManager entityManager = entityManagerFactory.createEntityManager())
        {
            EntityTransaction entityTransaction = entityManager.getTransaction();
            entityTransaction.begin();

            TypedQuery<StudentEntity> q =
                    entityManager.createQuery("SELECT s FROM StudentEntity s WHERE s.id = :id",
                            StudentEntity.class);
            q.setParameter("id", id);

            return q.getSingleResult();
        }
    }

    public List<StudentEntity> findAllStudents() {
        try (EntityManager entityManager = entityManagerFactory.createEntityManager()) {
            EntityTransaction entityTransaction = entityManager.getTransaction();
            entityTransaction.begin();

            TypedQuery<StudentEntity> query = entityManager.createQuery(
                    "SELECT s FROM StudentEntity s",
                    StudentEntity.class);

            List<StudentEntity> students = query.getResultList();

            entityTransaction.commit();
            return students;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
