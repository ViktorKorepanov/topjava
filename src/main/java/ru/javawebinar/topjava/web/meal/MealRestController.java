package ru.javawebinar.topjava.web.meal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.service.MealService;
import ru.javawebinar.topjava.to.MealTo;
import ru.javawebinar.topjava.util.MealsUtil;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import static ru.javawebinar.topjava.util.MealsUtil.getFilteredTos;
import static ru.javawebinar.topjava.util.MealsUtil.getTos;
import static ru.javawebinar.topjava.web.SecurityUtil.authUserId;

@Controller
public class MealRestController {
    protected final Logger log = LoggerFactory.getLogger(getClass());

    private MealService service;

    @Autowired
    public MealRestController(MealService service) {
        this.service = service;
    }

    // получение всех блюд
    public List<Meal> getAll() {
        log.info("getAllMeals");
        return service.getAll(authUserId());
    }

    // получение всех блюд с excess
    public List<MealTo> getAllExcess() {
        log.info("getAllMeals with excess");
        return getTos(getAll(), MealsUtil.DEFAULT_CALORIES_PER_DAY);
    }

    // фильтрация по времени
    public List<MealTo> getAllExcessFiltered(LocalTime timeFrom, LocalTime timeTo) {
        log.info("getAllMeals with excess filtered by time");
        return getFilteredTos(getAll(), MealsUtil.DEFAULT_CALORIES_PER_DAY, timeFrom, timeTo);
    }

    // фильтрация по дате
    public List<MealTo> getAllExcessFiltered(LocalDate dateFrom, LocalDate dateTo) {
        log.info("getAllMeals with excess filtered by date");
        return getFilteredTos(getAll(), MealsUtil.DEFAULT_CALORIES_PER_DAY, dateFrom, dateTo);
    }

    // фильтрация по дате и времени
    public List<MealTo> getAllExcessFiltered(LocalDate dateFrom, LocalDate dateTo, LocalTime timeFrom, LocalTime timeTo) {
        log.info("getAllMeals with excess filtered by date");
        return getFilteredTos(getAll(), MealsUtil.DEFAULT_CALORIES_PER_DAY, dateFrom, dateTo, timeFrom, timeTo);
    }

    // получение блюда
    public Meal get(int id) {
        log.info("get meal id {}", id);
        return service.get(id, authUserId());
    }

    // создание блюда
    public Meal create(Meal meal) {
        log.info("create meal {}", meal);
        return service.create(meal, authUserId());
    }

    // редактирование блюда
    public void update(Meal meal) {
        log.info("update meal {}", meal);
        service.update(meal, authUserId());
    }

    // удаление блюда
    public void delete(int id) {
        log.info("delete meal id {}", id);
        service.delete(id, authUserId());
    }
}