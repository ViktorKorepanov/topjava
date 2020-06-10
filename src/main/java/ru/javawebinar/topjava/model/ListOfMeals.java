package ru.javawebinar.topjava.model;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

public class ListOfMeals {
    //Создание списка и мапы для хранения блюд, назнаение максимального колличества калорий
    public static List<Meal> listOfMeals = new CopyOnWriteArrayList<>();
    public static Map<Integer, Meal> mapOfMeals = new ConcurrentHashMap<>();
    public final static int caloriesPerDay = 2000;

    //Добавление начальных значений в список и мапу
    static {
//        Collections.addAll(listOfMeals,
//                new Meal(LocalDateTime.of(2020, Month.JANUARY, 30, 10, 0), "Завтрак", 500),
//                new Meal(LocalDateTime.of(2020, Month.JANUARY, 30, 13, 0), "Обед", 1000),
//                new Meal(LocalDateTime.of(2020, Month.JANUARY, 30, 20, 0), "Ужин", 500),
//                new Meal(LocalDateTime.of(2020, Month.JANUARY, 31, 0, 0), "Еда на граничное значение", 100),
//                new Meal(LocalDateTime.of(2020, Month.JANUARY, 31, 10, 0), "Завтрак", 1000),
//                new Meal(LocalDateTime.of(2020, Month.JANUARY, 31, 13, 0), "Обед", 500),
//                new Meal(LocalDateTime.of(2020, Month.JANUARY, 31, 20, 0), "Ужин", 500));

        for (Meal meal : listOfMeals) {
            mapOfMeals.put(meal.getId(), meal);
        }
    }
}
