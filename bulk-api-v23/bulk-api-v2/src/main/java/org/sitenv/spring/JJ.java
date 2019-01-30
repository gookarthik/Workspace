package org.sitenv.spring;

import java.util.LinkedHashMap;

import org.json.JSONObject;

public class JJ {

	public static String jajn(LinkedHashMap<String,String> h1)
	{
		/*
		LinkedHashMap<String,String> h1 = new LinkedHashMap<String,String>();
		h1.put("hello","world");
		h1.put("albert","Einstien");
		h1.put("Neils", "bhor");
		h1.put("thomas", "mendel");
		 */
		
		String my = new JSONObject(h1).valueToString(h1);
		return my;
	}
}
