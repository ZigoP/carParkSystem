package sk.stuba.fei.uim.vsa.pr2.web.response.factory;

import sk.stuba.fei.uim.vsa.pr2.entities.CarPark;
import sk.stuba.fei.uim.vsa.pr2.entities.CarParkFloor;
import sk.stuba.fei.uim.vsa.pr2.entities.ParkingSpot;
import sk.stuba.fei.uim.vsa.pr2.entities.Reservation;
import sk.stuba.fei.uim.vsa.pr2.web.response.CarParkDto;
import sk.stuba.fei.uim.vsa.pr2.web.response.CarParkFloorDto;
import sk.stuba.fei.uim.vsa.pr2.web.response.ParkingSpotDto;
import sk.stuba.fei.uim.vsa.pr2.web.response.Reservation2Dto;

import java.util.ArrayList;
import java.util.List;

public class CarParkResponseFactory {

    private final CarParkFloorResponseFactory factory = new CarParkFloorResponseFactory();

    public CarParkDto toDto(CarPark entity){
        List<CarParkFloorDto> floors = new ArrayList<>();
        if(entity.getParkovaciePoschodie() != null){
            for(CarParkFloor cpf : entity.getParkovaciePoschodie()){

                /*List<ParkingSpotDto> spots = new ArrayList<>();
                if(cpf.getParkovacieMiesta() != null){
                    for(ParkingSpot ps : cpf.getParkovacieMiesta()){

                        List<Reservation2Dto> reservations = factory
                        if(ps.getReservations() != null){
                            for(Reservation r : ps.getReservations()){
                                ReservationResponseFactory factory = new ReservationResponseFactory();
                                Reservation2Dto reservation2Dto = factory.toDto(r);
                                reservations.add(reservation2Dto);
                            }
                        }
                        ParkingSpotDto parkingSpotDto = new ParkingSpotDto(ps.getId(), ps.getIdentifier(), ps.getCarParkFloor().getIdentifier(), cpf.getCarPark().getId(), reservations);
                        spots.add(parkingSpotDto);
                    }
                }*/
                //CarParkFloorDto floor = new CarParkFloorDto(cpf.getId(), cpf.getIdentifier(), cpf.getCarPark().getId(), spots);
                CarParkFloorDto floor = factory.toDto(cpf);
                floors.add(floor);
            }

        }
        return new CarParkDto(entity.getId(), entity.getNazov(), entity.getAdresa(), entity.getCenaZaHodinu(), floors);
    }

    public CarPark toEntity(CarParkDto dto){
        CarPark carPark = new CarPark(dto.getName(), dto.getAddress(), dto.getPrices());
        carPark.setId(dto.getId());
        List<CarParkFloor> carParkFloors = new ArrayList<>();
        if(dto.getFloors() != null){
            for(CarParkFloorDto cpf : dto.getFloors()){
                CarParkFloor carParkFloor = factory.toEntity(cpf);
                /*List<ParkingSpot> parkingSpots = new ArrayList<>();
                if(cpf.getSpots() != null){
                    for(ParkingSpotDto ps : cpf.getSpots()){
                        List<Reservation> reservations = new ArrayList<>();
                        if(ps.getReservations() != null){
                            for(Reservation2Dto r : ps.getReservations()){
                                ReservationResponseFactory factory = new ReservationResponseFactory();
                                Reservation reservation = factory.toEntity(r);
                                reservations.add(reservation);
                            }
                        }
                        ParkingSpot parkingSpot = new ParkingSpot(ps.getIdentifier());
                        parkingSpot.setId(ps.getId());
                        parkingSpot.setCarParkFloor(carParkFloor);
                        parkingSpot.setReservations(reservations);
                        parkingSpots.add(parkingSpot);
                    }
                }
                carParkFloor.setId(cpf.getId());
                carParkFloor.setIdentifier(cpf.getIdentifier());
                carParkFloor.setParkovacieMiesta(parkingSpots);
                carParkFloors.add(carParkFloor);*/
                carParkFloor.setCarPark(carPark);
            }
        }
        carPark.setParkovaciePoschodie(carParkFloors);
        return carPark;
    }
}
