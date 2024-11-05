package by.bsuir.homelibrary.service;

import java.util.List;
import java.util.Map;
import java.util.Properties;

import by.bsuir.homelibrary.dao.AdminDao;
import by.bsuir.homelibrary.dao.UserDao;
import by.bsuir.homelibrary.entity.Book;
import by.bsuir.homelibrary.entity.User;

import jakarta.mail.PasswordAuthentication;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.Authenticator;
import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;;

/**
 * The {@code EmailService} class provides functionalities for sending emails,
 * such as notifying users about new books added to the catalog or proposing books to admins.
 * This class follows the singleton pattern, ensuring a single instance.
 */
public class EmailService {
    private static EmailService instance = null;

    private static final UserDao USER_DAO = UserDao.getInstance();
    private static final AdminDao ADMIN_DAO = AdminDao.getInstance();

    private static final Map<String, String> ENV = System.getenv();
    
    private static final String HOST = System.getenv("SMTP_ADDR"); 
    private static final String PORT = "587";
    private static final String EMAIL_ADDRESS = ENV.get("EMAIL_ADDR");
    private static final String PASSWORD = ENV.get("EMAIL_PWD");
    
    private final Properties PROPERTIES = new Properties();
    private final Session SESSION;

    private EmailService() {
        PROPERTIES.put("mail.smtp.auth", "true");
        PROPERTIES.put("mail.smtp.starttls.enable", "true");
        PROPERTIES.put("mail.smtp.host", HOST);
        PROPERTIES.put("mail.smtp.port", PORT);

        SESSION = Session.getInstance(PROPERTIES, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(EMAIL_ADDRESS, PASSWORD);
            }
        });
    }

    public static EmailService getInstance() {
        if (instance == null) {
            instance = new EmailService();
        }

        return instance;
    }

    /**
     * Sends an email to all admins with a proposed list of books to add to the catalog.
     *
     * @param newBooks the list of books proposed for the catalog
     */    
    public void proposeBooksToCatalog(List<Book> newBooks) {
        List<User> admins = ADMIN_DAO.getAllAdmins();
        String messageBody = generateProposedBooksMessageBody(newBooks);                         
        for (var admin : admins) {
            sendEmail(admin.getEmail(), "Proposed changes to the book catalog", messageBody);
        }
    }
 
    private String generateProposedBooksMessageBody(List<Book> newBooks) {
        StringBuilder messageBody = new StringBuilder("Proposed books for the catalog:\n");
        for (var newBook : newBooks) {
            messageBody.append(newBook)
                       .append("\n")
                       .append("------------------------------------------\n");
        }

        return messageBody.toString();
    }

    /**
     * Notifies all users about newly added books in the catalog.
     *
     * @param newBooks the list of newly added books
     */    
    public void notifyAboutAddedBooks(List<Book> newBooks) {
        List<User> users = USER_DAO.getAllUsers();
        String messageBody = generateAddedBooksMessageBody(newBooks);
        for (var user : users) {
            System.out.println(user.getEmail() + ":");
            if (sendEmail(user.getEmail(), "Changes in the book catalog", messageBody)) {
                System.out.println("Success");
            }
        }
    }

    private String generateAddedBooksMessageBody(List<Book> newBooks) {
        StringBuilder messageBody = new StringBuilder("New books added to the catalog:\n");
        for (var newBook : newBooks) {
            messageBody.append(newBook)
                       .append("\n")
                       .append("------------------------------------------\n");
        }

        return messageBody.toString();
    }

    private boolean sendEmail(String emailAddress, String subject, String messageBody) {
        try {
            Message message = new MimeMessage(SESSION);
                message.setFrom(new InternetAddress(EMAIL_ADDRESS));
                message.setRecipient(Message.RecipientType.TO, new InternetAddress(emailAddress));
                message.setSubject(subject);
                message.setText(messageBody);

                Transport.send(message);
                return true;
        }
        catch (MessagingException e) {
            System.out.println(e.getMessage());
            return false;
        }
    }
}
