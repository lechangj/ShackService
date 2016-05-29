package net.smartcoder.shack.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import net.smartcoder.shack.model.Car;

@Repository("carDao")
public class CarDaoImpl extends AbstractDao<Long, Car> implements CarDao {

	@Override
	public Car findById(long id) {
		return getByKey(id);
	}

	@Override
	public void saveCar(Car car) {
		persist(car);

	}

	@Override
	public void deleteCarBySerial(Integer serial) {
		Query query = getSession().createSQLQuery("delete from Car where serial = :serial");
		query.setInteger("serial", serial);
		query.executeUpdate();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Car> findAllCars() {
		Criteria criteria = createEntityCriteria();
		return (List<Car>)criteria.list();
	}

	@Override
	public Car findCarBySerial(Integer serial) {
		Criteria criteria = createEntityCriteria();
		criteria.add(Restrictions.eqOrIsNull("serial", serial));
		return (Car)criteria.uniqueResult();
	}

}
