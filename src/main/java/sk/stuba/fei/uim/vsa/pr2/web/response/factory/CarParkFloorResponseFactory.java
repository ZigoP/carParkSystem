package sk.stuba.fei.uim.vsa.pr2.web.response.factory;

import sk.stuba.fei.uim.vsa.pr2.entities.CarParkFloor;
import sk.stuba.fei.uim.vsa.pr2.entities.ParkingSpot;
import sk.stuba.fei.uim.vsa.pr2.entities.Reservation;
import sk.stuba.fei.uim.vsa.pr2.web.response.CarParkFloorDto;
import sk.stuba.fei.uim.vsa.pr2.web.response.ParkingSpotDto;
import sk.stuba.fei.uim.vsa.pr2.web.response.Reservation2Dto;

import java.util.ArrayList;
import java.util.List;

public class CarParkFloorResponseFactory {

    private final ParkingSpotResponseFactory factory = new ParkingSpotResponseFactory();

    public CarParkFloorDto toDto(CarParkFloor entity){
        List<ParkingSpotDto> spots = new ArrayList<>();
        if(entity.getParkovacieMiesta() != null){
            for(ParkingSpot ps : entity.getParkovacieMiesta()){
                /*List<Reservation2Dto> reservations = new ArrayList<>();
                if(ps.getReservations() != null){
                    for(Reservation r : ps.getReservations()){
                        ReservationResponseFactory factory = new ReservationResponseFactory();
                        Reservation2Dto reservation2Dto = factory.toDto(r);
                        reservations.add(reservation2Dto);
                    }
                }
                ParkingSpotDto parkingSpotDto = new ParkingSpotDto(ps.getId(), ps.getIdentifier(), entity.getIdentifier(), entity.getCarPark().getId(), reservations);*/
                ParkingSpotDto parkingSpotDto = factory.toDto(ps);
                parkingSpotDto.setCarPark(entity.getCarPark().getId());
                spots.add(parkingSpotDto);
            }
        }
        return new CarParkFloorDto(entity.getId(), entity.getIdentifier(), entity.getCarPark().getId(), spots);
    }

    public CarParkFloor toEntity(CarParkFloorDto dto){
        CarParkFloor carParkFloor = new CarParkFloor();
        List<ParkingSpot> parkingSpots = new ArrayList<>();
        if(dto.getSpots() != null){
            for(ParkingSpotDto ps : dto.getSpots()){
                /*List<Reservation> reservations = new ArrayList<>();
                if(ps.getReservations() != null){
                    for(Reservation2Dto r : ps.getReservations()){
                        ReservationResponseFactory factory = new ReservationResponseFactory();
                        Reservation reservation = factory.toEntity(r);
                        reservations.add(reservation);
                    }
                }
                ParkingSpot parkingSpot = new ParkingSpot(ps.getIdentifier());
                parkingSpot.setId(ps.getId());

                parkingSpot.setReservations(reservations);*/

                ParkingSpot parkingSpot = factory.toEntity(ps);
                parkingSpot.setCarParkFloor(carParkFloor);
                parkingSpots.add(parkingSpot);
            }
        }
        carParkFloor.setId(dto.getId());
        carParkFloor.setIdentifier(dto.getIdentifier());
        carParkFloor.setParkovacieMiesta(parkingSpots);
        //carParkFloor.setCarPark(dto.getCarPark());
        return carParkFloor;
    }
}
