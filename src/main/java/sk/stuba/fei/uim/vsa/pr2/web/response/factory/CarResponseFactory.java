package sk.stuba.fei.uim.vsa.pr2.web.response.factory;

import sk.stuba.fei.uim.vsa.pr2.entities.Car;
import sk.stuba.fei.uim.vsa.pr2.entities.ParkingSpot;
import sk.stuba.fei.uim.vsa.pr2.entities.Reservation;
import sk.stuba.fei.uim.vsa.pr2.entities.User;
import sk.stuba.fei.uim.vsa.pr2.web.response.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class CarResponseFactory {
    private final ReservationResponseFactory factory = new ReservationResponseFactory();

    public Car2Dto toDto(Car entity) {
        User2Dto owner = new User2Dto(entity.getUser().getId(), entity.getUser().getMeno(), entity.getUser().getPriezvisko(), entity.getUser().getEmail());
        List<Reservation2Dto> reservation2DtoList = new ArrayList<>();
        if(entity.getReservations() != null){
            for(Object o : entity.getReservations()){
                Reservation reservation = (Reservation) o;
                //Reservation2Dto reservation2Dto = new Reservation2Dto(reservation.getId(), reservation.getZaciatok(), reservation.getKoniec(), reservation.getCena(), reservation.getCar().getId(), reservation.getParkingSpot().getId());
                Reservation2Dto reservation2Dto = factory.toDto(reservation);
                reservation2DtoList.add(reservation2Dto);
            }
        }
        return new Car2Dto(entity.getId(), entity.getZnacka(), entity.getModel(), entity.getECV(), entity.getFarba(), owner, reservation2DtoList);

    }

    public Car toEntity(Car2Dto dto) {
        User owner = new User(dto.getOwner().getFirstName(), dto.getOwner().getLastName(), dto.getOwner().getEmail());
        owner.setId(dto.getOwner().getId());
        Car car = new Car(dto.getBrand(), dto.getModel(), dto.getVrp(), dto.getColour(), owner);
        car.setId(dto.getId());
        if(dto.getReservations() != null){
            List<Reservation> reservations = new ArrayList<>();
            for(Reservation2Dto r : dto.getReservations()){
                Reservation res = factory.toEntity(r);
                res.setCar(car);
                reservations.add(res);
            }
            car.setReservations(reservations);
        }
        if(owner.getAuta() == null){
            owner.setAuta(new ArrayList<>());
        }
        owner.getAuta().add(car);
        return car;
    }

    public CarDto toDtoOther(Car entity){
        List<Reservation2Dto> reservations = new ArrayList<>();
        if(entity.getReservations() != null){
            for(Reservation r : entity.getReservations()){
                Reservation2Dto reservation2Dto = factory.toDto(r);
                reservations.add(reservation2Dto);
            }
        }
        return new CarDto(entity.getId(), entity.getZnacka(), entity.getModel(), entity.getECV(), entity.getFarba(), reservations);
    }
}
