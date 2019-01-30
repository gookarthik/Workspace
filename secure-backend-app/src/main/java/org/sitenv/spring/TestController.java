package org.sitenv.spring;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.security.Key;
import java.security.KeyFactory;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Date;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.json.JSONObject;
import org.sitenv.spring.service.ExtractionTaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Controller
@RequestMapping("")
@PropertySource(value = { "classpath:application.properties" })
public class TestController {

	@Autowired
    private Environment environment;
	
	@Autowired
	private ExtractionTaskService etService;


		/*GET    http://localhost:8080/secure-backend-app/fhir/Test/$export		Accept	text/plain ( Run in port 8080 with Apache 8.5)
		200 OK
		Gives Client Assertion*/
	
	@RequestMapping(value="fhir/Test/$export",method=RequestMethod.GET,produces="text/plain")
	@ResponseBody
	public void getResourceValues(HttpServletRequest request,HttpServletResponse response) throws Exception{
		
	
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
        
        HttpResponse resp = httpClient.execute(requestPost);
        if(resp.getStatusLine().getStatusCode()==200) {
        BufferedReader rd = new BufferedReader(
                new InputStreamReader(resp.getEntity().getContent()));

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
	//	return accessToken;
    	}catch(Exception e) {
    		System.out.println("error :"+e.getMessage());
    		e.printStackTrace();
    	}
    	//return null;
    	
    }
	@Bean
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
