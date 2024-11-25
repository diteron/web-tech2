package by.bsuir.hotelwebapp.servlet;

import java.io.IOException;
import java.time.LocalDateTime;

import org.hibernate.StatelessSession;
import org.thymeleaf.ITemplateEngine;
import org.thymeleaf.context.WebContext;
import org.thymeleaf.web.servlet.JakartaServletWebApplication;

import by.bsuir.hotelwebapp.config.HibernateSession;
import by.bsuir.hotelwebapp.config.ThymeleafConfig;
import by.bsuir.hotelwebapp.dto.request.BookingRequestDTO;
import by.bsuir.hotelwebapp.dto.request.RoomTypeRequestDTO;
import by.bsuir.hotelwebapp.dto.response.UserResponseDTO;
import by.bsuir.hotelwebapp.service.BookingService;
import by.bsuir.hotelwebapp.service.RoomTypeService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/booking")
public class BookingServlet extends HttpServlet {
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
        UserResponseDTO user = (UserResponseDTO) req.getSession().getAttribute("user");

        if (user != null) {
            Long id = Long.parseLong(req.getParameter("id"));
            String name = req.getParameter("name");
            Integer price = Integer.parseInt(req.getParameter("price"));
            Integer freeRooms = Integer.parseInt(req.getParameter("freeRooms"));

            context.setVariable("roomType", new RoomTypeRequestDTO(id, name, price, freeRooms));
            context.setVariable("isBookingConfirmed", false);
        }

        templateEngine.process("booking/booking", context, resp.getWriter());
    } 

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("text/html;charset=UTF-8");
        UserResponseDTO user = (UserResponseDTO) req.getSession().getAttribute("user");
        WebContext context = new WebContext(jakartaWebApp.buildExchange(req, resp), req.getLocale());

        try (StatelessSession hibernateSession = HibernateSession.getStatelessSession()) {
            hibernateSession.beginTransaction();

            RoomTypeService roomTypeService = new RoomTypeService(hibernateSession);
            Long roomTypeId = Long.parseLong(req.getParameter("id"));
            roomTypeService.decrementFreeRooms(roomTypeId);

            BookingService bookingService = new BookingService(hibernateSession);
            Integer numberOfNights = Integer.parseInt(req.getParameter("numberOfNights"));
            bookingService.addBooking(new BookingRequestDTO(null, user.id(), roomTypeId, numberOfNights, LocalDateTime.now()));
            
            hibernateSession.getTransaction().commit();
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
        
        context.setVariable("isBookingConfirmed", true);
        templateEngine.process("booking/booking", context, resp.getWriter());
    } 
}
