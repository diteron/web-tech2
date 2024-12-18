package by.bsuir.xmlparser.server.libparser;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import by.bsuir.xmlparser.common.entity.Author;
import by.bsuir.xmlparser.common.entity.Book;
import by.bsuir.xmlparser.common.entity.BooksList;
import by.bsuir.xmlparser.common.entity.Publisher;

public class SaxLibraryParser implements LibraryParser {
    private final List<Book> books = new ArrayList<>();

    private final String fileName;
    private final SAXParserFactory saxParserFactory = SAXParserFactory.newInstance();
    private final SAXParser saxParser;
    private final LibraryHandler libraryHandler = new LibraryHandler();
    
    public SaxLibraryParser(String fileName) {
        this.fileName = fileName;
        saxParserFactory.setNamespaceAware(true);
        
        try {
            saxParser = saxParserFactory.newSAXParser();
        }
        catch (ParserConfigurationException | SAXException e) {
            throw new RuntimeException("Failed to create SAX parser", e);
        }
    }

    @Override
    public BooksList parse()  {
        try {
            saxParser.parse(fileName, libraryHandler);
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


    private class LibraryHandler extends DefaultHandler {
        private String currentEntity;
        private String currentElement;
        private String currentEntityId;

        private Author currentAuthor;
        private Publisher currentPublisher;
        private Book currentBook;

        private final Map<String, Author> authorsMap = new HashMap<>();
        private final Map<String, Publisher> publishersMap = new HashMap<>();

        @Override
        public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
            currentElement = qName;

            switch (currentElement) {
                case "author": 
                    currentEntity = "author";
                    currentEntityId = attributes.getValue("id");
                    currentAuthor = new Author(null, null);
                    break;
                case "publisher": 
                    currentEntity = "publisher";
                    currentEntityId = attributes.getValue("id");
                    currentPublisher = new Publisher(null, new Publisher.Address(null, null));
                    break;
                case "book": 
                    currentEntity = "book";
                    String authorId = attributes.getValue("authorId");
                    String publisherId = attributes.getValue("publisherId");
                    currentBook = new Book(null, null, null,
                            authorsMap.get(authorId), publishersMap.get(publisherId));
                    break;
            }
        }

        @Override
        public void characters(char[] ch, int start, int length) throws SAXException {
            String text = new String(ch, start, length).trim();
            
            if (!text.isEmpty()) {
                switch (currentEntity) {
                    case "author":
                        processAuthorElement(text);
                        break;
                    case "publisher":
                        processPublisherElement(text);
                        break;
                    case "book":
                        processBookElement(text);
                        break;        
                    default:
                        break;
                }
            }
        }

        @Override
        public void endElement(String uri, String localName, String qName) throws SAXException {
            switch (qName) {
                case "author":
                    authorsMap.put(currentEntityId, currentAuthor);
                    break;
                case "publisher":
                    publishersMap.put(currentEntityId, currentPublisher);
                    break;
                case "book":
                    books.add(currentBook);
                    break;        
                default:
                    break;
            }
        }

        private void processAuthorElement(String elementText) {
            if (currentAuthor == null) {
                return;
            }
            
            switch (currentElement) {
                case "name":
                    currentAuthor.setName(elementText);
                    break;
                case "birthYear":
                    currentAuthor.setBirthYear(elementText);
                    break;
                default:
                    break;
            }
        }

        private void processPublisherElement(String elementText) {
            if (currentPublisher == null) {
                return;
            }
            
            switch (currentElement) {
                case "name":
                    currentPublisher.setName(elementText);
                    break;
                case "city":
                    currentPublisher.getAddress().setCity(elementText);
                    break;
                case "country":
                    currentPublisher.getAddress().setCountry(elementText);
                    break;
                default:
                    break;
            }
        }

        private void processBookElement(String elementText) {
            if (currentBook == null) {
                return;
            }

            switch (currentElement) {
                case "title":
                    currentBook.setTitle(elementText);
                    break;
                case "year":
                    currentBook.setYear(Integer.parseInt(elementText));
                    break;
                case "genre":
                    currentBook.setGenre(elementText);
                    break;
                default:
                    break;
            }
        }
    }
}
