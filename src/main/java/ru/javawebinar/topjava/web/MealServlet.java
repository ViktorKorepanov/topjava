package ru.javawebinar.topjava.web;

import org.slf4j.Logger;

import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.util.MealsUtil;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import static org.slf4j.LoggerFactory.getLogger;
import static ru.javawebinar.topjava.model.ListOfMeals.*;

public class MealServlet extends HttpServlet {
    //Создание логгера
    private static final Logger log = getLogger(UserServlet.class);

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //Назначение JSP на корорую нужно перейти и атрибута, по которому будет обход, передача отфильтрованного списка для обхода
        String forward = "/meals.jsp";
        request.setAttribute("meals", MealsUtil.filteredByStreamsNoFilter(listOfMeals, caloriesPerDay));

        //Назначение параметра action, по которому передается определенное действие
        String action = request.getParameter("action");

        //Проверка какое действие передалось
        if (action != null) {
            //Удаление блюда
            if (action.equalsIgnoreCase("delete")) {
                log.debug("Delete meal");

                int id = Integer.parseInt(request.getParameter("id"));
                listOfMeals.remove(mapOfMeals.get(id));
                mapOfMeals.remove(id);
                forward = "/meals.jsp";
                request.setAttribute("meals", MealsUtil.filteredByStreamsNoFilter(listOfMeals, caloriesPerDay));
                //Редактирование блюда
            } else if (action.equalsIgnoreCase("edit")) {
                log.debug("Edit meal");

                int id = Integer.parseInt(request.getParameter("id"));
                Meal meal = mapOfMeals.get(id);
                forward = "/meal.jsp";
                request.setAttribute("meal", meal);
                //Добавление блюда
            } else if (action.equals("add")) {
                log.debug("Add meal");

                forward = "/meal.jsp";
            }
        }

        //Переход на JSP
        log.debug("Forward to JSP");
        request.getRequestDispatcher(forward).forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //Изменение кодировки, чтобы кириллица отображалась корректно
        request.setCharacterEncoding("UTF-8");

        //Назначение формата даты и времени
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
        LocalDateTime dateTime = LocalDateTime.parse(request.getParameter("dateTime"), formatter);

        //Проверка на то существует ли такое блюдо в списке, если да, то измением, если нет, то добавляем
        if (request.getParameter("id") == null || request.getParameter("id").isEmpty()) {
            log.debug("Add meal");

            Meal meal = new Meal();

            meal.setDateTime(dateTime);
            meal.setDescription(request.getParameter("description"));
            meal.setCalories(Integer.parseInt(request.getParameter("calories")));

            listOfMeals.add(meal);
            mapOfMeals.put(meal.getId(), meal);
        } else {
            log.debug("Edit meal");

            Meal meal = mapOfMeals.get(Integer.parseInt(request.getParameter("id")));

            meal.setDateTime(dateTime);
            meal.setDescription(request.getParameter("description"));
            meal.setCalories(Integer.parseInt(request.getParameter("calories")));
        }

        request.setAttribute("meals", MealsUtil.filteredByStreamsNoFilter(listOfMeals, caloriesPerDay));

        //Переход на JSP
        log.debug("Forward to JSP");
        request.getRequestDispatcher("/meals.jsp").forward(request, response);
    }
}
