package com.scalesec.vulnado;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class LinkListerTests {

    // Helper method to mock Jsoup Document
    private Document mockDocumentWithLinks(List<String> links) {
        Document mockDocument = mock(Document.class);
        Elements mockElements = new Elements();
        for (String link : links) {
            Element mockElement = mock(Element.class);
            when(mockElement.absUrl("href")).thenReturn(link);
            mockElements.add(mockElement);
        }
        when(mockDocument.select("a")).thenReturn(mockElements);
        return mockDocument;
    }

    @Test
    public void getLinks_ShouldReturnLinks() throws IOException {
        // Arrange
        List<String> expectedLinks = List.of("http://example.com", "http://test.com");
        Document mockDocument = mockDocumentWithLinks(expectedLinks);
        Jsoup jsoupMock = mock(Jsoup.class);
        Mockito.when(Jsoup.connect("http://mockurl.com").get()).thenReturn(mockDocument);

        // Act
        List<String> actualLinks = LinkLister.getLinks("http://mockurl.com");

        // Assert
        assertEquals("The returned links should match the expected links", expectedLinks, actualLinks);
    }

    @Test(expected = IOException.class)
    public void getLinks_ShouldThrowIOException_WhenInvalidUrl() throws IOException {
        // Arrange
        Jsoup jsoupMock = mock(Jsoup.class);
        Mockito.when(Jsoup.connect("http://invalidurl.com").get()).thenThrow(new IOException("Invalid URL"));

        // Act
        LinkLister.getLinks("http://invalidurl.com");
    }

    @Test
    public void getLinksV2_ShouldReturnLinks_WhenValidUrl() throws BadRequest {
        // Arrange
        List<String> expectedLinks = List.of("http://example.com", "http://test.com");
        Document mockDocument = mockDocumentWithLinks(expectedLinks);
        Jsoup jsoupMock = mock(Jsoup.class);
        Mockito.when(Jsoup.connect("http://mockurl.com").get()).thenReturn(mockDocument);

        // Act
        List<String> actualLinks = LinkLister.getLinksV2("http://mockurl.com");

        // Assert
        assertEquals("The returned links should match the expected links", expectedLinks, actualLinks);
    }

    @Test(expected = BadRequest.class)
    public void getLinksV2_ShouldThrowBadRequest_WhenPrivateIP() throws BadRequest {
        // Act
        LinkLister.getLinksV2("http://192.168.1.1");
    }

    @Test(expected = BadRequest.class)
    public void getLinksV2_ShouldThrowBadRequest_WhenInvalidUrl() throws BadRequest {
        // Act
        LinkLister.getLinksV2("invalid-url");
    }

    @Test(expected = BadRequest.class)
    public void getLinksV2_ShouldThrowBadRequest_WhenMalformedUrl() throws BadRequest {
        // Act
        LinkLister.getLinksV2("http://172.16.0.1");
    }
}
