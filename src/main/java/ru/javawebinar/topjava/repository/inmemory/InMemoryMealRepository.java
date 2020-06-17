package ru.javawebinar.topjava.repository.inmemory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.util.MealsUtil;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Repository
public class InMemoryMealRepository implements MealRepository {
    private static final Logger log = LoggerFactory.getLogger(InMemoryUserRepository.class);

    private Map<Integer, Meal> mealMap = new ConcurrentHashMap<>();
    private AtomicInteger counter = new AtomicInteger(0);

    {
        MealsUtil.MEALS.forEach(meal -> save(meal, meal.getUserId()));
        
//        MealsUtil.MEALS.forEach(this::save);
    }

    @Override
    // создание или редактирование блюда
    public Meal save(Meal meal, int userId) {
        if (meal.isNew()) {
            log.info("create new meal");
            meal.setId(counter.incrementAndGet());
            mealMap.put(meal.getId(), meal);
            return meal;
        }
        else if (meal.getUserId() == userId) {
            log.info("update meal");
            return mealMap.computeIfPresent(meal.getId(), (id, oldMeal) -> meal);
        }
        else
            return null;
    }

    @Override
    // удаление блюда
    public boolean delete(int id, int userId) {
        if ((mealMap.get(id).getUserId() == userId) && (mealMap.get(id) != null)) {
            log.info("delete meal");
            mealMap.remove(id);
            return true;
        }
        else
            return false;
    }

    @Override
    // получение блюда
    public Meal get(int id, int userId) {
        if ((mealMap.get(id).getUserId() == userId) && (mealMap.get(id) != null)) {
            log.info("get meal");
            return mealMap.get(id);
        }
        else
            return null;
    }

    @Override
    // полуение всех блюд
    public List<Meal> getAll(int userId) {
        log.info("getAllMeals");
        List<Meal> mealList = new CopyOnWriteArrayList<>();
        mealList.addAll(mealMap.values());
        Collections.sort(mealList, (m1, m2) -> m2.getDateTime().compareTo(m1.getDateTime()));

        return mealList.stream()
                .filter(meal -> meal.getUserId() == userId)
                .collect(Collectors.toList());
    }
}

