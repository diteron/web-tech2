package by.bsuir.xmlparser.client.menu;

import java.util.Scanner;

public class ClientMenu {
    private boolean exit = false;
    private static final Scanner scanner = new Scanner(System.in);

    private static final ParseMenu parseMenu = new ParseMenu(scanner);

    public void start() {
        while (!exit) {
            displayMenu();
            int choice = scanner.nextInt();
            scanner.nextLine();
            handleChoice(choice);
        }
        scanner.close();
    }

    private void displayMenu() {
        System.out.println("\n============ Client Menu ============");
        System.out.println("1. Parse file");
        System.out.println("0. Exit");
        System.out.print("Choose an option: ");
    }

    private void handleChoice(int choice) {
        switch (choice) {
            case 1:
                handleParseFile();
                break;
            case 0:
                System.out.println("Exiting the program...");
                exit = true;
                break;
            default:
                System.out.println("Invalid choice. Please try again.");
        }
    }

    private void handleParseFile() {
        parseMenu.start();
    }
}
