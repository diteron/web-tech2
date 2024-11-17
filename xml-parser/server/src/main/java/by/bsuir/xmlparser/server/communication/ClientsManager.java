package by.bsuir.xmlparser.server.communication;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

import by.bsuir.xmlparser.common.ParserType;
import by.bsuir.xmlparser.common.entity.BooksList;
import by.bsuir.xmlparser.server.libparser.LibraryParser;
import by.bsuir.xmlparser.server.libparser.SaxLibraryParser;
import by.bsuir.xmlparser.server.libparser.StaxLibraryParser;

public class ClientsManager {
    private final static int PORT = 24110;
    private final static String FILE_PATH = System.getProperty("user.dir") + "/xml-files/library.xml";

    public void startProcessing() {
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("Server is started on port " + PORT);
            while (true) {
                try (Socket clientSocket = serverSocket.accept();
                        ObjectOutputStream out = new ObjectOutputStream(clientSocket.getOutputStream());
                        ObjectInputStream in = new ObjectInputStream(clientSocket.getInputStream())) {
                    System.out.println("New client is connected");

                    ParserType parserType = (ParserType) in.readObject();
                    BooksList booksList = parseBooks(parserType);
                    out.writeObject(booksList);
                    
                    System.out.println("Books is sent to the client");
                } 
                catch (IOException | ClassNotFoundException e) {
                    System.out.println("Failed to process a client:");
                    e.printStackTrace();
                }
            }
        }
        catch (IOException e) {
            System.out.printf("Failed to start the server on the port %d:\n", PORT);
            e.printStackTrace();
        }
    }

    private BooksList parseBooks(ParserType parserType) {
        BooksList booksList = null;
        LibraryParser libraryParser;

        switch (parserType) {
            case SAX:
                libraryParser = new SaxLibraryParser(FILE_PATH);
                booksList = libraryParser.parse();
                break;
            case STAX:
                libraryParser = new StaxLibraryParser(FILE_PATH);
                booksList = libraryParser.parse();
                break;
            case DOM:
                break;
            default:
                break;
        }

        return booksList;
    }
}
