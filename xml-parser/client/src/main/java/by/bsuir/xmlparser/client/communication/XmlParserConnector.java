package by.bsuir.xmlparser.client.communication;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import by.bsuir.xmlparser.common.ParserType;
import by.bsuir.xmlparser.common.entity.BooksList;

public class XmlParserConnector implements AutoCloseable {
    private final Socket SOCKET;

    public XmlParserConnector(String host, int port) throws IOException {
        SOCKET = new Socket(host, port);
    }

    public BooksList parseBooks(ParserType parserType) throws IOException, ClassNotFoundException {
        try (ObjectOutputStream out = new ObjectOutputStream(SOCKET.getOutputStream());
                ObjectInputStream in = new ObjectInputStream(SOCKET.getInputStream())) {
            setParserTypeOnServer(out, parserType);
            BooksList books = recieveBooksFromServer(in);
            return books;
        }
    }

    private void setParserTypeOnServer(ObjectOutputStream out, ParserType parserType) throws IOException {
        out.writeObject(parserType);
    }

    private BooksList recieveBooksFromServer(ObjectInputStream in) throws IOException, ClassNotFoundException {
        return (BooksList) in.readObject();
    }

    @Override
    public void close() throws IOException {
        if (SOCKET != null && !SOCKET.isClosed()) {
            SOCKET.close();
        }
    }
}