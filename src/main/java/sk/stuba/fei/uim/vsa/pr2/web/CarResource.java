package sk.stuba.fei.uim.vsa.pr2.web;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import sk.stuba.fei.uim.vsa.pr2.entities.Car;
import sk.stuba.fei.uim.vsa.pr2.entities.User;
import sk.stuba.fei.uim.vsa.pr2.service.CarParkService;
import sk.stuba.fei.uim.vsa.pr2.web.response.Car2Dto;
import sk.stuba.fei.uim.vsa.pr2.web.response.factory.CarResponseFactory;

import java.util.ArrayList;
import java.util.List;


@Path("/cars")
public class CarResource{
    private final CarParkService carParkService = new CarParkService();
    private final ObjectMapper json = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    private final CarResponseFactory factory = new CarResponseFactory();

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllCars(@QueryParam("user") Long id, @QueryParam("vrp") String vrp){
        List<Car2Dto> response = new ArrayList<>();
        if(id != null && vrp != null){
            List<Object> objects = carParkService.getCars(id);
            if(objects != null){
                for(Object o : objects){
                    if(((Car) o).getECV().equals(vrp)){
                        Car2Dto car2Dto = factory.toDto((Car) o);
                        response.add(car2Dto);
                        break;
                    }
                }
            }else{
                return Response.status(Response.Status.NOT_FOUND).build();
            }
        }else if(vrp != null){
            Object o = carParkService.getCar(vrp);
            if(o != null){
                Car2Dto car2Dto = factory.toDto((Car) o);
                response.add(car2Dto);
            }else{
                return Response.status(Response.Status.NOT_FOUND).build();
            }
        }else if(id != null){
            List<Object> objects = carParkService.getCars(id);
            if(objects != null){
                for(Object o : objects){
                    Car2Dto car2Dto = factory.toDto((Car) o);
                    response.add(car2Dto);
                }
            }else{
                return Response.status(Response.Status.NOT_FOUND).build();
            }
        }else{
            List<Object> objects = carParkService.getCars();
            if(objects != null){
                for(Object o : objects){
                    Car2Dto car2Dto = factory.toDto((Car) o);
                    response.add(car2Dto);
                }
            }else{
                return Response.status(Response.Status.NOT_FOUND).build();
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
    public Response getCarById(@PathParam("id") Long id){
        Object o = carParkService.getCar(id);
        if(o != null){
            Car2Dto response = factory.toDto((Car) o);
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
    public Response createCarFromBody(String carBody){
        try {
            Car2Dto car2Dto = json.readValue(carBody, Car2Dto.class);
            Object userObj = null;
            if(car2Dto.getOwner() == null){
                return Response.status(Response.Status.BAD_REQUEST).build();
            }
            if(car2Dto.getOwner().getId() != null){
                userObj = carParkService.getUser(car2Dto.getOwner().getId());
            }else if(car2Dto.getOwner().getEmail() != null){
                userObj = carParkService.getUser(car2Dto.getOwner().getEmail());
            }
            if(userObj == null){
                Object newUser = carParkService.createUser(car2Dto.getOwner().getFirstName(), car2Dto.getOwner().getLastName(), car2Dto.getOwner().getEmail());
                if(newUser != null){
                    User user = (User) newUser;
                    Object o = carParkService.createCar(user.getId(), car2Dto.getBrand(), car2Dto.getModel(), car2Dto.getColour(), car2Dto.getVrp());
                    if(o != null){
                        Car2Dto response = factory.toDto((Car) o);
                        return Response
                                .status(Response.Status.CREATED)
                                .entity(json.writeValueAsString(response))
                                .build();
                    }else{
                        carParkService.deleteUser(user.getId());
                    }
                }
            }else{
                User user = (User) userObj;
                Object o = carParkService.createCar(user.getId(), car2Dto.getBrand(), car2Dto.getModel(), car2Dto.getColour(), car2Dto.getVrp());
                if(o != null){
                    Car2Dto response = factory.toDto((Car) o);
                    return Response
                            .status(Response.Status.CREATED)
                            .entity(json.writeValueAsString(response))
                            .build();
                }
            }
            return Response.status(Response.Status.BAD_REQUEST).build();
        } catch (JsonProcessingException e) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
    }

    @PUT
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateCarInfo(@PathParam("id") Long id, String carBody){
        try {
            Car2Dto car2Dto = json.readValue(carBody, Car2Dto.class);
            car2Dto.setId(id);
            Car car = factory.toEntity(car2Dto);
            Object o = carParkService.updateCar(car);
            if(o != null){
                Car2Dto response = factory.toDto((Car) o);
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
    public Response destroyCar(@PathParam("id") Long id){
        Object o = carParkService.deleteCar(id);
        if(o != null){
            return Response.noContent().build();
        }
        return Response.status(Response.Status.NOT_FOUND).build();
    }
}
