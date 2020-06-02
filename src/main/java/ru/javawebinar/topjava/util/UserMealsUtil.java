package ru.javawebinar.topjava.util;

import ru.javawebinar.topjava.model.UserMeal;
import ru.javawebinar.topjava.model.UserMealWithExcess;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class UserMealsUtil {
    public static void main(String[] args) {
        List<UserMeal> meals = Arrays.asList(
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 30, 10, 0), "Завтрак", 500),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 30, 13, 0), "Обед", 1000),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 30, 20, 0), "Ужин", 500),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 0, 0), "Еда на граничное значение", 100),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 10, 0), "Завтрак", 1000),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 13, 0), "Обед", 500),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 20, 0), "Ужин", 410)
        );

//        List<UserMealWithExcess> mealsTo = filteredByCycles(meals, LocalTime.of(7, 0), LocalTime.of(12, 0), 2000);
//            mealsTo.forEach(System.out::println);

        System.out.println(filteredByStreams(meals, LocalTime.of(7, 0), LocalTime.of(12, 0), 2000));
    }

    public static List<UserMealWithExcess> filteredByCycles(List<UserMeal> meals, LocalTime startTime, LocalTime endTime, int maxCaloriesPerDay) {
        List<UserMealWithExcess> resultNoFilter = new ArrayList<>();
        List<UserMealWithExcess> resultFilter = new ArrayList<>();
        List<UserMeal> oneDayMeals = new ArrayList<>();
        LocalDate previousDate = meals.get(0).getDate();
        boolean excess = true;
        int caloriesPerDay = 0;
        int i = 0;

        for (UserMeal meal : meals) {
            LocalDate nextDate = meal.getDate();
            i++;

            if (nextDate.equals(previousDate)) {
                oneDayMeals.add(meal);
                caloriesPerDay += meal.getCalories();
            }

            if (!(nextDate.equals(previousDate)) || (i == meals.size())) {
                previousDate = nextDate;

                for (UserMeal oneDayMeal : oneDayMeals) {
                    if (caloriesPerDay > maxCaloriesPerDay)
                        excess = false;
                    resultNoFilter.add(new UserMealWithExcess(oneDayMeal.getDateTime(), oneDayMeal.getDescription(), oneDayMeal.getCalories(), excess));
                }

                oneDayMeals.clear();
                oneDayMeals.add(meal);
                caloriesPerDay = meal.getCalories();
                excess = true;
            }
        }

        //проверка каллорий в последнем дне
        if (caloriesPerDay > maxCaloriesPerDay)
            excess = false;

        //добавление последнего дня
        resultNoFilter.add(new UserMealWithExcess(oneDayMeals.get(0).getDateTime(), oneDayMeals.get(0).getDescription(), oneDayMeals.get(0).getCalories(), excess));

        for (UserMealWithExcess mealWithExcess : resultNoFilter) {
            if (TimeUtil.isBetweenHalfOpen(mealWithExcess.getTime(), startTime, endTime))
                resultFilter.add(mealWithExcess);
        }

        return resultFilter;
    }

    public static List<UserMealWithExcess> filteredByStreams(List<UserMeal> meals, LocalTime startTime, LocalTime endTime, int maxCaloriesPerDay) {
        List<UserMealWithExcess> resultNoFilter = new ArrayList<>();
        List<UserMealWithExcess> resultFilter = new ArrayList<>();
        List<UserMeal> oneDayMeals = new ArrayList<>();
        List<Integer> caloriesPerDay = new ArrayList<>();
        long i = 0;

        do {
            LocalDate date = meals.get((int) i).getDate();
            meals.stream()
                    .skip(i)
                    .filter(m -> {
                        if (m.getDate().equals(date)) {
                            caloriesPerDay.add(m.getCalories());
                            return true;
                        } else
                            return false;
                    })
                    .forEach(oneDayMeals::add);

            if (caloriesPerDay.stream().reduce((acc, c) -> acc + c).get() > maxCaloriesPerDay) {
                oneDayMeals.stream()
                        .forEach(m -> {
                            resultNoFilter.add(new UserMealWithExcess(m.getDateTime(), m.getDescription(), m.getCalories(), false));
                        });
            } else {
                oneDayMeals.stream()
                        .forEach(m -> {
                            resultNoFilter.add(new UserMealWithExcess(m.getDateTime(), m.getDescription(), m.getCalories(), true));
                        });
            }

            i += oneDayMeals.size();
            caloriesPerDay.clear();
            oneDayMeals.clear();
        } while (i < meals.size());

        resultNoFilter.stream()
                .filter(m -> (TimeUtil.isBetweenHalfOpen(m.getTime(), startTime, endTime)))
                .forEach(resultFilter::add);

        return resultFilter;
    }
}
