package com.tumblr.api;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import com.tumblr.model.BlogContent;
import com.tumblr.model.Post;
import com.tumblr.model.UserContext;
import com.tumblr.wrapper.XMLParser;

public class Connector 
{
	public static String POST_URL = "http://tumblr.com/api/write";
	public static String GET_URL = null;
	public static String DEL_URL = "http://www.tumblr.com/api/delete";
	public UserContext uCtx;
	
	public Connector(UserContext uCtx)
	{
		this.uCtx = uCtx;
		GET_URL = uCtx.hostURL + "/api/read";
	}
	
	public boolean makeNewPost(Post p) throws Exception
	{
        HttpClient client = new DefaultHttpClient();
        
        List<NameValuePair> params = new ArrayList<NameValuePair>();

        params.add(new BasicNameValuePair("email", uCtx.email));
        params.add(new BasicNameValuePair("password", uCtx.password));
        
        params.add(new BasicNameValuePair("type", p.type.toString()));
        params.add(new BasicNameValuePair("title", p.title));
        params.add(new BasicNameValuePair("body", p.body));
        
        UrlEncodedFormEntity entity = new UrlEncodedFormEntity(params, "UTF-8");
        HttpPost post = new HttpPost(POST_URL);
        post.setEntity(entity);

        System.out.println(post.getURI());

        HttpResponse response = client.execute(post);
        
        System.out.println(response.getStatusLine());

        client.getConnectionManager().shutdown();

        return true;
	}

	public String getAllPosts() throws Exception
	{
		String result = "";
		
		HttpClient client = new DefaultHttpClient();
        HttpGet httpget = new HttpGet(GET_URL); 
        HttpResponse response = client.execute(httpget);
        
        if (response.getStatusLine().getStatusCode() == HttpStatus.SC_SERVICE_UNAVAILABLE)
        {
        	client.getConnectionManager().shutdown();
        	int callNo = 2;
            while (callNo < 30)
            {
            	client = new DefaultHttpClient();
            	response = client.execute(httpget);
            	if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {break;}
            	callNo++;
            }
            System.out.println("Get all posts retrieved by: " + callNo + " times...");
        }

        int status = response.getStatusLine().getStatusCode();
        
        if (status != HttpStatus.SC_OK)
        {
        	System.out.println("Service unavailable => " + status);
        }
        
        HttpEntity entity = response.getEntity();
        if (entity != null) 
        {
        	BufferedReader reader = new BufferedReader(new InputStreamReader(entity.getContent()));            
            try 
            {
                String buffer = "";
                while ((buffer = reader.readLine()) != null) {
                	result = result + buffer;
                }
                System.out.println(result);
            } 
            catch (IOException ex) 
            {
                throw ex;
            } catch (RuntimeException ex) {
                httpget.abort();
                throw ex;
            } finally {reader.close();}
        }
        client.getConnectionManager().shutdown();
        return result;
	}
	
	public void deletePost(Post p) throws Exception
	{
		HttpClient client = new DefaultHttpClient();
        
        List<NameValuePair> params = new ArrayList<NameValuePair>();

        params.add(new BasicNameValuePair("email", uCtx.email));
        params.add(new BasicNameValuePair("password", uCtx.password));
        params.add(new BasicNameValuePair("post-id", p.id));
        
        UrlEncodedFormEntity entity = new UrlEncodedFormEntity(params, "UTF-8");
        HttpPost post = new HttpPost(DEL_URL);
        post.setEntity(entity);
        System.out.println(post.getURI());

        HttpResponse response = client.execute(post);
        
        System.out.println(response.getStatusLine());

        client.getConnectionManager().shutdown();
	}
	
	public BlogContent getBlog() throws Exception
	{
		String xml = this.getAllPosts();
		BlogContent blog = XMLParser.parseBlogXML(xml);
		return blog;
	}
}
