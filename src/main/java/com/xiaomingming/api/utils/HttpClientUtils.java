package com.xiaomingming.api.utils;

import com.alibaba.fastjson.JSON;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class HttpClientUtils {
    private static final Charset encode = Charset.forName("UTF-8");
    private static final CloseableHttpClient client = HttpClients.createDefault();

    public static Map post(URI url, String data) {
        List<NameValuePair> params = new ArrayList<>();
        params.add(new BasicNameValuePair("data", data));
        HttpPost post = new HttpPost(url);
        UrlEncodedFormEntity form = new UrlEncodedFormEntity(params, encode);
        post.setEntity(form);
        StringBuilder jsonStr = new StringBuilder();
        BufferedReader reader = null;
        try {
            HttpResponse response = client.execute(post);
            reader = new BufferedReader(new InputStreamReader(response
                    .getEntity().getContent(), encode));
            for (String line = reader.readLine(); ProjectUtil.isNotEmpty(line); line = reader
                    .readLine()) {
                jsonStr.append(line);
            }
            return JSON.parseObject(jsonStr.toString(), Map.class);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    public static String post(URI url, Map<String, String> params) {
        System.out.println("request para -> " + JSON.toJSONString(params));
        System.out.println("request url -> " + url.toString());
        List<NameValuePair> lParams = new ArrayList<>();
        //参数
        for (String pName : params.keySet()) {
            lParams.add(new BasicNameValuePair(pName, params.get(pName)));
        }
        //目标地址
        HttpPost post = new HttpPost(url);
        UrlEncodedFormEntity form = new UrlEncodedFormEntity(lParams, encode);
        post.setEntity(form);
        StringBuilder jsonStr = new StringBuilder();
        BufferedReader reader = null;
        try {
            HttpResponse response = client.execute(post);
            reader = new BufferedReader(new InputStreamReader(response
                    .getEntity().getContent(), encode));
            for (String line = reader.readLine(); ProjectUtil.isNotEmpty(line); line = reader
                    .readLine()) {
                jsonStr.append(line);
            }
            return jsonStr.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
