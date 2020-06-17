package ru.javawebinar.topjava.web;

import org.slf4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static org.slf4j.LoggerFactory.getLogger;

public class UserServlet extends HttpServlet {
    private static final Logger log = getLogger(UserServlet.class);

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        log.debug("forward to users");
        String user = request.getParameter("user");
        if (user.equals("user1"))
            SecurityUtil.setAuthUserId(1);
        else if (user.equals("user2"))
            SecurityUtil.setAuthUserId(2);

//        request.getRequestDispatcher("/meals.jsp").forward(request, response);
        response.sendRedirect("meals");
    }
}
