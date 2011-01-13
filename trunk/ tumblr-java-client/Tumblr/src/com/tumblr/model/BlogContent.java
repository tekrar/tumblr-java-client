package com.tumblr.model;

import java.util.HashMap;
import java.util.Map;

public class BlogContent 
{
	public Map <String, Post> posts;
	public String blogName, blogTitle;
	public int postCnt;
	
	public void addPost(Post post)
	{
		if (posts == null) {posts = new HashMap<String,Post>();}
		posts.put(post.id, post);
	}
	
	public BlogContent(String blogName, String blogTitle, int postCnt)
	{
		this.blogName=blogName;
		this.blogTitle=blogTitle;
		this.postCnt=postCnt;
	}
	public BlogContent() {}
	
	public String toString()
	{
		StringBuffer res = new StringBuffer("");
		res.append("Title: " + blogTitle + "\n");
		res.append("Name: " + blogName + "\n");
		res.append("Post cnt: " + postCnt + "\n");
		
		for (Post p : posts.values()) {
			res.append(p.id + " " + p.title + "\n" + p.body + "\n\n");
		}
		return res.toString();
	}
}
