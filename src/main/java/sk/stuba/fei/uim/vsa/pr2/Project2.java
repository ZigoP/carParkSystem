package sk.stuba.fei.uim.vsa.pr2;


import jakarta.ws.rs.core.Application;
import jakarta.ws.rs.core.UriBuilder;
import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;

import sk.stuba.fei.uim.vsa.pr2.service.CarParkService;

import java.net.URI;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;


public class Project2 {

    public static final Logger LOGGER = Logger.getLogger(Project2.class.getName());
    public static final String BASE_URI = "http://localhost/";
    public static final int PORT = 8080;
    public static final Class<? extends Application> APPLICATION_CLASS = Project2Application.class;

    public static void main(String[] args) {
        try {
            final HttpServer server = startServer();
            Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                try {
                    System.out.println("Shutting down the application...");
                    server.shutdownNow();
                    System.out.println("Exiting");
                } catch (Exception e) {
                    LOGGER.log(Level.SEVERE, null, e);
                }
            }));
            System.out.println("Last steps of setting up the application...");
            postStart();
            System.out.println(String.format("Application started.%nStop the application using CRL+C"));
            Thread.currentThread().join();
        } catch (InterruptedException e) {
            LOGGER.log(Level.SEVERE, null, e);
        }
    }

    public static HttpServer startServer() {
        final ResourceConfig config = ResourceConfig.forApplicationClass(APPLICATION_CLASS);
        URI baseUri = UriBuilder.fromUri(BASE_URI).port(PORT).build();
        LOGGER.info("Starting Grizzly2 HTTP server...");
        LOGGER.info("Server listening on " + BASE_URI + ":" + PORT);
        return GrizzlyHttpServerFactory.createHttpServer(baseUri, config);
    }

    public static void postStart() {
        // TODO sem napíš akékoľvek nastavenia, či volania, ktoré sa majú udiať ihneď po štarte
        CarParkService carParkService = new CarParkService();
        Object o;

        o = carParkService.createUser("admin", "admin", "admin@vsa.sk");

        /*o = carParkService.createUser("Na","Ta", "a@b.com");
        System.out.println(o);
        o = carParkService.createUser("PaT","Rik", "d@e.com");
        System.out.println(o);

        o = carParkService.createCar(2L, "Audi", "R8", "black", "SA456BB");
        System.out.println(o);
        o = carParkService.createCar(2L, "Mazda", "rx6", "red", "GA123GG");
        System.out.println(o);
        o = carParkService.createCar(3L, "BMW", "X6", "red", "BM111WW");
        System.out.println(o);

        Date date = new Date(1617660000000L);
        o = carParkService.createHoliday("dnes 2021", date);
        System.out.println(o);
        o = carParkService.createHoliday("dnes", new Date());
        System.out.println(o);
        date = new Date(1648936800000L);
        o = carParkService.createHoliday("predvcerou", date);
        System.out.println(o);

        o = carParkService.createCarPark("newPark", "Galanta 123", 5);
        System.out.println(o);
        o = carParkService.createCarPark("Park", "Sala 456", 99);
        System.out.println(o);

        o = carParkService.createCarParkFloor(10L, "FirstFloor");
        System.out.println(o);
        o = carParkService.createCarParkFloor(10L, "SecondFloor");
        System.out.println(o);
        o = carParkService.createCarParkFloor(10L, "3rd");
        System.out.println(o);
        o = carParkService.createCarParkFloor(11L, "1st");
        System.out.println(o);
        o = carParkService.createCarParkFloor(11L, "SecondFloor");
        System.out.println(o);

        o = carParkService.createParkingSpot(10L,"FirstFloor", "P1");
        System.out.println(o);
        o = carParkService.createParkingSpot(10L,"FirstFloor", "P2");
        System.out.println(o);
        o = carParkService.createParkingSpot(10L,"3rd", "P4");
        System.out.println(o);
        o = carParkService.createParkingSpot(11L,"1st", "P10");
        System.out.println(o);
        o = carParkService.createParkingSpot(11L,"1st", "P11");
        System.out.println(o);


        o = carParkService.createReservation(17L,4L);
        System.out.println(o);
        o = carParkService.createReservation(19L,5L);
        System.out.println(o);
        o = carParkService.createReservation(18L,6L);
        System.out.println(o);*/
    }

}
