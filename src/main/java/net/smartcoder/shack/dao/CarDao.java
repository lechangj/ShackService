package net.smartcoder.shack.dao;

import java.util.List;

import net.smartcoder.shack.model.Car;

public interface CarDao {
	
	Car findById(long id);
	
	void saveCar(Car car);
	
	void deleteCarBySerial(Integer serial);
	
	List<Car> findAllCars();
	
	Car findCarBySerial(Integer serial);

}
