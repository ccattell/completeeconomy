package me.ccattell.plugins.completeeconomy.utilities;

import me.ccattell.plugins.completeeconomy.CompleteEconomy;

import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
/**
 *
 * @author Charlie
 */

public class CEVersionCheck {
    
    private CompleteEconomy plugin;
    private URL filesFeed;
    
    public CEVersionCheck(CompleteEconomy plugin, String url){
        this.plugin = plugin;
        try{
            this.filesFeed = new URL(url);
        }catch (MalformedURLException e){
            e.printStackTrace();
        }
    }
    
    public boolean updateNeeded(){
        try{
            InputStream input = this.filesFeed.openConnection().getInputStream();
            Document document = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(input);
            
            Node latestFile = document.getElementsByTagName("item").item(0);
            
        }catch(Exception e){
            e.printStackTrace();
        }
        
        return false;
    }
}
