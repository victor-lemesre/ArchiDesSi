package fr.univtours.polytech.locationapp.business;

import javax.ejb.Local;

@Local
public interface TemperatureBusinessLocal {

    public Double getTemperature(Double lon, Double lat);
    
}
