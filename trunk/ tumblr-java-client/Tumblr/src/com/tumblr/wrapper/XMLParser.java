package com.tumblr.wrapper;

import java.io.StringReader;

import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import com.tumblr.model.BlogContent;
import com.tumblr.model.Post;
import com.tumblr.model.PostType;

public class XMLParser 
{
	public static final String TEST_XML = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><tumblr version=\"1.0\"><tumblelog name=\"axyt\" timezone=\"Europe/Warsaw\" title=\"axyt notes...\"></tumblelog><posts start=\"0\" total=\"5\"><post id=\"2719381469\" url=\"http://axyt.tumblr.com/post/2719381469\" url-with-slug=\"http://axyt.tumblr.com/post/2719381469/post-title-test\" type=\"regular\" date-gmt=\"2011-01-12 22:51:24 GMT\" date=\"Wed, 12 Jan 2011 23:51:24\" unix-timestamp=\"1294872684\" format=\"html\" reblog-key=\"TWULapRK\" slug=\"post-title-test\"><regular-title>Post title test</regular-title><regular-body>&lt;p&gt;Post title body&lt;/p&gt;</regular-body></post><post id=\"2718943199\" url=\"http://axyt.tumblr.com/post/2718943199\" url-with-slug=\"http://axyt.tumblr.com/post/2718943199/post-title-test\" type=\"regular\" date-gmt=\"2011-01-12 22:22:46 GMT\" date=\"Wed, 12 Jan 2011 23:22:46\" unix-timestamp=\"1294870966\" format=\"html\" reblog-key=\"hbxw3HgW\" slug=\"post-title-test\"><regular-title>Post title test</regular-title><regular-body>&lt;p&gt;Post title body&lt;/p&gt;</regular-body></post><post id=\"2718935887\" url=\"http://axyt.tumblr.com/post/2718935887\" url-with-slug=\"http://axyt.tumblr.com/post/2718935887/post-title-test\" type=\"regular\" date-gmt=\"2011-01-12 22:22:17 GMT\" date=\"Wed, 12 Jan 2011 23:22:17\" unix-timestamp=\"1294870937\" format=\"html\" reblog-key=\"dlqfqEe7\" slug=\"post-title-test\"><regular-title>Post title test</regular-title><regular-body>&lt;p&gt;Post title body&lt;/p&gt;</regular-body></post><post id=\"2718914296\" url=\"http://axyt.tumblr.com/post/2718914296\" url-with-slug=\"http://axyt.tumblr.com/post/2718914296/post-title-test\" type=\"regular\" date-gmt=\"2011-01-12 22:20:52 GMT\" date=\"Wed, 12 Jan 2011 23:20:52\" unix-timestamp=\"1294870852\" format=\"html\" reblog-key=\"P4sP3tp3\" slug=\"post-title-test\"><regular-title>Post title test</regular-title><regular-body>&lt;p&gt;Post title body&lt;/p&gt;</regular-body></post><post id=\"2639479937\" url=\"http://axyt.tumblr.com/post/2639479937\" url-with-slug=\"http://axyt.tumblr.com/post/2639479937/ryzy-harbia-sp-z-o-o\" type=\"regular\" date-gmt=\"2011-01-07 19:06:06 GMT\" date=\"Fri, 07 Jan 2011 20:06:06\" unix-timestamp=\"1294427166\" format=\"html\" reblog-key=\"hjviA0CS\" slug=\"ryzy-harbia-sp-z-o-o\"><regular-title>Ryży - Harbia Sp.z.o.o</regular-title><regular-body>&lt;p&gt;Po co to pierdolenie w mediach, że mamy małe &amp;#8220;spięcie&amp;#8221; na linii rząd - prezydent. Media twierdzą, że komor nie podpisze ustawy dotyczącej zwolnień nierobów w urzędach i skieruje ją do Trybunału. &amp;#8220;tusku&amp;#8221; będzie mógł teraz powiedzieć, że próbował ciąć wydatki, ale się nie udało z winy prezydenta&amp;#8230; I tak będzie z każdą przyszłą ustawą, która będzie miała ciąć stołki koleżków, rząd coś przygotuje, hrabia zawetuje i wkoło macieju&amp;#8230;&lt;/p&gt;</regular-body><tag>polityka</tag></post></posts></tumblr>";
	public static BlogContent parseBlogXML(String xml)
	{
		BlogContent blog = new BlogContent();
		
        DocumentBuilderFactory dbfac = DocumentBuilderFactory.newInstance();
		StringReader reader = new StringReader(xml);
        InputSource inputSource = new InputSource( reader );
        Document doc = null;
        try
        {
        	doc = dbfac.newDocumentBuilder().parse( inputSource );
        }
        catch (Exception e) {
			System.out.println("zesralo sie");
		}
        
        reader.close();

        if (doc != null)
        {
        	NodeList posts = doc.getElementsByTagName("post");
        	for (int i = 0; i < posts.getLength(); i++)
        	{
        		String title = "", body = "";
        		Node post = posts.item(i);
        		NodeList postElements = post.getChildNodes();
        		for (int j = 0; j< postElements.getLength(); j++)
        		{
        			Node n = postElements.item(j);
        			if (n.getNodeName().equals("regular-title"))
        			{
        				title=n.getTextContent();
        			}
        			else if (n.getNodeName().equals("regular-body"))
        			{
        				body=n.getTextContent();
        			}
        		}
        		
        		NamedNodeMap attrs = post.getAttributes();
        		String postID = attrs.getNamedItem("id").getTextContent();
        		
        		Post p = new Post(title, body, PostType.regular, postID);
        		blog.addPost(p);
        	}
        }
		return blog;
	}
	
	public static void main (String [] args)
	{
		BlogContent content = XMLParser.parseBlogXML(XMLParser.TEST_XML);
		System.out.println(content);
	}
}
