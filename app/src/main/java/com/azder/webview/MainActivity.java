package com.azder.webview;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.xml.parsers.SAXParserFactory;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView textView;
    private static final String TAG = "AZDER";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        WebView webView = (WebView)findViewById(R.id.webview);
//        webView.setWebViewClient(new WebViewClient());
//        webView.getSettings().setJavaScriptEnabled(true);
//        webView.loadUrl("http:www.baidu.com");
        Button send_request = (Button) findViewById(R.id.send_request);
        textView = (TextView) findViewById(R.id.response_text);
        send_request.setOnClickListener(this);

    }

    public void onClick(View v) {
        if (v.getId() == R.id.send_request) {
//            sendRequestWithHttpURLHttpConnection();
            sendRequestWithOKHttp();
        }
    }

    private void sendRequestWithHttpURLHttpConnection() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpURLConnection connection = null;
                BufferedReader reader = null;
                try {
                    URL url = new URL("https://www.baidu.com");
                    connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("GET");
                    connection.setConnectTimeout(8000);
                    connection.setReadTimeout(8000);
                    InputStream in = connection.getInputStream();

                    reader = new BufferedReader(new InputStreamReader(in));
                    StringBuilder responseText = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        responseText.append(line);
                    }
//                    showResponse(responseText.toString());
                    sendRequestWithOKHttp();
                    Log.d("TAG", responseText + "");
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    if (reader != null) {
                        try {
                            reader.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    if (connection != null) {
                        connection.disconnect();
                    }
                }
            }
        }).start();
    }

    private void sendRequestWithOKHttp() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {

                    OkHttpClient okHttpClient = new OkHttpClient();
                    Request request = new Request.Builder()
                            .url("http://192.168.56.1/test.xml")
                            .build();
                    Response response = okHttpClient.newCall(request).execute();
                    String responseData = response.body().string();
                    //showResponse(responseData);
                    //parseXMLWithPull(responseData);
                    parseXMLWithSAX(responseData);
                    Log.d(TAG, responseData.toString());
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }).start();
    }

    private void showResponse(final String responses) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                textView.setText(responses);
            }
        });
    }

    private void parseXMLWithPull(String xmlData) {
        try {
            Log.d(TAG, "test");
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            XmlPullParser xmlPullParser = factory.newPullParser();
            xmlPullParser.setInput(new StringReader(xmlData));
            int eventType = xmlPullParser.getEventType();
            String name = "";
            String age = "";
            String sex = "";
            while (eventType != XmlPullParser.END_DOCUMENT) {
                String nodeName = xmlPullParser.getName();
                switch (eventType) {
                    case XmlPullParser.START_TAG:
                        if ("name".equals(nodeName)) {
                            name = xmlPullParser.nextText();
                        } else if ("age".equals(nodeName)) {
                            age = xmlPullParser.nextText();
                        } else if ("sex".equals(nodeName)) {
                            sex = xmlPullParser.nextText();
                        }
                        break;
                    case XmlPullParser.END_TAG:
                        if ("student".equals(nodeName)) {
                            Log.d(TAG, "name is" + name);
                            Log.d(TAG, "age is" + age);
                            Log.d(TAG, "sex is" + sex);
                        }
                        break;
                    default:
                        break;
                }
                eventType = xmlPullParser.next();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void parseXMLWithSAX(String xmldata){
        try {
            SAXParserFactory factory = SAXParserFactory.newInstance();
            XMLReader xmlReader = factory.newSAXParser().getXMLReader();
            ContentHandler contentHandler = new ContentHandler();
            xmlReader.setContentHandler(contentHandler);
            xmlReader.parse(new InputSource(new StringReader(xmldata)));
        } catch (Exception e){
            e.printStackTrace();
        }
    }

}