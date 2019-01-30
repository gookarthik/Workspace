package org.sitenv.spring.dao;

import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.json.JSONArray;
import org.json.JSONObject;

public class FetchRest 
{
	static StringBuffer result = null;
	static String line = null;;
	static HttpClient client = null;
	static HttpGet request = null;
	static HttpResponse response = null;
	static BufferedReader rd = null;
	static JSONObject data = null,entry = null,res=null;
	static JSONArray entries = null;
	static String id = "";
	static int u = 0;
	static String justdate = "",url="";
	public static List<LocalDate> getDatesBetweenUsingJava8(LocalDate startDate, LocalDate endDate) 
	{ 

		long numOfDaysBetween = ChronoUnit.DAYS.between(startDate, endDate); 
		return IntStream.iterate(0, i -> i + 1)
				.limit(numOfDaysBetween)
				.mapToObj(i -> startDate.plusDays(i))
				.collect(Collectors.toList()); 
	}

	private void ObservationFetch(String url, String resourceName) throws Exception 
	{

		//------------------------------------------------LIST DATES-------------------------------------------------------------------

		String dateStr_1="30-Dec-1989";
		DateTimeFormatter formatter_1=DateTimeFormatter.ofPattern("dd-MMM-yyyy");
		LocalDate localDate_1= LocalDate.parse(dateStr_1,formatter_1);

		String dateStr_2="01-Jan-1990";
		DateTimeFormatter formatter_2=DateTimeFormatter.ofPattern("dd-MMM-yyyy");
		LocalDate localDate_2= LocalDate.parse(dateStr_2,formatter_2);

		System.out.println("Date 1 : "+dateStr_1+"\n Date 2 : "+dateStr_2);

		List<LocalDate> listDates= getDatesBetweenUsingJava8(localDate_1,localDate_2);				// listDates contains the list of dates between the 2 dates

		System.out.println("Result fetched in listDates list");

		System.out.println("sample 5 records");
		for (int i = 0; i < 2; i++) 
		{
			System.out.println(i+" \t : "+listDates.get(i));
		}

		//-------------------------------------------------LIST DATES------------------------------------------------------------------

		FileWriter fw=new FileWriter("C:\\Users\\batca\\Desktop\\fileids.txt",true);

		for(int j = 0 ; j< listDates.size();j++)
		{
			url = "https://open-ic.epic.com/FHIR/api/FHIR/DSTU2/Patient?birthdate="+listDates.get(j).toString();
			System.out.println("----------------------------------------");
			System.out.println("Date now :"+listDates.get(j).toString());
			
			client = HttpClientBuilder.create().build();
			request = new HttpGet(url);														// http request

			justdate = listDates.get(j).toString();

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
		}//end for
		fw.close();
	}

	public static void main(String[] args) {
		FetchRest f1 = new FetchRest();

		try {
			f1.ObservationFetch("https://open-ic.epic.com/FHIR/api/FHIR/DSTU2/Patient","getResults");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
