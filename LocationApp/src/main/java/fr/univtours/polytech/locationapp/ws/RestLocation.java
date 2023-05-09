package fr.univtours.polytech.locationapp.ws;

import java.util.Collections;
import java.util.List;

import javax.ejb.EJB;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.PATCH;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import fr.univtours.polytech.locationapp.business.LocationBusinessLocal;
import fr.univtours.polytech.locationapp.model.LocationBean;

@Path("api")
public class RestLocation {

	@EJB
	private LocationBusinessLocal locationBusiness;

	@GET
	@Path("Locations/{id}")
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public LocationBean getLocationJSON(@PathParam("id") Integer id) {
		return locationBusiness.getLocation(id);
	}

	@GET
	@Path("Locations")
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public List<LocationBean> getListLocation(@QueryParam("tri") String tri, @QueryParam("filtre") String filtre) {
		List<LocationBean> listLocation;

		if (filtre != null) {
			listLocation = locationBusiness.getLocationsFiltre(filtre);
		} else {
			listLocation = locationBusiness.getLocations();
		}

		if (tri != null) {
			Collections.sort(listLocation, LocationBean.ComparatorCityAlpha);
			if (tri.equals("desc")) {
				Collections.reverse(listLocation);
			}
		}
		return listLocation;
	}

	@DELETE
	@Path("Locations/{id}")
	public Response deleteLocation(@PathParam("id") Integer id,
			@HeaderParam(HttpHeaders.AUTHORIZATION) String authorization) {
		if ("".equals(authorization)) {
			return Response.status(Status.UNAUTHORIZED).build();
		} else if ("42".equals(authorization)) {
			if (null == locationBusiness.getLocation(id)) {
				return Response.status(Status.NOT_FOUND).build();
			} else {
				locationBusiness.deleteLocation(id);
				return Response.ok().build();
			}
		} else {
			return Response.status(Status.FORBIDDEN).build();
		}

	}

	@POST
	@Path("Locations")
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public Response createLocation(LocationBean locationBean) {
		locationBusiness.addLocation(locationBean);
		return Response.ok().build();
	}

	@POST
	@Path("Locations")
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public Response createLocation(@FormParam("address") String address, @FormParam("city") String city,
			@FormParam("nightPrice") Double price, @FormParam("zipCode") String zipCode) {

		LocationBean location = new LocationBean();
		location.setAddress(address);
		location.setCity(city);
		location.setNightPrice(price);
		location.setZipCode(zipCode);
		locationBusiness.addLocation(location);
		return Response.ok().build();
	}

	@PUT
	@Path("Locations/{id}")
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public Response ModifTotalLocation(@PathParam("id") Integer id,LocationBean locationBean) {
		if (null == locationBusiness.getLocation(id)) {
			return Response.status(Status.NOT_FOUND).build();
		}
		locationBean.setId(id);
		locationBusiness.updateLocation(locationBean);
		return Response.ok().build();
	}

	@PATCH
	@Path("Locations/{id}")
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public Response ModifPartielLocation(@PathParam("id") Integer id,LocationBean locationBean) {
		LocationBean location = locationBusiness.getLocation(id);
		if (null == location) {
			return Response.status(Status.NOT_FOUND).build();
		}
		if (locationBean.getAddress() != null) {
			location.setAddress(locationBean.getAddress());
		}
		if (locationBean.getCity() != null) {
			location.setCity(locationBean.getCity());
		}
		if (locationBean.getNightPrice() != null) {
			location.setNightPrice(locationBean.getNightPrice());
		}
		if (locationBean.getZipCode() != null) {
			location.setZipCode(locationBean.getZipCode());
		}
		locationBusiness.updateLocation(location);
		return Response.ok().build();
	}
}
