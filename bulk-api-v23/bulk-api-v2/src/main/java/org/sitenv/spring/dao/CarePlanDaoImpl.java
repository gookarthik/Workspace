package org.sitenv.spring.dao;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.sitenv.spring.model.DafCarePlan;
import org.sitenv.spring.model.DafCarePlanParticipant;
import org.sitenv.spring.query.CarePlanSearchCriteria;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

@Repository("carPlanDao")
public class CarePlanDaoImpl extends AbstractDao implements CarePlanDao {

    private static final Logger logger = LoggerFactory.getLogger(CarePlanDaoImpl.class);

    Session session;
    @SuppressWarnings("unchecked")
    @Override
    public List<DafCarePlan> getAllCarePlans() {
        Criteria criteria = getSession().createCriteria(DafCarePlan.class);
        return (List<DafCarePlan>) criteria.list();
    }

    @Override
    public DafCarePlan getCarePlanById(int id) {
        DafCarePlan dafCareTeam = (DafCarePlan) getSession().get(DafCarePlan.class, id);
        return dafCareTeam;
    }

    @Override
    public List<DafCarePlan> getCarePlanByPatient(String patient) {
        Criteria criteria = getSession().createCriteria(DafCarePlan.class, "careteam")
                .createAlias("careteam.patient", "dp")
                .add(Restrictions.eq("dp.id", Integer.valueOf(patient)));
        List<DafCarePlan> dafCareTeam = criteria.list();
        return dafCareTeam;
    }

   @Override
    public List<DafCarePlan> getCarePlanBySearchCriteria(CarePlanSearchCriteria carePlanSearchCriteria) {
        List<DafCarePlan> dafCareTeam = getCareTeam(carePlanSearchCriteria);
        return dafCareTeam;
    }
 
    @SuppressWarnings("unchecked")
	public List<DafCarePlan> getCareTeam(CarePlanSearchCriteria carePlanSearchCriteria) {

        Criteria criteria = getSession().createCriteria(DafCarePlan.class, "careplan").setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);

        if (carePlanSearchCriteria.getPatient() != null) {
            criteria.add(Restrictions.eq("careplan.patient.id", carePlanSearchCriteria.getPatient().intValue()));
        }

        if (StringUtils.isNotEmpty(carePlanSearchCriteria.getCat_code())) {
            criteria.add(Restrictions.eq("cat_code", carePlanSearchCriteria.getCat_code()));
        }

        if (StringUtils.isNotEmpty(carePlanSearchCriteria.getStatus())) {
            criteria.add(Restrictions.eq("status", carePlanSearchCriteria.getStatus()).ignoreCase());
        }
        return (List<DafCarePlan>) criteria.list();
    }

    
    //YOSH!!!
    @Override
    public List<DafCarePlanParticipant> getCarePlanparticipantByCareTeam(int id) {
        Criteria criteria = getSession().createCriteria(DafCarePlanParticipant.class, "dp").add(Restrictions.eq("careteam", id));        		
        //criteria.list();
		List<DafCarePlanParticipant> dafCareTeam = criteria.list();
				//.add(Restrictions.eq("careteam", id));
		
        return dafCareTeam;
    }

    
    @Override
    public List<DafCarePlan> getCarePlanForBulkData(List<Integer> patients, Date start){
	
	Criteria criteria = getSession().createCriteria(DafCarePlan.class, "careplan")
            .createAlias("careplan.patient", "dp");
			if(patients!=null) {
            criteria.add(Restrictions.in("dp.id", patients));
			}
			if(start != null) {
				criteria.add(Restrictions.ge("updated", start));
			}
	return criteria.list();
}


}
