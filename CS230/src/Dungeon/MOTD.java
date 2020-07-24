package Dungeon;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
/**
 * This class is in charge of fetching the "Message Of The Day"
 * @author 975359
 * @version 1.0
 * @since 03-12-2019
 */
public class MOTD {
	private static final String PUZ_URL = "http://cswebcat.swan.ac.uk/puzzle";//The url path to the unsolved puzzle
	private static final String SOL_URL = "http://cswebcat.swan.ac.uk/message?solution=";//The base url path to the solution
	private static final String FAIL_MSG = "MOTD FAILURE!";//The String to return in the event that getHTTP throws an exception
	
	/**
	 * This method is used to load and unscramble the message of the day from the API
	 * @return The message of the day
	 */
	public static String getMOTD() {
		String output = "";
		try {
			output = getHTTP(SOL_URL+unscramble(getHTTP(PUZ_URL)));//gets the puzzle, solves it and gets the message of the day
		} catch (IOException e) {
			output = FAIL_MSG;
			System.out.println("Error whilst getting message of the day. Is the URL down?");
		}
		return output;
	}
	
	/**
	 * This method is used to unscramble the scrambled puzzle 
	 * @param s The string to apply the "unscramble" function to
	 * @return The unscrambled string
	 */
	private static String unscramble(String s) {
		String retS = "";
		for(int i = 0; i<s.length();i++) {
			int asciiValue = (int)s.charAt(i);
			if(i%2 ==0) {
				asciiValue += (asciiValue+1)>90?-25:1;// shifts the value 
				//of asciiValue by 1 unless the value would be out of 
				//the alphabet in which case it takes 25
			}else {
				asciiValue += (asciiValue-1)<65?25:-1;// shifts the value 
				//of asciiValue by -1 unless the value would be out of 
				//the alphabet in which case it adds 25
			}
			retS += (char) asciiValue;
			
		}
		return retS;
	}
	/**
	 * This method gets the text that is displayed on a webpage
	 * @param urlAsString The URL to fetch the data from as a String
	 * @return The contents of the webpage
	 * @throws IOException when the URL is inaccessible or the page is empty
	 */
	private static String getHTTP(String urlAsString) throws IOException  {
		URL url =new URL(urlAsString);
	    HttpURLConnection conn = (HttpURLConnection) url.openConnection();//Establishes a connection from the URL
	    conn.setRequestMethod("GET");
	    BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
	    String line;
	    String result = "";
	    while ((line = br.readLine()) != null) {
	       result+=line;
	    }
	    br.close();
	    return result;//the contents of the webpage
	}
}
