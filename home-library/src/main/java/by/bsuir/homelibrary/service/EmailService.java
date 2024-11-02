package by.bsuir.homelibrary.service;

import java.util.List;
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

public class EmailService {
    //private static final BookDao BOOK_DAO = new BookDao();
    private static final UserDao USER_DAO = new UserDao();
    private static final AdminDao ADMIN_DAO = new AdminDao();

    private static final String HOST = "smpt.mail.ru";
    private static final String PORT = "587";
    private static final String EMAIL_ADDRESS = "bsuir.webtech@mail.ru";
    private static final String PASSWORD = "password";      // TODO: Change to the real password
    
    private final Properties PROPERTIES = new Properties();
    private final Session SESSION;

    public EmailService() {
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

    public void notifyAboutAddedBooks(List<Book> newBooks) {
        List<User> users = USER_DAO.getAllUsers();
        String messageBody = generateAddedBooksMessageBody(newBooks);
        for (var user : users) {
            sendEmail(user.getEmail(), "Changes in the book catalog", messageBody);
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

    private void sendEmail(String emailAddress, String subject, String messageBody) {
        try {
            Message message = new MimeMessage(SESSION);
                message.setFrom(new InternetAddress(EMAIL_ADDRESS));
                message.setRecipient(Message.RecipientType.TO, new InternetAddress(emailAddress));
                message.setSubject(subject);
                message.setText(messageBody);

                Transport.send(message);
        }
        catch (MessagingException e) {
            e.printStackTrace();
        }
    }
}
