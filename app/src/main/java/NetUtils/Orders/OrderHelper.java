package NetUtils.Orders;

import com.example.rush0714.myapplication.DataStorage;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import NetUtils.Resources.Resources;


public class OrderHelper {
    public static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd.MM.yyyy HH.mm");

    public static void parseOrders(String inputString) throws ParseException {
        List<Order> result = new ArrayList<Order>();

        Document document = Jsoup.parse(inputString);
        Elements elements = document.getElementsByClass("body-row");
        for (Element element : elements) {
            Element client = element.getElementsByClass("client").first();

            Element link = client.select("a[href]").first();
            String linkString = Resources.URL_DOMAIN + link.attr("href");

            Element status = element.getElementsByClass("status").first();
            String statusStyle = status.attr("style");

            String backgroundColor = statusStyle.substring(
                statusStyle.indexOf(":") + 1,
                statusStyle.length()
            );

            String clientName = client.ownText();
            String clientAddress = client.getElementsByTag("a").first().text();

            Date time = DATE_FORMAT.parse(element.getElementsByClass("timetable").first().text());
            String comment = element.getElementsByClass("comment").first()
                .getElementsByClass("comm").first().text();

            Order order = new Order(time, clientAddress, clientName, comment, linkString, backgroundColor);

            boolean isBenefit = false;

            Element benefit = element.getElementsByClass("BF").first();
            Element rowBenefit = benefit.getElementsByTag("img").first();
            String benefitTitle = null;
            if(rowBenefit!= null && rowBenefit.hasAttr("title")){
                benefitTitle = rowBenefit.attr("title");
            }
            if(benefitTitle != null && benefitTitle.contains("Льготная")){
                isBenefit = true;
            }
            order.setIsBenefit(isBenefit);
            result.add(order);
        }
        Collections.sort(result);
        DataStorage.setOrders(result);
    }

    public static void parseComment(Order order, String inputString) throws ParseException {
        Document doc = (Document) Jsoup.parse(inputString);

        List<Comment> comments = new LinkedList<Comment>();

        Elements commentRows = doc.getElementsByClass("comments-row");
        for (Element iComment : commentRows) {
            if (!iComment.hasClass("header")) {
                Elements cells = iComment.getElementsByClass("comments-td");

                String statusStyle = cells.get(0).attr("style");
                String backgroundColor = statusStyle.substring(
                    statusStyle.indexOf(":") + 1,
                    statusStyle.contains(";") ? statusStyle.indexOf(";") :  statusStyle.length()
                ).trim();

                Comment comment = new Comment(new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").parse(cells.get(0).text()),
                    cells.get(1).text(),
                    cells.get(2).text(),
                    cells.get(3).text(),
                    backgroundColor);
                comments.add(comment);
            }
        }

        order.setComments(comments);
    }

    public static List<String> parsePhoneNumbers (int orderNo){
        List<Comment> comments = DataStorage.getOrders().get(orderNo).getComments();
        Set<String> rowNumbers = new HashSet<String>();
        for(Comment iComment : comments){
            String rowComment = iComment.getMessage();
            Pattern pattern = Pattern.compile("(\\D|^)(\\d{11}|\\d{6})(\\D|$)");
            Matcher matcher = pattern.matcher(rowComment + " ");
            while(matcher.find()){
                rowNumbers.add(matcher.group(2));
            }
            Pattern pattern2 = Pattern.compile("(\\+\\d \\(\\d{3}\\) \\d{3}-\\d{4})");
            Matcher matcher2 = pattern2.matcher(rowComment + " ");
            while(matcher2.find()){
                String row = matcher2.group(1);
                row = row.replaceAll("\\s|\\(|\\)|\\-","");
                rowNumbers.add(row);
            }
        }
        List<String> result = new LinkedList<String>();
        for(String i : rowNumbers){
            if(i.length() == 11){
                result.add("+7" + i.substring(1));
            } else {
                result.add(i);
            }
        }
        return result;
    }
}
