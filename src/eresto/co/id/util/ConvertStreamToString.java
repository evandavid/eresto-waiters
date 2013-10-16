package eresto.co.id.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class ConvertStreamToString {
	 public ConvertStreamToString() {
	 }
	 
	 public String doConvert(InputStream is){
		 BufferedReader reader = new BufferedReader(new InputStreamReader(is));
	    	StringBuilder sb = new StringBuilder();
	    	 
	    	String line = null;
	    	try {
	    		while ((line = reader.readLine()) != null) {
	    			sb.append(line + "\n");
	    		}
	    	} catch (IOException e) {
	    		e.printStackTrace();
	    	} finally {
	    		try {
	    			is.close();
	    		} catch (IOException e) {
	    			e.printStackTrace();
	    		}
	    	}
	    	return sb.toString();
	 }
}
