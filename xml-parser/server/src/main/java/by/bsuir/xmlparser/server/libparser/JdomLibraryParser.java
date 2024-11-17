package by.bsuir.xmlparser.server.libparser;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jdom2.input.SAXBuilder;
import org.jdom2.Element;
import org.jdom2.Document;
import org.jdom2.JDOMException;

import by.bsuir.xmlparser.common.entity.Author;
import by.bsuir.xmlparser.common.entity.Book;
import by.bsuir.xmlparser.common.entity.BooksList;
import by.bsuir.xmlparser.common.entity.Publisher;

public class JdomLibraryParser implements LibraryParser {
    private final String FILE_NAME;

    private final Map<String, Author> AUTHORS_MAP = new HashMap<>();
    private final Map<String, Publisher> PUBLISHERS_MAP = new HashMap<>();
    private final List<Book> BOOKS = new ArrayList<>();

    private final SAXBuilder SAX_BUILDER = new SAXBuilder();
    
    public JdomLibraryParser(String fileName) {
        FILE_NAME = fileName;
    }

    @Override
    public BooksList parse() {
        try {
            Document document = SAX_BUILDER.build(FILE_NAME);
            Element rootElement = document.getRootElement();
            parseAuthors(rootElement);
            parsePublishers(rootElement);
            parseBooks(rootElement);
        }
        catch (JDOMException e) {
            System.out.println("Error processing xml in file '" + FILE_NAME + "':");
            e.printStackTrace();
        }
        catch (IOException e) {
            System.out.println("Error when reading file '" + FILE_NAME + "':");
            e.printStackTrace();
        }

        return new BooksList(BOOKS);
    }

    private void parseAuthors(Element rootElement) {
        List<Element> authorElements = rootElement.getChild("authors").getChildren("author");
        for (Element authorElement : authorElements) {
            String id = authorElement.getAttributeValue("id");
            String name = authorElement.getChildText("name");
            String birthYear = authorElement.getChildText("birthYear");

            AUTHORS_MAP.put(id, new Author(name, birthYear));
        }
    }

    private void parsePublishers(Element rootElement) {
        List<Element> publisherElements = rootElement.getChild("publishers").getChildren("publisher");
        for (Element publisherElement : publisherElements) {
            String id = publisherElement.getAttributeValue("id");
            String name = publisherElement.getChildText("name");
            
            Element address = publisherElement.getChild("address");
            String city = address.getChildText("city");
            String country = address.getChildText("country");

            PUBLISHERS_MAP.put(id, new Publisher(name, new Publisher.Address(city, country)));
        }
    }

    private void parseBooks(Element rootElement) {
        List<Element> bookElements = rootElement.getChild("books").getChildren("book");
        for (Element bookElement : bookElements) {
            String authorId = bookElement.getAttributeValue("authorId");
            String publisherId = bookElement.getAttributeValue("publisherId");
            
            String title = bookElement.getChildText("title");
            int year = Integer.parseInt(bookElement.getChildText("year"));
            String genre = bookElement.getChildText("genre");

            BOOKS.add(new Book(title, year, genre,
                    AUTHORS_MAP.get(authorId), PUBLISHERS_MAP.get(publisherId)));
        }
    }
}
