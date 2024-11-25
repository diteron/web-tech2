package by.bsuir.hotelwebapp.servlet;

import java.io.IOException;
import java.util.List;

import org.hibernate.StatelessSession;
import org.thymeleaf.ITemplateEngine;
import org.thymeleaf.context.WebContext;
import org.thymeleaf.web.servlet.JakartaServletWebApplication;

import by.bsuir.hotelwebapp.config.HibernateSession;
import by.bsuir.hotelwebapp.config.ThymeleafConfig;
import by.bsuir.hotelwebapp.dto.response.RoomTypeResponseDTO;
import by.bsuir.hotelwebapp.dto.response.UserResponseDTO;
import by.bsuir.hotelwebapp.service.RoomTypeService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/pricing")
public class PricingServlet extends HttpServlet {
    private JakartaServletWebApplication jakartaWebApp;
    private ITemplateEngine templateEngine;

    @Override
    public void init() throws ServletException {
        jakartaWebApp = ThymeleafConfig.getJakartaServletWebApplication(getServletContext());
        templateEngine = ThymeleafConfig.getTemplateEngine(getServletContext());
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String path = req.getPathInfo();
        
        switch (path) {
            case null:
                doGetRootPath(req, resp);
                break;
            case "/submit":
                doGetSubmitPath(req, resp);
                break;
            default:
                break;
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // TODO Auto-generated method stub
        super.doPost(req, resp);
    }

    private void doGetRootPath(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("text/html;charset=UTF-8");
        WebContext context = new WebContext(jakartaWebApp.buildExchange(req, resp), req.getLocale());

        UserResponseDTO user = (UserResponseDTO) req.getSession().getAttribute("user");
        if (user == null || !user.isAdmin()) {
            resp.sendError(HttpServletResponse.SC_FORBIDDEN, "Access is forbidden");
            return;
        }

        try (StatelessSession hibernateSession = HibernateSession.getStatelessSession()) {
            hibernateSession.beginTransaction();
            
            RoomTypeService roomTypeService = new RoomTypeService(hibernateSession);
            List<RoomTypeResponseDTO> roomTypes = roomTypeService.getAllRoomTypes();
            context.setVariable("roomTypes", roomTypes);

            hibernateSession.getTransaction().commit();
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }

        templateEngine.process("pricing/pricing", context, resp.getWriter());
    }

    private void doGetSubmitPath(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

    }
    
}
