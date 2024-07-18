package sk.stuba.fei.uim.vsa.pr2.service;

import sk.stuba.fei.uim.vsa.pr2.entities.*;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.*;

public class CarParkService extends AbstractCarParkService{

    @Override
    public Object createCarPark(String name, String address, Integer pricePerHour) {
        if(name != null && pricePerHour != null){
            if(getCarPark(name) == null){
                CarPark carPark = new CarPark(name, address, pricePerHour);
                EntityManager em = emf.createEntityManager();
                em.getTransaction().begin();
                try {
                    em.persist(carPark);
                    em.getTransaction().commit();
                } catch (RuntimeException e) {
                    return null;
                } finally {
                    em.close();
                }
                return carPark;
            }
        }
        return null;
    }

    @Override
    public Object getCarPark(Long carParkId) {
        if(carParkId != null){
            EntityManager em = emf.createEntityManager();
            return em.find(CarPark.class, carParkId);
        }
        return null;
    }

    @Override
    public Object getCarPark(String carParkName) {
        if(carParkName != null){
            EntityManager em = emf.createEntityManager();
            TypedQuery<CarPark> query= em.createQuery("select cp from CarPark cp where cp.nazov = '"+carParkName+"'", CarPark.class);
            try{
                return query.getSingleResult();
            }catch (NoResultException e){
                return null;
            }
        }
        return null;
    }

    @Override
    public List<Object> getCarParks() {
        EntityManager em = emf.createEntityManager();
        TypedQuery<CarPark> query= em.createQuery("select cp from CarPark cp" , CarPark.class);
        return new ArrayList<>(query.getResultList());
    }

    @Override
    public Object updateCarPark(Object carPark) {
        if(carPark instanceof CarPark){
            if(((CarPark) carPark).getId() != null){
                EntityManager em = emf.createEntityManager();
                CarPark updatedCarPark = em.find(CarPark.class, ((CarPark) carPark).getId());
                if(updatedCarPark == null){
                    return null;
                }
                em.getTransaction().begin();
                try {
                    if(((CarPark) carPark).getNazov() != null){
                        updatedCarPark.setNazov(((CarPark) carPark).getNazov());
                        em.merge(updatedCarPark);
                    }
                    if(((CarPark) carPark).getAdresa() != null){
                        updatedCarPark.setAdresa(((CarPark) carPark).getAdresa());
                        em.merge(updatedCarPark);
                    }
                    if(((CarPark) carPark).getCenaZaHodinu() != null){
                        updatedCarPark.setCenaZaHodinu(((CarPark) carPark).getCenaZaHodinu());
                        em.merge(updatedCarPark);
                    }
                    em.getTransaction().commit();
                } catch (RuntimeException e) {
                    return null;
                } finally {
                    em.close();
                }
                return updatedCarPark;
            }
        }
        return null;
    }

    @Override
    public Object deleteCarPark(Long carParkId) {
        if(carParkId != null){
            EntityManager em = emf.createEntityManager();
            CarPark carPark = em.find(CarPark.class,carParkId);
            if(carPark != null){
                em.getTransaction().begin();
                try{
                    for(CarParkFloor cpf : carPark.getParkovaciePoschodie()){
                        for(ParkingSpot ps : cpf.getParkovacieMiesta()){
                            for(Reservation r : ps.getReservations()){
                                if(r.getKoniec() == null){
                                    endReservation(r.getId());
                                }
                                r.setParkingSpot(null);
                            }
                        }
                    }
                    em.remove(carPark);
                    em.getTransaction().commit();
                }catch (RuntimeException e){
                    return null;
                }finally {
                    em.close();
                }
                return carPark;
            }
        }
        return null;
    }

    @Override
    public Object createCarParkFloor(Long carParkId, String floorIdentifier) {
        if(carParkId != null && floorIdentifier != null){
            EntityManager em = emf.createEntityManager();
            CarPark carPark = em.find(CarPark.class,carParkId);
            if(carPark != null){
                for(CarParkFloor cpf : carPark.getParkovaciePoschodie()){
                    if(cpf.getIdentifier().equals(floorIdentifier)){
                        return null;
                    }
                }
                CarParkFloor carParkFloor = new CarParkFloor();
                carParkFloor.setIdentifier(floorIdentifier);
                carParkFloor.setCarPark(carPark);
                em.getTransaction().begin();
                try{
                    em.persist(carParkFloor);
                    carPark.getParkovaciePoschodie().add(carParkFloor);
                    em.merge(carPark);
                    em.getTransaction().commit();
                }catch (RuntimeException e){
                    return null;
                }finally {
                    em.close();
                }
                return carParkFloor;
            }
        }
        return null;
    }

    @Override
    public Object getCarParkFloor(Long carParkFloorId) {
        if(carParkFloorId != null){
            EntityManager em = emf.createEntityManager();
            return em.find(CarParkFloor.class,carParkFloorId);
        }
        return null;
    }

    @Override
    public List<Object> getCarParkFloors(Long carParkId) {
        if(carParkId != null){
            EntityManager em = emf.createEntityManager();
            CarPark carPark = em.find(CarPark.class,carParkId);
            if(carPark != null){
                return new ArrayList<>(carPark.getParkovaciePoschodie());
            }
        }
        return null;
    }

    @Override
    public Object updateCarParkFloor(Object carParkFloor) {
        if(carParkFloor instanceof CarParkFloor){
            if(((CarParkFloor) carParkFloor).getId() != null){
                EntityManager em = emf.createEntityManager();
                CarParkFloor updatedCarParkFloor = em.find(CarParkFloor.class, ((CarParkFloor) carParkFloor).getId());
                if(updatedCarParkFloor == null){
                    return null;
                }
                em.getTransaction().begin();
                try {
                    if(((CarParkFloor) carParkFloor).getIdentifier() != null){
                        updatedCarParkFloor.setIdentifier(((CarParkFloor) carParkFloor).getIdentifier());
                        em.merge(updatedCarParkFloor);
                    }
                    em.getTransaction().commit();
                } catch (RuntimeException e) {
                    return null;
                } finally {
                    em.close();
                }
                return updatedCarParkFloor;
            }
        }
        return null;
    }

    @Override
    public Object deleteCarParkFloor(Long carParkFloorId) {
        if(carParkFloorId != null){
            EntityManager em = emf.createEntityManager();
            CarParkFloor carParkFloor = em.find(CarParkFloor.class,carParkFloorId);
            if(carParkFloor != null){
                if(carParkFloor.getCarPark() == null){
                    return null;
                }
                CarPark carPark = carParkFloor.getCarPark();
                em.getTransaction().begin();
                try{
                    carPark.getParkovaciePoschodie().remove(carParkFloor);
                    em.merge(carPark);
                    for(ParkingSpot ps : carParkFloor.getParkovacieMiesta()){
                        for(Reservation r : ps.getReservations()){
                            if(r.getKoniec() == null){
                                endReservation(r.getId());
                            }
                            r.setParkingSpot(null);
                        }
                    }
                    em.remove(carParkFloor);
                    em.getTransaction().commit();
                }catch (RuntimeException e){
                    return null;
                }finally {
                    em.close();
                }
                return carParkFloor;
            }
        }
        return null;
    }

    @Override
    public Object createParkingSpot(Long carParkId, String floorIdentifier, String spotIdentifier) {
        if(carParkId != null && floorIdentifier != null && spotIdentifier != null){
            EntityManager em = emf.createEntityManager();
            CarPark carPark = em.find(CarPark.class, carParkId);
            if(carPark != null){
                List<CarParkFloor> carParkFloors = carPark.getParkovaciePoschodie();
                CarParkFloor carParkFloor = null;
                for(CarParkFloor cp : carParkFloors){
                    for(ParkingSpot ps : cp.getParkovacieMiesta()){
                        if(ps.getIdentifier().equals(spotIdentifier)){
                            return null;
                        }
                    }
                    if(cp.getIdentifier().equals(floorIdentifier)){
                        carParkFloor = cp;
                    }
                }
                if(carParkFloor != null){
                    ParkingSpot parkingSpot = new ParkingSpot();
                    parkingSpot.setIdentifier(spotIdentifier);
                    parkingSpot.setCarParkFloor(carParkFloor);
                    em.getTransaction().begin();
                    try{
                        em.persist(parkingSpot);
                        carParkFloor.getParkovacieMiesta().add(parkingSpot);
                        em.merge(carParkFloor);
                        em.getTransaction().commit();
                    }catch (RuntimeException e){
                        return null;
                    }finally {
                        em.close();
                    }
                    return parkingSpot;
                }
            }
        }
        return null;
    }

    @Override
    public Object getParkingSpot(Long parkingSpotId) {
        if(parkingSpotId != null){
            EntityManager em = emf.createEntityManager();
            return em.find(ParkingSpot.class, parkingSpotId);
        }
        return null;
    }

    @Override
    public List<Object> getParkingSpots(Long carParkId, String floorIdentifier) {
        if(carParkId != null && floorIdentifier != null){
            EntityManager em = emf.createEntityManager();
            CarPark carPark = em.find(CarPark.class, carParkId);
            if(carPark != null){
                List<CarParkFloor> carParkFloors = carPark.getParkovaciePoschodie();
                CarParkFloor carParkFloor = null;
                for(CarParkFloor cp : carParkFloors){
                    if(cp.getIdentifier().equals(floorIdentifier)){
                        carParkFloor = cp;
                    }
                }
                if(carParkFloor != null){
                    return new ArrayList<>(carParkFloor.getParkovacieMiesta());
                }
            }
        }
        return null;
    }

    @Override
    public Map<String, List<Object>> getParkingSpots(Long carParkId) {
        if(carParkId != null){
            EntityManager em = emf.createEntityManager();
            CarPark carPark = em.find(CarPark.class, carParkId);
            if(carPark != null){
                Map<String, List<Object>> map = new HashMap<>();
                List<CarParkFloor> carParkFloors = carPark.getParkovaciePoschodie();
                for(CarParkFloor cp : carParkFloors){
                    List<Object> parkingSpots = new ArrayList<>(cp.getParkovacieMiesta());
                    map.put(cp.getIdentifier(), parkingSpots);
                }
                return map;
            }
        }
        return null;
    }

    @Override
    public Map<String, List<Object>> getAvailableParkingSpots(String carParkName) {
        if(carParkName != null){
            EntityManager em = emf.createEntityManager();
            TypedQuery<CarPark> q = em.createQuery("select cp from CarPark cp where cp.nazov = '"+carParkName+"'", CarPark.class);
            try {
                CarPark carPark = q.getSingleResult();
                Map<String, List<Object>> map = new HashMap<>();
                for(CarParkFloor cpf : carPark.getParkovaciePoschodie()){
                    List<Object> spots = new ArrayList<>();
                    for(ParkingSpot ps : cpf.getParkovacieMiesta()){
                        boolean isFree = true;
                        for(Reservation r : ps.getReservations()){
                            if (r.getKoniec() == null) {
                                isFree = false;
                                break;
                            }
                        }
                        if(isFree){
                            spots.add(ps);
                        }
                    }
                    map.put(cpf.getIdentifier(), spots);
                }
                return map;
            }catch (NoResultException e){
                return null;
            }
        }
        return null;
    }

    @Override
    public Map<String, List<Object>> getOccupiedParkingSpots(String carParkName) {
        if(carParkName != null){
            EntityManager em = emf.createEntityManager();
            TypedQuery<CarPark> q = em.createQuery("select cp from CarPark cp where cp.nazov = '"+carParkName+"'", CarPark.class);
            try {
                CarPark carPark = q.getSingleResult();
                Map<String, List<Object>> map = new HashMap<>();
                for(CarParkFloor cpf : carPark.getParkovaciePoschodie()){
                    List<Object> spots = new ArrayList<>();
                    for(ParkingSpot ps : cpf.getParkovacieMiesta()){
                        boolean isFree = true;
                        for(Reservation r : ps.getReservations()){
                            if (r.getKoniec() == null) {
                                isFree = false;
                                break;
                            }
                        }
                        if(!isFree){
                            spots.add(ps);
                        }
                    }
                    map.put(cpf.getIdentifier(), spots);
                }
                return map;
            }catch (NoResultException e){
                return null;
            }
        }
        return null;
    }

    @Override
    public Object updateParkingSpot(Object parkingSpot) {
        if(parkingSpot instanceof ParkingSpot){
            if(((ParkingSpot) parkingSpot).getId() != null){
                EntityManager em = emf.createEntityManager();
                ParkingSpot updatedParkingSpot = em.find(ParkingSpot.class, ((ParkingSpot) parkingSpot).getId());
                if(updatedParkingSpot != null){
                    em.getTransaction().begin();
                    try {
                        if(((ParkingSpot) parkingSpot).getIdentifier() != null){
                            updatedParkingSpot.setIdentifier(((ParkingSpot) parkingSpot).getIdentifier());
                            em.merge(updatedParkingSpot);
                        }
                        em.getTransaction().commit();
                    } catch (RuntimeException e) {
                        return null;
                    } finally {
                        em.close();
                    }
                    return updatedParkingSpot;
                }
            }
        }
        return null;
    }

    @Override
    public Object deleteParkingSpot(Long parkingSpotId) {
        if(parkingSpotId != null){
            EntityManager em = emf.createEntityManager();
            ParkingSpot parkingSpot = em.find(ParkingSpot.class, parkingSpotId);
            if(parkingSpot != null){
                CarParkFloor carParkFloor = parkingSpot.getCarParkFloor();
                em.getTransaction().begin();
                try{
                    carParkFloor.getParkovacieMiesta().remove(parkingSpot);
                    em.merge(carParkFloor);
                    for(Reservation r : parkingSpot.getReservations()){
                        if(r.getKoniec() == null){
                            endReservation(r.getId());
                        }
                        r.setParkingSpot(null);
                    }
                    em.remove(parkingSpot);
                    em.getTransaction().commit();
                }catch (RuntimeException e){
                    return null;
                }finally {
                    em.close();
                }
                return parkingSpot;
            }

        }
        return null;
    }

    @Override
    public Object createCar(Long userId, String brand, String model, String colour, String vehicleRegistrationPlate) {
        if(userId != null && vehicleRegistrationPlate != null){
            EntityManager em = emf.createEntityManager();
            User user = em.find(User.class, userId);
            if(user != null){
                if(getCar(vehicleRegistrationPlate) == null){
                    Car car = new Car(brand, model, vehicleRegistrationPlate, colour, user);
                    em.getTransaction().begin();
                    try{
                        em.persist(car);
                        user.getAuta().add(car);
                        em.merge(user);
                        em.getTransaction().commit();
                    }catch (RuntimeException e){
                        return null;
                    }finally {
                        em.close();
                    }
                    return car;
                }

            }
        }
        return null;
    }

    @Override
    public Object getCar(Long carId) {
        if(carId != null){
            EntityManager em = emf.createEntityManager();
            return em.find(Car.class, carId);
        }
        return null;
    }

    @Override
    public Object getCar(String vehicleRegistrationPlate) {
        if(vehicleRegistrationPlate != null){
            EntityManager em = emf.createEntityManager();
            TypedQuery<Car> query = em.createQuery("select c from Car c where c.ECV = '"+vehicleRegistrationPlate+"'", Car.class);
            try {
                return query.getSingleResult();
            }catch (NoResultException e){
                return null;
            }
        }
        return null;
    }

    public List<Object> getCars(){
        EntityManager em = emf.createEntityManager();
        TypedQuery<Car> query= em.createQuery("select c from Car c" , Car.class);
        return new ArrayList<>(query.getResultList());
    }

    @Override
    public List<Object> getCars(Long userId) {
        if(userId != null){
            EntityManager em = emf.createEntityManager();
            User user = em.find(User.class, userId);
            if(user != null){
                return new ArrayList<>(user.getAuta());
            }
        }
        return null;
    }

    @Override
    public Object updateCar(Object car) {
        if(car instanceof Car){
            EntityManager em = emf.createEntityManager();
            Car updatedCar = em.find(Car.class, ((Car) car).getId());
            if(updatedCar != null){
                em.getTransaction().begin();
                try {
                    if(((Car) car).getZnacka() != null){
                        updatedCar.setZnacka(((Car) car).getZnacka());
                        em.merge(updatedCar);
                    }
                    if(((Car) car).getModel() != null){
                        updatedCar.setModel(((Car) car).getModel());
                        em.merge(updatedCar);
                    }
                    if(((Car) car).getECV() != null){
                        updatedCar.setECV(((Car) car).getECV());
                        em.merge(updatedCar);
                    }
                    if(((Car) car).getFarba() != null){
                        updatedCar.setFarba(((Car) car).getFarba());
                        em.merge(updatedCar);
                    }
                    em.getTransaction().commit();
                } catch (RuntimeException e) {
                    return null;
                } finally {
                    em.close();
                }
                return updatedCar;
            }
        }
        return null;
    }

    @Override
    public Object deleteCar(Long carId) {
        if(carId != null){
            EntityManager em = emf.createEntityManager();
            Car car = em.find(Car.class, carId);
            if(car != null){
                User user = car.getUser();
                em.getTransaction().begin();
                try{
                    user.getAuta().remove(car);
                    em.merge(user);
                    for(Reservation r : car.getReservations()){
                        if(r.getKoniec() == null){
                            endReservation(r.getId());
                        }
                        r.setCar(null);
                    }
                    em.remove(car);
                    em.getTransaction().commit();
                }catch (RuntimeException e){
                    return null;
                }finally {
                    em.close();
                }
                return car;
            }
        }
        return null;
    }

    @Override
    public Object createUser(String firstname, String lastname, String email) {
        if(email != null){
            if(getUser(email) == null){
                User user = new User(firstname,lastname,email);
                EntityManager em = emf.createEntityManager();
                em.getTransaction().begin();
                try {
                    em.persist(user);
                    em.getTransaction().commit();
                } catch (RuntimeException e) {
                    return null;
                } finally {
                    em.close();
                }
                return user;
            }
        }
        return null;
    }

    @Override
    public Object getUser(Long userId) {
        if(userId != null){
            EntityManager em = emf.createEntityManager();
            return em.find(User.class, userId);
        }
        return null;
    }

    @Override
    public Object getUser(String email) {
        if(email != null){
            EntityManager em = emf.createEntityManager();
            TypedQuery<User> query = em.createQuery("select u from User u where u.email = '"+email+"'", User.class);
            try {
                return query.getSingleResult();
            }catch (NoResultException e){
                return null;
            }
        }
        return null;
    }

    @Override
    public List<Object> getUsers() {
        EntityManager em = emf.createEntityManager();
        TypedQuery<User> query = em.createQuery("select u from User u", User.class);
        return new ArrayList<>(query.getResultList());
    }

    @Override
    public Object updateUser(Object user) {
        if(user instanceof User){
            if(((User) user).getId() != null){
                EntityManager em = emf.createEntityManager();
                User updatedUser = em.find(User.class, ((User) user).getId());
                if(updatedUser != null){
                    em.getTransaction().begin();
                    try {
                        if(((User) user).getMeno() != null){
                            updatedUser.setMeno(((User) user).getMeno());
                            em.merge(updatedUser);
                        }
                        if(((User) user).getPriezvisko() != null){
                            updatedUser.setPriezvisko(((User) user).getPriezvisko());
                            em.merge(updatedUser);
                        }
                        if(((User) user).getEmail() != null){
                            updatedUser.setEmail(((User) user).getEmail());
                            em.merge(updatedUser);
                        }
                        em.getTransaction().commit();
                    } catch (RuntimeException e) {
                        return null;
                    } finally {
                        em.close();
                    }
                    return updatedUser;
                }
            }
        }
        return null;
    }

    @Override
    public Object deleteUser(Long userId) {
        if(userId != null){
            EntityManager em = emf.createEntityManager();
            User user = em.find(User.class, userId);
            if(user != null){
                em.getTransaction().begin();
                try{
                    for(Car c : user.getAuta()){
                        for(Reservation r : c.getReservations()){
                            if(r.getKoniec() == null){
                                endReservation(r.getId());
                            }
                            r.setCar(null);
                        }
                    }
                    em.remove(user);
                    em.getTransaction().commit();
                }catch (RuntimeException e){
                    return null;
                }finally {
                    em.close();
                }
                return user;
            }
        }
        return null;
    }

    @Override
    public Object createReservation(Long parkingSpotId, Long carId) {
        if(parkingSpotId != null && carId != null){
            EntityManager em = emf.createEntityManager();
            Car car = em.find(Car.class, carId);
            ParkingSpot parkingSpot = em.find(ParkingSpot.class, parkingSpotId);
            if(car != null && parkingSpot != null){
                for(Reservation r : car.getReservations()){
                    if(r.getKoniec() == null){
                        return null;
                    }
                }
                for(Reservation r : parkingSpot.getReservations()){
                    if(r.getKoniec() == null){
                        return null;
                    }
                }
                Reservation reservation = new Reservation();
                reservation.setCar(car);
                reservation.setParkingSpot(parkingSpot);
                reservation.setZaciatok(new Date());
                em.getTransaction().begin();
                try{
                    em.persist(reservation);
                    car.getReservations().add(reservation);
                    parkingSpot.getReservations().add(reservation);
                    parkingSpot.setFree(false);
                    em.merge(car);
                    em.merge(parkingSpot);
                    em.getTransaction().commit();
                }catch (RuntimeException e){
                    return null;
                }finally {
                    em.close();
                }
                return reservation;
            }
        }
        return null;
    }

    @Override
    public Object endReservation(Long reservationId) {
        if(reservationId != null){
            EntityManager em = emf.createEntityManager();
            Reservation reservation = em.find(Reservation.class, reservationId);
            if(reservation != null){
                if(reservation.getKoniec() == null){
                    Integer pricePerHour = null;
                    TypedQuery<CarPark> q = em.createQuery("select cp from ParkingSpot ps " +
                            "join CarParkFloor cpf on cpf.id = ps.carParkFloor.id " +
                            "join CarPark cp on cp.id = cpf.carPark.id where ps.id = '"+reservation.getParkingSpot().getId()+"'", CarPark.class);
                    try {
                        pricePerHour = q.getSingleResult().getCenaZaHodinu();
                    }catch (NoResultException e){
                        return null;
                    }
                    if(pricePerHour != null){
                        em.getTransaction().begin();
                        try{
                            reservation.setKoniec(new Date());
                            Long diff = reservation.getKoniec().getTime() - reservation.getZaciatok().getTime();
                            Long diffHours = diff / (60 * 60 * 1000);
                            Double fullPrice = Double.valueOf(pricePerHour * (diffHours.intValue() + 1));

                            TypedQuery<Holiday> holidayQ = em.createQuery("select h from Holiday h", Holiday.class);
                            List<Holiday> holidays = holidayQ.getResultList();
                            List<Holiday> reservationHolidays = new ArrayList<>();
                            Double discount = 0D;
                            for(Holiday h : holidays){
                                Calendar holidayDate = Calendar.getInstance();
                                holidayDate.setTime(h.getDatum());
                                Calendar start = Calendar.getInstance();
                                start.setTime(reservation.getZaciatok());
                                Calendar end = Calendar.getInstance();
                                end.setTime(reservation.getKoniec());
                                if((start.get(Calendar.MONTH) < holidayDate.get(Calendar.MONTH) && end.get(Calendar.MONTH) > holidayDate.get(Calendar.MONTH)) ||
                                        (start.get(Calendar.MONTH) == holidayDate.get(Calendar.MONTH) && start.get(Calendar.DAY_OF_MONTH) <= holidayDate.get(Calendar.DAY_OF_MONTH)) ||
                                        (end.get(Calendar.MONTH) == holidayDate.get(Calendar.MONTH) && end.get(Calendar.DAY_OF_MONTH) <= holidayDate.get(Calendar.DAY_OF_MONTH))){
                                    reservationHolidays.add(h);
                                    h.getReservations().add(reservation);
                                    em.merge(h);
                                    if((start.get(Calendar.MONTH) == end.get(Calendar.MONTH) && start.get(Calendar.DAY_OF_MONTH) == end.get(Calendar.DAY_OF_MONTH)) &&
                                            start.get(Calendar.MONTH) == holidayDate.get(Calendar.MONTH) && start.get(Calendar.DAY_OF_MONTH) == holidayDate.get(Calendar.DAY_OF_MONTH)){
                                        if(end.get(Calendar.MINUTE) > start.get(Calendar.MINUTE) || (end.get(Calendar.MINUTE) == start.get(Calendar.MINUTE) && end.get(Calendar.SECOND) > start.get(Calendar.SECOND))){
                                            discount += (end.get(Calendar.HOUR_OF_DAY) - start.get(Calendar.HOUR_OF_DAY) + 1) * pricePerHour * 0.25;
                                        }else{
                                            discount += (end.get(Calendar.HOUR_OF_DAY) - start.get(Calendar.HOUR_OF_DAY)) * pricePerHour * 0.25;
                                        }
                                    }else if(end.get(Calendar.MONTH) == holidayDate.get(Calendar.MONTH) && end.get(Calendar.DAY_OF_MONTH) == holidayDate.get(Calendar.DAY_OF_MONTH)) {
                                        if(end.get(Calendar.MINUTE) > start.get(Calendar.MINUTE) || (end.get(Calendar.MINUTE) == start.get(Calendar.MINUTE) && end.get(Calendar.SECOND) > start.get(Calendar.SECOND))){
                                            discount += end.get(Calendar.HOUR_OF_DAY) * pricePerHour * 0.25;
                                        }else{
                                            discount += (end.get(Calendar.HOUR_OF_DAY) - 1) * pricePerHour * 0.25;
                                        }
                                    }
                                    else if(start.get(Calendar.MONTH) == holidayDate.get(Calendar.MONTH) && start.get(Calendar.DAY_OF_MONTH) == holidayDate.get(Calendar.DAY_OF_MONTH)){
                                        if(start.get(Calendar.MINUTE) > 0 || start.get(Calendar.SECOND) > 0){
                                            discount += (24 - start.get(Calendar.HOUR_OF_DAY) - 1) * pricePerHour * 0.25;
                                        }else{
                                            discount += (24 - start.get(Calendar.HOUR_OF_DAY)) * pricePerHour * 0.25;
                                        }
                                    }else{
                                        if(start.get(Calendar.MINUTE) > 0 || start.get(Calendar.SECOND) > 0){
                                            discount += 23 * pricePerHour * 0.25;
                                        }else{
                                            discount += 24 * pricePerHour * 0.25;
                                        }

                                    }
                                }
                            }
                            reservation.setSviatky(reservationHolidays);
                            fullPrice -= discount;
                            reservation.setCena(fullPrice);
                            ParkingSpot parkingSpot = reservation.getParkingSpot();
                            parkingSpot.setFree(true);
                            em.merge(parkingSpot);
                            em.merge(reservation);
                            em.getTransaction().commit();
                        }catch (RuntimeException e){
                            return null;
                        }finally {
                            em.close();
                        }
                        return reservation;
                    }
                }

            }
        }
        return null;
    }

    public List<Object> getAllReservations(){
        EntityManager em = emf.createEntityManager();
        TypedQuery<Reservation> query = em.createQuery("select r from Reservation r", Reservation.class);
        return new ArrayList<>(query.getResultList());
    }

    public Object getReservation(Long reservationId){
        if(reservationId != null){
            EntityManager em = emf.createEntityManager();
            return em.find(Reservation.class, reservationId);
        }
        return null;
    }

    @Override
    public List<Object> getReservations(Long parkingSpotId, Date date) {
        if(parkingSpotId != null && date != null){
            EntityManager em = emf.createEntityManager();
            ParkingSpot parkingSpot = em.find(ParkingSpot.class, parkingSpotId);
            if(parkingSpot != null){
                List<Object> reservationList = new ArrayList<>();
                for(Reservation r : parkingSpot.getReservations()){
                    SimpleDateFormat fmt = new SimpleDateFormat("yyyyMMdd");
                    if(fmt.format(r.getZaciatok()).equals(fmt.format(date))){
                        reservationList.add(r);
                    }
                }
                return reservationList;
            }
        }
        return null;
    }

    @Override
    public List<Object> getMyReservations(Long userId) {
        if(userId != null){
            EntityManager em = emf.createEntityManager();
            User user = em.find(User.class, userId);
            if(user != null){
                List<Object> reservationList = new ArrayList<>();
                for(Car c : user.getAuta()){
                    for(Reservation r : c.getReservations()){
                        if(r.getKoniec() == null){
                            reservationList.add(r);
                        }
                    }
                }
                return reservationList;
            }
        }
        return null;
    }

    @Override
    public Object updateReservation(Object reservation) {
        if(reservation instanceof Reservation){
            if(((Reservation) reservation).getId() != null){
                EntityManager em = emf.createEntityManager();
                Reservation updatedReservation = em.find(Reservation.class, ((Reservation) reservation).getId());
                if(updatedReservation != null){
                    if(updatedReservation.getKoniec() == null){
                        em.getTransaction().begin();
                        try {
                            if(((Reservation) reservation).getZaciatok() != null){
                                updatedReservation.setZaciatok(((Reservation) reservation).getZaciatok());
                                em.merge(updatedReservation);
                            }
                            em.getTransaction().commit();
                        } catch (RuntimeException e) {
                            return null;
                        } finally {
                            em.close();
                        }
                        return updatedReservation;
                    }
                }
            }
        }
        return null;
    }

    @Override
    public Object createHoliday(String name, Date date) {
        if(date != null){
            if(getHoliday(date) == null){
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(date);
                calendar.set(Calendar.YEAR, 2022);
                date = calendar.getTime();

                Holiday holiday = new Holiday(name, date);
                EntityManager em = emf.createEntityManager();
                em.getTransaction().begin();
                try {
                    em.persist(holiday);
                    em.getTransaction().commit();
                } catch (RuntimeException e) {
                    return null;
                } finally {
                    em.close();
                }
                return holiday;
            }
        }
        return null;
    }

    @Override
    public Object getHoliday(Date date) {
        if(date != null){
            EntityManager em = emf.createEntityManager();
            TypedQuery<Holiday> q = em.createQuery("select h from Holiday h", Holiday.class);
            List<Holiday> holidays;
            try{
                holidays = q.getResultList();
            }catch (RuntimeException e){
                return null;
            }
            SimpleDateFormat fmt = new SimpleDateFormat("yyyyMMdd");
            for(Holiday h : holidays){
                if(fmt.format(h.getDatum()).equals(fmt.format(date))){
                    return h;
                }
            }
        }
        return null;
    }

    @Override
    public List<Object> getHolidays() {
        EntityManager em = emf.createEntityManager();
        TypedQuery<Holiday> q = em.createQuery("select h from Holiday h", Holiday.class);
        return new ArrayList<>(q.getResultList());
    }

    @Override
    public Object deleteHoliday(Long holidayId) {
        if(holidayId != null){
            EntityManager em = emf.createEntityManager();
            Holiday holiday = em.find(Holiday.class, holidayId);
            if(holiday != null){
                em.getTransaction().begin();
                try{
                    em.remove(holiday);
                    em.getTransaction().commit();
                }catch (RuntimeException e){
                    return null;
                }finally {
                    em.close();
                }
                return holiday;
            }
        }
        return null;
    }
}
