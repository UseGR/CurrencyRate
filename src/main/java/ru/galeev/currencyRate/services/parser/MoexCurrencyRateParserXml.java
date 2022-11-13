package ru.galeev.currencyRate.services.parser;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import ru.galeev.currencyRate.models.CurrencyRateFromMoex;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.StringReader;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class MoexCurrencyRateParserXml implements CurrencyRateParser {

    @Override
    public List<CurrencyRateFromMoex> parseRate(String ratesAsString) {
        List<CurrencyRateFromMoex> rates = new ArrayList<>();
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        dbf.setAttribute(XMLConstants.ACCESS_EXTERNAL_DTD, "");
        dbf.setAttribute(XMLConstants.ACCESS_EXTERNAL_SCHEMA, "");
        String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";
        DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern(DATE_FORMAT);
        try {
            dbf.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);
            DocumentBuilder db = dbf.newDocumentBuilder();

            try (StringReader reader = new StringReader(ratesAsString)) {
                Document doc = db.parse(new InputSource(reader));
                doc.getDocumentElement().normalize();

                NodeList list = doc.getElementsByTagName("rate");

                for (int rateIdx = 0; rateIdx < list.getLength(); rateIdx++) {
                    Node node = list.item(rateIdx);
                    if (node.getNodeType() == Node.ELEMENT_NODE) {
                        var element = (Element) node;
                        CurrencyRateFromMoex rateMoex = CurrencyRateFromMoex.builder()
                                .value(element.getAttribute("value"))
                                .momentStart(LocalDateTime.parse(element.getAttribute("moment"), DATE_FORMATTER))
                                .build();
                        rates.add(rateMoex);
                    }
                }
            }

        } catch (Exception e) {
            log.error("xml parsing error, xml:{}", ratesAsString, e);
            throw new CurrencyRateParsingException(e);
        }

        return rates;
    }
}
