package ru.javawebinar.topjava.repository.jpa;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.repository.MealRepository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Repository
@Transactional(readOnly = true)
public class JpaMealRepository implements MealRepository {

    @PersistenceContext
    private EntityManager em;

    @Override
    @Transactional
    public Meal save(Meal meal, int userId) {
        User user = em.getReference(User.class, userId);
        if (meal.isNew()) {
            meal.setUser(user);
            em.persist(meal);
            return meal;
        }
        else if (user.getId() == userId) {
            meal.setUser(user);
            return em.merge(meal);
        }
        else {
            return null;
        }
    }

    @Override
    @Transactional
    public boolean delete(int id, int userId) {
        User user = em.getReference(User.class, userId);
        if (user.getId() == userId) {
            return em.createQuery("DELETE FROM Meal m WHERE m.id=:id")
                    .setParameter("id", id)
                    .executeUpdate() != 0;
        }
        else {
            return false;
        }
    }

    @Override
    public Meal get(int id, int userId) {
        Meal meal = em.find(Meal.class, id);
        if (meal.getUser().getId() == userId) {
            return meal;
        }
        else {
            return null;
        }
    }

    @Override
    public List<Meal> getAll(int userId) {
        return em.createQuery("SELECT m FROM Meal m WHERE m.user.id=:userId ORDER BY m.dateTime DESC", Meal.class)
                .setParameter("userId", userId)
                .getResultList();
    }

    @Override
    public List<Meal> getBetweenHalfOpen(LocalDateTime startDateTime, LocalDateTime endDateTime, int userId) {
        List<Meal> meals = em.createQuery("SELECT m FROM Meal m WHERE m.user.id=:userId ORDER BY m.dateTime DESC", Meal.class)
                .setParameter("userId", userId)
                .getResultList()
                .stream()
                .filter(meal -> meal.getDateTime().isAfter(startDateTime) && meal.getDateTime().isBefore(endDateTime))
                .collect(Collectors.toList());
        return meals;
    }
}