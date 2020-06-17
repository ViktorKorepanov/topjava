package ru.javawebinar.topjava.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.repository.inmemory.InMemoryMealRepository;
import ru.javawebinar.topjava.service.MealService;
import ru.javawebinar.topjava.util.MealsUtil;
import ru.javawebinar.topjava.web.meal.MealRestController;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Objects;

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
                request.getRequestDispatcher("/mealForm.jsp").forward(request, response);
                break;
            case "all":
            default:
                log.info("getAll");
                request.setAttribute("meals", controller.getAllExcess());
                request.getRequestDispatcher("/meals.jsp").forward(request, response);
                break;
        }
    }

    private int getId(HttpServletRequest request) {
        String paramId = Objects.requireNonNull(request.getParameter("id"));
        return Integer.parseInt(paramId);
    }
}
