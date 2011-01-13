package com.tumblr.model;

public class UserContext {

	public String name, email, password;
	public UserContext(String name, String email, String password)
	{
		this.name = name;
		this.password = password;
		this.email = email;
	}
	public boolean isValid() {return name != null && password != null && email != null;}
}
