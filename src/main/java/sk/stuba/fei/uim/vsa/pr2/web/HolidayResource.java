package sk.stuba.fei.uim.vsa.pr2.web;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import sk.stuba.fei.uim.vsa.pr2.entities.Holiday;
import sk.stuba.fei.uim.vsa.pr2.service.CarParkService;
import sk.stuba.fei.uim.vsa.pr2.web.response.HolidayDto;
import sk.stuba.fei.uim.vsa.pr2.web.response.factory.HolidayResponseFactory;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Path("/holidays")
public class HolidayResource{
    private final CarParkService carParkService = new CarParkService();
    private final ObjectMapper json = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    private final HolidayResponseFactory factory = new HolidayResponseFactory();

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllHolidays(@QueryParam("date") String dateString){
        List<HolidayDto> response = new ArrayList<>();
        if(dateString != null){
            Date date = null;
            try {
                date = new SimpleDateFormat("yyyy-MM-dd").parse(dateString);
            } catch (ParseException e) {
                return Response.status(Response.Status.BAD_REQUEST).build();
            }
            Object o = carParkService.getHoliday(date);
            if(o != null){
                HolidayDto holidayDto =  factory.toDto((Holiday) o);
                response.add(holidayDto);
            }else{
                return Response.status(Response.Status.NOT_FOUND).build();
            }
        }else{
            List<Object> objects = carParkService.getHolidays();
            if(objects != null){
                for(Object o : objects){
                    HolidayDto holidayDto = factory.toDto((Holiday) o);
                    response.add(holidayDto);
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

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createHolidayFromBody(String holidayBody){
        try {
            HolidayDto holidayDto = json.readValue(holidayBody, HolidayDto.class);
            Object o = carParkService.createHoliday(holidayDto.getName(), holidayDto.getDate());
            if(o != null){
                HolidayDto response = factory.toDto((Holiday) o);
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

    @DELETE
    @Path("/{id}")
    public Response destroyHoliday(@PathParam("id") Long id){
        Object o = carParkService.deleteHoliday(id);
        if(o != null){
            return Response.noContent().build();
        }
        return Response.status(Response.Status.NOT_FOUND).build();
    }

}
