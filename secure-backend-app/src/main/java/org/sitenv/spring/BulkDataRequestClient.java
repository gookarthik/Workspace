package org.sitenv.spring;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.security.Key;
import java.security.KeyFactory;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Date;
import java.util.HashMap;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.client.LaxRedirectStrategy;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;
import org.sitenv.spring.model.ExtractionTask;
import org.sitenv.spring.service.ExtractionTaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Controller
@RequestMapping("")
@PropertySource(value = { "classpath:application.properties" })
public class BulkDataRequestClient {
	
	@Autowired
    private Environment environment;
	
	@Autowired
	private ExtractionTaskService etService;
	
	 
	/*GET    http://localhost:8080/secure-backend-app/fhir/Patient/$export		Accept	text/plain ( Run in port 8080 with Apache 8.5)
	  200 OK
	  Gives Client Assertion*/
	
	@RequestMapping(value="fhir/Patient/$export",method=RequestMethod.GET,produces="text/plain")
	@ResponseBody
	public String getResourceValues(HttpServletRequest request,HttpServletResponse response) throws Exception{
		
			String serverBase = environment.getRequiredProperty("serverbase");
			String queryString = request.getQueryString();
			String mode = request.getHeader("mode");
		   
	        String url = serverBase+request.getRequestURI().substring(request.getContextPath().length());
	        if(queryString!=null) {
	        	url = url+"?"+queryString;
	        }
	        
	        //String mode = environment.getRequiredProperty("mode");
	        String accessToken = null;
	        if(mode!=null && mode.equalsIgnoreCase("secure")) {
	        	
	        	accessToken = getAccessToken(request);
	        	System.err.println("===========accessToken========="+accessToken);//added
	        }
	        
	        String retVal = invokeURL(url, accessToken, request, response);
	        
	        String contentLocation = response.getHeader("Content-Location");
	        if(contentLocation!=null) {
	        	String[] splitList = contentLocation.split("/");
	        	String requestId = splitList[splitList.length-1];
	        	String uri = request.getScheme() + "://" +
		                request.getServerName() + 
		                ("http".equals(request.getScheme()) && request.getServerPort() == 80 || "https".equals(request.getScheme()) && request.getServerPort() == 443 ? "" : ":" + request.getServerPort() )
		                +request.getContextPath();
	        	response.setHeader("Content-Location", uri+"/bulkdata/"+requestId);
	        }
	        if(response.getStatus()==200) {
	        	response.setStatus(202);
	        }
	        
	     // save Extraction task details
	        ExtractionTask et = new ExtractionTask();
			et.setContentLocation(contentLocation);
			et.setRequestURL(url);
			et.setResponseBody(retVal);
			et.setStatus("Pending");
			et.setProcessFlag(false);
			et.setAuthenticationMode(mode);

			etService.saveOrUpdate(et);
	       System.err.println("==========retVal=============="+et.getResponseBody());//retVal);//added
	        return retVal;
		
	}
	
	@RequestMapping(value="fhir/Group/{id}/$export",method=RequestMethod.GET)
	@ResponseBody
	public String getGroupBulkData(@PathVariable Integer id, HttpServletRequest request,HttpServletResponse response) throws Exception{
		
			String serverBase = environment.getRequiredProperty("serverbase");
			String queryString = request.getQueryString();
			String mode = request.getHeader("mode");
		   
	        String url = serverBase+request.getRequestURI().substring(request.getContextPath().length());
	        if(queryString!=null) {
	        	url = url+"?"+queryString;
	        }
	        
	        //String mode = environment.getRequiredProperty("mode");
	        String accessToken = null;
	        if(mode!=null && mode.equalsIgnoreCase("secure")) {
	        	accessToken = getAccessToken(request);
	        }
	        String retVal = invokeURL(url, accessToken, request, response);
	        
	        String contentLocation = response.getHeader("Content-Location");
	        if(contentLocation!=null) {
	        	String[] splitList = contentLocation.split("/");
	        	String requestId = splitList[splitList.length-1];
	        	String uri = request.getScheme() + "://" +
		                request.getServerName() + 
		                ("http".equals(request.getScheme()) && request.getServerPort() == 80 || "https".equals(request.getScheme()) && request.getServerPort() == 443 ? "" : ":" + request.getServerPort() )
		                +request.getContextPath();
	        	response.setHeader("Content-Location", uri+"/bulkdata/"+requestId);
	        }
	        if(response.getStatus()==200) {
	        	response.setStatus(202);
	        }
	        
	        ExtractionTask et = new ExtractionTask();
			et.setGroupId(id);
			et.setContentLocation(contentLocation);
			et.setRequestURL(url);
			et.setResponseBody(retVal);
			et.setStatus("Pending");
			et.setProcessFlag(false);
			et.setAuthenticationMode(mode);

			etService.saveOrUpdate(et);
	        
	       return retVal;
	}
	
	 @RequestMapping(value="fhir/Group",method=RequestMethod.POST)
		@ResponseBody
	    public String postGroup(HttpServletRequest request,HttpServletResponse response, @RequestBody String payload) throws Exception {
	    	
	    	String serverBase = environment.getRequiredProperty("serverbase");
	    	String mode = request.getHeader("mode");
		   
	        String url = serverBase+request.getRequestURI().substring(request.getContextPath().length());
	    	
	        //String mode = environment.getRequiredProperty("mode");
	        String accessToken = null;
	        if(mode!=null && mode.equalsIgnoreCase("secure")) {
	        	accessToken = getAccessToken(request);
	        }
	        StringEntity entity = new StringEntity(payload,ContentType.APPLICATION_JSON);
	        
	        HttpClient httpClient = HttpClientBuilder.create().build();
	        HttpPost requestPost = new HttpPost(url);
	        requestPost.setEntity(entity);
	        //requestPost.addHeader("Authorization","bearer "+accessToken);
	        
	        HttpResponse httpResponse = httpClient.execute(requestPost);
	        response.setStatus(httpResponse.getStatusLine().getStatusCode());
	        response.setContentType("application/json+fhir;charset=UTF-8");
	        String json = EntityUtils.toString(httpResponse.getEntity());
			return json;
	    	
	    }
	
	@RequestMapping(value="bulkdata/{requestId}",method=RequestMethod.GET)
	@ResponseBody
	public String getContentLocationResponse(@PathVariable Integer requestId,HttpServletRequest request,
		       HttpServletResponse response) throws Exception {
		//http://localhost:8080/bulk-data-api/bulkdata/97
		String serverBase = environment.getRequiredProperty("serverbase");
		String mode = request.getHeader("mode");
		String url = serverBase+request.getRequestURI().substring(request.getContextPath().length());
		String clientUri = request.getScheme() + "://" + request.getServerName()
		+ ("http".equals(request.getScheme()) && request.getServerPort() == 80
				|| "https".equals(request.getScheme()) && request.getServerPort() == 443 ? ""
						: ":" + request.getServerPort())
		+ request.getContextPath();
		
		 //String mode = environment.getRequiredProperty("mode");
	        String accessToken = null;
	        if(mode.equalsIgnoreCase("secure")) {
	        	accessToken = getAccessToken(request);
	        }
		String retVal = invokeURL(url,accessToken, request, response);
		
		String linkHeader = response.getHeader("Link");
		if (linkHeader != null) {
			String[] splitList = linkHeader.split(",");
			
			StringBuilder customLinks = new StringBuilder();
			for (int i = 0; i < splitList.length; i++) {
				String[] linksList = splitList[i].replaceAll("[<>]", "").split("/");
				customLinks.append("<" + clientUri + "/bulkdata/download/" + linksList[linksList.length - 2] + "/"
						+ linksList[linksList.length - 1] + ">");

				if (i < splitList.length - 1) {
					customLinks.append(",");
				}

			}
			response.setHeader("Link", customLinks.toString());
		}
		
		return retVal;
	}
	
	@RequestMapping(value="fhir/{resource}",method=RequestMethod.GET,produces="text/plain")
	@ResponseBody
	public String getResource(@PathVariable String resource,HttpServletRequest request,HttpServletResponse response) throws Exception{
		
		
		String serverbase = environment.getProperty("serverbase");
		String mode = request.getHeader("mode");
		System.out.println("mode : "+mode);
		String url = serverbase+request.getRequestURI().substring(request.getContextPath().length());
		String queryString = request.getQueryString();
		if(queryString!=null) {
        	url = url+"?"+queryString;
        }
		
		HttpClient client = HttpClientBuilder.create().build();
        HttpGet bulkRequest = new HttpGet(url);
        //String mode = environment.getRequiredProperty("mode");
        String accessToken = null;
        if(mode!=null && mode.equalsIgnoreCase("secure")) {
        	accessToken = getAccessToken(request);
        }
        // add request header
        bulkRequest.addHeader("Prefer", "respond-async");
        if(accessToken!=null) 
        bulkRequest.addHeader("Authorization","bearer "+accessToken);
        
        HttpResponse bulkResponse = client.execute(bulkRequest);

        Header[] headers = bulkResponse.getAllHeaders();
        for (Header header : headers) {
        	if(!header.getName().equalsIgnoreCase("Transfer-Encoding")) {
        	response.setHeader(header.getName(), header.getValue());
        	}
        }
        
        	response.setContentType("application/json+fhir;charset=UTF-8");
	        String json = EntityUtils.toString(bulkResponse.getEntity());
	        
	        return json;
		
	}
	
	@RequestMapping(value="bulkdata/load/request/{id}",method=RequestMethod.GET)
	@ResponseBody
	public void loadRequestById(@PathVariable Integer id,HttpServletRequest request,
		       HttpServletResponse response) throws IOException {
		String serverBase = environment.getRequiredProperty("serverbase");
		String url = serverBase+request.getRequestURI().substring(request.getContextPath().length());
		response.sendRedirect(url);
	}
	
	public String invokeURL(String url, String accessToken, HttpServletRequest request, HttpServletResponse response) throws ClientProtocolException, IOException {
		 																														HttpClient client = HttpClientBuilder.create().build();
	        HttpGet bulkRequest = new HttpGet(url);

	        // add request header
	        bulkRequest.addHeader("Prefer", "respond-async");
	        bulkRequest.addHeader("Accept","application/fhir+json");
	        if(accessToken!=null) {
	        	bulkRequest.addHeader("Authorization","bearer "+accessToken);
	        }
	        
	        HttpResponse bulkResponse = client.execute(bulkRequest);
	        
	        Header[] headers = bulkResponse.getAllHeaders();
	        for (Header header : headers) {
	        	if(!header.getName().equalsIgnoreCase("Transfer-Encoding")) {
	        	response.setHeader(header.getName(), header.getValue());
	        	}
	        }
	        	response.setStatus(bulkResponse.getStatusLine().getStatusCode());
	        
	        BufferedReader rd = new BufferedReader(
		        	new InputStreamReader(bulkResponse.getEntity().getContent()));

		        StringBuffer result = new StringBuffer();
		        String line = "";
		        while ((line = rd.readLine()) != null) {
		        	result.append(line);
		        }
		        response.setContentType("application/xml+fhir;charset=UTF-8");
		        
		        return result.toString();
	}
	
	@RequestMapping(value="bulkdata/download/{id}/{fileName:.+}",method=RequestMethod.GET)
	@ResponseBody
	public void downloadFile(@PathVariable Integer id, @PathVariable String fileName,HttpServletRequest request,
		       HttpServletResponse response) throws IOException, ServletException {
		
		String serverBase = environment.getProperty("serverbase");
		
		String url = serverBase+request.getRequestURI().substring(request.getContextPath().length());
		
		response.sendRedirect(url);
		response.setStatus(302);
		
	}
	
	
	public File download(URL url, File dstFile) {
		CloseableHttpClient httpclient = HttpClients.custom()
				.setRedirectStrategy(new LaxRedirectStrategy()) // adds HTTP REDIRECT support to GET and POST methods 
				.build();
		try {
			HttpGet get = new HttpGet(url.toURI()); // we're using GET but it could be via POST as well
			File downloaded = httpclient.execute(get, new FileDownloadResponseHandler(dstFile));
			return downloaded;
		} catch (Exception e) {
			throw new IllegalStateException(e);
		} finally {
			IOUtils.closeQuietly(httpclient);
		}
	}
	
	static class FileDownloadResponseHandler implements ResponseHandler<File> {

		private final File target;

		public FileDownloadResponseHandler(File target) {
			this.target = target;
		}

		public File handleResponse(HttpResponse response) throws ClientProtocolException, IOException {
			InputStream source = response.getEntity().getContent();
			FileUtils.copyInputStreamToFile(source, this.target);
			return this.target;
		}
		
	}
	
	    
	    public String getAccessToken(HttpServletRequest request) throws Exception {
	    	try {
	    		System.err.println("==========Access Token is invoking============");
	    	String accessToken = null;
	    	String aud = environment.getRequiredProperty("aud");
	    	
	    	String client_assertion = getCompactJWS();
	    	System.out.println("=======================client_assertion="+client_assertion);//added
	        String json = "grant_type=client_credentials&scope=system/:resourceType.(read|write|*)&client_assertion_type=urn:ietf:params:oauth:client-assertion-type:jwt-bearer&client_assertion="+client_assertion;
	        StringEntity entity = new StringEntity(json,ContentType.DEFAULT_TEXT);
	        
	        HttpClient httpClient = HttpClientBuilder.create().build();
	        HttpPost requestPost = new HttpPost(aud);
	        requestPost.setEntity(entity);
	        
	        HttpResponse response = httpClient.execute(requestPost);
	        if(response.getStatusLine().getStatusCode()==200) {
	        BufferedReader rd = new BufferedReader(
                    new InputStreamReader(response.getEntity().getContent()));

	        StringBuffer result = new StringBuffer();
	        String line;
	        while ((line = rd.readLine()) != null) {
	        	result.append(line);
	        }	
	        
	        JSONObject payLoad = new JSONObject(result.toString());
            
            accessToken = payLoad.getString("access_token");
            if(request!=null) {
            HttpSession httpSession = request.getSession();
            HashMap<String , String> sessionMap = new HashMap<String, String>();
            sessionMap.put("access_token", accessToken);
            httpSession.setAttribute("access_token", accessToken);
            }
	        }
	        System.out.println("============================accessToken="+accessToken);//added
			return accessToken;
	    	}catch(Exception e) {
	    		System.out.println("error :"+e.getMessage());
	    		e.printStackTrace();
	    	}
	    	return null;
	    	
	    }
	    
	    
	    public String getCompactJWS() throws Exception {
	    	
	    	String issuer = environment.getProperty("iss");
	    	String keypath = environment.getProperty("keypath");
	    	String subject = environment.getProperty("sub");
	    	String aud = environment.getProperty("aud");
	    	
	    	
	    	File f = new File(keypath);
	    	DataInputStream dis = new DataInputStream(new FileInputStream(f));
	    	byte[] keyBytes = new byte[(int)f.length()];
	    	dis.readFully(keyBytes);
	    	dis.close();
	    	PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(keyBytes);
	    	Key key = KeyFactory.getInstance("RSA").generatePrivate(spec);
	    	String compactJws = Jwts.builder()
	    			  .setSubject(subject)
	    			  .setIssuedAt(new Date())
	    			  .setIssuer(issuer)
	    			  .setAudience(aud)
	    			  .signWith(SignatureAlgorithm.RS256, key)
	    			  .compact();
	    	System.out.println("========compactJws="+compactJws);//added
			return compactJws;
	    	
	    }
	    
	    	    
}
