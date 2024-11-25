package by.bsuir.hotelwebapp.servlet;

import java.io.IOException;

import org.hibernate.StatelessSession;
import org.thymeleaf.ITemplateEngine;
import org.thymeleaf.context.WebContext;
import org.thymeleaf.web.servlet.JakartaServletWebApplication;

import by.bsuir.hotelwebapp.config.HibernateSession;
import by.bsuir.hotelwebapp.config.ThymeleafConfig;
import by.bsuir.hotelwebapp.dto.request.UserRequestDTO;
import by.bsuir.hotelwebapp.service.UserService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/signin")
public class SignInServlet extends HttpServlet {
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
        templateEngine.process("signin/signin", context, resp.getWriter());
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
            String email = req.getParameter("email");
            UserRequestDTO userRequest = new UserRequestDTO(null, username, password, email, false);

            if (!userService.addUser(userRequest)) {
                context.setVariable("error", "error.user.exist");
                templateEngine.process("signin/signin", context, resp.getWriter());
                return;
            }
            
            hibernateSession.getTransaction().commit();
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }

        resp.sendRedirect("login");
    }
}
