package by.bsuir.homelibrary.menu;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import by.bsuir.homelibrary.entity.Book;

/**
 * Represents a menu for administrators in the application, providing options to manage the book catalog.
 * The admin menu allows users to view the catalog, find books, add new books, update existing books, and delete books.
 */
public class AdminMenu extends UserMenu {
    /**
     * Constructs an AdminMenu instance with the specified scanner.
     *
     * @param scanner the Scanner object used for user input
     */
    public AdminMenu(Scanner scanner) {
        super(scanner);
    }

    @Override
    protected void displayMenu() {
        System.out.println("\n============ Admin Menu (Log in as admin: " + userLogin +  ") ============");
        System.out.println("1. Show catalog");
        System.out.println("2. Find books");
        System.out.println("3. Add books");
        System.out.println("4. Update book");
        System.out.println("5. Delete book");
        System.out.println("0. Log out");
        System.out.print("Choose an option: ");
    }

    @Override
    protected void handleChoice(int choice) {
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
    
    private void handleDeleteBook() {
        System.out.println("\nEnter book to delete:");
        Book book = createBook();

        BOOK_SERVICE.deleteBook(book);
    }
}
