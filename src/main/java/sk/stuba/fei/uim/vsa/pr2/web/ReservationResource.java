package sk.stuba.fei.uim.vsa.pr2.web;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.HttpHeaders;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import sk.stuba.fei.uim.vsa.pr2.entities.Reservation;
import sk.stuba.fei.uim.vsa.pr2.entities.User;
import sk.stuba.fei.uim.vsa.pr2.service.CarParkService;
import sk.stuba.fei.uim.vsa.pr2.web.response.ReservationDto;
import sk.stuba.fei.uim.vsa.pr2.web.response.factory.ReservationResponseFactory;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Path("/reservations")
public class ReservationResource{
    private final CarParkService carParkService = new CarParkService();
    private final ObjectMapper json = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    private final ReservationResponseFactory factory = new ReservationResponseFactory();

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllReservations(@QueryParam("user") Long user, @QueryParam("spot") Long spot, @QueryParam("date") String dateString) {
        List<ReservationDto> response = new ArrayList<>();
        if (user != null) {
            List<Object> objects = carParkService.getMyReservations(user);
            if (objects != null) {
                for(Object o : objects){
                    ReservationDto reservationDto = factory.toFullDto((Reservation) o);
                    response.add(reservationDto);
                }
            }else{
                return Response.status(Response.Status.NOT_FOUND).build();
            }
        }else if(spot != null && dateString != null){
            Date date = null;
            try {
                date = new SimpleDateFormat("yyyy-MM-dd").parse(dateString);
            } catch (ParseException e) {
                return Response.status(Response.Status.BAD_REQUEST).build();
            }
            List<Object> objects = carParkService.getReservations(spot, date);
            if(objects != null){
                for(Object o : objects){
                    ReservationDto reservationDto = factory.toFullDto((Reservation) o);
                    response.add(reservationDto);
                }
            }else{
                return Response.status(Response.Status.NOT_FOUND).build();
            }
        }else{
            List<Object> objects = carParkService.getAllReservations();
            if(objects != null){
                for(Object o : objects){
                    ReservationDto reservationDto = factory.toFullDto((Reservation) o);
                    response.add(reservationDto);
                }
            }else{
                return Response.status(Response.Status.NOT_FOUND).build();
            }
        }
        try {
            return Response
                    .status(Response.Status.OK)
                    .entity(json.writeValueAsString(response))
                    .build();
        } catch (JsonProcessingException e) {
            return Response.noContent().build();
        }
    }

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getReservationById(@PathParam("id") Long id){
        Object o = carParkService.getReservation(id);
        if(o != null){
            ReservationDto response = factory.toFullDto((Reservation) o);
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
    @Path("/{id}/end")
    @Produces(MediaType.APPLICATION_JSON)
    public Response endReservation(@PathParam("id") Long id, @HeaderParam(HttpHeaders.AUTHORIZATION) String auth){
        Object ob = carParkService.getReservation(id);
        if(ob != null){
            Reservation reservation = (Reservation) ob;
            Object obj = carParkService.getUser(reservation.getCar().getUser().getId());
            if(obj != null){
                User reservationUser = (User) obj;
                if(!reservationUser.getEmail().equals(BasicAuthRequestFilter.getEmailForReservation(auth))){
                    return Response.status(Response.Status.UNAUTHORIZED).build();
                }
                Object o = carParkService.endReservation(id);
                if(o != null){
                    ReservationDto response = factory.toFullDto((Reservation) o);
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
        }
        return Response.status(Response.Status.BAD_REQUEST).build();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createReservationFromBody(String reservationBody){
        try {
            ReservationDto reservationDto = json.readValue(reservationBody, ReservationDto.class);
            Object o = carParkService.createReservation(reservationDto.getSpot().getId(), reservationDto.getCar().getId());
            if(o != null){
                ReservationDto response = factory.toFullDto((Reservation) o);
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
    public Response updateCarParkInfo(@PathParam("id") Long id, String reservationBody){
        try {
            ReservationDto reservationDto = json.readValue(reservationBody, ReservationDto.class);
            reservationDto.setId(id);
            Reservation reservation = factory.toEntity(reservationDto);
            Object o = carParkService.updateReservation(reservation);
            if(o != null){
                ReservationDto response = factory.toFullDto((Reservation) o);
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
}
