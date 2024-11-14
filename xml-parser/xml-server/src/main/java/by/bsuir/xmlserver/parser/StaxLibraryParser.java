package by.bsuir.xmlserver.parser;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

import by.bsuir.xmlserver.entity.Author;
import by.bsuir.xmlserver.entity.Book;
import by.bsuir.xmlserver.entity.Publisher;

public class StaxLibraryParser implements LibraryParser {
    private final String FILE_NAME;

    private Map<String, Author> authorsMap = new HashMap<>();
    private Map<String, Publisher> publishersMap = new HashMap<>();
    private List<Book> books = new ArrayList<>();

    StaxLibraryParser(String fileName) {
        FILE_NAME = fileName;
    }

    @Override
    public List<Book> parse() {
        try (FileInputStream fileInputStream = new FileInputStream(FILE_NAME)) {
            XMLInputFactory factory = XMLInputFactory.newInstance();
            XMLStreamReader reader = factory.createXMLStreamReader(fileInputStream);
            
            String currentElement = "";
            while (reader.hasNext()) {
                int event = reader.next();
                if (event == XMLStreamConstants.START_ELEMENT) {
                    currentElement = reader.getLocalName();
                    switch (currentElement) {
                        case "authors"  -> parseAuthors(reader);
                        case "publishers" -> parsePublishers(reader);
                        case "books" -> parseBooks(reader);
                    }
                }
            }
        }
        catch (FileNotFoundException e) {
            throw new RuntimeException("File" + FILE_NAME + " not found", e);
        }
        catch (IOException e) {
            throw new RuntimeException("Error when reading file " + FILE_NAME, e);
        }
        catch (XMLStreamException e) {
            throw new RuntimeException("Error processing xml in file " + FILE_NAME, e);
        }

        return books;
    }

    private void parseAuthors(XMLStreamReader reader) throws XMLStreamException {
        String id = "", name = "", birthYear = "";
        String currentElement = "";

        while (reader.hasNext()) {
            int event = reader.next();
            if (event == XMLStreamConstants.START_ELEMENT) {
                currentElement = reader.getLocalName();
                if (currentElement.equals("author")) {
                    id = reader.getAttributeValue(null, "id");
                }
            }
            else if (event == XMLStreamConstants.CHARACTERS) {
                String text = reader.getText().trim();
                if (!text.isEmpty()) {
                    switch (currentElement) {
                        case "name" -> name = text;
                        case "birthYear" -> birthYear = text;
                    }
                }
            }
            else if (event == XMLStreamConstants.END_ELEMENT) {
                switch (currentElement) {
                    case "author":
                        if (!id.isEmpty() && !name.isEmpty() && !birthYear.isEmpty()) {
                            authorsMap.put(id, new Author(name, Integer.parseInt(birthYear)));
                        }
                        break;
                    case "authors":
                        return;
                    default:
                        break;
                }               
            }
        }
    }

    private void parsePublishers(XMLStreamReader reader) {

    }

    private void parseBooks(XMLStreamReader reader) {

    }

}
