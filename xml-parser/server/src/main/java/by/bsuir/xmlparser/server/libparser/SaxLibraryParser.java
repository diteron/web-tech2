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
    private final List<Book> BOOKS = new ArrayList<>();

    private final String FILE_NAME;
    private final SAXParserFactory SAX_PARSER_FACTORY = SAXParserFactory.newInstance();
    private final SAXParser SAX_PARSER;
    private final LibraryHandler LIBRARY_HANDLER = new LibraryHandler();
    
    public SaxLibraryParser(String fileName) {
        FILE_NAME = fileName;
        SAX_PARSER_FACTORY.setNamespaceAware(true);
        
        try {
            SAX_PARSER = SAX_PARSER_FACTORY.newSAXParser();
        }
        catch (ParserConfigurationException | SAXException e) {
            throw new RuntimeException("Failed to create SAX parser", e);
        }
    }

    @Override
    public BooksList parse()  {
        try {
            SAX_PARSER.parse(FILE_NAME, LIBRARY_HANDLER);
        }
        catch (SAXException e) {
            System.out.println("Error processing xml in file '" + FILE_NAME + "':");
            e.printStackTrace();
        }
        catch (IOException e) {
            System.out.println("Error when reading file '" + FILE_NAME + "':");
            e.printStackTrace();
        }
        
        return new BooksList(BOOKS);
    }


    private class LibraryHandler extends DefaultHandler {
        private String currentEntity;
        private String currentElement;
        private String currentEntityId;

        private Author currentAuthor;
        private Publisher currentPublisher;
        private Book currentBook;

        private final Map<String, Author> AUTHORS_MAP = new HashMap<>();
        private final Map<String, Publisher> PUBLISHERS_MAP = new HashMap<>();

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
                            AUTHORS_MAP.get(authorId), PUBLISHERS_MAP.get(publisherId));
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
                    AUTHORS_MAP.put(currentEntityId, currentAuthor);
                    break;
                case "publisher":
                    PUBLISHERS_MAP.put(currentEntityId, currentPublisher);
                    break;
                case "book":
                    BOOKS.add(currentBook);
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
