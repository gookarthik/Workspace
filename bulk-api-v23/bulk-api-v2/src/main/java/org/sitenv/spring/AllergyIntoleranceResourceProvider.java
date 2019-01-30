package org.sitenv.spring;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.sitenv.spring.configuration.AppConfig;
import org.sitenv.spring.model.DafAllergyIntolerance;
import org.sitenv.spring.model.DafPatientJson;
import org.sitenv.spring.service.AllergyIntoleranceService;
import org.sitenv.spring.util.HapiUtils;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Scope;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonGenerationException;

import ca.uhn.fhir.model.api.Include;
import ca.uhn.fhir.model.dstu2.composite.CodeableConceptDt;
import ca.uhn.fhir.model.dstu2.composite.CodingDt;
import ca.uhn.fhir.model.dstu2.composite.ResourceReferenceDt;
import ca.uhn.fhir.model.dstu2.resource.AllergyIntolerance;
import ca.uhn.fhir.model.dstu2.resource.AllergyIntolerance.Reaction;
import ca.uhn.fhir.model.dstu2.valueset.AllergyIntoleranceCategoryEnum;
import ca.uhn.fhir.model.dstu2.valueset.AllergyIntoleranceCriticalityEnum;
import ca.uhn.fhir.model.dstu2.valueset.AllergyIntoleranceSeverityEnum;
import ca.uhn.fhir.model.dstu2.valueset.AllergyIntoleranceStatusEnum;
import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.rest.annotation.Count;
import ca.uhn.fhir.rest.annotation.Create;
import ca.uhn.fhir.rest.annotation.IdParam;
import ca.uhn.fhir.rest.annotation.IncludeParam;
import ca.uhn.fhir.rest.annotation.Read;
import ca.uhn.fhir.rest.annotation.RequiredParam;
import ca.uhn.fhir.rest.annotation.ResourceParam;
import ca.uhn.fhir.rest.annotation.Search;
import ca.uhn.fhir.rest.annotation.Sort;
import ca.uhn.fhir.rest.api.MethodOutcome;
import ca.uhn.fhir.rest.api.SortSpec;
import ca.uhn.fhir.rest.param.ReferenceParam;
import ca.uhn.fhir.rest.server.IResourceProvider;


@Component
@Scope("request")
public class AllergyIntoleranceResourceProvider implements IResourceProvider {

	public static final String RESOURCE_TYPE = "AllergyIntolerance";
	public static final String VERSION_ID = "2.0";
	AbstractApplicationContext context;
	AllergyIntoleranceService service;


	public AllergyIntoleranceResourceProvider() {
		context = new AnnotationConfigApplicationContext(AppConfig.class);
		service = (AllergyIntoleranceService) context.getBean("allergyIntoleranceResourceService");
	}

	/**
	 * The getResourceType method comes from IResourceProvider, and must
	 * be overridden to indicate what type of resource this provider
	 * supplies.
	 */
	@Override
	public Class<AllergyIntolerance> getResourceType() {
		return AllergyIntolerance.class;
	}


	/**The "@Search" annotation indicates that this method supports the search operation. You may have many different method annotated with this annotation, to support many different search criteria.
	 * @param theIncludes
	 * @param theSort
	 * @param theCount
	 * @return Returns list of resource matching this identifier, or null if none exists.
	 * 
	 * Ex: http://<server name>/<context>/fhir/AllergyIntolerance?_pretty=true&_format=json
	 */
	@Search
	public List<AllergyIntolerance> getAllAllergyIntolerance(@IncludeParam(allow = "*") Set<Include> theIncludes, @Sort SortSpec theSort, @Count Integer theCount) {

		List<DafAllergyIntolerance> dafAllergyIntoleranceList = service.getAllAllergyIntolerance();

		List<AllergyIntolerance> allergyIntoleranceList = new ArrayList<AllergyIntolerance>();

		for (DafAllergyIntolerance dafAllergyIntolerance : dafAllergyIntoleranceList) {
			allergyIntoleranceList.add(createAllergyIntoleranceObject(dafAllergyIntolerance));
		}

		return allergyIntoleranceList;
	}

	/**
	 * This is the "read" operation. The "@Read" annotation indicates that this method supports the read and/or vread operation.
	 * <p>
	 * Read operations take a single parameter annotated with the {@link IdParam} paramater, and should return a single resource instance.
	 * </p>
	 *
	 * @param theId The read operation takes one parameter, which must be of type IdDt and must be annotated with the "@Read.IdParam" annotation.
	 * @return Returns a resource matching this identifier, or null if none exists.
	 * 
	 * Ex: http://<server name>/<context>/fhir/AllergyIntolerance/1?_pretty=true&_format=json
	 */
	@Read()
	public AllergyIntolerance getAllergyIntoleranceResourceById(@IdParam IdDt theId) {
		
		DafAllergyIntolerance dafAllergyIntolerance = service.getAllergyIntoleranceResourceById(theId.getIdPartAsLong().intValue());
		
		AllergyIntolerance allergyIntolerance = createAllergyIntoleranceObject(dafAllergyIntolerance);

		return allergyIntolerance;
	}

	/**
	 * The "@Search" annotation indicates that this method supports the search operation. You may have many different method annotated with this annotation, to support many different search criteria.
	 * 
	 *
	 * @param thePatient
	 * @param theIncludes
	 * @param theSort
	 * @param theCount
	 * @return This method returns a list of AllergyIntolerance. This list may contain multiple matching resources, or it may also be empty.
	 * 
	 * Ex: http://<server name>/<context>/fhir/AllergyIntolerance?patient=1&_format=json
	 */
	@Search()
	public List<AllergyIntolerance> searchByPatient(@RequiredParam(name = AllergyIntolerance.SP_PATIENT) ReferenceParam thePatient,
			@IncludeParam(allow = "*") Set<Include> theIncludes, @Sort SortSpec theSort, @Count Integer theCount) {
		String patientId = thePatient.getIdPart();
		List<DafAllergyIntolerance> dafAllergyIntoleranceList = service.getAllergyIntoleranceByPatient(patientId);

		List<AllergyIntolerance> allergyIntoleranceList = new ArrayList<AllergyIntolerance>();

		for (DafAllergyIntolerance dafAllergyIntolerance : dafAllergyIntoleranceList) {
			allergyIntoleranceList.add(createAllergyIntoleranceObject(dafAllergyIntolerance));
		}
		return allergyIntoleranceList;
	}

	@Create
	public MethodOutcome saveAllergyIntolerance(@ResourceParam AllergyIntolerance intolerance ) throws JsonGenerationException, JsonMappingException, IOException {

		MethodOutcome retVal = new MethodOutcome();
		try 
		{	
			DafAllergyIntolerance dafAllergy = convertAllergyIntoleranceToDafAllergyIntolerance(intolerance );
			DafAllergyIntolerance daf1 = service.saveAllergyIntolerance(dafAllergy);
//			DafPatientJson daf1 = service.savePatient(dafpatient);
			retVal.setId(new IdDt("1"));
		}
		catch(Exception e)
		{
			System.out.println(e.getMessage());
			e.printStackTrace();
		}
		return retVal;
	}

	private DafAllergyIntolerance convertAllergyIntoleranceToDafAllergyIntolerance(AllergyIntolerance intolerance) throws JsonGenerationException, JsonMappingException, IOException{
		DafAllergyIntolerance dafAllergy = new DafAllergyIntolerance();
		ObjectMapper mapperObj =  new ObjectMapper();


		//Substance
		dafAllergy.setSubstance_system(intolerance.getSubstance().getCoding().get(0).getSystem());

		dafAllergy.setSubstance_code(intolerance.getSubstance().getCoding().get(0).getCode());
		dafAllergy.setSubstance_display(intolerance.getSubstance().getCoding().get(0).getDisplay());
		System.out.println(intolerance.getSubstance().getCoding().get(0).getDisplay());


		//status
		dafAllergy.setStatus(intolerance.getStatus());
		//System.out.println(intolerance.getStatus());

		//Criticality
		dafAllergy.setCriticality(intolerance.getCriticality());
		System.out.println(intolerance.getCriticality());

		// Category
		dafAllergy.setCategory(intolerance.getCategory());
		System.out.println(intolerance.getCategory());


		//reaction
		HashMap<String, String> reaction = new HashMap<String,String>();
		reaction.put("mainfest_code", intolerance.getReaction().get(0).getManifestation().get(0).getCoding().get(0).getCode());
		reaction.put("manifest_display", intolerance.getReaction().get(0).getManifestation().get(0).getCoding().get(0).getDisplay());
    	reaction.put("manifest_system", intolerance.getReaction().get(0).getManifestation().get(0).getCoding().get(0).getSystem());
		reaction.put("manifest_text", intolerance.getReaction().get(0).getManifestation().get(0).getText());
		reaction.put("severity", intolerance.getReaction().get(0).getSeverity());

		String dafreaction =  mapperObj.writeValueAsString(reaction);
		System.out.println(dafreaction);
		dafAllergy.setReaction(dafreaction); 	


		return dafAllergy;
	}


	/**
	 * This method is implemented to get AllergyIntolerances for Bulk data request
	 * @param patients
	 * @param start
	 * @return This method returns a list of AllergyIntolerance. This list may contain multiple matching resources, or it may also be empty.
	 */

	public List<AllergyIntolerance> getAllergyIntoleranceForBulkDataRequest(List<Integer> patients, Date start) {



		List<DafAllergyIntolerance> dafAllergyIntoleranceList = service.getAllergyIntoleranceForBulkData(patients, start);

		List<AllergyIntolerance> allergyIntoleranceList = new ArrayList<AllergyIntolerance>();


		for (DafAllergyIntolerance dafAllergyIntolerance : dafAllergyIntoleranceList) {
			allergyIntoleranceList.add(createAllergyIntoleranceObject(dafAllergyIntolerance));
		}


		return allergyIntoleranceList;
	}


	/** 
	 *
	 * @param dafAllergyIntolerance
	 * @return AllergyIntolerance object
	 *
	 * This method converts DafAllergyIntolerance object to AllergyIntolerance object
	 *
	 */
	private AllergyIntolerance createAllergyIntoleranceObject(DafAllergyIntolerance dafAllergyIntolerance) {

		AllergyIntolerance allergyIntolerance = new AllergyIntolerance();

		// Set Version
		allergyIntolerance.setId(new IdDt(RESOURCE_TYPE, dafAllergyIntolerance.getId() + "", VERSION_ID));

		//Set identifier
		/*List<IdentifierDt> identifier = new ArrayList<IdentifierDt>();
    	IdentifierDt identifierDt = new IdentifierDt();
    	identifierDt.setSystem(dafAllergyIntolerance.getIdentifier_system().trim());
    	identifierDt.setValue(dafAllergyIntolerance.getIdentifier_value().trim());
    	identifier.add(identifierDt);
    	allergyIntolerance.setIdentifier(identifier);*/

		//set patient
		ResourceReferenceDt patientResource = new ResourceReferenceDt();
		String theId = "Patient/" + Integer.toString(dafAllergyIntolerance.getPatient().getId());
		patientResource.setReference(theId);
		allergyIntolerance.setPatient(patientResource);

		//Set Asserter
		ResourceReferenceDt dispenseResource = new ResourceReferenceDt();
		String thePrescriberId = "Practitioner/" + Integer.toString(dafAllergyIntolerance.getRecorder().getId());
		dispenseResource.setReference(thePrescriberId);
		allergyIntolerance.setRecorder(dispenseResource);

		//set Substance
		CodeableConceptDt classCodeDt = new CodeableConceptDt();
		CodingDt classCodingDt = new CodingDt();
		classCodingDt.setSystem(dafAllergyIntolerance.getSubstance_system().trim());
		classCodingDt.setCode(dafAllergyIntolerance.getSubstance_code().trim());
		classCodingDt.setDisplay(dafAllergyIntolerance.getSubstance_display().trim());
		classCodeDt.addCoding(classCodingDt);
		allergyIntolerance.setSubstance(classCodeDt);

		//set Status
		allergyIntolerance.setStatus(AllergyIntoleranceStatusEnum.valueOf(dafAllergyIntolerance.getStatus().trim()));

		//set Criticality
		allergyIntolerance.setCriticality(AllergyIntoleranceCriticalityEnum.valueOf(dafAllergyIntolerance.getCriticality().trim()));

		//set Category
		allergyIntolerance.setCategory(AllergyIntoleranceCategoryEnum.valueOf(dafAllergyIntolerance.getCategory().trim()));

		//set reaction
		Map<String, String> reaction = HapiUtils.convertToJsonMap(dafAllergyIntolerance.getReaction());
		List<Reaction> allergyReaactionList = new ArrayList<AllergyIntolerance.Reaction>();
		Reaction allergyReaction = new Reaction();
		//set Reaction Substance
		/*CodeableConceptDt substanceCodeDt = new CodeableConceptDt();
	        CodingDt substanceCodingDt = new CodingDt();
	        substanceCodingDt.setCode(reaction.get("substance_code"));
	        substanceCodingDt.setDisplay(reaction.get("substance_display"));
	        substanceCodeDt.addCoding(substanceCodingDt);
	        allergyReaction.setSubstance(substanceCodeDt);*/
		//set manifestation
		List<CodeableConceptDt> manifestList = new ArrayList<CodeableConceptDt>();
		CodeableConceptDt manifestcode = new CodeableConceptDt();
		CodingDt manifestcoding = new CodingDt();
		manifestcoding.setCode(reaction.get("mainfest_code"));
		manifestcoding.setDisplay(reaction.get("manifest_display"));
		manifestcoding.setSystem(reaction.get("manifest_system"));
		manifestcode.addCoding(manifestcoding);
		manifestcode.setText(reaction.get("manifest_text"));
		manifestList.add(manifestcode);
		allergyReaction.setManifestation(manifestList);
		//set severity
		allergyReaction.setSeverity(AllergyIntoleranceSeverityEnum.valueOf(reaction.get("severity").toUpperCase()));
		allergyReaactionList.add(allergyReaction);
		allergyIntolerance.setReaction(allergyReaactionList);

		return allergyIntolerance;
	}
}