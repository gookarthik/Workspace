package org.sitenv.spring.dao;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.json.JSONArray;
import org.json.JSONObject;

public class FetchObservation 
{
	static StringBuffer result = null;
	static String line = null;
	static HttpClient client = null;
	static HttpGet request = null;
	static HttpResponse response = null;
	static BufferedReader rd = null;
	static JSONObject data = null,entry = null,res=null;
	static JSONArray entries = null;
	static String id = "";
	static int u = 0;
	static String justdate = "",url="";
	static List<String> readthelist = null;
	static List<String> listread = null;
	static List<String> readid = null;
	static String full_url = null;
	static String res_type = null;

	private static List<String> readFile(String path) {
		String line = null;
		readthelist = new ArrayList<String>();
		try {
			BufferedReader reader = new BufferedReader(new FileReader(path));
			while((line = reader.readLine()) != null){
				readthelist.add(line);

			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return readthelist;
	}

	public void restURL(String url, String resourceName) throws Exception 
	{

		listread = readFile("C:\\Users\\batca\\Desktop\\codes1.txt");			// listread contains the codes now . . .
		readid = readFile("C:\\Users\\batca\\Desktop\\idbbAll.txt");	

		FileWriter fw=new FileWriter("C:\\Users\\batca\\Desktop\\SamCOmes\\ob_url5.txt",true);
		FileWriter fw1=new FileWriter("C:\\Users\\batca\\Desktop\\SamCOmes\\ob_id5.txt",true);

		for(int j = 0 ; j<readid.size() ;j++)																//readid.size()
		{	
			System.out.println("Start time (1000 pound ho) : "+System.nanoTime());
			for(int o = 0 ;o<listread.size();o++)
			{

				url = "https://open-ic.epic.com/FHIR/api/FHIR/DSTU2/Observation?patient="+readid.get(j)+"&code="+listread.get(o)+"&date=lt20180518";
				client = HttpClientBuilder.create().build();
				request = new HttpGet(url);														// http request

				request.addHeader("Accept","application/json+fhir");
				response = client.execute(request);										// Execute http request

				if(response.getStatusLine().getStatusCode()==200) 
				{
					rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));

					result = new StringBuffer();
					line = "";

					while ((line = rd.readLine()) != null) 
					{
						result.append(line);
					}

					System.out.println(result.toString());

					try {
						data = new JSONObject(result.toString());

						if(data.has("entry"))
						{
							entries = data.getJSONArray("entry");

							for (int  x= 0; x < entries.length(); x++) 
							{
								entry = entries.getJSONObject(x);
								if(entry.has("fullUrl"))
								{
									full_url = entry.getString("fullUrl");
									try
									{
										fw1.write(full_url+ System.getProperty( "line.separator" ));
									}
									catch(Exception e)
									{
										e.printStackTrace();
									}
								}
								if(entry.has("resource"))
								{
									res = entry.getJSONObject("resource");


									if(res.has("id"))
									{
										id = res.getString("id");
										System.out.println(id);
										try
										{
											u = Integer.parseInt(id);
										}
										catch(Exception e)
										{
											fw.write(id+ System.getProperty( "line.separator" ));
										}	
									}

								}
							}//end for
						}//end if
					}
					catch(Exception e )
					{
						e.printStackTrace();
					}
				}//end resp if
			}//end for 2
			System.out.println("End time (1000 pound ho) : "+System.nanoTime());
		}//end for1
		fw.close();
		fw1.close();
	}

	public static void main(String[] args) {
		FetchObservation f1 = new FetchObservation();

		try {
			f1.restURL("","getResults");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
