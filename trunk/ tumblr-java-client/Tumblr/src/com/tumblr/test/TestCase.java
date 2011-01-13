package com.tumblr.test;

import com.tumblr.api.Connector;
import com.tumblr.model.BlogContent;
import com.tumblr.model.Post;
import com.tumblr.model.PostType;
import com.tumblr.model.UserContext;

public class TestCase 
{
	public static void main (String [] args)
	{
		try
		{
			Post one = new Post("Post no one", "1st post body", PostType.regular);
			Post two = new Post("Post no two", "2nd post body", PostType.regular);
			
			UserContext uCtx = new UserContext("", "", "");
			Connector conn = new Connector(uCtx);
			
			conn.makeNewPost(one);
			conn.makeNewPost(two);
			
			BlogContent blog = conn.getBlog();
			
			if (blog.posts.values().size() > 0)
			{
				for (Post post : blog.posts.values())
				{
					conn.deletePost(post);
				}
			}
		} catch (Exception e)
		{
			throw new RuntimeException(e);
		}
	}
}
