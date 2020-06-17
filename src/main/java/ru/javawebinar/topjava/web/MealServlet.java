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
    private static final Logger log = LoggerFactory.getLogger(MealServlet.class);

    // создание контекста Spring для внедрения зависимостей
    ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("spring/spring-app.xml");
    MealRestController controller = context.getBean(MealRestController.class);

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        String id = request.getParameter("id");

        Meal meal;

        // создание нового блюда
        if (id.isEmpty()) {
            meal = new Meal(null, LocalDateTime.parse(request.getParameter("dateTime")),
                    request.getParameter("description"),
                    Integer.parseInt(request.getParameter("calories")));
            log.info("create meal");
            controller.create(meal);
        }

        // редактирование существующего блюда
        else {
            meal = new Meal(Integer.valueOf(id), LocalDateTime.parse(request.getParameter("dateTime")),
                    request.getParameter("description"),
                    Integer.parseInt(request.getParameter("calories")));
            log.info("update meal");
            controller.update(meal);
        }

        response.sendRedirect("meals");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //Назначение JSP на корорую нужно перейти и атрибута, по которому будет обход, передача отфильтрованного списка для обхода
        String forward = "/meals.jsp";
        request.setAttribute("meals", MealsUtil.filteredByStreamsNoFilter(listOfMeals, caloriesPerDay));

        //Назначение параметра action, по которому передается определенное действие
        String action = request.getParameter("action");
        String tFrom = request.getParameter("timeFrom");
        String tTo = request.getParameter("timeTo");
        String dFrom = request.getParameter("dateFrom");
        String dTo = request.getParameter("dateTo");

        // фильтрация по времени
        if (tFrom != null && tTo != null && dFrom != null && dTo != null) {
            log.info("getAll filter by time");
            LocalTime timeFrom;
            LocalTime timeTo;
            LocalDate dateFrom;
            LocalDate dateTo;

            try {
                timeFrom = LocalTime.parse(tFrom);
            } catch (Exception e) {
                timeFrom = LocalTime.of(0, 0);
            }
            try {
                timeTo = LocalTime.parse(tTo);
            } catch (Exception e) {
                timeTo = LocalTime.of(23,59);
            }
            try {
                dateFrom = LocalDate.parse(dFrom);
                log.info(dateFrom.toString());
            } catch (Exception e) {
                dateFrom = controller.getAll().get(controller.getAll().size() - 1).getDate();
                log.info(dateFrom.toString());
            }
            try {
                dateTo = LocalDate.parse(dTo);
                log.info(dateTo.toString());
            } catch (Exception e) {
                dateTo = controller.getAll().get(0).getDate();
                log.info(dateTo.toString());
            }

            request.setAttribute("meals", controller.getAllExcessFiltered(dateFrom, dateTo, timeFrom, timeTo));
            request.setAttribute("dateFrom", dateFrom);
            request.setAttribute("dateTo", dateTo);
            request.setAttribute("timeFrom", timeFrom);
            request.setAttribute("timeTo", timeTo);
            request.getRequestDispatcher("/meals.jsp").forward(request, response);
        }

        // создание, получение, редактирование, удаление блюда
        switch (action == null ? "all" : action) {
            case "delete":
                int id = getId(request);
                log.info("Delete {}", id);
                controller.delete(id);
                response.sendRedirect("meals");
                break;
            case "create":
            case "update":
                final Meal meal = "create".equals(action) ?
                        new Meal(LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES), "", 1000) :
                        controller.get(getId(request));
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
