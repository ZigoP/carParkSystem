package sk.stuba.fei.uim.vsa.pr2.web;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import sk.stuba.fei.uim.vsa.pr2.entities.ParkingSpot;
import sk.stuba.fei.uim.vsa.pr2.service.CarParkService;
import sk.stuba.fei.uim.vsa.pr2.web.response.ParkingSpotDto;
import sk.stuba.fei.uim.vsa.pr2.web.response.factory.ParkingSpotResponseFactory;

@Path("/parkingspots")
public class ParkingSpotResource{
    private final CarParkService carParkService = new CarParkService();
    private final ObjectMapper json = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    private final ParkingSpotResponseFactory factory = new ParkingSpotResponseFactory();

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getParkingSpotById(@PathParam("id") Long id){
        Object o = carParkService.getParkingSpot(id);
        if(o != null){
            ParkingSpot parkingSpot = (ParkingSpot) o;
            ParkingSpotDto response = factory.toDto(parkingSpot);
            response.setCarPark(parkingSpot.getCarParkFloor().getCarPark().getId());
            try{
                return Response
                        .status(Response.Status.OK)
                        .entity(json.writeValueAsString(response))
                        .build();
            }catch (JsonProcessingException e) {
                return Response.noContent().build();
            }
        }
        return Response.status(Response.Status.NOT_FOUND).build();
    }

    @PUT
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateCarParkInfo(@PathParam("id") Long id, String parkingSpotBody){
        try {
            ParkingSpotDto parkingSpotDto = json.readValue(parkingSpotBody, ParkingSpotDto.class);
            parkingSpotDto.setId(id);
            ParkingSpot parkingSpot = factory.toEntity(parkingSpotDto);
            Object o = carParkService.updateParkingSpot(parkingSpot);
            if(o != null){
                ParkingSpotDto response = factory.toDto((ParkingSpot) o);
                parkingSpot = (ParkingSpot) o;
                response.setCarPark(parkingSpot.getCarParkFloor().getCarPark().getId());
                return Response
                        .status(Response.Status.OK)
                        .entity(json.writeValueAsString(response))
                        .build();
            }
            return Response.status(Response.Status.BAD_REQUEST).build();
        } catch (JsonProcessingException e) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
    }

    @DELETE
    @Path("/{id}")
    public Response destroyParkingSpot(@PathParam("id") Long id){
        Object o = carParkService.deleteParkingSpot(id);
        if(o != null){
            return Response.noContent().build();
        }
        return Response.status(Response.Status.NOT_FOUND).build();
    }
}
