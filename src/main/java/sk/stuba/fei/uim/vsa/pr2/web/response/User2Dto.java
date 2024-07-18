package sk.stuba.fei.uim.vsa.pr2.web.response;

import java.util.Objects;

public class User2Dto extends Dto{

    private String firstName;
    private String lastName;
    private String email;

    public User2Dto() {
    }

    public User2Dto(Long id, String firstName, String lastName, String email) {
        super(id);
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User2Dto user2Dto = (User2Dto) o;
        return email.equals(user2Dto.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(email);
    }
}
