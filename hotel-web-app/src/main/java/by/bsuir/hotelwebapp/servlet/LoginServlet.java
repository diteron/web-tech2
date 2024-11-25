package by.bsuir.hotelwebapp.servlet;

import java.io.IOException;

import org.hibernate.StatelessSession;
import org.thymeleaf.ITemplateEngine;
import org.thymeleaf.context.WebContext;
import org.thymeleaf.web.servlet.JakartaServletWebApplication;

import by.bsuir.hotelwebapp.config.HibernateSession;
import by.bsuir.hotelwebapp.config.ThymeleafConfig;
import by.bsuir.hotelwebapp.dto.request.UserRequestDTO;
import by.bsuir.hotelwebapp.dto.response.UserResponseDTO;
import by.bsuir.hotelwebapp.service.UserService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/login")
public class LoginServlet extends HttpServlet{
    private JakartaServletWebApplication jakartaWebApp;
    private ITemplateEngine templateEngine;

    @Override
    public void init() throws ServletException {
        jakartaWebApp = ThymeleafConfig.getJakartaServletWebApplication(getServletContext());
        templateEngine = ThymeleafConfig.getTemplateEngine(getServletContext());
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("text/html;charset=UTF-8");
        WebContext context = new WebContext(jakartaWebApp.buildExchange(req, resp), req.getLocale());
        templateEngine.process("login/login", context, resp.getWriter());
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("text/html;charset=UTF-8");
        WebContext context = new WebContext(jakartaWebApp.buildExchange(req, resp), req.getLocale());

        try (StatelessSession hibernateSession = HibernateSession.getStatelessSession()) {
            hibernateSession.beginTransaction();

            UserService userService = new UserService(hibernateSession);
            String username = req.getParameter("username");
            String password = req.getParameter("password");
            UserRequestDTO userRequest = new UserRequestDTO(null, username, password, null, null);

            if (!isUserAuthorized(userRequest, userService, context, resp)) {
                return;
            }

            UserResponseDTO user = userService.getUserByUsername(username);
            createUserSession(user, req);
            
            hibernateSession.getTransaction().commit();
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }

        resp.sendRedirect("home");
    }

    private boolean isUserAuthorized(UserRequestDTO userRequest, UserService userService,
            WebContext context, HttpServletResponse resp) throws IOException {
        if (!userService.isUserExist(userRequest.username())) {
            context.setVariable("error", "error.user.not-exist");
            templateEngine.process("login/login", context, resp.getWriter());
            return false;
        }
        else if (!userService.isUserAuthorized(userRequest)) {
            context.setVariable("error", "error.user.incorrect-password");
            templateEngine.process("login/login", context, resp.getWriter());
            return false;
        }

        return true;
    }

    private void createUserSession(UserResponseDTO user, HttpServletRequest req) {
        HttpSession httpSession = req.getSession();
        httpSession.setAttribute("user", user);
    }
}
