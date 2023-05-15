package net.skidcode.gh.server;

import java.net.URL;
import java.net.URLClassLoader;

public class ServerClassLoader extends URLClassLoader{

	public ServerClassLoader(URL[] urls, ClassLoader parent) {
		super(urls, parent);
	}
	
	public void addUrl(URL url) {
		super.addURL(url);
	}
	
}
