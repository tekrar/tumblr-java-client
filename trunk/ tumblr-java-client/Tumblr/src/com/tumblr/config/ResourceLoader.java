package com.tumblr.config;

import java.net.*;
import java.io.*;
import java.util.*;
import java.text.*;

public class ResourceLoader extends Object
{
    private String  _resourcePath;
    private InputStream  _inputStream;

    public ResourceLoader(String resourcePath)
    {
        super();
        _resourcePath = "" + resourcePath;
    }

    /** @return the pathname as specified when the receiver was constructed */
    public String getResourcePath() { return _resourcePath; }

    public InputStream inputStream()
    {
        if ( _inputStream == null ) { _inputStream = this.createInputStream(); }
        return _inputStream;
    }

    public Properties loadProperties(Properties props) throws IOException
    {
        if (props == null)
        {
            throw new IOException("null Properties instance passed in");
        }
        InputStream stream = this.inputStream();
        if (stream == null)
        {
            throw new IOException("cannot locate Properties resource: " + this.getResourcePath());
        }
        props.load(this.inputStream());
        return props;
    }

    protected InputStream createInputStream()
    {
        InputStream in = this.getClass().getClassLoader().getSystemResourceAsStream(this.getResourcePath());
        if( in == null )
        {
            in = this.getClass().getClassLoader().getResourceAsStream(this.getResourcePath());
            if ( in == null )
            {
                in = this.loadInputStreamAsUrl() ;
            }
        }
        return in;
    }

    protected InputStream loadInputStreamAsUrl()
    {
        InputStream in = null;
        URL url = this.urlOrNull();
        if ( url != null )
        {
            try
            {
                in = (InputStream) url.getContent();
            }
            catch(IOException ioe)
            {
            }
        }
        return in;
    }

    protected URL urlOrNull()
    {
        URL url = this.urlFromFileURIOrNull();
        if ( url == null )
        {
            url = this.urlFromProtocolSchemeOrNull();
        }
        return url;
    }

    protected URL urlFromFileURIOrNull()
    {
        URL url = null;
        try
        {
            URL tmpUrl = new File(this.getResourcePath()).toURI().toURL();
            Object content = tmpUrl.getContent();
            if ( content == null )
            {
                return null;
            }
            url = tmpUrl;
        }
        catch(MalformedURLException e)
        {
        }
        catch(IOException e)
        {
        }
        return url;
    }

    protected URL urlFromProtocolSchemeOrNull()
    {
        URL url = null;
        try
        {
            url = new URL( this.getResourcePath() );
            Object content = url.getContent();
            if ( content == null )
            {
                return null;
            }
        }
        catch(MalformedURLException e)
        {
        }
        catch(IOException e)
        {
        }
        return url;
    }

    public String toString() { return "ResourceLoader{" + this.getResourcePath()+"}"; }

    public static void read(InputStream in) throws Throwable
    {
        byte[] buf = new byte[1024];
        int read = in.read(buf, 0, 1024);
        OutputStream os = new FileOutputStream(File.createTempFile("resourceloader_",".props"));
        while (read > 0)
        {
            os.write(buf, 0, read);
            read = in.read(buf, 0, 1024);
        }
        os.close();
        in.close();
    }

    public static void main(String[] argv) throws Throwable
    {
        ResourceLoader rl = new ResourceLoader(argv[0]);
        InputStream in = rl.inputStream();
        read(in);
    }

} // End Class -- ResourceLoader
