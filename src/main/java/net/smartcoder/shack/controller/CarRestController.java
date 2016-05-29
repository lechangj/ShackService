package net.smartcoder.shack.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import net.smartcoder.shack.model.Car;
import net.smartcoder.shack.service.CarService;

@RestController
@RequestMapping("/api")
public class CarRestController {
	
	@Autowired
	CarService carService;
	
	// Retrieve All Cars
	@RequestMapping(value = "/car/list", method = RequestMethod.GET)
	public ResponseEntity<List<Car>> listAllCars(){
		List<Car> cars = carService.findAllCars();
		if(cars.isEmpty()){
			return new ResponseEntity<List<Car>>(HttpStatus.NO_CONTENT);
		}
		return new ResponseEntity<List<Car>>(cars, HttpStatus.OK);
	}
	
	// Retrieve Single Car
	@RequestMapping(value = "/car/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Car> getar(@PathVariable("id") long id){
		Car car = carService.findById(id);
		if(car == null){
			return new ResponseEntity<Car>(HttpStatus.NO_CONTENT);
		}
		return new ResponseEntity<Car>(car, HttpStatus.OK);
	}
	
	// Create a Car
	@RequestMapping(value ="/car/", method = RequestMethod.POST)
	public ResponseEntity<Void> createCar(@RequestBody Car car, UriComponentsBuilder ucBuilder){
		if(!carService.isCarSerialUnique(car.getId(), car.getSerial())) {
			return new ResponseEntity<Void>(HttpStatus.CONFLICT);
		}
		carService.saveCar(car);
		HttpHeaders headers = new HttpHeaders();
		headers.setLocation(ucBuilder.path("/car/{id}").buildAndExpand(car.getId()).toUri());
		return new ResponseEntity<Void>(headers, HttpStatus.CREATED);
	}
	
	// Update a Car
	@RequestMapping(value = "/car/{id}", method = RequestMethod.PUT)
	public ResponseEntity<Car> updateCar(@PathVariable("id") long id, @RequestBody Car car) {
		Car currentCar = carService.findById(id);
		if(currentCar == null) {
			return new ResponseEntity<Car>(HttpStatus.NOT_FOUND);
		}
		currentCar.setColor(car.getColor());
		
		carService.updateCar(currentCar);
		return new ResponseEntity<Car>(currentCar, HttpStatus.OK);
	}
	
	// Delete a Car
	@RequestMapping(value = "/car/{id}", method = RequestMethod.DELETE)
	public ResponseEntity<Car> deleteCar(@PathVariable("id") long id) {
		Car car = carService.findById(id);
		if(car == null) {
			return new ResponseEntity<Car>(HttpStatus.NOT_FOUND);
		}
		
		carService.deleteCarBySerial(car.getSerial());
		return new ResponseEntity<Car>(HttpStatus.NO_CONTENT);
	}

}
