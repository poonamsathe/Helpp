package com.blakky.help123;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;

public class Parser {

	
	HttpClient httpClient;
	@SuppressWarnings("unused")
	HttpResponse httpResponse;
	HttpPost httpPost;
	InputStream is = null;

	
	private String getData(String url) throws Exception{
		
		String result = null;
		HttpClient client = new DefaultHttpClient();
        HttpGet request = new HttpGet();
        request.setURI(new URI(url));
        HttpResponse response = client.execute(request);
	
        BufferedReader in = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
        
        StringBuffer sb = new StringBuffer("");
          String line="";
          
          while ((line = in.readLine()) != null) {
             sb.append(line);
             break;
           }
           in.close();
           result = sb.toString();
	
		
		
		return result;
		
	}
	
}
