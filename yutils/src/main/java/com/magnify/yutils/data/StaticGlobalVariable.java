
package com.magnify.yutils.data;

import android.content.Context;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
public class StaticGlobalVariable  {

	public static String getShortCityName(String chooseaddress){
		String shortCityName = chooseaddress;
		Pattern p = null;
		String[] str=null;
		
		if( chooseaddress.contains("province")){
			if(chooseaddress.contains("area")){
	    		p=Pattern.compile("(?s)province(.*?)area");
	    	}else if(chooseaddress.contains("county")){
	    		p=Pattern.compile("(?s)province(.*?)county");
	    	}else if(chooseaddress.contains("qi")){
	    		p=Pattern.compile("(?s)province(.*?)qi");
	    	}else{
	    		p=Pattern.compile("(?s)province(.*?)city(.*?)city");
	    		str = chooseaddress.split("city");
	    	}
	    	
	        Matcher m=p.matcher(chooseaddress);
	        m.find();
	        shortCityName =  m.group(1);   //Get similar :PanYu GuangZhou
		}
		
		shortCityName = shortCityName.replace("city","");
		if(str!=null)
			shortCityName =  shortCityName+str[1];
		
		return shortCityName;
	}
	
	/**
	 2      *get json data from local files
	 3      * 
	 4      * @param fileName
	 5      * @return
	 6      */
	 public static String getJson(String fileName,Context context) {
	  
	          StringBuilder stringBuilder = new StringBuilder();
	       try {
	             BufferedReader bf = new BufferedReader(new InputStreamReader(
	                     context.getAssets().open(fileName)));
	             String line;
	             while ((line = bf.readLine()) != null) {
	                 stringBuilder.append(line);
	             }
	         } catch (IOException e) {
	             e.printStackTrace();
	         }
	         return stringBuilder.toString();
	     }

}