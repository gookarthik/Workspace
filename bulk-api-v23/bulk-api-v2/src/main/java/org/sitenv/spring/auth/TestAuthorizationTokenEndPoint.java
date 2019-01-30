/*package org.sitenv.spring.auth;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.spec.X509EncodedKeySpec;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;


import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.codec.binary.Base64;
import org.apache.oltu.oauth2.as.issuer.MD5Generator;
import org.apache.oltu.oauth2.as.issuer.OAuthIssuer;
import org.apache.oltu.oauth2.as.issuer.OAuthIssuerImpl;
import org.apache.oltu.oauth2.common.message.types.GrantType;
import org.json.JSONObject;
import org.sitenv.spring.dao.AuthTempDao;
import org.sitenv.spring.model.DafAuthtemp;
import org.sitenv.spring.model.DafClientRegister;
import org.sitenv.spring.service.ClientRegistrationService;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import ch.qos.logback.classic.Logger;
import io.jsonwebtoken.Jwts;

//	POST 	http://localhost:9090/bulk-data-api/token    Content-Type    x-www-form-urlencoded
//	BODY 	grant_type=client_credentials&scope=system/:resourceType.(read|write|*)&client_assertion_type=urn:ietf:params:oauth:client-assertion-type:jwt-bearer&client_assertion=eyJhbGciOiJSUzI1NiJ9.eyJzdWIiOiJidWxrIGRhdGEgYXBpVnhBSHRyQXF0MSIsImlhdCI6MTUzMTMxMDc3NSwiaXNzIjoiaHR0cHM6Ly9zaXRlbnYub3JnIiwiYXVkIjoiaHR0cDovL2xvY2FsaG9zdDo5MDkwL2J1bGstZGF0YS1hcGkvdG9rZW4ifQ.sC0JCOmzkuDAXRgS02vAI8GL2neYW_bnBo-etSCrjUJ0eXQAtviuj5nlg-Mc-rgNW9sdeKVz9g7iRyeX1pJE7rpTAgoJv0i1bCgcCsfAZ89BTFc2NCWWn1-AevbjlQtiGd2hwrXqMY3mZrr-hj1Lj7AHsU1r126Lz-DYCRzj3sSEcYT6muvSBU0IUsGJu7TNBghyC8_lGafYRFfN0mqby-58xgcKFily3Ouuim08FNpLoR2SY6QuUsHRfg11FrN-TZLeW_gZEIE4AoRexIuilbdD4zLMHQnoyAX-iAkFKaXtzKs7e8c-nZ9gSmmQ8HHjaf4yM9Q1qkppoJshupxRbQ
//    200 OK
//	Get Access Tocken



@Controller
@RequestMapping("/token")
public class TestAuthorizationTokenEndPoint extends HttpServlet {
	private static final long serialVersionUID = 1L;

	Logger log = (Logger) LoggerFactory.getLogger(TestAuthorizationTokenEndPoint.class);

	@Autowired
	private ClientRegistrationService service;

	@Autowired
	private AuthTempDao dao;

	@RequestMapping(value = "", method = RequestMethod.POST)
	@ResponseBody
	public void getAuthorization(HttpServletRequest request, HttpServletResponse response) throws Exception {

		PrintWriter out = response.getWriter();

		log.info("Token url:" + request.getRequestURL().append('?').append(request.getQueryString()));

		// String credentials = request.getHeader("Authorization");
		String client_id = null;
		String client_secret = null;
		String code = null;
	
		String grant_type = null;
		// String scope = null;
		String client_assertion_type = null;
		String client_assertion = null;

		

			StringBuffer sb = new StringBuffer();
			BufferedReader bufferedReader = null;

			try {
				bufferedReader = request.getReader(); // new BufferedReader(new InputStreamReader(inputStream));
				char[] charBuffer = new char[128];
				int bytesRead;
				while ((bytesRead = bufferedReader.read(charBuffer)) != -1) {
					log.info(" Reading characters ");
					sb.append(charBuffer, 0, bytesRead);
				}

			} catch (IOException ex) {
				throw ex;
			} finally {
				if (bufferedReader != null) {
					try {
						bufferedReader.close();
					} catch (IOException ex) {
						throw ex;
					}
				}
			}
			log.info(" Body ============== " + (sb == null ? "Null Body" : sb.toString()));
			
		
			if (!sb.toString().isEmpty() && sb.toString() != null) {
				
				log.info("String Buffer " + sb);
				if (sb.toString().contains("client_assertion")) {
					
					
					log.info(" Found Client assertion ");
					String[] params = sb.toString().split("&");
					
					
					
					for (String p : params) {
						String[] pair = p.split("=");
						if ("client_assertion".equalsIgnoreCase(pair[0])) {
							client_assertion = pair[1];
						}

						if ("grant_type".equalsIgnoreCase(pair[0])) {
							grant_type = pair[1];
						}

					  }
					}else {
						System.out.println("======Client Assertion Not Found==========");
					}

			}
		log.info("client id : " + client_id);
		log.info("Client_secret: " + client_secret);
		log.info("code:" + code);
		log.info("grant_type:" + grant_type);
		
	
		 if(grant_type.equals(GrantType.CLIENT_CREDENTIALS.toString())) {
        	System.out.println(client_assertion_type);
        	
				String client_Id = null;	
				
				System.out.println("------------ Decode JWT ------------");
				String[] split_string = client_assertion.split("\\.");
				String base64EncodedHeader = split_string[0];
				String base64EncodedBody = split_string[1];
		

				System.out.println("~~~~~~~~~ JWT Header ~~~~~~~");
				Base64 base64Url = new Base64(true);
				String header = new String(base64Url.decode(base64EncodedHeader));
				System.out.println("JWT Header : " + header);

				System.out.println("~~~~~~~~~ JWT Body ~~~~~~~");
				String body = new String(base64Url.decode(base64EncodedBody));
				System.out.println("JWT Body : " + body);
				
				JSONObject jwtBody = new JSONObject(body);
				if (jwtBody != null) {
					client_Id = jwtBody.getString("sub");

				}
				System.err.println("=========client_Id===========" + client_Id);
				if (client_Id != null) {
					
					
					DafClientRegister clientDetails = service.getClient(client_Id); // changed clientId, client

					System.err.println("=========clientDetails====== " + clientDetails);
					
					
				
						
						String contextPath = System.getProperty("catalina.base");
						String mainDirPath = clientDetails.getDirPath(); // changed client
						String fileName = clientDetails.getFiles(); // changed client

						File publicFile = new File(contextPath + mainDirPath + fileName);

						System.err.println("=========publicFile========" + publicFile);// added

						if (publicFile.exists()) {
							
							byte[] keyBytes = Files.readAllBytes(Paths.get(publicFile.getAbsolutePath()));

							X509EncodedKeySpec spec = new X509EncodedKeySpec(keyBytes);
							KeyFactory kf = KeyFactory.getInstance("RSA");

							PublicKey key2 = kf.generatePublic(spec);

							boolean asserter = Jwts.parser().setSigningKey(key2).parseClaimsJws(client_assertion)
									.getBody().getSubject().equals(clientDetails.getClient_id());// changed client
							
							if (asserter) {

								DafAuthtemp authTemp = new DafAuthtemp();
								OAuthIssuer oauthIssuerImpl = new OAuthIssuerImpl(new MD5Generator());
								final String accessToken = oauthIssuerImpl.accessToken();

								JSONObject jsonOb = new JSONObject();
								jsonOb.put("access_token", accessToken);
								jsonOb.put("token_type", "bearer");
								jsonOb.put("expires_in", "900");
								
								
								response.addHeader("Content-Type", "application/json");
								String timeStamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
										.format(new Timestamp(System.currentTimeMillis()));
								System.err.println("=======timeStamp========" + timeStamp);// added
								authTemp.setClient_secret(client_secret);
								authTemp.setAccess_token(accessToken);
								authTemp.setExpiry(timeStamp);
								authTemp.setRefresh_token(authTemp.getRefresh_token());

								dao.saveOrUpdate(authTemp);

								out.println(jsonOb.toString());

							}
							}else {
								response.sendError(500, "Public key was not found");
							}
			
				}else {
					response.sendError(401, "JWT - subject is missing");
				}
	
		}
			else {
			
			System.err.println("=======No input ==============");
			
			
		}
			
	}
}
*/