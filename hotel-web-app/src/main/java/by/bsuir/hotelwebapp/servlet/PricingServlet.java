package by.bsuir.hotelwebapp.servlet;

import java.io.IOException;
import java.util.List;

import org.hibernate.StatelessSession;
import org.thymeleaf.ITemplateEngine;
import org.thymeleaf.context.WebContext;
import org.thymeleaf.web.servlet.JakartaServletWebApplication;

import by.bsuir.hotelwebapp.config.HibernateSession;
import by.bsuir.hotelwebapp.config.ThymeleafConfig;
import by.bsuir.hotelwebapp.dto.request.RoomTypeRequestDTO;
import by.bsuir.hotelwebapp.dto.response.RoomTypeResponseDTO;
import by.bsuir.hotelwebapp.dto.response.UserResponseDTO;
import by.bsuir.hotelwebapp.service.RoomTypeService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet({"/pricing/*"})
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
        UserResponseDTO user = (UserResponseDTO) req.getSession().getAttribute("user");
        if (user == null || !user.isAdmin()) {
            resp.sendError(HttpServletResponse.SC_FORBIDDEN, "Access is forbidden");
            return;
        }
        
        String path = req.getPathInfo();
        if (path == null || path.equals("/")) {
            doGetRootPath(req, resp);
        }
        else if (path.equals("/submit")) {
            doGetSubmitPath(req, resp);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        UserResponseDTO user = (UserResponseDTO) req.getSession().getAttribute("user");
        if (user == null || !user.isAdmin()) {
            resp.sendError(HttpServletResponse.SC_FORBIDDEN, "Access is forbidden");
            return;
        }

        String path = req.getPathInfo();
        if (path == null || path.equals("/")) {
            return;
        }
        else if (path.equals("/submit")) {
            doPostSubmitPath(req, resp);
        }
    }

    private void doGetRootPath(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("text/html;charset=UTF-8");
        WebContext context = new WebContext(jakartaWebApp.buildExchange(req, resp), req.getLocale());

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
        resp.setContentType("text/html;charset=UTF-8");
        WebContext context = new WebContext(jakartaWebApp.buildExchange(req, resp), req.getLocale());

        Long id = Long.parseLong(req.getParameter("id"));
        String name = req.getParameter("name");
        Integer price = Integer.parseInt(req.getParameter("price"));
        Integer freeRooms = Integer.parseInt(req.getParameter("freeRooms"));
        RoomTypeRequestDTO roomType = new RoomTypeRequestDTO(id, name, price, freeRooms);

        context.setVariable("roomType", roomType);

        templateEngine.process("pricing/submit", context, resp.getWriter());
    }
    
    private void doPostSubmitPath(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("text/html;charset=UTF-8");

        try (StatelessSession hibernateSession = HibernateSession.getStatelessSession()) {
            Long id = Long.parseLong(req.getParameter("id"));
            String name = req.getParameter("name");
            Integer price = Integer.parseInt(req.getParameter("price"));
            Integer freeRooms = Integer.parseInt(req.getParameter("freeRooms"));
            RoomTypeRequestDTO roomType = new RoomTypeRequestDTO(id, name, price, freeRooms);
            
            hibernateSession.beginTransaction();
            RoomTypeService roomTypeService = new RoomTypeService(hibernateSession);
            roomTypeService.updateRoomType(roomType);
            hibernateSession.getTransaction().commit();
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }

        resp.sendRedirect(req.getContextPath() + "/pricing");
    }
}
