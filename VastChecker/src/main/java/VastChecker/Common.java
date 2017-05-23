package main.java.VastChecker;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Properties;
import java.util.Scanner;

import org.apache.log4j.Logger;


public class Common {
	
	private static final Logger log = Logger.getLogger(VastChecker.class);
	
	 public static Properties propLoad() {
	      Properties prop = new Properties();
	      FileInputStream stream = null;
	      InputStreamReader reader = null;

	      try {
	         stream = new FileInputStream(new File("VastChecker.conf"));
	         reader = new InputStreamReader(stream, "Windows-1251");
	         prop.load(reader);
	      } catch (Exception ex) {
	    		log.error(ex.getLocalizedMessage(),ex);
	      }

	      return prop;
	   }
	 
	 public static HttpURLConnection makeHttpReq(String url_string, String cookie)
		{
			try{
				  
					URL url = new URL(url_string);
					log.info("URL is: "+url_string);
					log.info("Request's cookies are: "+cookie);
				    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
				    conn.setRequestMethod("GET");
				    conn.setRequestProperty("Cookie", cookie);			 	
				    conn.connect();
				     return conn;
				    }catch(IOException ex)
				    {
				    log.error(ex.getLocalizedMessage(),ex);
				     return null;
				    }
		}

	 public static void print(String str) {
	      System.out.println(str);
	   }
	 
	 public static String GetRespBody (InputStream stream)
		{
			
			try {
				String resp="";
			Scanner in = new Scanner(stream);
			while(in.hasNextLine())
			{
				resp = resp + in.nextLine();
				
			}
			return resp;
			}
			catch (Exception ex)
			{
				log.error(ex.getLocalizedMessage(),ex);
				return null;
				
			}
		}
	 
}
