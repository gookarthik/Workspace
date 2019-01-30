package org.sitenv.spring;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.TimeZone;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FileUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.sitenv.spring.model.BulkDataOutput;
import org.sitenv.spring.model.BulkDataOutputInfo;
import org.sitenv.spring.model.DafBulkDataRequest;
import org.sitenv.spring.model.DafGroup;
import org.sitenv.spring.service.BulkDataRequestService;
import org.sitenv.spring.service.GroupService;
import org.sitenv.spring.util.CommonUtil;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.gson.Gson;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.model.dstu2.resource.AllergyIntolerance;
import ca.uhn.fhir.model.dstu2.resource.CarePlan;
import ca.uhn.fhir.model.dstu2.resource.Condition;
import ca.uhn.fhir.model.dstu2.resource.Device;
import ca.uhn.fhir.model.dstu2.resource.DiagnosticReport;
import ca.uhn.fhir.model.dstu2.resource.DocumentReference;
import ca.uhn.fhir.model.dstu2.resource.Goal;
import ca.uhn.fhir.model.dstu2.resource.Immunization;
import ca.uhn.fhir.model.dstu2.resource.Location;
import ca.uhn.fhir.model.dstu2.resource.Medication;
import ca.uhn.fhir.model.dstu2.resource.MedicationAdministration;
import ca.uhn.fhir.model.dstu2.resource.MedicationDispense;
import ca.uhn.fhir.model.dstu2.resource.MedicationOrder;
import ca.uhn.fhir.model.dstu2.resource.MedicationStatement;
import ca.uhn.fhir.model.dstu2.resource.Observation;
import ca.uhn.fhir.model.dstu2.resource.Organization;
import ca.uhn.fhir.model.dstu2.resource.Patient;
import ca.uhn.fhir.model.dstu2.resource.Procedure;
import ca.uhn.fhir.model.primitive.DateDt;
import ca.uhn.fhir.rest.server.exceptions.ResourceNotFoundException;
import ch.qos.logback.classic.Logger;

@Controller
@RequestMapping("/bulkdata")
public class BulkDataRequestProvider {

	static long stime = 0 ;
	static long etime = 0 ;
	static long tottime = 0;
	static String x = "";
	Logger log = (Logger) LoggerFactory.getLogger(BulkDataRequestProvider.class);

	@Autowired
	private BulkDataRequestService bdrService;

	@Autowired
	private GroupService groupService;

/*	@RequestMapping(value = "/{requestId}", method = RequestMethod.POST)
	@ResponseBody
	public void saveContentLocationResponse(@PathVariable Integer requestId, HttpServletRequest request,
			HttpServletResponse response) {
		PatientJsonResourceProvider patientResourceProvider = new PatientJsonResourceProvider();
	}*/
	
	@RequestMapping(value = "/{requestId}", method = RequestMethod.GET)
	@ResponseBody
	public String getContentLocationResponse(@PathVariable Integer requestId, HttpServletRequest request,
			HttpServletResponse response) {

		String body = "";
		DafBulkDataRequest bdr = bdrService.getBulkDataRequestById(requestId);
		if (bdr != null) {

			if (bdr.getStatus().equalsIgnoreCase("In Progress")) {
				response.setHeader("X-Progress", "In Progress");
				response.setStatus(202);
			}

			if (bdr.getStatus().equalsIgnoreCase("Accepted")) {
				response.setHeader("X-Progress", "Accepted");
				response.setStatus(202);
			}
			if (bdr.getStatus().equalsIgnoreCase("Completed")) {

				BulkDataOutput bdo = new BulkDataOutput();

				GregorianCalendar cal = new GregorianCalendar();
				cal.setTime(new Date());
				cal.setTimeZone(TimeZone.getTimeZone("GMT"));
				cal.add(Calendar.DATE, 10);
				String dt = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss zzz").format(cal.getTime());
				bdo.setTransactionStartTime(dt);

				bdo.setRequest(bdr.getRequestResource());
				bdo.setSecure("false");

				String[] links = bdr.getFiles().split(",");
				StringBuilder linksHeader = new StringBuilder();
				String uri = request.getScheme() + "://" + request.getServerName()
						+ ("http".equals(request.getScheme()) && request.getServerPort() == 80
								|| "https".equals(request.getScheme()) && request.getServerPort() == 443 ? ""
										: ":" + request.getServerPort())
						+ request.getContextPath();

				for (int i = 0; i < links.length; i++) {

					if (links[i] != null && !links[i].equals("null")) {

						BulkDataOutputInfo bdoi = new BulkDataOutputInfo();

						String linkForBody = uri + "/bulkdata/download/" + bdr.getRequestId() + "/" + links[i];
						String l = "<" + uri + "/bulkdata/download/" + bdr.getRequestId() + "/" + links[i] + ">";
						bdoi.setUrl(linkForBody);
						bdo.add(bdoi);

						linksHeader.append(l);

						if (i < links.length - 1) {
							linksHeader.append(",");
						}
					}
				}
				Gson g = new Gson();
				body = g.toJson(bdo);

				response.setHeader("Link", linksHeader.toString());
			}
		} else {
			response.setStatus(404);
			throw new ResourceNotFoundException(
					"The requested Content-Location was not found. Please contact the Admin.");
		}

		return body;
	}

	@RequestMapping(value = "/download/{id}/{fileName:.+}", method = RequestMethod.GET)
	@ResponseBody
	public int downloadFile(@PathVariable Integer id, @PathVariable String fileName, HttpServletRequest request,
			HttpServletResponse response) throws IOException {

		String contextPath = System.getProperty("catalina.base");
		String destDir = contextPath + "/bulk data/" + id + "/";
		return CommonUtil.downloadFIleByName(new File(destDir + fileName), response);

	}

	// delete content-location request
	@RequestMapping(value = "/{requestId}", method = RequestMethod.DELETE)
	@ResponseBody
	public void deleteContentRequest(@PathVariable Integer requestId, HttpServletRequest request,
			HttpServletResponse response) throws IOException {

		Integer res = bdrService.deleteRequestById(requestId);

		// delete folder with files
		if (res > 0) {
			String contextPath = System.getProperty("catalina.base");
			String destDir = contextPath + "/bulk data/" + requestId + "/";
			File directory = new File(destDir);
			if (directory.exists()) 
			{
				FileUtils.deleteDirectory(directory);
			}
		}
		log.info("Numer of records effected due to content-location delete request : " + res);
		response.setStatus(202);
	}

	@RequestMapping(value = "/load/request/{id}", method = RequestMethod.GET)
	@ResponseBody
	public void loadRequestById(@PathVariable Integer id, HttpServletRequest request, HttpServletResponse response)
			throws IOException {

		DafBulkDataRequest bdr = bdrService.getBulkDataRequestById(id);
		processBulkDataRequest(bdr);

	}

	@Scheduled(cron = "*/5 * * * * ?")
	public void processBulkDataRequestSchedular() {

		System.out.println("Schedular checking for pending requests...!");

		List<DafBulkDataRequest> requests = bdrService.getBulkDataRequestsByProcessedFlag(false);
		try {
		for (DafBulkDataRequest bdr : requests) {
			
			long startTime = System.nanoTime();
			log.info("request with id : "+bdr.getRequestId() +" is processing...");

			processBulkDataRequest(bdr);
			
			
			long endTime   = System.nanoTime();
			long totalTime = endTime - startTime;
			log.info("request with id : "+bdr.getRequestId() +" - processing completed. The total time is : '"+totalTime+"' in nano seconds");

		}
		}catch(Exception e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		}
	}

	public void processBulkDataRequest(DafBulkDataRequest bdr) throws IOException 
	{
		List<Integer> patientList = null;
		Date start = null;
		if (bdr.getStart() != null) 
		{
			DateDt dateDt = new DateDt();
			dateDt.setValueAsString(bdr.getStart());
			start = dateDt.getValue();
		}

		FhirContext ctx = new FhirContext().forDstu2();

		String contextPath = System.getProperty("catalina.base");
		File destDir = new File(contextPath + "/bulk data/" + bdr.getRequestId() + "/");
		
		bdr.setStatus("In Progress");
		bdr.setProcessedFlag(true);
		bdrService.saveBulkDataRequest(bdr);

		StringBuilder files = new StringBuilder();

		if (!destDir.exists()) {
			destDir.mkdirs();
		}

		if (bdr.getResourceName() != null && bdr.getResourceName().equalsIgnoreCase("GROUP")) 
		{
			DafGroup dafGroup = groupService.getGroupById(bdr.getResourceId());

			if (dafGroup != null) 
			{
				patientList = new ArrayList<Integer>();
				JSONArray jsonArr = new JSONArray(dafGroup.getMember());
				for (int i = 0; i < jsonArr.length(); i++) 
				{
					JSONObject jsonObj = jsonArr.getJSONObject(i);
					if (jsonObj.getJSONObject("entity") != null) 
					{
						String referenceId = jsonObj.getJSONObject("entity").getString("reference").split("/")[1];
						Integer patientId = Integer.parseInt(referenceId);
						patientList.add(patientId);
					}
				}
			}

		}
		
		//---------------------------------------------------------------------------------------

		FileWriter fw=new FileWriter("C:\\Users\\batca\\Desktop\\output.txt",true);
		//---------------------------------------------------------------------------------------

		stime=0;
		stime = System.nanoTime(); 
		System.out.println("\n \n \n \n \n Start time "+stime+" \n \n \n \n");
		
		// Process Patient Bulk data request
		String fileName = processPatientData(bdr, destDir, ctx, patientList, start);
		files.append(fileName);
		String type = bdr.getType();

		tottime = 0;
		tottime = System.nanoTime() - stime;
		try {
			fw.write(" Hello welcome ------- LOGS "+ System.getProperty( "line.separator" ));
			fw.write("Patient -  Loop Time : "+tottime+ System.getProperty( "line.separator" ));
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		System.out.println("\n \n \n \n \n TOTAL time (Patient) : "+tottime+" \n \n \n \n");
		System.out.println("=================================================");
		
		
		
		stime=0;
		stime = System.nanoTime(); 
		System.out.println("\n \n \n \n \n Start time "+stime+" \n \n \n \n");
		
		// Process AllergyIntolerance Bulk data request
		if (type == null || Arrays.asList(type.split(",")).contains("AllergyIntolerance")) 
		{
			System.out.println("ALLERGY INTOLERANCE STARTS HERE : "+System.nanoTime());
			
			String allergyIntoleranceFileName = processAllergyIntoleranceData(bdr, destDir, ctx, patientList, start);
			files.append(",");
			files.append(allergyIntoleranceFileName);
		}
		tottime = 0;
		tottime = System.nanoTime() - stime;
		try {
			fw.write("AllergyIntolerance -  Loop Time : "+tottime+ System.getProperty( "line.separator" )); 
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		
		System.out.println("\n \n \n \n \n TOTAL time (AllergyIntolerance) : "+tottime+" \n \n \n \n");
		System.out.println("=================================================");
		
		
//---------------------------------------------------------------------------------------------------------------------------------------
		//passing DafBulkDataRequest, Destination directory file, FHIR version, patientList
		
		System.out.println("\n \n \n \n \n Start time "+stime+" \n \n \n \n");
		
//		// Process CarePlan Bulk data request
		if (type == null || Arrays.asList(type.split(",")).contains("CarePlan")) 
		{
			   
				System.out.println("Care plan if loop STARTS @ "+System.nanoTime());
			
			   String carePlanFileName = processCarePlanData(bdr, destDir, ctx, patientList, start);
			   files.append(",");
			   files.append(carePlanFileName);
			   System.out.println("Care plan if loop ENDS @ "+System.nanoTime());
		
		}
		tottime = 0;
		tottime = System.nanoTime() - stime;
		try {
			fw.write("CarePlan -  Loop Time : "+tottime+ System.getProperty( "line.separator" )); 
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		System.out.println("\n \n \n \n \n TOTAL time (CarePlan) : "+tottime+" \n \n \n \n");
		System.out.println("=================================================");	
		
		
//---------------------------------------------------------------------------------------------------------------------------------------
		
		
		
		System.out.println("\n \n \n \n \n Start time "+stime+" \n \n \n \n");

		stime=0;
		stime = System.nanoTime(); 
		// Process Condition Bulk data request
		if (type == null || Arrays.asList(type.split(",")).contains("Condition")) {
			String conditionFileName = processConditionData(bdr, destDir, ctx, patientList, start);
			files.append(",");
			files.append(conditionFileName);
		}
		tottime = 0;
		tottime = System.nanoTime() - stime;
		try {
			fw.write("Condition -  Loop Time : "+tottime+ System.getProperty( "line.separator" )); 
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		System.out.println("\n \n \n \n \n TOTAL time (Condition) : "+tottime+" \n \n \n \n");
		System.out.println("=================================================");
		
		
		
		System.out.println("\n \n \n \n \n Start time "+stime+" \n \n \n \n");

		stime=0;
		stime = System.nanoTime(); 
		// Process Device Bulk data request
		if (type == null || Arrays.asList(type.split(",")).contains("Device")) {
			String deviceFileName = processDeviceData(bdr, destDir, ctx, patientList, start);
			files.append(",");
			files.append(deviceFileName);
		}
		tottime = 0;
		tottime = System.nanoTime() - stime;
		try {
			fw.write("Device -  Loop Time : "+tottime+ System.getProperty( "line.separator" )); 
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}

		System.out.println("\n \n \n \n \n TOTAL time (Device) : "+tottime+" \n \n \n \n");
		System.out.println("=================================================");
		
		
		
		System.out.println("\n \n \n \n \n Start time "+stime+" \n \n \n \n");
		stime=0;
		stime = System.nanoTime(); 

		// Process DiagnosticReport Bulk data request
		if (type == null || Arrays.asList(type.split(",")).contains("DiagnosticReport")) {
			String diagnosticReportFileName = processDiagnosticReportData(bdr, destDir, ctx, patientList, start);
			files.append(",");
			files.append(diagnosticReportFileName);
		}
		tottime = 0;
		tottime = System.nanoTime() - stime;
		try {
			fw.write("DiagnosticReport -  Loop Time : "+tottime+ System.getProperty( "line.separator" )); 
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		System.out.println("\n \n \n \n \n TOTAL time (DiagnosticReport) : "+tottime+" \n \n \n \n");
		System.out.println("=================================================");
		
		
		
		System.out.println("\n \n \n \n \n Start time "+stime+" \n \n \n \n");

		stime=0;
		stime = System.nanoTime(); 

		// Process DocumentReference Bulk data request
		if (type == null || Arrays.asList(type.split(",")).contains("DocumentReference")) {
			String documentReferenceFileName = processDocumentReferenceData(bdr, destDir, ctx, patientList, start);
			files.append(",");
			files.append(documentReferenceFileName);
		}
		tottime = 0;
		tottime = System.nanoTime() - stime;
		try {
			fw.write("DocumentReference -  Loop Time : "+tottime+ System.getProperty( "line.separator" )); 
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		System.out.println("\n \n \n \n \n TOTAL time (DocumentReference) : "+tottime+" \n \n \n \n");
		System.out.println("=================================================");
		
		
		
		System.out.println("\n \n \n \n \n Start time "+stime+" \n \n \n \n");

		stime=0;
		stime = System.nanoTime(); 

		// Process Goal Bulk data request
		if (type == null || Arrays.asList(type.split(",")).contains("Goal")) {
			String goalFileName = processGoalsData(bdr, destDir, ctx, patientList, start);
			files.append(",");
			files.append(goalFileName);
		}
		tottime = 0;
		tottime = System.nanoTime() - stime;
		try {
			fw.write("Goal -  Loop Time : "+tottime+ System.getProperty( "line.separator" )); 
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}

		System.out.println("\n \n \n \n \n TOTAL time (Goal) : "+tottime+" \n \n \n \n");
		System.out.println("=================================================");
		
		
		
		System.out.println("\n \n \n \n \n Start time "+stime+" \n \n \n \n");
		stime=0;
		stime = System.nanoTime(); 

		// Process Immunization Bulk data request
		if (type == null || Arrays.asList(type.split(",")).contains("Immunization")) {
			String immunizationFileName = processImmunizationData(bdr, destDir, ctx, patientList, start);
			files.append(",");
			files.append(immunizationFileName);
		}
		tottime = 0;
		tottime = System.nanoTime() - stime;
		try {
			fw.write("Immunization -  Loop Time : "+tottime+ System.getProperty( "line.separator" )); 
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		System.out.println("\n \n \n \n \n TOTAL time (Immunization) : "+tottime+" \n \n \n \n");
		System.out.println("=================================================");
		
		
		
		System.out.println("\n \n \n \n \n Start time "+stime+" \n \n \n \n");

		stime=0;
		stime = System.nanoTime(); 

		// Process Location Bulk data request
		if (type == null || Arrays.asList(type.split(",")).contains("Location")) {
			String locationFileName = processLocationData(bdr, destDir, ctx, patientList, start);
			files.append(",");
			files.append(locationFileName);
		}
		tottime = 0;
		tottime = System.nanoTime() - stime;
		try {
			fw.write("Location -  Loop Time : "+tottime+ System.getProperty( "line.separator" )); 
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		System.out.println("\n \n \n \n \n TOTAL time (Location) : "+tottime+" \n \n \n \n");
		System.out.println("=================================================");
		
		
		
		System.out.println("\n \n \n \n \n Start time "+stime+" \n \n \n \n");

		stime=0;
		stime = System.nanoTime(); 

		// Process MedicationAdministration Bulk data request
		if (type == null || Arrays.asList(type.split(",")).contains("MedicationAdministration")) {
			String medicationAdministrationFileName = processMedicationAdministrationData(bdr, destDir, ctx,
					patientList, start);
			files.append(",");
			files.append(medicationAdministrationFileName);
		}
		tottime = 0;
		tottime = System.nanoTime() - stime;
		try {
			fw.write("MedicationAdministration -  Loop Time : "+tottime+ System.getProperty( "line.separator" )); 
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}

		System.out.println("\n \n \n \n \n TOTAL time (MedicationAdministration) : "+tottime+" \n \n \n \n");
		System.out.println("=================================================");
		
		
		
		System.out.println("\n \n \n \n \n Start time "+stime+" \n \n \n \n");
		stime=0;
		stime = System.nanoTime(); 

		// Process MedicationDispense Bulk data request
		if (type == null || Arrays.asList(type.split(",")).contains("MedicationDispense")) {
			String medicationDispenseFileName = processMedicationDispenseData(bdr, destDir, ctx, patientList, start);
			files.append(",");
			files.append(medicationDispenseFileName);
		}
		tottime = 0;
		tottime = System.nanoTime() - stime;
		try {
			fw.write("MedicationDispense -  Loop Time : "+tottime+ System.getProperty( "line.separator" )); 
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}

		System.out.println("\n \n \n \n \n TOTAL time (MedicationDispense) : "+tottime+" \n \n \n \n");
		System.out.println("=================================================");
		
		
		
		System.out.println("\n \n \n \n \n Start time "+stime+" \n \n \n \n");

		stime=0;
		stime = System.nanoTime(); 

		// Process MedicationOrder Bulk data request
		if (type == null || Arrays.asList(type.split(",")).contains("MedicationOrder")) {
			String medicationOrderFileName = processMedicationOrderData(bdr, destDir, ctx, patientList, start);
			files.append(",");
			files.append(medicationOrderFileName);
		}
		tottime = 0;
		tottime = System.nanoTime() - stime;
		try {
			fw.write("MedicationOrder -  Loop Time : "+tottime+ System.getProperty( "line.separator" )); 
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}

		System.out.println("\n \n \n \n \n TOTAL time (MedicationOrder) : "+tottime+" \n \n \n \n");
		System.out.println("=================================================");
		
		
		
		
		System.out.println("\n \n \n \n \n Start time "+stime+" \n \n \n \n");
		stime=0;
		stime = System.nanoTime(); 

		// Process Medication Bulk data request
		if (type == null || Arrays.asList(type.split(",")).contains("Medication")) {
			String medicationFileName = processMedicationData(bdr, destDir, ctx, patientList, start);
			files.append(",");
			files.append(medicationFileName);
		}
		tottime = 0;
		tottime = System.nanoTime() - stime;
		try {
			fw.write("Medication -  Loop Time : "+tottime+ System.getProperty( "line.separator" )); 
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		System.out.println("\n \n \n \n \n TOTAL time (Medication) : "+tottime+" \n \n \n \n");
		System.out.println("=================================================");
		
		
		
		
		System.out.println("\n \n \n \n \n Start time "+stime+" \n \n \n \n");
		stime=0;
		stime = System.nanoTime(); 

		// Process MedicationStatement Bulk data request
		if (type == null || Arrays.asList(type.split(",")).contains("MedicationStatement")) {
			String medicationStatementFileName = processMedicationStatementData(bdr, destDir, ctx, patientList, start);
			files.append(",");
			files.append(medicationStatementFileName);
		}
		tottime = 0;
		tottime = System.nanoTime() - stime;
		try {
			fw.write("MedicationStatement -  Loop Time : "+tottime+ System.getProperty( "line.separator" )); 
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}

		System.out.println("\n \n \n \n \n TOTAL time (MedicationStatement) : "+tottime+" \n \n \n \n");
		System.out.println("=================================================");
		
		
		
		
		System.out.println("\n \n \n \n \n Start time "+stime+" \n \n \n \n");
		stime=0;
		stime = System.nanoTime(); 

		// Process Observation Bulk data request
		if (type == null || Arrays.asList(type.split(",")).contains("Observation")) {
			String observationFileName = processObservationData(bdr, destDir, ctx, patientList, start);
			files.append(",");
			files.append(observationFileName);
		}
		tottime = 0;
		tottime = System.nanoTime() - stime;
		try {
			fw.write("Observation -  Loop Time : "+tottime+ System.getProperty( "line.separator" )); 
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}

		System.out.println("\n \n \n \n \n TOTAL time (Observation) : "+tottime+" \n \n \n \n");
		System.out.println("=================================================");
		
		
		
		
		
		System.out.println("\n \n \n \n \n Start time "+stime+" \n \n \n \n");
		stime=0;
		stime = System.nanoTime(); 

		// Process Organization Bulk data request
		if (type == null || Arrays.asList(type.split(",")).contains("Organization")) {
			String organizationFileName = processOrganizationData(bdr, destDir, ctx, patientList, start);
			files.append(",");
			files.append(organizationFileName);
		}
		tottime = 0;
		tottime = System.nanoTime() - stime;
		try {
			fw.write("Organization -  Loop Time : "+tottime+ System.getProperty( "line.separator" )); 
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}

		System.out.println("\n \n \n \n \n TOTAL time (Organization) : "+tottime+" \n \n \n \n");
		System.out.println("=================================================");
		
		
		
		
		
		System.out.println("\n \n \n \n \n Start time "+stime+" \n \n \n \n");
		stime=0;
		stime = System.nanoTime(); 

		// Process Procedure Bulk data request
		if (type == null || Arrays.asList(type.split(",")).contains("Procedure")) {
			String procedureFileName = processProcedureData(bdr, destDir, ctx, patientList, start);
			files.append(",");
			files.append(procedureFileName);
		}
		tottime = 0;
		tottime = System.nanoTime() - stime;
		try {
			fw.write("Procedure -  Loop Time : "+tottime+ System.getProperty( "line.separator" )); 
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}

		System.out.println("\n \n \n \n \n TOTAL time (Procedure) : "+tottime+" \n \n \n \n");
		System.out.println("=================================================");
		
		
		
		
		bdr.setStatus("Completed");
		bdr.setFiles(files.toString());
		bdrService.saveBulkDataRequest(bdr);
		fw.close();
	}
	
	public String processPatientData(DafBulkDataRequest bdr, File destDir, FhirContext ctx, List<Integer> patientList,
			Date start) {

		String fileName = "Patient.ndjson";

		PatientJsonResourceProvider patientResourceProvider = new PatientJsonResourceProvider();

		try {
			
			stime=0;
			stime = System.nanoTime(); 
			System.out.println("\n \n \n \n \n  getting Start Patient time "+stime+" \n \n \n \n");
			
			List<Patient> patients = patientResourceProvider.getPatientForBulkDataRequest(patientList, start);
			
			tottime = 0;
			tottime = System.nanoTime() - stime;
			System.out.println("\n \n \n \n \n getting TOTAL time (Patient) : "+tottime+" \n \n \n \n");
			
			System.out.println("##################################################################");
			
			stime=0;
			stime = System.nanoTime(); 
			System.out.println("\n \n \n \n \n  loading Patient Start time "+stime+" \n \n \n \n");
			
			File ndJsonFile = new File(destDir.getAbsolutePath() + "/" + fileName);
			StringBuilder responseData = new StringBuilder();
			
			PrintWriter pw = new PrintWriter(ndJsonFile);
			
			// for(Patient patient : patients){
			for (int i = 0; i < patients.size(); i++) {
				StringBuilder exportData = new StringBuilder();
				/*
				 * if(i==0) { exportData.append("["); exportData.append('\n'); }
				 */

				exportData.append(ctx.newJsonParser().encodeResourceToString(patients.get(i)));

				if (i < patients.size() - 1) {
					// exportData.append(',');
					exportData.append('\n');
				} else {
					// exportData.append('\n');
					// exportData.append("]");
				}

				//responseData.append(exportData);
				
				pw.write(exportData.toString());
			}
			
			pw.close();
			
			tottime = 0;
			tottime = System.nanoTime() - stime;
			System.out.println("\n \n \n \n \n loading TOTAL time (Patient) : "+tottime+" \n \n \n \n");
			System.out.println("##################################################################");
			
			return fileName;

		} catch (Exception e) {
			System.out.println(e.getMessage());
		}

		return null;
	}

	public String processAllergyIntoleranceData(DafBulkDataRequest bdr, File destDir, FhirContext ctx,
			List<Integer> patientList, Date start) {

		String fileName = "AllergyIntolerance.ndjson";

		AllergyIntoleranceResourceProvider allergyIntoleranceProvider = new AllergyIntoleranceResourceProvider();

		try {
			stime=0;
			stime = System.nanoTime(); 
			System.out.println("\n \n \n \n \n  getting Start AllergyIntolerance time "+stime+" \n \n \n \n");
			
//			System.out.println();
//			System.out.println("processCarePlanData - Allergy intolerance allergyIntoleranceList OBJECT STARTS @ "+System.nanoTime());
//			System.out.println();
			List<AllergyIntolerance> allergyIntoleranceList = allergyIntoleranceProvider
					.getAllergyIntoleranceForBulkDataRequest(patientList, start);
			
//			System.out.println();
//			System.out.println("processCarePlanData - Allergy intolerance allergyIntoleranceList OBJECT STARTS @ "+System.nanoTime());
//			System.out.println();
			tottime = 0;
			tottime = System.nanoTime() - stime;
			System.out.println("\n \n \n \n \n getting TOTAL time (AllergyIntolerance) : "+tottime+" \n \n \n \n");
			
			System.out.println("##################################################################");
			
			stime=0;
			stime = System.nanoTime(); 
			System.out.println("\n \n \n \n \n  loading AllergyIntolerance Start time "+stime+" \n \n \n \n");
			
//			
			
			File ndJsonFile = new File(destDir.getAbsolutePath() + "/" + fileName);
			StringBuilder responseData = new StringBuilder();
			for (int i = 0; i < allergyIntoleranceList.size(); i++) {
				StringBuilder exportData = new StringBuilder();
				/*
				 * if(i==0) { exportData.append("["); exportData.append('\n'); }
				 */

				exportData.append(ctx.newJsonParser().encodeResourceToString(allergyIntoleranceList.get(i)));

				if (i < allergyIntoleranceList.size() - 1) {
					// exportData.append(',');
					exportData.append('\n');
				} else {
					// exportData.append('\n');
					// exportData.append("]");
				}

				responseData.append(exportData);
			}
			/*
			 * for(AllergyIntolerance allergyIntolerance : allergyIntoleranceList){
			 * StringBuilder exportData = new StringBuilder();
			 * exportData.append(ctx.newJsonParser().encodeResourceToString(
			 * allergyIntolerance)); exportData.append('\n');
			 * responseData.append(exportData); }
			 */

			PrintWriter pw = new PrintWriter(ndJsonFile);
			pw.write(responseData.toString());
			pw.close();
			tottime = 0;
			tottime = System.nanoTime() - stime;
			System.out.println("\n \n \n \n \n loading TOTAL time (AllergyIntolerance) : "+tottime+" \n \n \n \n");
			System.out.println("##################################################################");
			return fileName;

		} catch (Exception e) {
			System.out.println(e.getMessage());
		}

		return null;
	}
	
	public String processCarePlanData(DafBulkDataRequest bdr, File destDir, FhirContext ctx, List<Integer> patientList,
			Date start) throws IOException 
	{			
		String fileName = "CarePlan.ndjson";
		CarePlanResourceProvider carePlanProvider = new CarePlanResourceProvider();

		try {
			
			//---------------------------------------------------------------------------------------
			//	    FileWriter fw=new FileWriter("C:\\Users\\batca\\Desktop\\outputCareplan.txt",true);
			//---------------------------------------------------------------------------------------

			
				System.out.println("processCarePlanData - CAREPLAN carePlanList OBJECT STARTS @ "+System.nanoTime());
			
					List<CarePlan> carePlanList = carePlanProvider.getCarePlanForBulkData(patientList, start);

				System.out.println("processCarePlanData - CAREPLAN carePlanList OBJECT ENDS @ "+System.nanoTime());
/*	
				StringBuilder axa = new StringBuilder();
				
				for (int k = 0; k < carePlanList.size(); k++)
				{
					StringBuilder axb = new StringBuilder();					
					axb.append(carePlanList.get(k));
					axa.append(axb);

				}
				
				fw.write(axa.toString());
				fw.close();
*/
				
//----------------------------------------------------------------------------------------------------------------------------------------------------------------
				
			File ndJsonFile = new File(destDir.getAbsolutePath() + "/" + fileName);
			StringBuilder responseData = new StringBuilder();
			/*
			 * for(CarePlan carePlan : CarePlanList){ StringBuilder exportData = new
			 * StringBuilder();
			 * exportData.append(ctx.newJsonParser().encodeResourceToString(carePlan));
			 * exportData.append('\n'); responseData.append(exportData); }
			 */
				System.out.println("processCarePlanData  MAIN LOOP STARTS @ "+System.nanoTime());

			for (int i = 0; i < carePlanList.size(); i++) {
				StringBuilder exportData = new StringBuilder();
				/*
				 * if(i==0) { exportData.append("["); exportData.append('\n'); }
				 */

					System.out.println("\t\t\t processCarePlanData - LOOP ("+i+") value  @ "+System.nanoTime());

				exportData.append(ctx.newJsonParser().encodeResourceToString(carePlanList.get(i)));

				
				if (i < carePlanList.size() - 1) {
					// exportData.append(',');
					exportData.append('\n');
				} else {
					// exportData.append('\n');
					// exportData.append("]");
				}

				responseData.append(exportData);
			}
			System.out.println("processCarePlanData  MAIN LOOP ENDS @ "+System.nanoTime());

			PrintWriter pw = new PrintWriter(ndJsonFile);
			pw.write(responseData.toString());
			pw.close();
			return fileName;

		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		return null;
		
	}

	public String processConditionData(DafBulkDataRequest bdr, File destDir, FhirContext ctx, List<Integer> patientList,
			Date start) {

		String fileName = "Condition.ndjson";

		ConditionResourceProvider conditionProvider = new ConditionResourceProvider();

		try {
			List<Condition> conditionList = conditionProvider.getConditionForBulkDataRequest(patientList, start);

			File ndJsonFile = new File(destDir.getAbsolutePath() + "/" + fileName);
			StringBuilder responseData = new StringBuilder();
			/*
			 * for(Condition condition : conditionList){ StringBuilder exportData = new
			 * StringBuilder();
			 * exportData.append(ctx.newJsonParser().encodeResourceToString(condition));
			 * exportData.append('\n'); responseData.append(exportData); }
			 */

			for (int i = 0; i < conditionList.size(); i++) {
				StringBuilder exportData = new StringBuilder();
				/*
				 * if(i==0) { exportData.append("["); exportData.append('\n'); }
				 */

				exportData.append(ctx.newJsonParser().encodeResourceToString(conditionList.get(i)));

				if (i < conditionList.size() - 1) {
					// exportData.append(',');
					exportData.append('\n');
				} else {
					// exportData.append('\n');
					// exportData.append("]");
				}

				responseData.append(exportData);
			}

			PrintWriter pw = new PrintWriter(ndJsonFile);
			pw.write(responseData.toString());
			pw.close();
			return fileName;

		} catch (Exception e) {
			System.out.println(e.getMessage());
		}

		return null;
	}

	public String processDeviceData(DafBulkDataRequest bdr, File destDir, FhirContext ctx, List<Integer> patientList,
			Date start) {

		String fileName = "Device.ndjson";

		DeviceResourceProvider deviceProvider = new DeviceResourceProvider();

		try {
			List<Device> deviceList = deviceProvider.getDeviceForBulkDataRequest(patientList, start);

			File ndJsonFile = new File(destDir.getAbsolutePath() + "/" + fileName);
			StringBuilder responseData = new StringBuilder();
			/*
			 * for(Device device : deviceList){ StringBuilder exportData = new
			 * StringBuilder();
			 * exportData.append(ctx.newJsonParser().encodeResourceToString(device));
			 * exportData.append('\n'); responseData.append(exportData); }
			 */

			for (int i = 0; i < deviceList.size(); i++) {
				StringBuilder exportData = new StringBuilder();
				/*
				 * if(i==0) { exportData.append("["); exportData.append('\n'); }
				 */

				exportData.append(ctx.newJsonParser().encodeResourceToString(deviceList.get(i)));

				if (i < deviceList.size() - 1) {
					// exportData.append(',');
					exportData.append('\n');
				} else {
					// exportData.append('\n');
					// exportData.append("]");
				}

				responseData.append(exportData);
			}

			PrintWriter pw = new PrintWriter(ndJsonFile);
			pw.write(responseData.toString());
			pw.close();
			return fileName;

		} catch (Exception e) {
			System.out.println(e.getMessage());
		}

		return null;
	}

	public String processDiagnosticReportData(DafBulkDataRequest bdr, File destDir, FhirContext ctx,
			List<Integer> patientList, Date start) {

		String fileName = "DiagnosticReport.ndjson";

		DiagnosticReportResourceProvider diagnosticReportProvider = new DiagnosticReportResourceProvider();

		try {
			List<DiagnosticReport> diagnosticReportList = diagnosticReportProvider
					.getDiagnosticReportForBulkDataRequest(patientList, start);

			File ndJsonFile = new File(destDir.getAbsolutePath() + "/" + fileName);
			StringBuilder responseData = new StringBuilder();
			/*
			 * for(DiagnosticReport diagnosticReport : diagnosticReportList){ StringBuilder
			 * exportData = new StringBuilder();
			 * exportData.append(ctx.newJsonParser().encodeResourceToString(diagnosticReport
			 * )); exportData.append('\n'); responseData.append(exportData); }
			 */

			for (int i = 0; i < diagnosticReportList.size(); i++) {
				StringBuilder exportData = new StringBuilder();
				/*
				 * if(i==0) { exportData.append("["); exportData.append('\n'); }
				 */

				exportData.append(ctx.newJsonParser().encodeResourceToString(diagnosticReportList.get(i)));

				if (i < diagnosticReportList.size() - 1) {
					// exportData.append(',');
					exportData.append('\n');
				} else {
					// exportData.append('\n');
					// exportData.append("]");
				}

				responseData.append(exportData);
			}

			PrintWriter pw = new PrintWriter(ndJsonFile);
			pw.write(responseData.toString());
			pw.close();
			return fileName;

		} catch (Exception e) {
			System.out.println(e.getMessage());
		}

		return null;
	}

	public String processDocumentReferenceData(DafBulkDataRequest bdr, File destDir, FhirContext ctx,
			List<Integer> patientList, Date start) {

		String fileName = "DocumentReference.ndjson";

		DocumentReferenceResourceProvider documentReferenceResourceProvider = new DocumentReferenceResourceProvider();

		try {
			List<DocumentReference> documentReferenceList = documentReferenceResourceProvider
					.getDocumentReferenceForBulkDataRequest(patientList, start);

			File ndJsonFile = new File(destDir.getAbsolutePath() + "/" + fileName);
			StringBuilder responseData = new StringBuilder();
			/*
			 * for(DocumentReference documentReference : documentReferenceList){
			 * StringBuilder exportData = new StringBuilder();
			 * exportData.append(ctx.newJsonParser().encodeResourceToString(
			 * documentReference)); exportData.append('\n');
			 * responseData.append(exportData); }
			 */

			for (int i = 0; i < documentReferenceList.size(); i++) {
				StringBuilder exportData = new StringBuilder();
				/*
				 * if(i==0) { exportData.append("["); exportData.append('\n'); }
				 */

				exportData.append(ctx.newJsonParser().encodeResourceToString(documentReferenceList.get(i)));

				if (i < documentReferenceList.size() - 1) {
					// exportData.append(',');
					exportData.append('\n');
				} else {
					// exportData.append('\n');
					// exportData.append("]");
				}

				responseData.append(exportData);
			}

			PrintWriter pw = new PrintWriter(ndJsonFile);
			pw.write(responseData.toString());
			pw.close();
			return fileName;

		} catch (Exception e) {
			System.out.println(e.getMessage());
		}

		return null;
	}

	public String processGoalsData(DafBulkDataRequest bdr, File destDir, FhirContext ctx, List<Integer> patientList,
			Date start) {

		String fileName = "Goal.ndjson";

		GoalsResourceProvider goalsResourceProvider = new GoalsResourceProvider();

		try {
			List<Goal> goalList = goalsResourceProvider.getGoalsForBulkDataRequest(patientList, start);

			File ndJsonFile = new File(destDir.getAbsolutePath() + "/" + fileName);
			StringBuilder responseData = new StringBuilder();
			/*
			 * for(Goal goal : goalList){ StringBuilder exportData = new StringBuilder();
			 * exportData.append(ctx.newJsonParser().encodeResourceToString(goal));
			 * exportData.append('\n'); responseData.append(exportData); }
			 */

			for (int i = 0; i < goalList.size(); i++) {
				StringBuilder exportData = new StringBuilder();
				/*
				 * if(i==0) { exportData.append("["); exportData.append('\n'); }
				 */

				exportData.append(ctx.newJsonParser().encodeResourceToString(goalList.get(i)));

				if (i < goalList.size() - 1) {
					// exportData.append(',');
					exportData.append('\n');
				} else {
					// exportData.append('\n');
					// exportData.append("]");
				}

				responseData.append(exportData);
			}

			PrintWriter pw = new PrintWriter(ndJsonFile);
			pw.write(responseData.toString());
			pw.close();
			return fileName;

		} catch (Exception e) {
			System.out.println(e.getMessage());
		}

		return null;
	}

	public String processImmunizationData(DafBulkDataRequest bdr, File destDir, FhirContext ctx,
			List<Integer> patientList, Date start) {

		String fileName = "Immunization.ndjson";

		ImmunizationResourceProvider immunizationResourceProvider = new ImmunizationResourceProvider();

		try {
			List<Immunization> immunizationList = immunizationResourceProvider
					.getImmunizationForBulkDataRequest(patientList, start);

			File ndJsonFile = new File(destDir.getAbsolutePath() + "/" + fileName);
			StringBuilder responseData = new StringBuilder();
			/*
			 * for(Immunization immunization : immunizationList){ StringBuilder exportData =
			 * new StringBuilder();
			 * exportData.append(ctx.newJsonParser().encodeResourceToString(immunization));
			 * exportData.append('\n'); responseData.append(exportData); }
			 */

			for (int i = 0; i < immunizationList.size(); i++) {
				StringBuilder exportData = new StringBuilder();
				/*
				 * if(i==0) { exportData.append("["); exportData.append('\n'); }
				 */

				exportData.append(ctx.newJsonParser().encodeResourceToString(immunizationList.get(i)));

				if (i < immunizationList.size() - 1) {
					// exportData.append(',');
					exportData.append('\n');
				} else {
					// exportData.append('\n');
					// exportData.append("]");
				}

				responseData.append(exportData);
			}

			PrintWriter pw = new PrintWriter(ndJsonFile);
			pw.write(responseData.toString());
			pw.close();
			return fileName;

		} catch (Exception e) {
			System.out.println(e.getMessage());
		}

		return null;
	}

	public String processLocationData(DafBulkDataRequest bdr, File destDir, FhirContext ctx, List<Integer> patientList,
			Date start) {

		String fileName = "Location.ndjson";

		LocationResourceProvider locationResourceProvider = new LocationResourceProvider();

		try {
			List<Location> locationList = locationResourceProvider.getLocationForBulkDataRequest(patientList, start);

			File ndJsonFile = new File(destDir.getAbsolutePath() + "/" + fileName);
			StringBuilder responseData = new StringBuilder();
			/*
			 * for(Location location : locationList){ StringBuilder exportData = new
			 * StringBuilder();
			 * exportData.append(ctx.newJsonParser().encodeResourceToString(location));
			 * exportData.append('\n'); responseData.append(exportData); }
			 */

			for (int i = 0; i < locationList.size(); i++) {
				StringBuilder exportData = new StringBuilder();
				/*
				 * if(i==0) { exportData.append("["); exportData.append('\n'); }
				 */

				exportData.append(ctx.newJsonParser().encodeResourceToString(locationList.get(i)));

				if (i < locationList.size() - 1) {
					// exportData.append(',');
					exportData.append('\n');
				} else {
					// exportData.append('\n');
					// exportData.append("]");
				}

				responseData.append(exportData);
			}

			PrintWriter pw = new PrintWriter(ndJsonFile);
			pw.write(responseData.toString());
			pw.close();
			return fileName;

		} catch (Exception e) {
			System.out.println(e.getMessage());
		}

		return null;
	}

	public String processMedicationAdministrationData(DafBulkDataRequest bdr, File destDir, FhirContext ctx,
			List<Integer> patientList, Date start) {

		String fileName = "MedicationAdministration.ndjson";

		MedicationAdministrationResourceProvider medicationAdministrationResourceProvider = new MedicationAdministrationResourceProvider();

		try {
			List<MedicationAdministration> medicationAdministrationList = medicationAdministrationResourceProvider
					.getMedicationAdministrationForBulkDataRequest(patientList, start);

			File ndJsonFile = new File(destDir.getAbsolutePath() + "/" + fileName);
			StringBuilder responseData = new StringBuilder();
			/*
			 * for(MedicationAdministration medicationAdministration :
			 * medicationAdministrationList){ StringBuilder exportData = new
			 * StringBuilder();
			 * exportData.append(ctx.newJsonParser().encodeResourceToString(
			 * medicationAdministration)); exportData.append('\n');
			 * responseData.append(exportData); }
			 */

			for (int i = 0; i < medicationAdministrationList.size(); i++) {
				StringBuilder exportData = new StringBuilder();
				/*
				 * if(i==0) { exportData.append("["); exportData.append('\n'); }
				 */

				exportData.append(ctx.newJsonParser().encodeResourceToString(medicationAdministrationList.get(i)));

				if (i < medicationAdministrationList.size() - 1) {
					// exportData.append(',');
					exportData.append('\n');
				} else {
					// exportData.append('\n');
					// exportData.append("]");
				}

				responseData.append(exportData);
			}

			PrintWriter pw = new PrintWriter(ndJsonFile);
			pw.write(responseData.toString());
			pw.close();
			return fileName;

		} catch (Exception e) {
			System.out.println(e.getMessage());
		}

		return null;
	}

	public String processMedicationDispenseData(DafBulkDataRequest bdr, File destDir, FhirContext ctx,
			List<Integer> patientList, Date start) {

		String fileName = "MedicationDispense.ndjson";

		MedicationDispenseResourceProvider medicationDispenseResourceProvider = new MedicationDispenseResourceProvider();

		try {
			List<MedicationDispense> medicationDispenseList = medicationDispenseResourceProvider
					.getMedicationDispenseForBulkDataRequest(patientList, start);

			File ndJsonFile = new File(destDir.getAbsolutePath() + "/" + fileName);
			StringBuilder responseData = new StringBuilder();
			/*
			 * for(MedicationDispense medicationDispense : medicationDispenseList){
			 * StringBuilder exportData = new StringBuilder();
			 * exportData.append(ctx.newJsonParser().encodeResourceToString(
			 * medicationDispense)); exportData.append('\n');
			 * responseData.append(exportData); }
			 */

			for (int i = 0; i < medicationDispenseList.size(); i++) {
				StringBuilder exportData = new StringBuilder();
				/*
				 * if(i==0) { exportData.append("["); exportData.append('\n'); }
				 */

				exportData.append(ctx.newJsonParser().encodeResourceToString(medicationDispenseList.get(i)));

				if (i < medicationDispenseList.size() - 1) {
					// exportData.append(',');
					exportData.append('\n');
				} else {
					// exportData.append('\n');
					// exportData.append("]");
				}

				responseData.append(exportData);
			}

			PrintWriter pw = new PrintWriter(ndJsonFile);
			pw.write(responseData.toString());
			pw.close();
			return fileName;

		} catch (Exception e) {
			System.out.println(e.getMessage());
		}

		return null;
	}

	public String processMedicationOrderData(DafBulkDataRequest bdr, File destDir, FhirContext ctx,
			List<Integer> patientList, Date start) {

		String fileName = "MedicationOrder.ndjson";

		MedicationOrderResourceProvider medicationOrderResourceProvider = new MedicationOrderResourceProvider();

		try {
			List<MedicationOrder> medicationOrderList = medicationOrderResourceProvider
					.getMedicationOrderForBulkDataRequest(patientList, start);

			File ndJsonFile = new File(destDir.getAbsolutePath() + "/" + fileName);
			StringBuilder responseData = new StringBuilder();
			/*
			 * for(MedicationOrder medicationOrder : medicationOrderList){ StringBuilder
			 * exportData = new StringBuilder();
			 * exportData.append(ctx.newJsonParser().encodeResourceToString(medicationOrder)
			 * ); exportData.append('\n'); responseData.append(exportData); }
			 */

			for (int i = 0; i < medicationOrderList.size(); i++) {
				StringBuilder exportData = new StringBuilder();
				/*
				 * if(i==0) { exportData.append("["); exportData.append('\n'); }
				 */

				exportData.append(ctx.newJsonParser().encodeResourceToString(medicationOrderList.get(i)));

				if (i < medicationOrderList.size() - 1) {
					// exportData.append(',');
					exportData.append('\n');
				} else {
					// exportData.append('\n');
					// exportData.append("]");
				}

				responseData.append(exportData);
			}

			PrintWriter pw = new PrintWriter(ndJsonFile);
			pw.write(responseData.toString());
			pw.close();
			return fileName;

		} catch (Exception e) {
			System.out.println(e.getMessage());
		}

		return null;
	}

	public String processMedicationData(DafBulkDataRequest bdr, File destDir, FhirContext ctx,
			List<Integer> patientList, Date start) {

		String fileName = "Medication.ndjson";

		MedicationResourceProvider medicationResourceProvider = new MedicationResourceProvider();

		try {
			List<Medication> medicationList = medicationResourceProvider.getMedicationForBulkDataRequest(patientList,
					start);

			File ndJsonFile = new File(destDir.getAbsolutePath() + "/" + fileName);
			StringBuilder responseData = new StringBuilder();
			/*
			 * for(Medication medication : medicationList){ StringBuilder exportData = new
			 * StringBuilder();
			 * exportData.append(ctx.newJsonParser().encodeResourceToString(medication));
			 * exportData.append('\n'); responseData.append(exportData); }
			 */

			for (int i = 0; i < medicationList.size(); i++) {
				StringBuilder exportData = new StringBuilder();
				/*
				 * if(i==0) { exportData.append("["); exportData.append('\n'); }
				 */

				exportData.append(ctx.newJsonParser().encodeResourceToString(medicationList.get(i)));

				if (i < medicationList.size() - 1) {
					// exportData.append(',');
					exportData.append('\n');
				} else {
					// exportData.append('\n');
					// exportData.append("]");
				}

				responseData.append(exportData);
			}

			PrintWriter pw = new PrintWriter(ndJsonFile);
			pw.write(responseData.toString());
			pw.close();
			return fileName;

		} catch (Exception e) {
			System.out.println(e.getMessage());
		}

		return null;
	}

	public String processMedicationStatementData(DafBulkDataRequest bdr, File destDir, FhirContext ctx,
			List<Integer> patientList, Date start) {

		String fileName = "MedicationStatement.ndjson";

		MedicationStatementResourceProvider medicationStatementResourceProvider = new MedicationStatementResourceProvider();

		try {
			List<MedicationStatement> medicationStatementList = medicationStatementResourceProvider
					.getMedicationStatementForBulkDataRequest(patientList, start);

			File ndJsonFile = new File(destDir.getAbsolutePath() + "/" + fileName);
			StringBuilder responseData = new StringBuilder();
			/*
			 * for(MedicationStatement medicationStatement : medicationStatementList){
			 * StringBuilder exportData = new StringBuilder();
			 * exportData.append(ctx.newJsonParser().encodeResourceToString(
			 * medicationStatement)); exportData.append('\n');
			 * responseData.append(exportData); }
			 */

			for (int i = 0; i < medicationStatementList.size(); i++) {
				StringBuilder exportData = new StringBuilder();
				/*
				 * if(i==0) { exportData.append("["); exportData.append('\n'); }
				 */

				exportData.append(ctx.newJsonParser().encodeResourceToString(medicationStatementList.get(i)));

				if (i < medicationStatementList.size() - 1) {
					// exportData.append(',');
					exportData.append('\n');
				} else {
					// exportData.append('\n');
					// exportData.append("]");
				}

				responseData.append(exportData);
			}

			PrintWriter pw = new PrintWriter(ndJsonFile);
			pw.write(responseData.toString());
			pw.close();
			return fileName;

		} catch (Exception e) {
			System.out.println(e.getMessage());
		}

		return null;
	}

	public String processObservationData(DafBulkDataRequest bdr, File destDir, FhirContext ctx,
			List<Integer> patientList, Date start) {

		String fileName = "Observation.ndjson";

		ObservationResourceProvider observationResourceProvider = new ObservationResourceProvider();

		try {
			List<Observation> observationList = observationResourceProvider
					.getObservationForBulkDataRequest(patientList, start);

			File ndJsonFile = new File(destDir.getAbsolutePath() + "/" + fileName);
			StringBuilder responseData = new StringBuilder();
			/*
			 * for(Observation observation : observationList){ StringBuilder exportData =
			 * new StringBuilder();
			 * exportData.append(ctx.newJsonParser().encodeResourceToString(observation));
			 * exportData.append('\n'); responseData.append(exportData); }
			 */

			for (int i = 0; i < observationList.size(); i++) {
				StringBuilder exportData = new StringBuilder();
				/*
				 * if(i==0) { exportData.append("["); exportData.append('\n'); }
				 */

				exportData.append(ctx.newJsonParser().encodeResourceToString(observationList.get(i)));

				if (i < observationList.size() - 1) {
					// exportData.append(',');
					exportData.append('\n');
				} else {
					// exportData.append('\n');
					// exportData.append("]");
				}

				responseData.append(exportData);
			}

			PrintWriter pw = new PrintWriter(ndJsonFile);
			pw.write(responseData.toString());
			pw.close();
			return fileName;

		} catch (Exception e) {
			System.out.println(e.getMessage());
		}

		return null;
	}

	public String processOrganizationData(DafBulkDataRequest bdr, File destDir, FhirContext ctx,
			List<Integer> patientList, Date start) {

		String fileName = "Organization.ndjson";

		OrganizationResourceProvider organizationResourceProvider = new OrganizationResourceProvider();

		try {
			List<Organization> organizationList = organizationResourceProvider
					.getOrganizationForBulkDataRequest(patientList, start);

			File ndJsonFile = new File(destDir.getAbsolutePath() + "/" + fileName);
			StringBuilder responseData = new StringBuilder();
			/*
			 * for(Organization organization : organizationList){ StringBuilder exportData =
			 * new StringBuilder();
			 * exportData.append(ctx.newJsonParser().encodeResourceToString(organization));
			 * exportData.append('\n'); responseData.append(exportData); }
			 */

			for (int i = 0; i < organizationList.size(); i++) {
				StringBuilder exportData = new StringBuilder();
				/*
				 * if(i==0) { exportData.append("["); exportData.append('\n'); }
				 */

				exportData.append(ctx.newJsonParser().encodeResourceToString(organizationList.get(i)));

				if (i < organizationList.size() - 1) {
					// exportData.append(',');
					exportData.append('\n');
				} else {
					// exportData.append('\n');
					// exportData.append("]");
				}

				responseData.append(exportData);
			}

			PrintWriter pw = new PrintWriter(ndJsonFile);
			pw.write(responseData.toString());
			pw.close();
			return fileName;

		} catch (Exception e) {
			System.out.println(e.getMessage());
		}

		return null;
	}

	public String processProcedureData(DafBulkDataRequest bdr, File destDir, FhirContext ctx, List<Integer> patientList,
			Date start) {

		String fileName = "Procedure.ndjson";

		ProcedureResourceProvider procedureResourceProvider = new ProcedureResourceProvider();

		try {
			List<Procedure> procedureList = procedureResourceProvider.getProcedureForBulkDataRequest(patientList,
					start);

			File ndJsonFile = new File(destDir.getAbsolutePath() + "/" + fileName);
			StringBuilder responseData = new StringBuilder();
			/*
			 * for(Procedure procedure : procedureList){ StringBuilder exportData = new
			 * StringBuilder();
			 * exportData.append(ctx.newJsonParser().encodeResourceToString(procedure));
			 * exportData.append('\n'); responseData.append(exportData); }
			 */

			for (int i = 0; i < procedureList.size(); i++) {
				StringBuilder exportData = new StringBuilder();
				/*
				 * if(i==0) { exportData.append("["); exportData.append('\n'); }
				 */

				exportData.append(ctx.newJsonParser().encodeResourceToString(procedureList.get(i)));

				if (i < procedureList.size() - 1) {
					// exportData.append(',');
					exportData.append('\n');
				} else {
					// exportData.append('\n');
					// exportData.append("]");
				}

				responseData.append(exportData);
			}

			PrintWriter pw = new PrintWriter(ndJsonFile);
			pw.write(responseData.toString());
			pw.close();
			return fileName;

		} catch (Exception e) {
			System.out.println(e.getMessage());
		}

		return null;
	}

}
