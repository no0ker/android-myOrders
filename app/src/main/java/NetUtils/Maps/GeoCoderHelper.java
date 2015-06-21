package NetUtils.Maps;

import android.util.Log;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

public class GeoCoderHelper {
    private static final String baseAddress= "http://geocode-maps.yandex.ru/1.x/?geocode=";
    public static final String TAG = GeoCoderHelper.class.toString();

    public static GeoPoint getGeoPoint(String address){
        GeoPoint result = new GeoPoint();
        try {
            URL url = new URL(baseAddress + URLEncoder.encode(address,"UTF-8"));
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            if (200 != connection.getResponseCode()) {
                throw new Exception("ошибка при получении координат");
            }

            SAXParserFactory factory = SAXParserFactory.newInstance();
            SAXParser parser = factory.newSAXParser();
            mySaxparser saxp = new mySaxparser();
            parser.parse(connection.getInputStream(), saxp);
            String parsedRsult = saxp.getValue();
            Pattern pattern = Pattern.compile("(\\d+.\\d+)\\s(\\d+.\\d+)");
            Matcher matchsr = pattern.matcher(parsedRsult);
            if(matchsr.matches()){
                result.setLat(Double.parseDouble(matchsr.group(1)));
                result.setLng(Double.parseDouble(matchsr.group(2)));
            }
        } catch (Exception e) {
            Log.d(TAG, Log.getStackTraceString(e));
        }
        return result;
    }

    private static class mySaxparser extends DefaultHandler {
        private boolean thisElement;
        private String value;
        @Override
        public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
            if(qName.equals("pos")){
                thisElement = true;
            } else {
                thisElement = false;
            }
        }

        @Override
        public void endElement(String uri, String localName, String qName) throws SAXException {
            thisElement = false;
        }

        @Override
        public void characters(char[] ch, int start, int length) throws SAXException {
            if (thisElement){
                value = new String(ch, start, length);
            }
        }

        public String getValue() {
            return value;
        }
    }
}
