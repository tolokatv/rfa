package media.toloka.rfa.podcast;

import com.rometools.rome.feed.synd.*;
import com.rometools.rome.io.FeedException;
import com.rometools.rome.io.SyndFeedInput;
import com.rometools.rome.io.SyndFeedOutput;
import media.toloka.rfa.podcast.model.PodcastChannel;
import media.toloka.rfa.podcast.model.PodcastItem;
import media.toloka.rfa.podcast.model.PodcastImage;
import media.toloka.rfa.podcast.service.PodcastService;
import media.toloka.rfa.radio.client.service.ClientService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.w3c.dom.CDATASection;
import org.w3c.dom.Comment;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.helpers.DefaultHandler;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.stream.XMLEventFactory;
import javax.xml.stream.XMLEventWriter;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.Characters;
import javax.xml.stream.events.EndElement;
import javax.xml.stream.events.StartDocument;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.net.URL;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RestController
//@RequestMapping(path = "/rss", produces = "application/xml")
public class RSSController {

    @Autowired
    private PodcastService podcastService;
//    @Autowired
//    private ClientService clientService;

    final Logger logger = LoggerFactory.getLogger(PodcastController.class);

    /* Формуємо RSS для подкасту */
    @GetMapping(value = "/rss/{puuid}", produces = "application/xml")
    public String PodcastChanel(
            @PathVariable String puuid,
            Model model ) {
        PodcastChannel podcastChannel = podcastService.GetChanelByUUID(puuid);

        DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder docBuilder;
        try {
            docBuilder = docFactory.newDocumentBuilder();
        // root elements
        Document doc = docBuilder.newDocument();
        Element rootElement = doc.createElement("company");
        doc.appendChild(rootElement);

        // staff 1001

        // add xml elements
        Element staff = doc.createElement("staff");
        // add staff to root
        rootElement.appendChild(staff);
        // add xml attribute
        staff.setAttribute("id", "1001");

        // alternative
        // Attr attr = doc.createAttribute("id");
        // attr.setValue("1001");
        // staff.setAttributeNode(attr);

        Element name = doc.createElement("name");
        // JDK 1.4
        //name.appendChild(doc.createTextNode("Пробуємо українські літери їЇєЄіІ"));
        // JDK 1.5
        name.setTextContent("Пробуємо українські літери їЇєЄіІ");
        staff.appendChild(name);

        Element role = doc.createElement("role");
        role.setTextContent("support");
        staff.appendChild(role);

        Element salary = doc.createElement("salary");
        salary.setAttribute("currency", "USD");
        salary.setTextContent("5000");
        staff.appendChild(salary);

        // add xml comment
        Comment comment = doc.createComment(
                "for special characters like < &, need CDATA");
        staff.appendChild(comment);

        Element bio = doc.createElement("bio");
        // add xml CDATA
        CDATASection cdataSection =
                doc.createCDATASection("HTML tag <code>testing</code>");
        bio.appendChild(cdataSection);
        staff.appendChild(bio);

        // staff 1002
        Element staff2 = doc.createElement("staff");
        // add staff to root
        rootElement.appendChild(staff2);
        staff2.setAttribute("id", "1002");

        Element name2 = doc.createElement("name");
        name2.setTextContent("yflow");
        staff2.appendChild(name2);

        Element role2 = doc.createElement("role");
        role2.setTextContent("admin");
        staff2.appendChild(role2);

        Element salary2 = doc.createElement("salary");
        salary2.setAttribute("currency", "EUD");
        salary2.setTextContent("8000");
        staff2.appendChild(salary2);

        Element bio2 = doc.createElement("bio");
        // add xml CDATA
        bio2.appendChild(doc.createCDATASection("a & b"));
        staff2.appendChild(bio2);

//        db.setErrorHandler(new DefaultHandler());
//        final Document doc = db.parse(new URL(url).openStream());
//        final SyndFeed feed = new SyndFeedInput().build(doc);
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            try {

                writeXml(doc, outputStream);
            } catch (TransformerException e) {
                logger.warn("Щось пішло не так при System.out ");
                return "";
            }


            String sss =  outputStream.toString();
            return sss;
        } catch (ParserConfigurationException e) {
            logger.warn("Щось пішло не так при формуванні XML :( ");
            return "";
        }

        /* цикл по епізодам */
//        for (PodcastItem item : podcastChannel.getItem())
//        {
//
//        }
    }
    private static void writeXml(Document doc,
                                 OutputStream output)
            throws TransformerException {

        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();

        // pretty print
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");

        DOMSource source = new DOMSource(doc);
        StreamResult result = new StreamResult(output);
//        transformer.
        transformer.transform(source, result);

    }
}
