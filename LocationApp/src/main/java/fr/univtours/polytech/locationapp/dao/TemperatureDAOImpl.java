package fr.univtours.polytech.locationapp.dao;

import javax.ejb.Stateless;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;

import fr.univtours.polytech.locationapp.model.weathermap.WsWeatherMapResult;

@Stateless
public class TemperatureDAOImpl implements TemperatureDAO {

    private static String URL = "https://api-adresse.data.gouv.fr";
    private static String key = "2832ed6df19fca6ad3e115a805becf6d";

    @Override
    public Double getTemperature(Double longitude, Double latitude) {
        Client client = ClientBuilder.newClient();
        WebTarget target = client.target(URL);

        target = target.queryParam("appid", key);
        target = target.queryParam("lon", longitude);
        target = target.queryParam("lat", latitude);

        WsWeatherMapResult wsResult = target.request(MediaType.APPLICATION_JSON).get(WsWeatherMapResult.class);
        return wsResult.getMain().getTemp();
    }

}
