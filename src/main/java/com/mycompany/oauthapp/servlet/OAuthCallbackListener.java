/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.oauthapp.servlet;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.restfb.DefaultFacebookClient;
import com.restfb.FacebookClient;
import com.restfb.types.*;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;
import org.apache.commons.codec.binary.Base64;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;
import org.json.simple.parser.JSONParser;
/**
 *
 * @author KALINDU
 */
public class OAuthCallbackListener extends HttpServlet {

 

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        String authorizationCode = request.getParameter("code");
        if (authorizationCode != null && authorizationCode.length() > 0) {
            final String TOKEN_ENDPOINT = "https://graph.facebook.com/oauth/access_token";   
            final String GRANT_TYPE = "authorization_code";
            final String REDIRECT_URI = "https://localhost:8443/OAuthApp/callback";
            final String CLIENT_ID = "1789938591061725";
            final String CLIENT_SECRET = "1e4e5fdf232feea13b23aa4760e42923";
            // Generate POST request
            HttpPost httpPost = new HttpPost(TOKEN_ENDPOINT + "?grant_type=" + URLEncoder.encode(GRANT_TYPE,StandardCharsets.UTF_8.name()) + "&code=" + URLEncoder.encode(authorizationCode,StandardCharsets.UTF_8.name()) + "&redirect_uri=" + URLEncoder.encode(REDIRECT_URI,StandardCharsets.UTF_8.name()) + "&client_id=" + URLEncoder.encode(CLIENT_ID,StandardCharsets.UTF_8.name()));
            String clientCredentials = CLIENT_ID + ":" + CLIENT_SECRET;
            String encodedClientCredentials = new String(Base64.encodeBase64(clientCredentials.getBytes()));
            httpPost.setHeader("Authorization", "Basic " + encodedClientCredentials);            
            CloseableHttpClient httpClient = HttpClients.createDefault();
            HttpResponse httpResponse = httpClient.execute(httpPost);

            
            Reader reader = new InputStreamReader(httpResponse.getEntity().getContent());
            BufferedReader bufferedReader = new BufferedReader(reader);
            String line = bufferedReader.readLine();
            String accessToken = null;
            String[] responseProperties = line.split("&");
            for (String responseProperty : responseProperties) {  
                try {
                    JSONParser parser = new JSONParser();
                    Object obj = parser.parse(responseProperty);
                    JSONObject jsonobj = (JSONObject) obj;
                    accessToken = jsonobj.get("access_token").toString();
                    System.out.println("Access token: " + accessToken);
                } catch (ParseException e) {
                    System.out.println("Error while parsing the token from facebook : " + e.getMessage());
                }
            }
            String url = "https://graph.facebook.com/v2.10/me/friends";
            httpPost = new HttpPost(url);
            httpPost.addHeader("Authorization", "Bearer " + accessToken);
            List<NameValuePair> urlParameters = new ArrayList<NameValuePair>();
            urlParameters.add(new BasicNameValuePair("method", "get"));
            httpPost.setEntity(new UrlEncodedFormEntity(urlParameters));
            httpResponse = httpClient.execute(httpPost);
            
            bufferedReader = new BufferedReader(new InputStreamReader(httpResponse.getEntity().getContent()));
            String feedData = bufferedReader.readLine();
            String friendsobj = null;
            String[] rp = feedData.split("&");
            for (String singlerp : rp){             
                try{
                    JSONParser psr = new JSONParser();
                    Object object = psr.parse(singlerp);
                    JSONObject jobject = (JSONObject) object;
                    friendsobj = jobject.get("summary").toString(); 
                }catch(Exception e){
                    System.out.println("Error while parsing the values from data : " + e.getMessage());
                }
            }
            String friendsCount = null;
            String[] rp2 = friendsobj.split("&");
            for (String singlerp : rp2){               
                try{
                    JSONParser psr = new JSONParser();
                    Object object = psr.parse(singlerp);
                    JSONObject jobject = (JSONObject) object;
                    friendsCount = jobject.get("total_count").toString(); 
                }catch(Exception e){
                    System.out.println("Error while parsing values from friends object : " + e.getMessage());
                }
            }
            System.out.println("friends : "+friendsCount);
 
            httpClient.close();
            
                    
            FacebookClient fbclient = new DefaultFacebookClient(accessToken);
            User me = fbclient.fetchObject("me", User.class);
            String name = me.getName();
            String id = me.getId();
            
            /*  
            Following commented code is to update facebok status im not sure if its working :)
                String url ="http://graph.facebook.com/me/feed";
                String msg = "Graph API Test";
                HttpPost httpPost2 = new HttpPost(url + "?message=" +URLEncoder.encode(msg,StandardCharsets.UTF_8.name()));
                httpPost2.addHeader("Authorization", "Bearer " + accessToken);
                HttpClient httpClient2 = HttpClients.createDefault();
                httpClient2.execute(httpPost2);
            */
            request.setAttribute("UserName",name);
            request.setAttribute("UserId",id);
            request.setAttribute("friendsCount",friendsCount);
            request.getRequestDispatcher("userdata.jsp").forward(request, response);
            
            

        } else {
            System.out.println("Error in authorization code !");
        }
                
    }
}




