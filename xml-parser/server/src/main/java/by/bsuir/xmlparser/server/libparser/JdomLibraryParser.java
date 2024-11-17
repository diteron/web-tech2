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
    private final String fileName;

    private final Map<String, Author> authorsMap = new HashMap<>();
    private final Map<String, Publisher> publishersMap = new HashMap<>();
    private final List<Book> books = new ArrayList<>();

    private final SAXBuilder saxBuilder = new SAXBuilder();
    
    public JdomLibraryParser(String fileName) {
        this.fileName = fileName;
    }

    @Override
    public BooksList parse() {
        try {
            Document document = saxBuilder.build(fileName);
            Element rootElement = document.getRootElement();
            parseAuthors(rootElement);
            parsePublishers(rootElement);
            parseBooks(rootElement);
        }
        catch (JDOMException e) {
            System.out.println("Error processing xml in file '" + fileName + "':");
            e.printStackTrace();
        }
        catch (IOException e) {
            System.out.println("Error when reading file '" + fileName + "':");
            e.printStackTrace();
        }

        return new BooksList(books);
    }

    private void parseAuthors(Element rootElement) {
        List<Element> authorElements = rootElement.getChild("authors").getChildren("author");
        for (Element authorElement : authorElements) {
            String id = authorElement.getAttributeValue("id");
            String name = authorElement.getChildText("name");
            String birthYear = authorElement.getChildText("birthYear");

            authorsMap.put(id, new Author(name, birthYear));
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

            publishersMap.put(id, new Publisher(name, new Publisher.Address(city, country)));
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

            books.add(new Book(title, year, genre,
                    authorsMap.get(authorId), publishersMap.get(publisherId)));
        }
    }
}
