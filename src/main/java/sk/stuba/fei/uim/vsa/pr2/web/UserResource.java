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
import sk.stuba.fei.uim.vsa.pr2.web.response.CarDto;
import sk.stuba.fei.uim.vsa.pr2.web.response.UserDto;
import sk.stuba.fei.uim.vsa.pr2.web.response.factory.UserResponseFactory;

import java.util.ArrayList;
import java.util.List;


@Path("/users")
public class UserResource{
    private final CarParkService carParkService = new CarParkService();
    private final ObjectMapper json = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    private final UserResponseFactory factory = new UserResponseFactory();

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllUsers(@QueryParam("email") String email){
        List<UserDto> response = new ArrayList<>();
        if(email != null){
            Object o = carParkService.getUser(email);
            if(o != null){
                UserDto userDto = factory.toDto((User) o);
                response.add(userDto);
            }else {
                return Response.status(Response.Status.NOT_FOUND).build();
            }
        }else{
            List<Object> objects = carParkService.getUsers();
            if(objects != null){
                for(Object o : objects){
                    UserDto userDto = factory.toDto((User) o);
                    response.add(userDto);
                }
            }else {
                return Response.status(Response.Status.NOT_FOUND).build();
            }
        }
        try{
            return Response
                    .status(Response.Status.OK)
                    .entity(json.writeValueAsString(response))
                    .build();
        }catch (JsonProcessingException e){
            return Response.noContent().build();
        }
    }

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getUserById(@PathParam("id") Long id){
        Object o = carParkService.getUser(id);
        if(o != null){
            UserDto response = factory.toDto((User) o);
            try{
                return Response
                        .status(Response.Status.OK)
                        .entity(json.writeValueAsString(response))
                        .build();
            }catch (JsonProcessingException e){
                return Response.noContent().build();
            }
        }
        return Response.status(Response.Status.NOT_FOUND).build();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createUserFromBody(String userBody){
        try {
            UserDto userDto = json.readValue(userBody, UserDto.class);
            Object o = carParkService.createUser(userDto.getFirstName(), userDto.getLastName(), userDto.getEmail());
            if(o != null){
                User user = (User) o;
                if(userDto.getCars() != null){
                    List<Car> cars = new ArrayList<>();
                    for(CarDto carDto : userDto.getCars()){
                        o = carParkService.createCar(user.getId(), carDto.getBrand(), carDto.getModel(), carDto.getColour(), carDto.getVrp());
                        if(o != null){
                            cars.add((Car) o);
                        }else{
                            carParkService.deleteUser(user.getId());
                            return Response.status(Response.Status.BAD_REQUEST).build();
                        }
                    }
                    user.setAuta(cars);
                }
                UserDto response = factory.toDto(user);
                return Response
                        .status(Response.Status.CREATED)
                        .entity(json.writeValueAsString(response))
                        .build();
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
    public Response updateUserInfo(@PathParam("id") Long id, String userBody){
        try {
            UserDto userDto = json.readValue(userBody, UserDto.class);
            userDto.setId(id);
            User user = factory.toEntity(userDto);
            Object o = carParkService.updateUser(user);
            if(o != null){
                UserDto response = factory.toDto((User) o);
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
    public Response destroyUser(@PathParam("id") Long id){
        Object o = carParkService.deleteUser(id);
        if(o != null){
            return Response.noContent().build();
        }
        return Response.status(Response.Status.NOT_FOUND).build();
    }
}
