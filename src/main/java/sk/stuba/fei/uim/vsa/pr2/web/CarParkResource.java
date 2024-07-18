package sk.stuba.fei.uim.vsa.pr2.web;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import sk.stuba.fei.uim.vsa.pr2.entities.CarPark;
import sk.stuba.fei.uim.vsa.pr2.entities.CarParkFloor;
import sk.stuba.fei.uim.vsa.pr2.entities.ParkingSpot;
import sk.stuba.fei.uim.vsa.pr2.service.CarParkService;
import sk.stuba.fei.uim.vsa.pr2.web.response.CarParkDto;
import sk.stuba.fei.uim.vsa.pr2.web.response.CarParkFloorDto;
import sk.stuba.fei.uim.vsa.pr2.web.response.ParkingSpotDto;
import sk.stuba.fei.uim.vsa.pr2.web.response.factory.CarParkFloorResponseFactory;
import sk.stuba.fei.uim.vsa.pr2.web.response.factory.CarParkResponseFactory;
import sk.stuba.fei.uim.vsa.pr2.web.response.factory.ParkingSpotResponseFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


@Path("/carparks")
public class CarParkResource{
    private final CarParkService carParkService = new CarParkService();
    private final ObjectMapper json = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    private final CarParkResponseFactory factory = new CarParkResponseFactory();
    private final CarParkFloorResponseFactory factory2 = new CarParkFloorResponseFactory();
    private final ParkingSpotResponseFactory factory3 = new ParkingSpotResponseFactory();

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllCarParks(@QueryParam("name") String name){
        List<CarParkDto> response = new ArrayList<>();
        if(name != null){
            Object o = carParkService.getCarPark(name);
            if(o != null){
                CarParkDto carParkDto = factory.toDto((CarPark) o);
                response.add(carParkDto);
            }
        }else{
            List<Object> objects = carParkService.getCarParks();
            if(objects != null){
                for(Object o : objects){
                    CarParkDto carParkDto = factory.toDto((CarPark) o);
                    response.add(carParkDto);
                }
            }
        }
        try{
            return Response
                    .status(Response.Status.OK)
                    .entity(json.writeValueAsString(response))
                    .build();
        }catch (JsonProcessingException e) {
            return Response.noContent().build();
        }
    }

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getCarParkById(@PathParam("id") Long id){
        Object o = carParkService.getCarPark(id);
        if(o != null){
            CarParkDto response = factory.toDto((CarPark) o);
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

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createCarParkFromBody(String carParkBody){
        try{
            CarParkDto carParkDto = json.readValue(carParkBody, CarParkDto.class);
            Object o = carParkService.createCarPark(carParkDto.getName(),carParkDto.getAddress(),carParkDto.getPrices());
            if(o != null){
                CarPark carPark = (CarPark) o;
                if(carParkDto.getFloors() != null){
                    List<CarParkFloor> carParkFloors = new ArrayList<>();
                    for(CarParkFloorDto carParkFloorDto : carParkDto.getFloors()){
                        Object floor = carParkService.createCarParkFloor(carPark.getId(), carParkFloorDto.getIdentifier());
                        if(floor != null){
                            CarParkFloor carParkFloor = (CarParkFloor) floor;
                            carParkFloor.setCarPark(carPark);
                            carParkFloors.add(carParkFloor);
                            if(carParkFloorDto.getSpots() != null){
                                List<ParkingSpot> parkingSpots = new ArrayList<>();
                                for(ParkingSpotDto parkingSpotDto : carParkFloorDto.getSpots()){
                                    Object spot = carParkService.createParkingSpot(carPark.getId(), carParkFloorDto.getIdentifier(), parkingSpotDto.getIdentifier());
                                    if(spot != null){
                                        ParkingSpot parkingSpot = (ParkingSpot) spot;
                                        parkingSpot.setCarParkFloor(carParkFloor);
                                        parkingSpots.add(parkingSpot);
                                    }else{
                                        carParkService.deleteCarPark(carPark.getId());
                                        return Response.status(Response.Status.BAD_REQUEST).build();
                                    }
                                }
                                carParkFloor.setParkovacieMiesta(parkingSpots);
                            }
                        }else{
                            carParkService.deleteCarPark(carPark.getId());
                            return Response.status(Response.Status.BAD_REQUEST).build();
                        }
                    }
                    carPark.setParkovaciePoschodie(carParkFloors);
                }
                CarParkDto response = factory.toDto(carPark);
                return Response
                        .status(Response.Status.CREATED)
                        .entity(json.writeValueAsString(response))
                        .build();
            }
            return Response.status(Response.Status.BAD_REQUEST).build();
        }catch (JsonProcessingException e) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
    }

    @PUT
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateCarParkInfo(@PathParam("id") Long id, String carParkBody){
        try {
            CarParkDto carParkDto = json.readValue(carParkBody, CarParkDto.class);
            carParkDto.setId(id);
            CarPark carPark = factory.toEntity(carParkDto);
            Object o = carParkService.updateCarPark(carPark);
            if(o != null){
                CarParkDto response = factory.toDto((CarPark) o);
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
    public Response destroyCarPark(@PathParam("id") Long id){
        Object o = carParkService.deleteCarPark(id);
        if(o != null){
            return Response.noContent().build();
        }
        return Response.status(Response.Status.NOT_FOUND).build();
    }


    /////////////////////////////////CarParkFloor Methods/////////////////////////////////

    @GET
    @Path("/{id}/floors")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllCarParkFloors(@PathParam("id") Long id){
        List<Object> objects = carParkService.getCarParkFloors(id);
        if(objects != null){
            List<CarParkFloorDto> response = new ArrayList<>();
            for(Object o : objects){
                CarParkFloorDto carParkFloorDto = factory2.toDto((CarParkFloor) o);
                response.add(carParkFloorDto);
            }
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

    @POST
    @Path("/{id}/floors")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createCarParkFloorFromBody(@PathParam("id") Long id, String carParkFloorBody){
        try{
            CarParkFloorDto carParkFloorDto = json.readValue(carParkFloorBody, CarParkFloorDto.class);
            Object floor = carParkService.createCarParkFloor(id, carParkFloorDto.getIdentifier());
            if(floor != null){
                CarParkFloor carParkFloor = (CarParkFloor) floor;
                Object carPark = carParkService.getCarPark(id);
                carParkFloor.setCarPark((CarPark) carPark);
                if(carParkFloorDto.getSpots() != null){
                    List<ParkingSpot> parkingSpots = new ArrayList<>();
                    for(ParkingSpotDto parkingSpotDto : carParkFloorDto.getSpots()){
                        Object spot = carParkService.createParkingSpot(id, carParkFloorDto.getIdentifier(), parkingSpotDto.getIdentifier());
                        if(spot != null){
                            ParkingSpot parkingSpot = (ParkingSpot) spot;
                            parkingSpot.setCarParkFloor(carParkFloor);
                            parkingSpots.add(parkingSpot);
                        }else{
                            carParkService.deleteCarParkFloor(carParkFloor.getId());
                            return Response.status(Response.Status.BAD_REQUEST).build();
                        }
                    }
                    carParkFloor.setParkovacieMiesta(parkingSpots);
                }
                CarParkFloorDto response = factory2.toDto(carParkFloor);
                return Response
                        .status(Response.Status.CREATED)
                        .entity(json.writeValueAsString(response))
                        .build();
            }
            return Response.status(Response.Status.BAD_REQUEST).build();
        }catch (JsonProcessingException e) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
    }

    @PUT
    @Path("/{id}/floors/{identifier}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateCarParkFloorInfo(@PathParam("id") Long id, @PathParam("identifier") String identifier, String carParkFloorBody){
        try {
            CarParkFloorDto carParkFloorDtoDto = json.readValue(carParkFloorBody, CarParkFloorDto.class);
            CarParkFloor carParkFloor = factory2.toEntity(carParkFloorDtoDto);
            List<Object> objects = carParkService.getCarParkFloors(id);
            if(objects != null){
                for(Object o : objects){
                    CarParkFloor cpf = (CarParkFloor) o;
                    if(cpf.getIdentifier().equals(identifier)){
                        carParkFloor.setId(cpf.getId());
                        Object ob = carParkService.updateCarParkFloor(carParkFloor);
                        if(ob != null){
                            CarParkFloorDto response = factory2.toDto((CarParkFloor) ob);
                            return Response
                                    .status(Response.Status.OK)
                                    .entity(json.writeValueAsString(response))
                                    .build();
                        }
                        return Response.status(Response.Status.BAD_REQUEST).build();
                    }
                }
            }
            return Response.status(Response.Status.NOT_FOUND).build();
        } catch (JsonProcessingException e) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
    }

    @DELETE
    @Path("/{id}/floors/{identifier}")
    public Response destroyCarParkFloor(@PathParam("id") Long id, @PathParam("identifier") String identifier){
        List<Object> objects = carParkService.getCarParkFloors(id);
        if(objects != null) {
            for (Object o : objects) {
                CarParkFloor cpf = (CarParkFloor) o;
                if (cpf.getIdentifier().equals(identifier)) {
                    Object ob = carParkService.deleteCarParkFloor(cpf.getId());
                    if(ob != null){
                        return Response.noContent().build();
                    }
                }
            }
        }
        return Response.status(Response.Status.NOT_FOUND).build();
    }


    /////////////////////////////////ParkingSpot Methods/////////////////////////////////

    @GET
    @Path("/{id}/spots")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllParkingSpots(@PathParam("id") Long id, @QueryParam("free") Boolean free){
        if(free != null){
            Object o = carParkService.getCarPark(id);
            if(o != null){
                List<ParkingSpotDto> response = new ArrayList<>();
                CarPark carPark = (CarPark) o;
                Map<String, List<Object>> map;
                if(free){
                    map = carParkService.getAvailableParkingSpots(carPark.getNazov());
                }else{
                    map = carParkService.getOccupiedParkingSpots(carPark.getNazov());
                }
                if(map != null){
                    for (Map.Entry<String, List<Object>> entry : map.entrySet()) {
                        if(entry.getValue() != null){
                            for(Object ob : entry.getValue()){
                                ParkingSpotDto parkingSpotDto = factory3.toDto((ParkingSpot) ob);
                                parkingSpotDto.setCarPark(id);
                                parkingSpotDto.setFree(free);
                                response.add(parkingSpotDto);
                            }
                        }
                    }
                    try{
                        return Response
                                .status(Response.Status.OK)
                                .entity(json.writeValueAsString(response))
                                .build();
                    }catch (JsonProcessingException e) {
                        return Response.noContent().build();
                    }
                }
            }
        }else{
            List<ParkingSpotDto> response = new ArrayList<>();
            Map<String, List<Object>> map = carParkService.getParkingSpots(id);
            if(map != null){
                for (Map.Entry<String, List<Object>> entry : map.entrySet()) {
                    if(entry.getValue() != null){
                        for(Object o : entry.getValue()){
                            ParkingSpotDto parkingSpotDto = factory3.toDto((ParkingSpot) o);
                            parkingSpotDto.setCarPark(id);
                            response.add(parkingSpotDto);
                        }
                    }
                }
                try{
                    return Response
                            .status(Response.Status.OK)
                            .entity(json.writeValueAsString(response))
                            .build();
                }catch (JsonProcessingException e) {
                    return Response.noContent().build();
                }
            }
        }
        return Response.status(Response.Status.NOT_FOUND).build();
    }

    @GET
    @Path("/{id}/floors/{identifier}/spots")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getFloorParkingSpots(@PathParam("id") Long id, @PathParam("identifier") String identifier){
        List<Object> objects = carParkService.getParkingSpots(id, identifier);
        if(objects != null){
            List<ParkingSpotDto> response = new ArrayList<>();
            for(Object o : objects){
                ParkingSpotDto parkingSpotDto = factory3.toDto((ParkingSpot) o);
                parkingSpotDto.setCarPark(id);
                response.add(parkingSpotDto);
            }
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

    @POST
    @Path("/{id}/floors/{identifier}/spots")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createParkingSpotFromBody(@PathParam("id") Long id, @PathParam("identifier") String identifier, String parkingSpotBody){
        try{
            ParkingSpotDto parkingSpotDto = json.readValue(parkingSpotBody, ParkingSpotDto.class);
            Object o = carParkService.createParkingSpot(id, identifier, parkingSpotDto.getIdentifier());
            if(o != null){
                ParkingSpotDto response = factory3.toDto((ParkingSpot) o);
                response.setCarPark(id);
                return Response
                        .status(Response.Status.CREATED)
                        .entity(json.writeValueAsString(response))
                        .build();
            }
            return Response.status(Response.Status.BAD_REQUEST).build();
        }catch (JsonProcessingException e) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
    }
}
