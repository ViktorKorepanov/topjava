package ru.javawebinar.topjava.repository.datajpa;

import org.springframework.beans.factory.annotation.Autowired;
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
public class DataJpaMealRepository implements MealRepository {

//    @PersistenceContext
//    private EntityManager em;

    private final CrudMealRepository crudMealRepository;
    private final CrudUserRepository crudUserRepository;

    public DataJpaMealRepository(CrudMealRepository crudMealRepository, CrudUserRepository crudUserRepository) {
        this.crudMealRepository = crudMealRepository;
        this.crudUserRepository = crudUserRepository;
    }

    @Override
    public Meal save(Meal meal, int userId) {
//        meal.setUser(em.getReference(User.class, userId));
        meal.setUser(crudUserRepository.getOne(userId));
        if (meal.isNew()) {
            crudMealRepository.save(meal);
            return meal;
        }
        else if (get(meal.getId(), userId) == null) {
            return null;
        }
        Meal updateMeal = crudMealRepository.findByIdAndUserId(meal.getId(), userId);
        updateMeal.setUser(meal.getUser());
        updateMeal.setCalories(meal.getCalories());
        updateMeal.setDescription(meal.getDescription());
        updateMeal.setDateTime(meal.getDateTime());
        crudMealRepository.save(updateMeal);
        return updateMeal;
    }

    @Override
    public boolean delete(int id, int userId) {
        return crudMealRepository.deleteByIdAndUserId(id, userId) != 0;
    }

    @Override
    public Meal get(int id, int userId) {
        return crudMealRepository.findByIdAndUserId(id, userId);
    }

    @Override
    public List<Meal> getAll(int userId) {
        return crudMealRepository.findAllByUserIdOrderByDateTimeDesc(userId);
    }

    @Override
    public List<Meal> getBetweenHalfOpen(LocalDateTime startDateTime, LocalDateTime endDateTime, int userId) {
        return crudMealRepository.findAllByUserIdOrderByDateTimeDesc(userId)
                .stream()
                .filter(meal -> meal.getDateTime().isAfter(startDateTime) && meal.getDateTime().isBefore(endDateTime))
                .collect(Collectors.toList());
    }
}
