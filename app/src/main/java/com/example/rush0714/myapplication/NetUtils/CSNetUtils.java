package com.example.rush0714.myapplication.NetUtils;

import android.os.AsyncTask;

import com.example.rush0714.myapplication.DataStorage;
import com.example.rush0714.myapplication.R;

import java.io.BufferedInputStream;
import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.CookiePolicy;
import java.net.HttpURLConnection;
import java.net.URL;

import NetUtils.DataSite.DataSiteHelperV2;

public abstract class CSNetUtils<T> {
    private String login;
    private String password;
    private CSRequest csRequest;

    public CSNetUtils(String login, String password) {
        this.login = login;
        this.password = password;
    }

    public void setCsRequest(CSRequest csRequest) {
        this.csRequest = csRequest;
    }

    public T doRequest() throws Exception {
        DataSiteHelperV2.authorize(login, password);
        csRequest.setVariables();
        final String[] siteResult = new String[0];

        new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... voids) {
                String siteString = null;
                try {
                    CookieManager cookieManager = new CookieManager(DataStorage.getCookieStore(), CookiePolicy.ACCEPT_ALL);
                    CookieHandler.setDefault(cookieManager);
                    URL url = new URL(csRequest.getAddress());
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();

                    if (CSRequest.Method.GET.equals(csRequest.getMethod())) {
                        connection.setRequestMethod("GET");
                        if (200 != connection.getResponseCode()) {
                            throw new Exception(String.valueOf(R.string.error));
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
                        siteString = stringBuilder.toString();
//                        OrderHelper.parseOrders(res);
                    } else if (CSRequest.Method.GET.equals(csRequest.getMethod())) {

                    }


//                    connection.setDoOutput(true);
//                    DataOutputStream wr = new DataOutputStream(connection.getOutputStream());
//                    wr.writeBytes(
//                        "tab=now&" +
//                            "page=1&" +
//                            "order[]=performed&" +
//                            "order[]=DESC&" +
//                            "moptions=false&" +
//                            "street=&" +
//                            "texnik=&");
//                    wr.flush();
//                    wr.close();


                } catch (Exception e) {
                    e.printStackTrace();
                }
                return siteString;
            }

            @Override
            protected void onPostExecute(String s) {
                siteResult[0] = s;
            }
        }.execute();

        return parse(siteResult[0]);
    }

    public abstract T parse(String stringIn);
}
