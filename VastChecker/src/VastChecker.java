import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;




public class VastChecker {
	
	private static final Logger log = Logger.getLogger(VastChecker.class);
	private static ArrayList<String> BadTrackers = new ArrayList<String>();
	
	public static boolean checkVast(String vast)
	{
		int counter = 0;
		ArrayList<String> trackers = new ArrayList<String>();
		Pattern p = Pattern.compile(Common.propLoad().getProperty("TrackersRegexp"));
		Matcher m = p.matcher(vast);
		while(m.find())
		{
		   if(m.group(1).trim().length()!=0) trackers.add(m.group(1)); 
		}
		
		
		for (int i=0; i<trackers.size(); i++ )
		{
			boolean validUrl=true;
			String tracker = trackers.get(i);
			Pattern pattern = Pattern.compile("[А-Яа-яЁё]+");
			 Matcher mat = pattern.matcher(tracker);
			 try {
		            URL url = new URL(tracker);
		        }
		        catch(MalformedURLException e) {
		        	log.error(e.getLocalizedMessage(),e);
		        	validUrl=false;
		        	
		        }
			 
			if(tracker.contains("[")|tracker.contains("]")|!tracker.contains("http")|tracker.contains(" ")|mat.find()|validUrl==false)
			{
				log.error("Bad tracker in vast: "+tracker);
				BadTrackers.add(tracker);
				counter=counter+1;
			}
			
		}

		if (counter!=0) return false;
		else return true;
	}
	
	public static  boolean checkXML (File filename)
	{
		try {
		
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
		   dBuilder.parse(filename);
		}
		catch (Exception ex)
		{
			log.error(ex.getLocalizedMessage(),ex);
			return false;
		}
	return true;
	}
	
	public static void saveToFile (String filename, String text)
	{
		try {
		    BufferedWriter out = new BufferedWriter(new FileWriter(filename));
		    out.write(text);                                   
		    out.close();
		}
		catch (IOException e)
		{
			log.error(e.getLocalizedMessage(),e);

		}
	}
	

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String random = String.valueOf(Math.round(Math.random() * 1.0E9D));
		String url = Common.propLoad().getProperty("URL");
		String cookie = Common.propLoad().getProperty("Cookie");
		String puidString = Common.propLoad().getProperty("PuidString");
		Date respDate = new Date();
		SimpleDateFormat format = new SimpleDateFormat("ddMMyyyy_HHmmss");
		
		HttpURLConnection conn= Common.makeHttpReq(url+"&random="+random+puidString, cookie);
		try
		{
		InputStream in = conn.getInputStream();
		String resp = Common.GetRespBody(in);
		String filename="vast.xml";
		log.debug("Response is: "+resp);
		saveToFile(filename, resp);
		if (checkXML(new File(filename))==false|checkVast(resp)==false)
		{
			log.info("Response is: "+resp);
			String errorrFilename="errorVast"+format.format(respDate)+".xml";
			System.err.println("BAD-VAST-send-email:"+errorrFilename);
			for (int i=0; i<BadTrackers.size(); i++ )
			{
				System.err.println("BAD-TRACKER is: "+BadTrackers.get(i));
			}
			saveToFile(errorrFilename, resp);
			
		}
		else log.info("Vast is OK, without errorrs in trackers and XML is OK");
		}
		catch (Exception ex)
		{
			log.error(ex.getLocalizedMessage(),ex);
		}
		
		
	}

}
