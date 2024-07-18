package sk.stuba.fei.uim.vsa.pr2.web.response.factory;

import sk.stuba.fei.uim.vsa.pr2.entities.Car;
import sk.stuba.fei.uim.vsa.pr2.entities.ParkingSpot;
import sk.stuba.fei.uim.vsa.pr2.entities.Reservation;
import sk.stuba.fei.uim.vsa.pr2.entities.User;
import sk.stuba.fei.uim.vsa.pr2.web.response.*;

import java.util.Date;

public class ReservationResponseFactory {


    public Reservation2Dto toDto(Reservation entity){
        return new Reservation2Dto(entity.getId(), entity.getZaciatok(), entity.getKoniec(), entity.getCena(), entity.getCar().getId(), entity.getParkingSpot().getId());
    }

    public Reservation toEntity(Reservation2Dto dto){
        Car car = new Car();
        ParkingSpot parkingSpot = new ParkingSpot();
        car.setId(dto.getCar());
        parkingSpot.setId(dto.getSpot());
        Reservation reservation = new Reservation(car, parkingSpot, dto.getStart(), dto.getEnd(), dto.getPrices());
        reservation.setId(dto.getId());
        return reservation;
    }

    public ReservationDto toFullDto(Reservation entity){
        CarResponseFactory factory = new CarResponseFactory();
        ParkingSpotResponseFactory factory2 = new ParkingSpotResponseFactory();
        Car2Dto car = factory.toDto(entity.getCar());
        ParkingSpot2Dto spot = factory2.toDtoOther(entity.getParkingSpot());
        return new ReservationDto(entity.getId(), entity.getZaciatok(), entity.getKoniec(), entity.getCena(), car, spot);
    }

    public Reservation toEntity(ReservationDto dto){
        Car car = new Car();
        ParkingSpot parkingSpot = new ParkingSpot();
        if(dto.getCar() != null){
            car = new Car(dto.getCar().getBrand(), dto.getCar().getModel(), dto.getCar().getVrp(), dto.getCar().getColour(), null);
            car.setId(dto.getCar().getId());
        }
        if(dto.getSpot() != null){
            parkingSpot = new ParkingSpot(dto.getSpot().getIdentifier());
            parkingSpot.setId(dto.getSpot().getId());
        }
        Reservation reservation = new Reservation(car, parkingSpot, dto.getStart(), dto.getEnd(), dto.getPrices());
        reservation.setId(dto.getId());
        return reservation;
    }
}
