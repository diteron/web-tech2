package by.bsuir.xmlparser.client.menu;

import java.io.IOException;
import java.util.Scanner;

import by.bsuir.xmlparser.common.ParserType;
import by.bsuir.xmlparser.common.entity.BooksList;
import by.bsuir.xmlparser.client.communication.XmlParserConnector;

public class ParseMenu {
    private boolean exit = false;
    private final Scanner SCANNER;

    private static final String HOST = "localhost";
    private static final int PORT = 24110;

    public ParseMenu(Scanner scanner) {
        SCANNER = scanner;
    }

    public void start() {
        exit = false;

        while (!exit) {
            displayMenu();
            int choice = SCANNER.nextInt();
            SCANNER.nextLine();
            handleChoice(choice);
        }
    }

    private void displayMenu() {
        System.out.println("\n============ Select Parser ============");
        System.out.println("1. SAX");
        System.out.println("2. StAX");
        System.out.println("3. DOM");
        System.out.println("0. Exit");
        System.out.print("Choose an option: ");
    }

    private void handleChoice(int choice) {
        switch (choice) {
            case 1:
                handleParse(ParserType.SAX);
                break;
            case 2:
                handleParse(ParserType.STAX);
                break;
            case 3:
                //handleParse(ParserType.DOM);
                break;            
            case 0:
                System.out.println("Returning to the client menu...");
                exit = true;
                break;
            default:
                System.out.println("Invalid choice. Please try again.");
        }
    }

    private void handleParse(ParserType parserType) {
        try (XmlParserConnector parserConnector = new XmlParserConnector(HOST, PORT)) {
            BooksList booksList = parserConnector.parseBooks(parserType);
            System.out.println("\nBooks in the library:");
            booksList.printBooks();
        } 
        catch (ClassNotFoundException | IOException e) {
            System.out.printf("Failed to read books from the XML parser server on '%s':\n",
                    HOST + ":" + PORT);
            e.printStackTrace();
        }
    }
}
