package sk.stuba.fei.uim.vsa.pr2.web.response;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;


public class ReservationDto extends Dto{

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Europe/Bratislava")
    private Date start;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Europe/Bratislava")
    private Date end;
    private Double prices;
    private Car2Dto car;
    private ParkingSpot2Dto spot;


    public ReservationDto() {
    }

    public ReservationDto(Long id, Date start, Date end, Double prices, Car2Dto car, ParkingSpot2Dto spot) {
        super(id);
        this.start = start;
        this.end = end;
        this.prices = prices;
        this.car = car;
        this.spot = spot;
    }

    public Date getStart() {
        return start;
    }

    public void setStart(Date start) {
        this.start = start;
    }

    public Date getEnd() {
        return end;
    }

    public void setEnd(Date end) {
        this.end = end;
    }

    public Double getPrices() {
        return prices;
    }

    public void setPrices(Double prices) {
        this.prices = prices;
    }

    public Car2Dto getCar() {
        return car;
    }

    public void setCar(Car2Dto car) {
        this.car = car;
    }

    public ParkingSpot2Dto getSpot() {
        return spot;
    }

    public void setSpot(ParkingSpot2Dto spot) {
        this.spot = spot;
    }
}
