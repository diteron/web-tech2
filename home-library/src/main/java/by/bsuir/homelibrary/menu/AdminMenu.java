package by.bsuir.homelibrary.menu;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import by.bsuir.homelibrary.entity.Book;
import by.bsuir.homelibrary.service.BookService;
import by.bsuir.homelibrary.service.EmailService;

public class AdminMenu {
    private boolean exit = false;
    private final Scanner SCANNER;
    private String adminLogin;

    private final static BookService BOOK_SERVICE = BookService.getInstance();
    private final static EmailService EMAIL_SERVICE = EmailService.getInstance();

    public AdminMenu(Scanner scanner) {
        SCANNER = scanner;
    }
    public void start(String login) {
        exit = false;
        adminLogin = login;

        while (!exit) {
            displayMenu();
            int choice = SCANNER.nextInt();
            SCANNER.nextLine();
            handleChoice(choice);
        }
    }

    private void displayMenu() {
        System.out.println("\n============ Admin Menu (Log in as admin: " + adminLogin +  ") ============");
        System.out.println("1. Show catalog");
        System.out.println("2. Find books");
        System.out.println("3. Add books");
        System.out.println("4. Update book");
        System.out.println("5. Delete book");
        System.out.println("0. Log out");
        System.out.print("Choose an option: ");
    }

    private void handleChoice(int choice) {
        switch (choice) {
            case 1:
                handleShowCatalog();
                break;
            case 2:
                handleFindBooks();
                break;
            case 3:
                handleAddBooks();
                break;
            case 4:
                handleUpdateBook();
                break;
            case 5:
                handleDeleteBook();
                break;            
            case 0:
                System.out.println("Logging out...");
                exit = true;
                break;
            default:
                System.out.println("Invalid choice. Please try again.");
        }
    }

    private void handleShowCatalog() {
        List<Book> books = BOOK_SERVICE.getAllBooks();

        System.out.println("\nAll books in the catalog:");
        printBooks(books);
    }

    private void printBooks(List<Book> books) {
        for (var book : books) {
            System.out.println(book);
            System.out.println("---------------------------------------------");
        }
    }

    private void handleFindBooks() {
        System.out.println("\nEnter fields to find book. (To exclude a field from search, leave it empty):");
        Book bookWithSearchFields = createBookWithPossibleNullFields();

        List<Book> books = BOOK_SERVICE.findBooksByFields(bookWithSearchFields);
        System.out.println("\nBooks found in the catalog:");
        printBooks(books);
    }

    private Book.Type enteredStringToBookType(String type) {
        if (type.isEmpty() || (!type.equals("book") && !type.equals("e-book"))) {
            return null;
        }

        return Book.Type.fromString(type);
    }

    private void handleAddBooks() {
        boolean isAddingOneMoreBook = true;
        List<Book> books = new ArrayList<>();

        System.out.println("\nEnter new books for the catalog:");
        while (isAddingOneMoreBook) {
            Book book = createBook();

            System.out.println("Add one more book? (y/n):");
            String input = SCANNER.nextLine();

            if (input.equals("n")) {
                isAddingOneMoreBook = false;
            }
            else if (!input.equals("y")) {
                System.out.println("Incorrect input. Please try again.");
                continue;
            }

            books.add(book);
        }

        BOOK_SERVICE.addBooks(books);
        System.out.println("Sending notification for users...");
        EMAIL_SERVICE.notifyAboutAddedBooks(books);
    }

    private void handleUpdateBook() {
        System.out.println("\nEnter original book fields:");
        Book originalBook = createBook();

        System.out.println("\nEnter new values for fields. (To keep the value of the original field, leave it empty):");
        Book bookWithFieldsToUpdate = createBookWithPossibleNullFields();

        BOOK_SERVICE.updateBook(originalBook, bookWithFieldsToUpdate);
    }

    private Book createBook() {
        System.out.print("Title: ");
        String title = SCANNER.nextLine();

        System.out.print("Author: ");
        String author = SCANNER.nextLine();

        System.out.print("Year of publication: ");
        String yearOfPublication = SCANNER.nextLine();

        System.out.print("Book type (book/e-book): ");
        String type = SCANNER.nextLine();

        return new Book(title, author, Integer.parseInt(yearOfPublication), Book.Type.fromString(type));
    }

    private Book createBookWithPossibleNullFields() {
        System.out.print("Title: ");
        String title = SCANNER.nextLine();

        System.out.print("Author: ");
        String author = SCANNER.nextLine();

        System.out.print("Year of publication: ");
        String yearOfPublication = SCANNER.nextLine();

        System.out.print("Book type (book/e-book): ");
        String type = SCANNER.nextLine();

        return new Book(!title.isEmpty() ? title : null,
                !author.isEmpty() ? author : null,
                !yearOfPublication.isEmpty()
                        ? Integer.parseInt(yearOfPublication)
                        : null,
                enteredStringToBookType(type));
    }

    private void handleDeleteBook() {
        System.out.println("\nEnter book to delete:");
        Book book = createBook();

        BOOK_SERVICE.deleteBook(book);
    }
}
