package fr.univtours.polytech.locationapp.business;

import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;

import fr.univtours.polytech.locationapp.dao.AddressDao;
import fr.univtours.polytech.locationapp.dao.LocationDao;
import fr.univtours.polytech.locationapp.dao.TemperatureDAO;
import fr.univtours.polytech.locationapp.model.LocationBean;

@Stateless
public class LocationBusinessImpl implements LocationBusinessLocal, LocationBusinessRemote {

    @Inject
    private LocationDao locationDao;

    @Inject
    private TemperatureDAO temperatureDao;

    @Inject
    private AddressDao addressDao;

    @Override
    public void addLocation(LocationBean bean) {
        locationDao.createLocation(bean);

    }

    @Override
    public List<LocationBean> getLocations() {

        List<LocationBean> results = locationDao.getLocations();

        for (LocationBean b : results) {
            Double lon = addressDao.getAddresses(b.getAddress() + " " + b.getZipCode()).get(0).getGeometry()
                    .getCoordinates().get(0);
            Double lat = addressDao.getAddresses(b.getAddress() + " " + b.getZipCode()).get(0).getGeometry()
                    .getCoordinates().get(1);

            if(null != lon && null != lat)
            {
                b.setTemperature((double) Math.round(temperatureDao.getTemperature(lon, lat)));
            }
            

        }
        return results;
    }

    @Override
    public List<LocationBean> getLocationsFiltre(String Filtre) {
        
        List<LocationBean> results = locationDao.getLocationsFiltre(Filtre);;

        for (LocationBean b : results) {
            Double lon = addressDao.getAddresses(b.getAddress() + " " + b.getZipCode()).get(0).getGeometry()
                    .getCoordinates().get(0);
            Double lat = addressDao.getAddresses(b.getAddress() + " " + b.getZipCode()).get(0).getGeometry()
                    .getCoordinates().get(1);

            if(null != lon && null != lat)
            {
                b.setTemperature((double) Math.round(temperatureDao.getTemperature(lon, lat)));
            }
        }
        return results;
    }

    @Override
    public LocationBean getLocation(Integer id) {
        LocationBean currentLocation = locationDao.getLocation(id);
        ;
        Double lon = addressDao.getAddresses(currentLocation.getAddress() + " " + currentLocation.getZipCode()).get(0)
                .getGeometry().getCoordinates().get(0);
        Double lat = addressDao.getAddresses(currentLocation.getAddress() + " " + currentLocation.getZipCode()).get(0)
                .getGeometry().getCoordinates().get(1);
        if(null != lon && null != lat)
        {
            currentLocation.setTemperature((double) Math.round(temperatureDao.getTemperature(lon, lat)));
            
        }
        return currentLocation;
    }

    @Override
    public void updateLocation(LocationBean locationBean) {
        locationDao.updateLocation(locationBean);
    }

    @Override
    public void deleteLocation(Integer id) {
        LocationBean locationBean = getLocation(id);
        locationDao.deleteLocation(locationBean);
    }

}
