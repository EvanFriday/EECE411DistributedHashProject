package clientserver;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.URL;

public class IpTools {

    public static String getIp() throws Exception {
        URL whatismyip = new URL("http://checkip.amazonaws.com");
        BufferedReader in = null;
        try {
            in = new BufferedReader(new InputStreamReader(
                    whatismyip.openStream()));
            String ip = in.readLine();
            return ip;
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    public static String getIpFromHostname(String hostname) throws Exception {  
        String ip = InetAddress.getByName(hostname).getHostAddress();
    	return ip;
}  
    public static String getHostnameFromIp(String ip) throws Exception {
    	String hostname = InetAddress.getByName(ip).getHostName();
    	return hostname;
    }
    
    
    
}