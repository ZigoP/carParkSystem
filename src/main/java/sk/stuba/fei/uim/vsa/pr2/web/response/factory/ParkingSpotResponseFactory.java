package sk.stuba.fei.uim.vsa.pr2.web.response.factory;

import sk.stuba.fei.uim.vsa.pr2.entities.ParkingSpot;
import sk.stuba.fei.uim.vsa.pr2.entities.Reservation;
import sk.stuba.fei.uim.vsa.pr2.web.response.ParkingSpot2Dto;
import sk.stuba.fei.uim.vsa.pr2.web.response.ParkingSpotDto;
import sk.stuba.fei.uim.vsa.pr2.web.response.Reservation2Dto;

import java.util.ArrayList;
import java.util.List;

public class ParkingSpotResponseFactory {

    public ParkingSpotDto toDto(ParkingSpot entity){
        List<Reservation2Dto> reservations = new ArrayList<>();
        if(entity.getReservations() != null){
            for(Reservation r : entity.getReservations()){
                ReservationResponseFactory factory = new ReservationResponseFactory();
                Reservation2Dto reservation2Dto = factory.toDto(r);
                reservations.add(reservation2Dto);
            }
        }
        ParkingSpotDto parkingSpotDto = new ParkingSpotDto();
        parkingSpotDto.setId(entity.getId());
        parkingSpotDto.setIdentifier(entity.getIdentifier());
        parkingSpotDto.setCarParkFloor(entity.getCarParkFloor().getIdentifier());
        parkingSpotDto.setReservations(reservations);
        parkingSpotDto.setFree(entity.isFree());
        return parkingSpotDto;
    }

    public ParkingSpot toEntity(ParkingSpotDto dto){
        List<Reservation> reservations = new ArrayList<>();
        if(dto.getReservations() != null){
            for(Reservation2Dto r : dto.getReservations()){
                ReservationResponseFactory factory = new ReservationResponseFactory();
                Reservation reservation = factory.toEntity(r);
                reservations.add(reservation);
            }
        }
        ParkingSpot parkingSpot = new ParkingSpot(dto.getIdentifier());
        parkingSpot.setId(dto.getId());
        //parkingSpot.setCarParkFloor(carParkFloor);
        parkingSpot.setReservations(reservations);
        return parkingSpot;
    }

    public ParkingSpot2Dto toDtoOther(ParkingSpot entity){
        return new ParkingSpot2Dto(entity.getId(), entity.getIdentifier(), entity.getCarParkFloor().getIdentifier(), entity.getCarParkFloor().getCarPark().getId(), entity.isFree());
    }
}
