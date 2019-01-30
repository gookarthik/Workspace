package org.sitenv.spring;

import ca.uhn.fhir.model.api.Include;
import ca.uhn.fhir.model.dstu2.composite.*;
import ca.uhn.fhir.model.dstu2.resource.MedicationOrder;
import ca.uhn.fhir.model.dstu2.resource.Observation;
import ca.uhn.fhir.model.dstu2.resource.Observation.Component;
import ca.uhn.fhir.model.dstu2.valueset.ObservationStatusEnum;
import ca.uhn.fhir.model.primitive.*;
import ca.uhn.fhir.rest.annotation.*;
import ca.uhn.fhir.rest.api.MethodOutcome;
import ca.uhn.fhir.rest.api.SortSpec;
import ca.uhn.fhir.rest.param.DateRangeParam;
import ca.uhn.fhir.rest.param.ReferenceParam;
import ca.uhn.fhir.rest.param.StringOrListParam;
import ca.uhn.fhir.rest.param.StringParam;
import ca.uhn.fhir.rest.server.IResourceProvider;
import ca.uhn.fhir.rest.server.exceptions.ResourceNotFoundException;

import org.sitenv.spring.configuration.AppConfig;
import org.sitenv.spring.model.DafMedicationOrder;
import org.sitenv.spring.model.DafObservation;
import org.sitenv.spring.query.ObservationSearchCriteria;
import org.sitenv.spring.service.ObservationService;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.AbstractApplicationContext;

import java.util.*;

public class ObservationResourceProvider implements IResourceProvider {

	public static final String RESOURCE_TYPE = "Observation";
	public static final String VERSION_ID = "2.0";
	AbstractApplicationContext context;
	ObservationService service;

	public ObservationResourceProvider() {
		context = new AnnotationConfigApplicationContext(AppConfig.class);
		service = (ObservationService) context.getBean("observationResourceService");
	}

	/**
	 * The getResourceType method comes from IResourceProvider, and must be
	 * overridden to indicate what type of resource this provider supplies.
	 */
	@Override
	public Class<Observation> getResourceType() {
		return Observation.class;
	}

	/**
	 * The "@Search" annotation indicates that this method supports the search
	 * operation. You may have many different method annotated with this
	 * annotation, to support many different search criteria.
	 * 
	 * @param theIncludes
	 * @param theSort
	 * @param theCount
	 * @return Returns all the available Observation records.
	 * 
	 *         Ex: http://<server name>/
	 *         <context>/fhir/Observation?_pretty=true&_format=json
	 */
	@Search
	public List<Observation> getAllObservations(@IncludeParam(allow = "*") Set<Include> theIncludes,
			@Sort SortSpec theSort, @Count Integer theCount) {

		List<DafObservation> dafObservationsList = service.getAllObservations();

		List<Observation> observationList = new ArrayList<Observation>();

		for (DafObservation dafObservation : dafObservationsList) {
			if (dafObservation.getPatient() != null) {
				observationList.add(createObservationObject(dafObservation));
			}
		}

		return observationList;
	}
	
	  public List<Observation> getObservationForBulkDataRequest(List<Integer> patients, Date start) {
	    	
	    	
			
			List<DafObservation> dafObservationList = service.getObservationForBulkData(patients, start);

			List<Observation> observationList = new ArrayList<Observation>();

			for (DafObservation dafObservation : dafObservationList) {
				observationList.add(createObservationObject(dafObservation));
			}
			
			return observationList;
		}


	/**
	 * This is the "read" operation. The "@Read" annotation indicates that this
	 * method supports the read and/or vread operation.
	 * <p>
	 * Read operations take a single parameter annotated with the
	 * {@link IdParam} paramater, and should return a single resource instance.
	 * </p>
	 *
	 * @param theId
	 *            The read operation takes one parameter, which must be of type
	 *            IdDt and must be annotated with the "@Read.IdParam"
	 *            annotation.
	 * @return Returns a resource matching this identifier, or null if none
	 *         exists.
	 */
	@Read()
	public Observation getObservationResourceById(@IdParam IdDt theId) {

		try {
			Integer observationId = theId.getIdPartAsLong().intValue();
			DafObservation dafObservation = service.getObservationResourceById(observationId);
			if (dafObservation.getPatient() != null) {
				Observation observation = createObservationObject(dafObservation);
				return observation;
			} else {
				throw new ResourceNotFoundException(theId);
			}
		} catch (Exception e) {
			/*
			 * If we can't parse the ID as a long, it's not valid so this is an
			 * unknown resource
			 */
			throw new ResourceNotFoundException(theId);
		}
	}

	@Search
	public Observation getObservationResourceVitalSignsById(@RequiredParam(name = "vital-signs") ReferenceParam theId) {
		try {
			DafObservation dafObservation = service
					.getObservationResourceByIdandCategory(theId.getIdPartAsLong().intValue(), "vital-signs");

			Observation observation = createObservationObject(dafObservation);

			return observation;
		} catch (Exception e) {
			/*
			 * If we can't parse the ID as a long, it's not valid so this is an
			 * unknown resource
			 */
			throw new ResourceNotFoundException("Resource vital-signs with Id " + theId.getIdPart() + " is not present");
		}
	}

	@Search
	public Observation getObservationResourceBySocialHistoryId(
			@RequiredParam(name = "social-history") ReferenceParam theId) {
		try {

			DafObservation dafObservation = service
					.getObservationResourceByIdandCategory(theId.getIdPartAsLong().intValue(), "social-history");

			Observation observation = createObservationObject(dafObservation);

			return observation;
		} catch (Exception e) {
			/*
			 * If we can't parse the ID as a long, it's not valid so this is an
			 * unknown resource
			 */
			throw new ResourceNotFoundException("Resource social-history with Id " + theId.getIdPart() + " is not present");
		}
	}

	@Search
	public Observation getObservationResourceByResultsId(@RequiredParam(name = "laboratory") ReferenceParam theId) {
		try {

			DafObservation dafObservation = service
					.getObservationResourceByIdandCategory(theId.getIdPartAsLong().intValue(), "laboratory");

			Observation observation = createObservationObject(dafObservation);

			return observation;
		} catch (Exception e) {
			/*
			 * If we can't parse the ID as a long, it's not valid so this is an
			 * unknown resource
			 */
			throw new ResourceNotFoundException("Resource laboratory with Id " + theId.getIdPart() + " is not present");
		}
	}

	/**
	 * The "@Search" annotation indicates that this method supports the search
	 * operation. You may have many different method annotated with this
	 * annotation, to support many different search criteria. This example
	 * searches by patient
	 *
	 * @param thePatient
	 * @param theCategory
	 * @param theCode
	 * @param theRange
	 * @param theIncludes
	 * @param theSort
	 * @param theCount
	 * @return This method returns a list of Observations. This list may contain
	 *         multiple matching resources, or it may also be empty.
	 * 
	 *         Ex: http://<server name>/
	 *         <context>/fhir/Observation?patient=1&category=vital-signs&_format
	 *         =json
	 */
	@Search()
	public List<Observation> searchByPatient(@RequiredParam(name = Observation.SP_PATIENT) ReferenceParam thePatient,
			@OptionalParam(name = Observation.SP_CATEGORY) StringDt theCategory,
			@OptionalParam(name = Observation.SP_CODE) StringOrListParam theCode,
			@OptionalParam(name = Observation.SP_DATE) DateRangeParam theRange,
			@IncludeParam(allow = "*") Set<Include> theIncludes, @Sort SortSpec theSort, @Count Integer theCount) {
		String patientId = thePatient.getIdPart();

		ObservationSearchCriteria observationSearchCriteria = new ObservationSearchCriteria();
		observationSearchCriteria.setPatient(Integer.parseInt(patientId));
		if (theCategory != null) {
			observationSearchCriteria.setCategory(theCategory.getValue());
		}
		if (theCode != null) {
			List<StringParam> wantedCodings = theCode.getValuesAsQueryTokens();
			List<String> aList = new ArrayList<String>();
			for (int i = 0; i < wantedCodings.size(); i++) {
				aList.add(wantedCodings.get(i).getValue().toString());
			}
			observationSearchCriteria.setCode(aList);
		}
		if (theRange != null) {
			observationSearchCriteria.setRangedates(theRange);
		}
		// List<DafObservation> dafObservationList =
		// service.getObservationByPatient(patientId);
		List<DafObservation> dafObservationList = service.getObservationBySearchCriteria(observationSearchCriteria);

		List<Observation> observationList = new ArrayList<Observation>();

		for (DafObservation dafObservation : dafObservationList) {
			if (dafObservation.getPatient() != null) {
				observationList.add(createObservationObject(dafObservation));
			}
		}
		return observationList;
	}

	/**
	 * This method converts DafObservation object to Observation object
	 */
	private Observation createObservationObject(DafObservation dafObservation) {
		Observation observation = new Observation();

		// Set Version
		observation.setId(new IdDt(RESOURCE_TYPE, dafObservation.getId() + "", VERSION_ID));

		// set patient
		ResourceReferenceDt patientResource = new ResourceReferenceDt();
		String theId = "Patient/" + Integer.toString(dafObservation.getPatient().getId());
		patientResource.setReference(theId);
		observation.setSubject(patientResource);

		// set Observation Identifier
		List<IdentifierDt> identifierDts = new ArrayList<IdentifierDt>();
		IdentifierDt identifierDt = new IdentifierDt();
		identifierDt.setSystem(dafObservation.getIdentifier_system().trim());
		identifierDt.setValue(dafObservation.getIdentifier_value().trim());
		identifierDts.add(identifierDt);
		observation.setIdentifier(identifierDts);

		// Set Observation Code
		CodeableConceptDt classCodeDt = new CodeableConceptDt();
		CodingDt classCodingDt = new CodingDt();
		classCodingDt.setSystem(dafObservation.getCode_system().trim());
		classCodingDt.setCode(dafObservation.getCode().trim());
		classCodingDt.setDisplay(dafObservation.getCode_display().trim());
		classCodeDt.addCoding(classCodingDt);
		classCodeDt.setText(dafObservation.getCode_text().trim());
		observation.setCode(classCodeDt);

		// Set observation category
		CodeableConceptDt codeableConceptDt = new CodeableConceptDt();
		CodingDt codingDt = new CodingDt();
		codingDt.setCode(dafObservation.getCat_code().trim());
		codingDt.setDisplay(dafObservation.getCat_display().trim());
		codingDt.setSystem(dafObservation.getCat_system().trim());
		codeableConceptDt.addCoding(codingDt);
		observation.setCategory(codeableConceptDt);

		// Set Observation Status
		observation.setStatus(ObservationStatusEnum.valueOf(dafObservation.getStatus().trim()));

		if (dafObservation.getCat_code().trim().equalsIgnoreCase("SOCIAL-HISTORY")) {

			// set value
			CodeableConceptDt severityCodeDt = new CodeableConceptDt();
			CodingDt severityCodingDt = new CodingDt();
			severityCodingDt.setSystem(dafObservation.getVal_system().trim());
			severityCodingDt.setCode(dafObservation.getVal_code().trim());
			severityCodingDt.setDisplay(dafObservation.getVal_display().trim());
			severityCodeDt.addCoding(severityCodingDt);
			severityCodeDt.setText(dafObservation.getVal_text().trim());
			observation.setValue(severityCodeDt);

			InstantDt instantDt = new InstantDt();
			instantDt.setValue(dafObservation.getEffectiveDate());
			observation.setIssued(instantDt);

		} else if (dafObservation.getCat_code().trim().equalsIgnoreCase("LABORATORY")) {

			QuantityDt quanDt = new QuantityDt();
			quanDt.setValue(dafObservation.getValQuanValue());
			quanDt.setUnit(dafObservation.getValQuanUnit().trim());
			quanDt.setSystem(dafObservation.getValQuanSystem().trim());

			observation.setValue(quanDt);
			DateTimeDt effectDate = new DateTimeDt();
			effectDate.setValue(dafObservation.getEffectiveDate());
			observation.setEffective(effectDate);

		} else if (dafObservation.getCat_code().trim().equalsIgnoreCase("VITAL-SIGNS")) {

			if (dafObservation.getVal_code().trim().equals("55284-4")) {
				List<DafObservation> bpList = service.getObservationByBPCode(dafObservation.getVal_code());
				List<Component> components = new ArrayList<Observation.Component>();
				for (int i = 0; i < bpList.size(); i++) {
					Component nComp = new Component();
					CodeableConceptDt compCodeDt = new CodeableConceptDt();
					CodingDt compCodingDt = new CodingDt();
					compCodingDt.setSystem(bpList.get(i).getVal_system().trim());
					compCodingDt.setCode(bpList.get(i).getVal_code().trim());
					compCodingDt.setDisplay(bpList.get(i).getVal_display().trim());
					compCodeDt.addCoding(compCodingDt);
					compCodeDt.setText(bpList.get(i).getVal_text().trim());

					nComp.setCode(compCodeDt);

					QuantityDt compValDt = new QuantityDt();
					compValDt.setValue(bpList.get(i).getValQuanValue());
					compValDt.setUnit(bpList.get(i).getValQuanUnit().trim());
					compValDt.setSystem(bpList.get(i).getValQuanSystem().trim());

					CodeDt compValCodeDt = new CodeDt();
					compValCodeDt.setValue(bpList.get(i).getValQuanCode().trim());
					compValDt.setCode(compValCodeDt);

					nComp.setValue(compValDt);

					components.add(nComp);
				}
				observation.setComponent(components);
			} else {
				List<Component> compList = new ArrayList<Observation.Component>();
				Component nComp = new Component();
				CodeableConceptDt compCodeDt = new CodeableConceptDt();
				CodingDt compCodingDt = new CodingDt();
				compCodingDt.setSystem(dafObservation.getVal_system().trim());
				compCodingDt.setCode(dafObservation.getVal_code().trim());
				compCodingDt.setDisplay(dafObservation.getVal_display().trim());
				compCodeDt.addCoding(compCodingDt);
				compCodeDt.setText(dafObservation.getVal_text().trim());

				nComp.setCode(compCodeDt);

				QuantityDt compValDt = new QuantityDt();
				compValDt.setValue(dafObservation.getValQuanValue());
				compValDt.setUnit(dafObservation.getValQuanUnit().trim());
				compValDt.setSystem(dafObservation.getValQuanSystem().trim());

				CodeDt compValCodeDt = new CodeDt();
				compValCodeDt.setValue(dafObservation.getValQuanCode().trim());
				compValDt.setCode(compValCodeDt);

				nComp.setValue(compValDt);

				compList.add(nComp);

				observation.setComponent(compList);
			}
			DateTimeDt vitalEffectDate = new DateTimeDt();
			vitalEffectDate.setValue(dafObservation.getEffectiveDate());
			observation.setEffective(vitalEffectDate);
		}
		return observation;
	}
	
	@Create
	public MethodOutcome saveMedOrd(@ResourceParam Observation theObservation) {
		
		MethodOutcome retVal  = new MethodOutcome();
		try {
			DafObservation dafObser = ObserveToDaf(theObservation); 
			retVal.setId(new IdDt("1"));
			
			DafObservation daf = service.saveMed(dafObser);
		}catch(Exception e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		}
		System.err.println("save MedicationOrder done!!!");
		return retVal;
	}

	private DafObservation ObserveToDaf(Observation theObservation) {
		
		System.err.println("in ObserveToDaf");
		DafObservation daf = new DafObservation();
		
		daf.setCode(theObservation.getCode().getCoding().get(0).getCode().toString());
		daf.setCat_system(theObservation.getCode().getCoding().get(0).getSystem().toString());
		daf.setCode_display(theObservation.getCode().getCoding().get(0).getDisplay().toString());
		daf.setCat_code(theObservation.getCategory().getCoding().get(0).getCode().toString());
		daf.setCat_display(theObservation.getCategory().getCoding().get(0).getDisplay().toString());
		daf.setCat_system(theObservation.getCategory().getCoding().get(0).getSystem().toString());
		daf.setVal_code(theObservation.getValue().toString());
		daf.setVal_display(theObservation.getCode().toString());
		daf.setVal_system(theObservation.getText().toString());
		daf.setStatus(theObservation.getStatus().toString());
		
		System.err.println("Coming out of ObserveToDaf");	
		return daf;
	}

}
