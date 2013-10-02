package com.github.steingrd.bloominghollows.temperatures;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlElement;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "temperatures")
public class Temperature {

	@Id
	@GeneratedValue(generator = "increment")
	@GenericGenerator(name = "increment", strategy = "increment")
	public Long id;

	@XmlElement
	private String timestamp;
	
	@XmlElement
	private Integer temperature;

	private Temperature() {
	}
	
	public Temperature(String timestamp, Integer temperature) {
		this();
		this.timestamp = timestamp;
		this.temperature = temperature;
	}

	public Integer getTemperature() {
		return temperature;
	}
	
	public String getTimestamp() {
		return timestamp;
	}
	
}