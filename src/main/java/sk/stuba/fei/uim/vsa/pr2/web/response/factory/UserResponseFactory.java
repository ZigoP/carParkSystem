package sk.stuba.fei.uim.vsa.pr2.web.response.factory;

import sk.stuba.fei.uim.vsa.pr2.entities.Car;
import sk.stuba.fei.uim.vsa.pr2.entities.Reservation;
import sk.stuba.fei.uim.vsa.pr2.entities.User;
import sk.stuba.fei.uim.vsa.pr2.web.response.CarDto;
import sk.stuba.fei.uim.vsa.pr2.web.response.Reservation2Dto;
import sk.stuba.fei.uim.vsa.pr2.web.response.UserDto;

import java.util.ArrayList;
import java.util.List;

public class UserResponseFactory {

    public UserDto toDto(User entity) {
        List<CarDto> carsDto = new ArrayList<>();

        if(entity.getAuta() != null){
            for(Car c : entity.getAuta()){
                List<Reservation2Dto> res = new ArrayList<>();
                if(c.getReservations() != null){
                    ReservationResponseFactory factory = new ReservationResponseFactory();
                    for(Reservation r : c.getReservations()){
                        Reservation2Dto reservation2Dto = factory.toDto(r);
                        res.add(reservation2Dto);
                    }
                }
                CarDto carDto = new CarDto(c.getId(), c.getZnacka(), c.getModel(), c.getECV(), c.getFarba(), res);
                carsDto.add(carDto);
            }
        }
        return new UserDto(entity.getId(), entity.getMeno(), entity.getPriezvisko(), entity.getEmail(), carsDto);
    }

    public User toEntity(UserDto dto) {
        User user = new User(dto.getFirstName(), dto.getLastName(), dto.getEmail());
        user.setId(dto.getId());
        List<Car> cars = new ArrayList<>();

        if(dto.getCars() != null){
            for(CarDto c : dto.getCars()){
                Car car = new Car(c.getBrand(), c.getModel() ,c.getVrp(), c.getColour(), user);
                car.setId(c.getId());
                cars.add(car);
            }
        }
        user.setAuta(cars);

        return user;
    }
}
