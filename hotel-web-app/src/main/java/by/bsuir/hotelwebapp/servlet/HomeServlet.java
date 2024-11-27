package by.bsuir.hotelwebapp.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.hibernate.StatelessSession;
import org.thymeleaf.ITemplateEngine;
import org.thymeleaf.context.WebContext;
import org.thymeleaf.web.servlet.JakartaServletWebApplication;

import by.bsuir.hotelwebapp.config.HibernateSession;
import by.bsuir.hotelwebapp.config.ThymeleafConfig;
import by.bsuir.hotelwebapp.dto.response.RoomTypeResponseDTO;
import by.bsuir.hotelwebapp.service.RoomTypeService;

import java.io.IOException;
import java.util.List;

@WebServlet("/home")
public class HomeServlet extends HttpServlet {
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

        try (StatelessSession hibernateSession = HibernateSession.getStatelessSession()) {
            hibernateSession.beginTransaction();

            RoomTypeService roomTypeService = new RoomTypeService(hibernateSession);
            List<RoomTypeResponseDTO> roomTypes = roomTypeService.getAvailableRooms();
            context.setVariable("roomTypes", roomTypes);
            
            hibernateSession.getTransaction().commit();
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
        
        templateEngine.process("home", context, resp.getWriter());
    }
}
