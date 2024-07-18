package sk.stuba.fei.uim.vsa.pr2.web;

import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerRequestFilter;
import jakarta.ws.rs.container.PreMatching;
import jakarta.ws.rs.core.HttpHeaders;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.Provider;
import sk.stuba.fei.uim.vsa.pr2.entities.User;
import sk.stuba.fei.uim.vsa.pr2.service.CarParkService;

import java.io.IOException;
import java.util.Base64;

@PreMatching
@Provider
public class BasicAuthRequestFilter implements ContainerRequestFilter {
    private final CarParkService carParkService = new CarParkService();

    @Override
    public void filter(ContainerRequestContext ctx) throws IOException {
        String auth = ctx.getHeaderString(HttpHeaders.AUTHORIZATION);
        if(auth == null || !checkAuth(auth)) {
            ctx.abortWith(Response.status(Response.Status.UNAUTHORIZED).build());
        }
    }

    private String getEmail(String decoded) {
        return decoded.split(":")[0];
    }

    private Long getId(String decoded) {
        return Long.parseLong(decoded.split(":")[1]);
    }

    private boolean checkAuth(String authHeader){
        String base64Encoded = authHeader.substring("Basic ".length());
        String decoded = new String(Base64.getDecoder().decode(base64Encoded));
        Object o = carParkService.getUser(getEmail(decoded));
        if(o != null){
            User user = (User) o;
            return user.getId().equals(getId(decoded));
        }
        return false;
    }



    public static String getEmailForReservation(String authHeader){
        String base64Encoded = authHeader.substring("Basic ".length());
        String decoded = new String(Base64.getDecoder().decode(base64Encoded));
        return decoded.split(":")[0];
    }
}
