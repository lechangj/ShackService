package net.smartcoder.shack.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import net.smartcoder.shack.dao.CarDao;
import net.smartcoder.shack.model.Car;

@Service("carService")
@Transactional
public class CarServiceImpl implements CarService {
	
	@Autowired
	private CarDao dao;

	@Override
	public Car findById(long id) {
		return dao.findById(id);
	}

	@Override
	public void saveCar(Car car) {
		dao.saveCar(car);		
	}

	@Override
	public void updateCar(Car car) {
		Car entity = dao.findById(car.getId());
		if(entity != null){
			entity.setColor(car.getColor());
		}		
	}

	@Override
	public void deleteCarBySerial(Integer serial) {
		dao.deleteCarBySerial(serial);
	}

	@Override
	public List<Car> findAllCars() {
		return dao.findAllCars();
	}

	@Override
	public Car findCarBySerial(Integer serial) {
		return dao.findCarBySerial(serial);
	}

	@Override
	public boolean isCarSerialUnique(Long id, Integer serial) {
		Car car = findCarBySerial(serial);
		return (car == null || ((id != null) && (car.getId() == id)));
	}

}
