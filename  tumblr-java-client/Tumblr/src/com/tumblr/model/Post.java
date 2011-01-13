package com.tumblr.model;

public class Post 
{
	public String title, body, id;
	public PostType type;
	
	public Post(String title, String body, PostType type, String id)
	{
		this.title=title;
		this.body=body;
		this.type=type;
		this.id=id;
	}
	public Post(String title, String body, PostType type)
	{
		this(title,body,type,null);
	}
	
	public String toString()
	{
		return id + " " + title + "\n" + body + "\n\n";
	}
}