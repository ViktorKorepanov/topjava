package ru.javawebinar.topjava;

import ru.javawebinar.topjava.model.Meal;

import java.time.LocalDateTime;

public class MealTestData {
    public static final Meal MEAL = new Meal(LocalDateTime.now(), "Завтрак", 1500);
}
