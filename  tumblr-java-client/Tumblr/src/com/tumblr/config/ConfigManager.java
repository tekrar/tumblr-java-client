package com.tumblr.config;

import java.io.File;
import java.io.InputStream;
import java.util.Iterator;
import java.util.Properties;

public class ConfigManager 
{
	private static volatile ConfigManager instance = null;
	private static String PROPERTIES_FILE_NAME = "tumblr.properties";
	public static String LOGIN_EMAIL_PROP = "loginEmail";
	public static String HOST_URL_PROP = "hostURL";
	public static String LOGIN_PASSWORD_PROP = "loginPassword";
	

	public static ConfigManager getInstance() 
	{
        if (instance == null) {
            synchronized (ConfigManager.class) 
            {
                if (instance == null) 
                {
                    instance = new ConfigManager();
                }
            }
        }
        String uri = (System.getenv().get("USERPROFILE")) + File.separator + PROPERTIES_FILE_NAME;
        instance.load(uri);
        return instance;
    }
	 
    private ConfigManager() {}
	 
    protected void load(String uri)
    {
        if ( uri == null )
        {
            return;
        }
        ResourceLoader rl = new ResourceLoader(uri);
        InputStream propertiesStream = rl.inputStream();
        if ( propertiesStream == null )
        {
        	System.out.println("Loading properties failed, properties stream is null or invalid, check file: " + uri);
        }
        else
        {
            this.loadPropertiesFromResourceContent(propertiesStream, uri);
        }
    }

    protected void loadPropertiesFromResourceContent(InputStream resourceStream, String uri)
    {
        try
        {
            Properties p = new Properties();
            p.load(resourceStream);

            Iterator it = p.keySet().iterator();
            while(it.hasNext())
            {
                String key = (String) it.next();
                String value = (String) p.get(key);
                this.getProperties().put(key,value);
            }
        }
        catch(Throwable t)
        {
            System.out.println("Failed to load resource properties because: " + t.getMessage());
        }
    }
    
    public Properties getProperties() { return System.getProperties(); }
    
    public String getProperty(String propertyName) {return System.getProperty(propertyName);}
    
    public static void main (String [] args)
    {
    	System.out.println(ConfigManager.getInstance().getProperty(LOGIN_EMAIL_PROP));
    	System.out.println(ConfigManager.getInstance().getProperty(LOGIN_PASSWORD_PROP));
    	System.out.println(ConfigManager.getInstance().getProperty(HOST_URL_PROP));
    }
}
