package fr.univtours.polytech.locationapp.business;

import javax.ejb.Stateless;
import javax.inject.Inject;

import fr.univtours.polytech.locationapp.dao.TemperatureDAO;

@Stateless
public class TemperatureBusinessImpl implements TemperatureBusinessLocal {

    @Inject
    TemperatureDAO temperatureDao;

    @Override
    public Double getTemperature(Double lon, Double lat) {
        
        return temperatureDao.getTemperature(lon, lat);
    }

}
