package ru.javawebinar.topjava;

import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.Role;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.web.meal.MealRestController;
import ru.javawebinar.topjava.web.user.AdminRestController;

import java.time.LocalDateTime;
import java.util.Arrays;

public class SpringMain {
    public static void main(String[] args) {
        // java 7 automatic resource management
        try (ConfigurableApplicationContext appCtx = new ClassPathXmlApplicationContext("spring/spring-app.xml")) {
            System.out.println("Bean definition names: " + Arrays.toString(appCtx.getBeanDefinitionNames()));
            AdminRestController adminUserController = appCtx.getBean(AdminRestController.class);
            adminUserController.create(new User(null, "userName", "email@mail.ru", "password", Role.ADMIN));

//            User user1 = new User(null, "user1", "user1@mail.ru", "user1", Role.USER);
//            User user2 = new User(null, "user2", "user2@mail.ru", "user2", Role.USER);
//            adminUserController.create(user1);
//            adminUserController.create(user2);
//
//            System.out.println(adminUserController.get(2));
//            System.out.println(adminUserController.getByMail("email@mail.ru"));
//
//            System.out.println(adminUserController.getAll());

            MealRestController mealRestController = appCtx.getBean(MealRestController.class);
            Meal meal = new Meal(LocalDateTime.now(), "Ужин", 1000);
            mealRestController.create(meal);
            System.out.println(mealRestController.getAll());
            mealRestController.delete(6);
            System.out.println(mealRestController.getAll());
            System.out.println(mealRestController.get(4));

        }
    }
}
