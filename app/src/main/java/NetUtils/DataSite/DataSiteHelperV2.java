package NetUtils.DataSite;

import com.example.rush0714.myapplication.DataStorage;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.CookiePolicy;
import java.net.CookieStore;
import java.net.HttpCookie;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import NetUtils.Orders.Order;
import NetUtils.Orders.OrderHelper;
import NetUtils.Resources.Resources;

public class DataSiteHelperV2 {

    public void authorize() throws Exception {
        boolean isExpired = true;
        CookieStore cookieStore = DataStorage.getCookieStore();
        for (HttpCookie httpCookie : cookieStore.getCookies()) {
            if ("PHPSESSID".equals(httpCookie.getName()) && !httpCookie.hasExpired()) {
                isExpired = false;
            }
        }

        if (isExpired) {
            CookieManager cookieManager = new CookieManager(cookieStore, CookiePolicy.ACCEPT_ALL);
            CookieHandler.setDefault(cookieManager);

            URL url = new URL(Resources.URL_MAIN);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
//            connection.setRequestProperty("User-Agent", Resources.USER_AGENT);
            if (200 != connection.getResponseCode()) {
                throw new Exception("1. удаленный узел не отвечает");
            }
            connection.disconnect();

            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setDoOutput(true);
            DataOutputStream wr = new DataOutputStream(connection.getOutputStream());
            wr.writeBytes("login=" + DataStorage.getLogin() +
                "&password=" + DataStorage.getPassword()+
                "&logIn=%D0%92%D0%BE%D0%B9%D1%82%D0%B8");
            wr.flush();
            wr.close();
            int a = connection.getResponseCode();
            if (302 != connection.getResponseCode()) {
                throw new Exception("2. Авторизация не удалась");
            }
            connection.disconnect();
            DataStorage.setCookieStore(cookieManager.getCookieStore());
        }
    }

    public void getOrders() throws Exception {
        authorize();
        CookieStore cookieStore = DataStorage.getCookieStore();
        CookieManager cookieManager = new CookieManager(DataStorage.getCookieStore(), CookiePolicy.ACCEPT_ALL);
        CookieHandler.setDefault(cookieManager);

        URL url = new URL(Resources.URL_ORDERS);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("POST");
        connection.setDoOutput(true);
        DataOutputStream wr = new DataOutputStream(connection.getOutputStream());
        wr.writeBytes(
            "tab=now&" +
                "page=1&" +
                "order[]=performed&" +
                "order[]=DESC&" +
                "moptions=false&" +
                "street=&" +
                "texnik=&");
        wr.flush();
        wr.close();

        if (200 != connection.getResponseCode()) {
            throw new Exception("3. ошибка при получении заявок");
        }

        BufferedInputStream in = new BufferedInputStream(connection.getInputStream());
        byte[] contents = new byte[512];
        int bytesRead = 0;
        StringBuilder stringBuilder = new StringBuilder();
        while ((bytesRead = in.read(contents)) != -1) {
            stringBuilder.append(new String(contents, 0, bytesRead));
        }
        in.close();
        connection.disconnect();

        DataStorage.setCookieStore(cookieManager.getCookieStore());
        String res = stringBuilder.toString();
        OrderHelper.parseOrders(res);
    }

    public void setComments(Order order) throws Exception {
        authorize();
        CookieStore cookieStore = DataStorage.getCookieStore();
//        cookieStore.add(new URI("http://csrvr.ru"), new HttpCookie("group", "1"));

        CookieManager cookieManager = new CookieManager(cookieStore, CookiePolicy.ACCEPT_ALL);
        CookieHandler.setDefault(cookieManager);

        String pAddress = order.getLink().split("[\\?,#]")[1];
        String pKeyValues[] = pAddress.split("&");
        Map<String, String> params = new HashMap<String, String>();
        for (String i : pKeyValues) {
            String pKeysAndValues[] = i.split("=");
            params.put(pKeysAndValues[0], pKeysAndValues[1]);
        }

        URL url = new URL("http://csrvr.ru/js/ajax/get-comments-app.php");
        HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
        httpURLConnection.setRequestMethod("POST");
        httpURLConnection.setRequestProperty("Referer", order.getLink());
        httpURLConnection.setDoOutput(true);
        DataOutputStream wr = new DataOutputStream(httpURLConnection.getOutputStream());
        wr.writeBytes(
            "id=" + params.get("id") + "&" +
                "city=" + params.get("cityID"));
        wr.flush();
        wr.close();

        if (200 != httpURLConnection.getResponseCode()) {
            throw new Exception("4. ошибка при получении комментариев");
        }

        BufferedReader br = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
        StringBuilder stringBuilder = new StringBuilder();
        String tmpLine;
        while ((tmpLine = br.readLine()) != null) {
            stringBuilder.append(tmpLine);
        }
        br.close();
        httpURLConnection.disconnect();

        String res = stringBuilder.toString();

        OrderHelper.parseComment(order, res);
    }
}
