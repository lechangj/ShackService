package net.smartcoder.shack.service;

import java.util.List;

import net.smartcoder.shack.model.Car;

public interface CarService {
	
	Car findById(long id);
	
	void saveCar(Car car);
	
	void updateCar(Car car);
	
	void deleteCarBySerial(Integer serial);
	
	List<Car> findAllCars();
	
	Car findCarBySerial(Integer serial);
	
	boolean isCarSerialUnique(Long id, Integer serial);
}
