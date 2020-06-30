package ru.javawebinar.topjava.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.test.context.junit4.SpringRunner;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.repository.MealRepository;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.Assert.*;
import static ru.javawebinar.topjava.MealTestData.MEAL;
import static ru.javawebinar.topjava.UserTestData.*;

@ContextConfiguration({
        "classpath:spring/spring-app.xml",
        "classpath:spring/spring-db.xml"
})
@RunWith(SpringRunner.class)
@Sql(scripts = "classpath:db/populateDB.sql", config = @SqlConfig(encoding = "UTF-8"))
public class MealServiceTest {

    @Autowired
    MealService service;

    @Autowired
    MealRepository repository;

    @Test
    public void create() {
        Meal newMeal = new Meal(LocalDateTime.now(), "Обед", 1000);
        Meal created = service.create(newMeal, USER_ID);
        int newId = created.id();
        newMeal.setId(newId);
        assertEquals(created, newMeal);
//        assertMatch(service.get(newId), newUser);
    }

    @Test
    public void update() {
        Meal updateMeal = new Meal(MEAL);
        updateMeal.setCalories(2000);
        updateMeal.setDateTime(LocalDateTime.now());
        updateMeal.setDescription("Ужин");
        service.update(updateMeal, USER_ID);
        assertEquals(service.get(updateMeal.getId(), USER_ID), updateMeal);
    }

    @Test
    public void delete() {
        service.delete(MEAL.getId(), USER_ID);
        assertNull(service.get(MEAL.getId(), USER_ID));
    }

    @Test
    public void get() {
        Meal meal = service.get(MEAL.getId(), USER_ID);
        assertEquals(meal, MEAL);
    }

    @Test
    public void getAll() {
        List<Meal> allMeals = service.getAll(USER_ID);
        assertArrayEquals(allMeals.toArray(), service.getAll(USER_ID).toArray());
    }

    @Test
    public void getBetweenInclusive() {
    }
}