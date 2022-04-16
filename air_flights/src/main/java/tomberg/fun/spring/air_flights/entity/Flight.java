package tomberg.fun.spring.air_flights.entity;

import javax.persistence.*;
import java.sql.Date;

@Entity
@Table(name = "flights")
public class Flight {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Column
    private Date arrive_date;

    @Column
    private Date return_date;

    @ManyToOne
    @JoinColumn(name = "plane_id")
    private Plane plane;

    public Plane getPlane() {
        return plane;
    }

    public User getUser() {
        return user;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Date getArrive_date() {
        return arrive_date;
    }

    public void setArrive_date(Date arrive_date) {
        this.arrive_date = arrive_date;
    }

    public Date getReturn_date() {
        return return_date;
    }

    public void setReturn_date(Date return_date) {
        this.return_date = return_date;
    }

    public void setPlane(Plane plane) {
        this.plane = plane;
    }
}