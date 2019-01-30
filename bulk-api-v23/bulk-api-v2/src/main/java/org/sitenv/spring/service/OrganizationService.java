package org.sitenv.spring.service;

import java.util.Date;
import java.util.List;

import org.sitenv.spring.model.DafObservation;
import org.sitenv.spring.model.DafOrganization;
import org.sitenv.spring.query.OrganizationSearchCriteria;

public interface OrganizationService {

	public List<DafOrganization> getAllOrganizations();

	public DafOrganization getOrganizationResourceById(int id);

	public List<DafOrganization> getOrganizationBySearchCriteria(OrganizationSearchCriteria criteria);

	 public List<DafOrganization> getOrganizationForBulkData(List<Integer> patients, Date start);

	public DafOrganization saveToOrg(DafOrganization dafOrg);
}
