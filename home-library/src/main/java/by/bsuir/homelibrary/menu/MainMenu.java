package by.bsuir.homelibrary.menu;

import java.util.Scanner;

import by.bsuir.homelibrary.service.AdminService;
import by.bsuir.homelibrary.service.UserService;

/**
 * Provides the main interactive menu for user and admin login operations.
 * <p>
 * The {@code MainMenu} class allows users to log in, log in as an administrator, 
 * or create a new user account. It serves as the entry point for the application.
 * </p>
 */
public class MainMenu {
    private boolean exit = false;
    private static final Scanner SCANNER = new Scanner(System.in);

    private static final UserService USER_SERVICE = UserService.getInstance();
    private static final AdminService ADMIN_SERVICE = AdminService.getInstance();

    private static final UserMenu USER_MENU = new UserMenu(SCANNER);
    private static final AdminMenu ADMIN_MENU = new AdminMenu(SCANNER);

    /**
     * Starts the main menu session, displaying options until the user decides to exit.
     */
    public void start() {
        while (!exit) {
            displayMenu();
            int choice = SCANNER.nextInt();
            SCANNER.nextLine();
            handleChoice(choice);
        }
        SCANNER.close();
    }

    private void displayMenu() {
        System.out.println("\n============ Main Menu ============");
        System.out.println("1. Log in");
        System.out.println("2. Log in as admin");
        System.out.println("3. Create user account");
        System.out.println("0. Exit");
        System.out.print("Choose an option: ");
    }

    private void handleChoice(int choice) {
        switch (choice) {
            case 1:
                handleLogin();
                break;
            case 2:
                handleLoginAsAdmin();
                break;
            case 3:
                handleCreateAccount();
                break;
            case 0:
                System.out.println("Exiting the program...");
                exit = true;
                break;
            default:
                System.out.println("Invalid choice. Please try again.");
        }
    }

    private void handleLogin() {
        System.out.print("Log in: ");
        String login = SCANNER.nextLine();

        System.out.print("Password: ");
        String password = SCANNER.nextLine();

        if (USER_SERVICE.isLogIn(login, password)) {
            USER_MENU.start(login);
        }
        else {
            System.out.println("Invalid login or password. Please try again.");
        }
    }

    private void handleLoginAsAdmin() {
        System.out.print("Login: ");
        String login = SCANNER.nextLine();

        System.out.print("Password: ");
        String password = SCANNER.nextLine();

        if (ADMIN_SERVICE.isLogIn(login, password)) {
            ADMIN_MENU.start(login);
        }
        else {
            System.out.println("Invalid login or password. Please try again.");
        }
    }

    private void handleCreateAccount() {
        System.out.print("New login: ");
        String login = SCANNER.nextLine();

        System.out.print("New password: ");
        String password = SCANNER.nextLine();

        System.out.print("New emain: ");
        String email = SCANNER.nextLine();

        if (USER_SERVICE.addUser(login, password, email)) {
            System.out.println("New user created successfully.");    
        }
        else {
            System.out.println("Failed to create new user. Please try again.");    
        }
    }
}
