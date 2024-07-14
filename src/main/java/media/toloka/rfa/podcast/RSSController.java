package media.toloka.rfa.podcast;

import com.rometools.rome.feed.synd.*;
import com.rometools.rome.io.FeedException;
import com.rometools.rome.io.SyndFeedInput;
import com.rometools.rome.io.SyndFeedOutput;
import dev.stalla.model.Episode;
import dev.stalla.model.Podcast;
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

import java.io.*;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
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

        List<Episode> episodeList =new ArrayList<>();
        Episode episode = Episode.builder()
                .title("перший епізод")
                .description("Перший опис")
                .author("Перший автор")
                .source("https://rfa.toloka.media")
//                .getItunesBuilder()
                .build();
//        episodeList.add(episode);
//                Podcast podcast = Podcast.builder()
//                        .title("Заголовок подкасту")
//                        .description("Опис подкасту")
//                        .addEpisodeBuilder(episodeList)
//                        .build();


        DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder docBuilder;
        try {
            docBuilder = docFactory.newDocumentBuilder();
        // root elements
        Document doc = docBuilder.newDocument();

//            URL namespaceURL = new URL("http://www.w3.org/2001/XMLSchema-instance");
//            String namespace = "xmlns:xsi="+namespaceURL.toString();
//
//            Element messages = doc.createElementNS(namespace, "messages");

        Element rootElement = doc.createElement("rss");
//            rootElement.createElementNS("http://example/namespace", "PREFIX:aNodeName");
            rootElement.setAttribute("xmlns:content", "http://purl.org/rss/1.0/modules/content/");
            rootElement.setAttribute("xmlns:dc", "http://purl.org/dc/elements/1.1/");
            rootElement.setAttribute("xmlns:wfw", "http://wellformedweb.org/CommentAPI/");
            rootElement.setAttribute("xmlns:atom", "http://www.w3.org/2005/Atom");
            rootElement.setAttribute("xmlns:sy", "http://purl.org/rss/1.0/modules/syndication/");
            rootElement.setAttribute("xmlns:slash", "http://purl.org/rss/1.0/modules/slash/");
            rootElement.setAttribute("xmlns:itunes", "http://www.itunes.com/dtds/podcast-1.0.dtd");
            rootElement.setAttribute("xmlns:podcast", "https://podcastindex.org/namespace/1.0");
            rootElement.setAttribute("xmlns:rawvoice", "http://www.rawvoice.com/rawvoiceRssModule/");
            rootElement.setAttribute("xmlns:googleplay", "http://www.google.com/schemas/play-podcasts/1.0");
            rootElement.setAttribute("version", "2.0");
        doc.appendChild(rootElement);

        // staff 1001

        // add xml elements
        Element chanel = doc.createElement("channel");
        // add staff to root
        rootElement.appendChild(chanel);
        // add xml attribute
//        staff.setAttribute("id", "1001");

        // alternative
        // Attr attr = doc.createAttribute("id");
        // attr.setValue("1001");
        // staff.setAttributeNode(attr);

        Element title = doc.createElement("title");
        // JDK 1.4
        //name.appendChild(doc.createTextNode("Пробуємо українські літери їЇєЄіІ"));
        // JDK 1.5
//            title.setTextContent(decodeUTF8(podcastChannel.getTitle()));
            title.setTextContent(new String(podcastChannel.getTitle().getBytes(StandardCharsets.UTF_8)));

            chanel.appendChild(title);

        Element link = doc.createElement("link");
            link.setTextContent("URL chanel name");
            chanel.appendChild(link);

        Element description = doc.createElement("description");
//        salary.setAttribute("currency", "USD");
//            description.setTextContent(decodeUTF8(podcastChannel.getDescription()));
            description.setTextContent(podcastChannel.getDescription());
            chanel.appendChild(description);

        // add xml comment
        Comment comment = doc.createComment(
                "for special characters like < &, need CDATA");
            chanel.appendChild(comment);

        Element bio = doc.createElement("bio");
        // add xml CDATA
        CDATASection cdataSection =
                doc.createCDATASection("HTML tag <code>testing</code>");
        bio.appendChild(cdataSection);
            chanel.appendChild(bio);

        // staff 1002
//        Element staff2 = doc.createElement("staff");
//        // add staff to root
//        rootElement.appendChild(staff2);
//        staff2.setAttribute("id", "1002");
//
//        Element name2 = doc.createElement("name");
//        name2.setTextContent("yflow");
//        staff2.appendChild(name2);
//
//        Element role2 = doc.createElement("role");
//        role2.setTextContent("admin");
//        staff2.appendChild(role2);
//
//        Element salary2 = doc.createElement("salary");
//        salary2.setAttribute("currency", "EUD");
//        salary2.setTextContent("8000");
//        staff2.appendChild(salary2);
//
//        Element bio2 = doc.createElement("bio");
//        // add xml CDATA
//        bio2.appendChild(doc.createCDATASection("a & b"));
//        staff2.appendChild(bio2);

//        db.setErrorHandler(new DefaultHandler());
//        final Document doc = db.parse(new URL(url).openStream());
//        final SyndFeed feed = new SyndFeedInput().build(doc);

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream(); //inputStream,"UTF-8"
//            OutputStream outputStream = new OutputStream(); //inputStream,"UTF-8"
            try {
                writeXml(doc, outputStream);
            } catch (TransformerException e) {
                logger.warn("Щось пішло не так при System.out ");
                return "";
            }


            String sss =  outputStream.toString(StandardCharsets.UTF_8);// toString();//.getBytes(StandardCharsets.UTF_8);
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
        transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
        transformer.setOutputProperty(OutputKeys.METHOD, "xml");
        transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
//        transformer.setOutputProperty(OutputKeys.INDENT, "no");
//        transformer.transform(doc, new StreamResult(output));

        DOMSource source = new DOMSource(doc);
        StreamResult result = new StreamResult(output);
////        transformer.
//        output.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>".getBytes("UTF-8"));
        try {
//            byte[] ttt = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>".getBytes("UTF-8");
            output.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>".getBytes("UTF-8"));
        } catch (UnsupportedEncodingException e) {
            System.out.println ("Щось пішло не так при перекодуванні XML :( ");
        } catch (IOException exception) {
            System.out.println ("Щось пішло не так при запису потоку :( ");
        }

//        transformer.transform(source, result);
        transformer.transform(new DOMSource(doc), new StreamResult(output));
//        String ttt = new String();
//        transformer.transform(new DOMSource(doc), new StreamResult(ttt));
//        System.out.println (ttt);

    }

    String decodeUTF8(String str) {
        String sss;
        try {
            sss = new String(str.getBytes("UTF-8"), Charset.forName("UTF-8"));
            return sss;
        } catch (UnsupportedEncodingException e) {
            logger.warn("Якась фігня при перетворенні стока->масив->строка");
        }
        return null;


    }
}
