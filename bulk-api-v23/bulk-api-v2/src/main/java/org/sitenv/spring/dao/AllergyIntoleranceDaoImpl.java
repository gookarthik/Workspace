package org.sitenv.spring.dao;

import java.util.Date;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.sitenv.spring.model.DafAllergyIntolerance;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

@Repository("allergyIntoleranceDao")
public class AllergyIntoleranceDaoImpl extends AbstractDao implements AllergyIntoleranceDao {

    private static final Logger logger = LoggerFactory.getLogger(AllergyIntoleranceDaoImpl.class);

    @SuppressWarnings("unchecked")
    @Override
    public List<DafAllergyIntolerance> getAllAllergyIntolerance() {
        Criteria criteria = getSession().createCriteria(DafAllergyIntolerance.class);
        return (List<DafAllergyIntolerance>) criteria.list();
    }
    
    
    @Override
    public DafAllergyIntolerance saveAllergyIntolerance(DafAllergyIntolerance  allergy) {
          getSession().save(allergy);
          return allergy;
    }

    @Override
    public DafAllergyIntolerance updateAllergyIntolerance(DafAllergyIntolerance allergy) {
    	getSession().update(allergy);
    	return allergy;
    }

    @Override
    public DafAllergyIntolerance getAllergyIntoleranceResourceById(int id) {
    	DafAllergyIntolerance dafAllergyIntolerance = null;
        try {
             dafAllergyIntolerance = (DafAllergyIntolerance) getSession().get(DafAllergyIntolerance.class, id);
        }catch(Exception e) {
        	e.printStackTrace();
        	System.out.println(e.getMessage());
        }        
        return dafAllergyIntolerance;
    }

    @Override
    public List<DafAllergyIntolerance> getAllergyIntoleranceByPatient(String patient) {
        Criteria criteria = getSession().createCriteria(DafAllergyIntolerance.class, "allergyIntolerance")
                .createAlias("allergyIntolerance.patient", "dp")
                .add(Restrictions.eq("dp.id", Integer.valueOf(patient)));
        List<DafAllergyIntolerance> dafAllergyIntolerance = criteria.list();
        return dafAllergyIntolerance;
    }

	@Override
	public List<DafAllergyIntolerance> getAllergyIntoleranceForBulkData(List<Integer> patients, Date start) {
		
		Criteria criteria = getSession().createCriteria(DafAllergyIntolerance.class, "allergyIntolerance")
                .createAlias("allergyIntolerance.patient", "dp");
				if(patients!=null) {
                criteria.add(Restrictions.in("dp.id", patients));
				}
				if(start != null) {
					criteria.add(Restrictions.ge("updated", start));
				}
		return criteria.list();
	}

}
