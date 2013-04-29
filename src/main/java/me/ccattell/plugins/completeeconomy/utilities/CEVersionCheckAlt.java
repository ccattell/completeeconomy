package me.ccattell.plugins.completeeconomy.utilities;

import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Locale;
import javax.xml.parsers.DocumentBuilderFactory;
import me.ccattell.plugins.completeeconomy.CompleteEconomyAlt;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 *
 * @author Charlie
 */
public class CEVersionCheckAlt {

    private CompleteEconomyAlt plugin;
    private URL filesFeed;
    private String version;
    private String link;
    String this_version;

    public CEVersionCheckAlt(CompleteEconomyAlt plugin, String url) {
        this.plugin = plugin;
        try {
            this.filesFeed = new URL(url);
        } catch (MalformedURLException e) {
            System.out.println("Could not get URL: " + e.getMessage());
        }
    }

    public boolean updateNeeded() {
        try {
            InputStream input = this.filesFeed.openConnection().getInputStream();
            Document document = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(input);
            this_version = plugin.getDescription().getVersion();
            System.out.println(this_version);
            Channel UpdateChannel = Channel.valueOf(plugin.getConfig().getString("System.UpdateChannel").toUpperCase(Locale.ENGLISH));
            // channel found?
            boolean release = false;
            boolean beta = false;
            // versions
            float r1 = 0;
            float b1 = 0;
            float a1 = 0;
            // subversions
            int r2 = 0;
            int b2 = 0;
            int a2 = 0;
            // links
            String r_link = "";
            String b_link = "";
            String a_link = "";
            // versions
            String r_vers = "";
            String b_vers = "";
            String a_vers = "";
            int count = 0;
            int i = 0;
            int num_files = document.getElementsByTagName("item").getLength();
            while (count < 3 && i < num_files) {
                Node latestFile = document.getElementsByTagName("item").item(i);
                NodeList children = latestFile.getChildNodes();
                String tmp = children.item(1).getTextContent().replace("TARDIS ", "");
                String[] data = tmp.split("-");
                try {
                    if (data.length < 2 && !release) {
                        // found latest release build
                        r1 = Float.parseFloat(data[0]);
                        r_link = children.item(3).getTextContent();
                        r_vers = tmp;
                        release = true;
                        ++count;
                    } else if (data.length > 1) {
                        if (data[1].equals("beta") && !beta) {
                            // found latest beta build
                            b1 = Float.parseFloat(data[0]);
                            if (data.length > 2) {
                                b2 = Integer.parseInt(data[2]);
                            }
                            b_link = children.item(3).getTextContent();
                            b_vers = tmp;
                            beta = true;
                            ++count;
                        }
                        if (data[1].equals("pre")) { // change this to alpha for release
                            // found latest alpha build
                            a1 = Float.parseFloat(data[0]);
                            if (data.length > 2) {
                                a2 = Integer.parseInt(data[2]);
                            }
                            a_link = children.item(3).getTextContent();
                            a_vers = tmp;
                            ++count;
                        }
                    }
                } catch (NumberFormatException nfe) {
                    System.out.println("Could not parse number: " + nfe.getMessage());
                }
                i++;
            }
            switch (UpdateChannel) {
                case RELEASE:
                    this.link = r_link;
                    this.version = r_vers;
                    return compareVersions(r1, r2);
                case BETA:
                    this.link = b_link;
                    this.version = b_vers;
                    return compareVersions(b1, b2);
                default:
                    this.link = a_link;
                    this.version = a_vers;
                    return compareVersions(a1, a2);
            }
        } catch (Exception e) {
            System.out.println("Could not get latest channels: " + e);
        }
        return false;
    }

    public String getVersion() {
        return version;
    }

    public String getLink() {
        return link;
    }

    private boolean compareVersions(float a, int b) {
        int minor = 0;
        // get current version
        String[] running = this_version.split("-");
        float major = Float.parseFloat(running[0]);
        if (running.length > 2) {
            minor = Integer.parseInt(running[2]);
        }
        if (a > major) {
            return true;
        }
        if (a == major && b > minor) {
            return true;
        } else {
            return false;
        }
    }

    private enum Channel {

        RELEASE, BETA, ALPHA;
    }
}
