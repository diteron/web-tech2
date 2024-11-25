package by.bsuir.hotelwebapp.servlet;

import java.util.Timer;

import by.bsuir.hotelwebapp.config.HibernateSession;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;

@WebListener
public class AppServletContextListener implements ServletContextListener {
    private Timer timer;

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        timer = new Timer(true);
        timer.scheduleAtFixedRate(new BookingsTimer(), 0, 30000);   // 30 sec for testing purposes
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        timer.cancel();
        HibernateSession.shutdown();
    }
}
