package org.sitenv.spring.service;

import java.util.Date;
import java.util.List;

import org.sitenv.spring.model.DafPatientJson;
import org.sitenv.spring.query.PatientSearchCriteria;

/**
 * Created by Prabhushankar.Byrapp on 8/22/2015.
 */
public interface PatientService {

    public List<DafPatientJson> getAllPatient();

    public DafPatientJson getPatientById(int id);

    public List<DafPatientJson> getPatientBySearchOption(PatientSearchCriteria criteria);

    public List<DafPatientJson> getPatientJsonForBulkData(List<Integer> patients, Date start);

    public DafPatientJson savePatient(DafPatientJson daf);

}
