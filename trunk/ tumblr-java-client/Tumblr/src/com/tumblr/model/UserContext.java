package com.tumblr.model;

public class UserContext {

	public String hostURL, email, password;
	public UserContext(String hostURL, String email, String password)
	{
		this.hostURL = hostURL;
		this.password = password;
		this.email = email;
	}
	public boolean isValid() {return hostURL != null && password != null && email != null;}
}
