package me.ccattell.plugins.completeeconomy;

import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class UpdateChecker {
	
	private CompleteEconomy plugin;
	private URL filesfeed;
	
	private String updateString;
	
	public UpdateChecker(CompleteEconomy plugin, String url){
		this.plugin = plugin;
		
		try {
			this.filesfeed = new URL(url);
		} catch (MalformedURLException e) {e.printStackTrace();}
	}
	
	public boolean updateNeeded(){
		
		if(plugin.getConfig().getBoolean("UpdateMessage")){
			try {
				InputStream input = this.filesfeed.openConnection().getInputStream();
				Document document = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(input);
			
				Node latestFile = document.getElementsByTagName("item").item(0);
				NodeList children = latestFile.getChildNodes();
				
				//Remove letters from the second line to determine version number and gather the link to the file from the fourth line
				String version = children.item(1).getTextContent().replaceAll("[ a-zA-Z ]", "");
				String link = children.item(3).getTextContent();
				
				//Message that is displayed to OPs and the console when starting the server
				this.updateString = "&9"+plugin.getName()+ "&bA new version is available: &2V"+version+"! &bYou can download it here: &2"+link;
				
				if(!plugin.getDescription().getVersion().equals(version)){
					return true;
				}
				
			} catch (Exception e) {return false;}
		}
		
		return false;
	}
	
	public String getUpdateString(){
		return this.updateString;
	}
}