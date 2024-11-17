package by.bsuir.xmlparser.server.libparser;

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

import by.bsuir.xmlparser.common.entity.Author;
import by.bsuir.xmlparser.common.entity.Book;
import by.bsuir.xmlparser.common.entity.BooksList;
import by.bsuir.xmlparser.common.entity.Publisher;
import by.bsuir.xmlparser.common.entity.Publisher.Address;

public class StaxLibraryParser implements LibraryParser {
    private final String FILE_NAME;

    private Map<String, Author> authorsMap = new HashMap<>();
    private Map<String, Publisher> publishersMap = new HashMap<>();
    private List<Book> books = new ArrayList<>();

    public StaxLibraryParser(String fileName) {
        FILE_NAME = fileName;
    }

    @Override
    public BooksList parse() {
        try (FileInputStream fileInputStream = new FileInputStream(FILE_NAME)) {
            XMLInputFactory factory = XMLInputFactory.newInstance();
            XMLStreamReader reader = factory.createXMLStreamReader(fileInputStream);

            int event;
            String currentElement = "";
            while (reader.hasNext()) {
                event = reader.next();
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
            System.out.println("File '" + FILE_NAME + "' not found:");
            e.printStackTrace();
        }
        catch (IOException e) {
            System.out.println("Error when reading file '" + FILE_NAME + "':");
            e.printStackTrace();
        }
        catch (XMLStreamException e) {
            System.out.println("Error processing xml in file '" + FILE_NAME + "':");
            e.printStackTrace();
        }

        return new BooksList(books);
    }

    private void parseAuthors(XMLStreamReader reader) throws XMLStreamException {
        String id = "", name = "", birthYear = "";
        
        int event;
        String currentElement = "";
        while (reader.hasNext()) {
            event = reader.next();
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
                currentElement = reader.getLocalName();
                switch (currentElement) {
                    case "author":
                        if (!id.isEmpty() && !name.isEmpty() && !birthYear.isEmpty()) {
                            authorsMap.put(id, new Author(name, birthYear));
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

    private void parsePublishers(XMLStreamReader reader) throws XMLStreamException {
        String id = "", name = "", city = "", country = "";
        
        int event;
        String currentElement = "";
        while (reader.hasNext()) {
            event = reader.next();
            if (event == XMLStreamConstants.START_ELEMENT) {
                currentElement = reader.getLocalName();
                if (currentElement.equals("publisher")) {
                    id = reader.getAttributeValue(null, "id");
                }
            }
            else if (event == XMLStreamConstants.CHARACTERS) {
                String text = reader.getText().trim();
                if (!text.isEmpty()) {
                    switch (currentElement) {
                        case "name" -> name = text;
                        case "city" -> city = text;
                        case "country" -> country = text;
                    }
                }
            }
            else if (event == XMLStreamConstants.END_ELEMENT) {
                currentElement = reader.getLocalName();
                switch (currentElement) {
                    case "publisher":
                        if (!id.isEmpty() && !name.isEmpty() && !city.isEmpty()
                                && !country.isEmpty()) {
                            Address address = new Address(city, country);
                            publishersMap.put(id, new Publisher(name, address));
                        }
                        break;
                    case "publishers":
                        return;
                    default:
                        break;
                }
            }
        }

    }

    private void parseBooks(XMLStreamReader reader) throws XMLStreamException {
        String authorId = "", publisherId = "";
        String title = "", year = "", genre = "";

        int event;
        String currentElement = "";
        while (reader.hasNext()) {
            event = reader.next();
            if (event == XMLStreamConstants.START_ELEMENT) {
                currentElement = reader.getLocalName();
                if (currentElement.equals("book")) {
                    authorId = reader.getAttributeValue(null, "authorId");
                    publisherId = reader.getAttributeValue(null, "publisherId");
                }
            }
            else if (event == XMLStreamConstants.CHARACTERS) {
                String text = reader.getText().trim();
                if (!text.isEmpty()) {
                    switch (currentElement) {
                        case "title" -> title = text;
                        case "year" -> year = text;
                        case "genre" -> genre = text;
                    }
                }
            }
            else if (event == XMLStreamConstants.END_ELEMENT) {
                currentElement = reader.getLocalName();
                switch (currentElement) {
                    case "book":
                        if (!authorId.isEmpty() && !publisherId.isEmpty()
                                && !title.isEmpty() && !year.isEmpty() && !genre.isEmpty()) {
                            books.add(new Book(title, Integer.parseInt(year), genre,
                                    authorsMap.get(authorId), publishersMap.get(publisherId)));
                        }
                        break;
                    case "books":
                        return;
                    default:
                        break;
                }
            }
        }        
    }
}
