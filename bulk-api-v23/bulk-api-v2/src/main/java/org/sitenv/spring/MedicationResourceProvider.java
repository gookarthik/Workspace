package org.sitenv.spring;

import ca.uhn.fhir.model.api.Include;
import ca.uhn.fhir.model.dstu2.composite.*;
import ca.uhn.fhir.model.dstu2.resource.Medication;
import ca.uhn.fhir.model.dstu2.resource.Medication.ProductIngredient;
import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.rest.annotation.*;
import ca.uhn.fhir.rest.api.MethodOutcome;
import ca.uhn.fhir.rest.api.SortSpec;
import ca.uhn.fhir.rest.server.IResourceProvider;

import org.sitenv.spring.configuration.AppConfig;
import org.sitenv.spring.model.DafManufacturer;
import org.sitenv.spring.model.DafMedication;
import org.sitenv.spring.service.MedicationService;
import org.sitenv.spring.util.HapiUtils;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Scope;
import org.springframework.context.support.AbstractApplicationContext;

import java.util.*;

@Scope("request")
public class MedicationResourceProvider implements IResourceProvider {

    public static final String RESOURCE_TYPE = "Medication";
    public static final String VERSION_ID = "2.0";
    AbstractApplicationContext context;
    MedicationService service;

    public MedicationResourceProvider() {
        context = new AnnotationConfigApplicationContext(AppConfig.class);
        service = (MedicationService) context.getBean("medicationService");
    }

    /**
     * The getResourceType method comes from IResourceProvider, and must
     * be overridden to indicate what type of resource this provider
     * supplies.
     */
    @Override
    public Class<Medication> getResourceType() {
        return Medication.class;
    }

    /**
     * This method returns all the available Medication records.
     * <p/>
     * Ex: http://<server name>/<context>/fhir/Medication?_pretty=true&_format=json
     */
    @Search
    public List<Medication> getAllMedication(@IncludeParam(allow = "*") Set<Include> theIncludes, @Sort SortSpec theSort, @Count Integer theCount) {

        List<DafMedication> dafMedList = service.getAllMedication();

        List<Medication> medList = new ArrayList<Medication>();

        for (DafMedication dafMed : dafMedList) {
            
            medList.add(createMedicationResourceObject(dafMed));
        }

        return medList;
    }
    
  public List<Medication> getMedicationForBulkDataRequest(List<Integer> patients, Date start) {

  	
		
		List<DafMedication> dafMedicationList = service.getMedicationForBulkData(patients, start);

		List<Medication> medList = new ArrayList<Medication>();

		for (DafMedication dafMedication : dafMedicationList) {
			medList.add(createMedicationResourceObject(dafMedication));
		}
		
		return medList;
	}


    /**
     * This is the "read" operation. The "@Read" annotation indicates that this method supports the read and/or vread operation.
     * <p>
     * Read operations take a single parameter annotated with the {@link IdParam} paramater, and should return a single resource instance.
     * </p>
     *
     * @param theId The read operation takes one parameter, which must be of type IdDt and must be annotated with the "@Read.IdParam" annotation.
     * @return Returns a resource matching this identifier, or null if none exists.
	 *Ex: http://<server name>/<context>/fhir/Medication/1?_format=json
     */
    @Read()
    public Medication getMedicationResourceById(@IdParam IdDt theId) {

        DafMedication dafMed = service.getMedicationResourceById(theId.getIdPartAsLong().intValue());

        Medication med = createMedicationResourceObject(dafMed);

        return med;
    }
    
    /**
     * The "@Search" annotation indicates that this method supports the search operation. You may have many different method annotated with this annotation, to support many different search criteria.
     * This example searches by the Code
     *
     * @param code
     * @param theIncludes
     * @param theSort
     * @param theCount
     * @return This method returns a list of Medications. This list may contain multiple matching resources, or it may also be empty.
     * 
     *  Ex: http://<server name>/<context>/fhir/Medication?code=2823-3&_format=json
     */
    @Search()
    public List<Medication> searchByCode(@RequiredParam(name = Medication.SP_CODE) String code,
                                         @IncludeParam(allow = "*") Set<Include> theIncludes, @Sort SortSpec theSort, @Count Integer theCount) {
        //String codeValue = code.getIdPart();
        List<DafMedication> dafMedList = service.getMedicationByCode(code);
       
        List<Medication> medList = new ArrayList<Medication>();

        for (DafMedication dafMed : dafMedList) {
           
            medList.add(createMedicationResourceObject(dafMed));
        }

        return medList;
    }

    /**
     * This method converts DafMedication object to Medication object
     */
    private Medication createMedicationResourceObject(DafMedication dafMed) {
        Medication medRes = new Medication();

        //Set Version
        medRes.setId(new IdDt(RESOURCE_TYPE, dafMed.getId() + "", VERSION_ID));

        //Set Code
        CodeableConceptDt medCode = new CodeableConceptDt();
        CodingDt medCoding = new CodingDt();
        medCoding.setCode(dafMed.getMed_code());
        medCoding.setDisplay(dafMed.getMed_display());
        medCode.addCoding(medCoding);
        medRes.setCode(medCode);

        //Set is_Brand
        medRes.setIsBrand(dafMed.isMed_isBrand());

        //Set Product
        Medication.Product medProd = new Medication.Product();

        CodeableConceptDt medFormCode = new CodeableConceptDt();
        CodingDt medFormCoding = new CodingDt();
        medFormCoding.setCode(dafMed.getMed_form());
        medFormCode.addCoding(medFormCoding);
        medProd.setForm(medFormCode);
        medRes.setProduct(medProd);

        Map<String, String> ingredients = HapiUtils.convertToJsonMap(dafMed.getMed_ingredient());
        List<ProductIngredient> medProdIngredient = new ArrayList<ProductIngredient>();
        ProductIngredient medProdIng = new ProductIngredient();
        //Set Item
        ResourceReferenceDt resRefDt = new ResourceReferenceDt();
        String theId = ingredients.get("item");
        resRefDt.setReference(theId);
        //Set Amount
        RatioDt ratioDt = new RatioDt();
        QuantityDt quantityDt = new QuantityDt();
        //String theQantity = ingredients.get("amount");
        //quantityDt.setUnits(theQantity);
        ratioDt.setNumerator(quantityDt);
        medProdIng.setItem(resRefDt);
        medProdIng.setAmount(ratioDt);
        medProdIngredient.add(medProdIng);
        medProd.setIngredient(medProdIngredient);

        medRes.setProduct(medProd);

        //Set Manufacturer
        ResourceReferenceDt authorReferenceDt1 = new ResourceReferenceDt();
        String theManID = dafMed.getDafManufacturer().getName();
        authorReferenceDt1.setReference(theManID);
        medRes.setManufacturer(authorReferenceDt1);

        return medRes;
    }
    
    @Create
    public MethodOutcome setMedication(@ResourceParam Medication medication)throws Exception {
    	
    	MethodOutcome retVal = new MethodOutcome();
    	try {
    		DafMedication dafMed = MedToDaf(medication);
    		retVal.setId(new IdDt("1"));
    		
    		DafMedication daf = service.SetMedication(dafMed);
    	}catch(Exception e) {
    		System.out.println(e.getMessage());
    		e.printStackTrace();
    	}
    	System.out.println("save Medication done!!!");
    	return retVal;
    }

	private DafMedication MedToDaf(Medication medication)throws Exception {
		
		System.err.println("in MedToDaf");
		DafMedication daf = new DafMedication();
		
		String x = medication.getIsBrand().toString();
		
		daf.setMed_isBrand(Boolean.parseBoolean(x));
		
		LinkedHashMap<String,String> h1 = new LinkedHashMap<String,String>();
		h1.put("item", medication.getProduct().getIngredient().get(0).getItem().getDisplay().toString());
		h1.put("amount", medication.getProduct().getIngredient().get(0).getAmount().getNumerator().getValue().toString());
				
		daf.setMed_ingredient(JJ.jajn(h1));
		daf.setMed_display(medication.getCode().getCoding().get(0).getDisplay().toString());
//		daf.setMed_display(medication.getCode().getCoding().get(0).getSystem().toString());
		daf.setDafManufacturer(new DafManufacturer());
		
		System.err.println("Coming out of MedToDaf");	

		System.out.println(daf);
		return  daf;
	}
}
