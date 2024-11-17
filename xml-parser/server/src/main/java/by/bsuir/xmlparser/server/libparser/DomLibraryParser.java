package by.bsuir.xmlparser.server.libparser;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import by.bsuir.xmlparser.common.entity.Author;
import by.bsuir.xmlparser.common.entity.Book;
import by.bsuir.xmlparser.common.entity.BooksList;
import by.bsuir.xmlparser.common.entity.Publisher;

public class DomLibraryParser implements LibraryParser {
    private final String fileName;

    private final Map<String, Author> authorsMap = new HashMap<>();
    private final Map<String, Publisher> publishersMap = new HashMap<>();
    private final List<Book> books = new ArrayList<>();

    private final DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
    private final DocumentBuilder dockBuilder;

    
    public DomLibraryParser(String fileName) {
        this.fileName = fileName;

        try {
            dockBuilder = docBuilderFactory.newDocumentBuilder();
        }
        catch (ParserConfigurationException e) {
            throw new RuntimeException("Failed to create DOM parser", e);
        }
    }

    @Override
    public BooksList parse() {
        try {
            Document document = dockBuilder.parse(fileName);
            parseAuthors(document);
            parsePublishers(document);
            parseBooks(document);
        }
        catch (SAXException e) {
            System.out.println("Error processing xml in file '" + fileName + "':");
            e.printStackTrace();
        }
        catch (IOException e) {
            System.out.println("Error when reading file '" + fileName + "':");
            e.printStackTrace();
        }

        return new BooksList(books);
    }

    private void parseAuthors(Document document) {
        NodeList authorNodes = document.getElementsByTagName("author");
        for (int i = 0; i < authorNodes.getLength(); ++i) {
            Element authorElement = (Element) authorNodes.item(i);
            String id = authorElement.getAttribute("id");
            String name = getElementTextContent(authorElement, "name");
            String birthYear = getElementTextContent(authorElement, "birthYear");

            authorsMap.put(id, new Author(name, birthYear));
        }
    }

    private void parsePublishers(Document document) {
        NodeList publisherNodes = document.getElementsByTagName("publisher");
        for (int i = 0; i < publisherNodes.getLength(); ++i) {
            Element publisherElement = (Element) publisherNodes.item(i);
            String id = publisherElement.getAttribute("id");
            String name = getElementTextContent(publisherElement, "name");
            String city = getElementTextContent(publisherElement, "city");
            String country = getElementTextContent(publisherElement, "country");
            
            publishersMap.put(id, new Publisher(name, new Publisher.Address(city, country)));
        }
    }

    private void parseBooks(Document document) {
        NodeList bookNodes = document.getElementsByTagName("book");
        for (int i = 0; i < bookNodes.getLength(); ++i) {
            Element bookElement = (Element) bookNodes.item(i);
            String authorId = bookElement.getAttribute("authorId");
            String publisherId = bookElement.getAttribute("publisherId");

            String title = getElementTextContent(bookElement, "title");
            Integer year = Integer.parseInt(getElementTextContent(bookElement, "year"));
            String genre = getElementTextContent(bookElement, "genre");
            
            books.add(new Book(title, year, genre,
                    authorsMap.get(authorId), publishersMap.get(publisherId)));
        }
    }

    private String getElementTextContent(Element parent, String tagName) {
        NodeList nodes = parent.getElementsByTagName(tagName);
        return nodes.getLength() > 0 ? nodes.item(0).getTextContent().trim() : null;
    }
}
