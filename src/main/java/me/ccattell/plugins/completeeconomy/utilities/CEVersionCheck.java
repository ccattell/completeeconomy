package me.ccattell.plugins.completeeconomy.utilities;

import me.ccattell.plugins.completeeconomy.CompleteEconomy;

import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import javax.xml.parsers.DocumentBuilderFactory;
import org.bukkit.ChatColor;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
/**
 *
 * @author Charlie
 */

public class CEVersionCheck {
    
    private CompleteEconomy plugin;
    private URL filesFeed;
    private String version;
    private String link;
    
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
            
            String UpdateChannel = plugin.getConfig().getString("System.UpdateChannel");
            boolean test = false;
            int i = 0;
            int num_files = document.getElementsByTagName("item").getLength();
            plugin.console.sendMessage(plugin.pluginName + "Update Channel: " + UpdateChannel);
            while(test == false && i < num_files){
                Node latestFile = document.getElementsByTagName("item").item(i);
                NodeList children = latestFile.getChildNodes();
                version = children.item(1).getTextContent().replace("ComleteEconomy ", "");
                link = children.item(3).getTextContent();
                if(UpdateChannel.equalsIgnoreCase("release")) {
                    if(!version.contains("alpha") && !version.contains("beta")){
                        test = true;
                    }
                } else if(UpdateChannel.equalsIgnoreCase("beta")) {
                    if(!version.contains("alpha")){
                        test = true;
                    }
                } else {
                    test = true;
                }
                i++;
            }
            if(test == false){
                plugin.console.sendMessage(plugin.pluginName + "There are no files to test in your channel");
            }else{
                if(plugin.getDescription().getVersion().equals(version)){
                    plugin.console.sendMessage(plugin.pluginName + "Congratulations, you are running the latest version of CompleteEconomy!");
                }else{
                    String[] data = version.split("-");
                    String version1 = version;
                    if(data.length == 1){
                        version1 = version1.concat("-release");
                        version1 = version1.concat("-1");
                    }else if(data.length == 2){
                        version1 = version1.concat("-1");
                    }
                    version1=version1.toLowerCase().replace("-release-", "4");
                    version1=version1.toLowerCase().replace("-pre-", "3");
                    version1=version1.toLowerCase().replace("-beta-", "2");
                    version1=version1.toLowerCase().replace("-alpha-", "1");
                    double value1 = Double.parseDouble(version1);
                    
                    String[] data1 = plugin.getDescription().getVersion().split("-");
                    String version2 = plugin.getDescription().getVersion();
                    if(data1.length == 1){
                        version2 = version2.concat("-release");
                        version2 = version2.concat("-1");
                    }else if(data1.length == 2){
                        version2 = version2.concat("-1");
                    }
                    version2=version2.toLowerCase().replace("-release-", "4");
                    version2=version2.toLowerCase().replace("-pre-", "3");
                    version2=version2.toLowerCase().replace("-beta-", "2");
                    version2=version2.toLowerCase().replace("-alpha-", "1");
                    double value2 = Double.parseDouble(version2);

                    if(value1 > value2){
                        plugin.console.sendMessage(plugin.pluginName + "A new version is available: " + ChatColor.GOLD + version + ChatColor.RESET);
                        plugin.console.sendMessage(plugin.pluginName + "Get it from: " + ChatColor.GOLD + link + ChatColor.RESET);
                    }else if(value1 < value2){
                        plugin.console.sendMessage(plugin.pluginName + "You are using an unreleased version of the plugin!");
                    }
                }
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        
        return false;
    }
    
}
