package net.smartcoder.shack.model;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.TableGenerator;
import javax.validation.constraints.Digits;

import org.hibernate.validator.constraints.NotEmpty;

@Entity
@Table(name = "car")
public class Car {

	@Id
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "TABLE_GEN")
	@TableGenerator(name = "TABLE_GEN", table = "SEQUENCE_TABLE", pkColumnName = "SEQ_NAME", valueColumnName = "SEQ_CNT", pkColumnValue = "CAR_SEQ", allocationSize = 1)
	private long id;

	@NotEmpty
	@Column(name = "serial", unique = true, nullable = false)
	private Integer serial;

	@NotEmpty
	@Column(name = "make", nullable = false)
	private String make;

	@NotEmpty
	@Column(name = "model", nullable = false)
	private String model;

	@Column(name = "color")
	private String color;

	@NotEmpty
	@Column(name = "year", nullable = false)
	private Integer year;

	@NotEmpty
	@Column(name = "engine_type", nullable = false)
	private String engineType;

	@NotEmpty
	@Digits(integer = 10, fraction = 2)
	@Column(name = "list_price", nullable = false)
	private BigDecimal listPrice;

	@Column(name = "image")
	private String image;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public Integer getSerial() {
		return serial;
	}

	public void setSerial(Integer serial) {
		this.serial = serial;
	}

	public String getMake() {
		return make;
	}

	public void setMake(String make) {
		this.make = make;
	}

	public String getModel() {
		return model;
	}

	public void setModel(String model) {
		this.model = model;
	}

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}

	public Integer getYear() {
		return year;
	}

	public void setYear(Integer year) {
		this.year = year;
	}

	public String getEngineType() {
		return engineType;
	}

	public void setEngineType(String engineType) {
		this.engineType = engineType;
	}

	public BigDecimal getListPrice() {
		return listPrice;
	}

	public void setListPrice(BigDecimal listPrice) {
		this.listPrice = listPrice;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

}
