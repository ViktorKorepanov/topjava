package ru.javawebinar.topjava.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;

import java.util.List;

@Service
public class MealService {

    private final MealRepository repository;

    @Autowired
    public MealService(MealRepository repository) {
        this.repository = repository;
    }

    // создание блюда
    public Meal create(Meal meal, int userId) {
        return repository.save(meal, userId);
    }

    // редактирование блюда
    public void update(Meal meal, int userId) {
        repository.save(meal, userId);
    }

    // удаление блюда
    public void delete(int id, int userId) {
        repository.delete(id, userId);
    }

    // получение блюда
    public Meal get(int id, int userId) {
        return repository.get(id, userId);
    }

    // получение всех блюд
    public List<Meal> getAll(int userId) {
        return repository.getAll(userId);
    }
}