package sk.stuba.fei.uim.vsa.pr2.web.response;

public abstract class Dto {
    private Long id;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Dto() {
    }

    public Dto(Long id) {
        this.id = id;
    }
}
