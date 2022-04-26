package tomberg.fun.spring.air_flights.entity;

import org.springframework.lang.Nullable;

import javax.persistence.*;
import java.sql.Time;
import java.time.LocalDate;

@Entity
@Table(name = "self_flight")
public class SelfFlight {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Nullable
    private LocalDate depDate;

    @Nullable
    private LocalDate arrDate;

    @Nullable
    private Time depTime;

    @Nullable
    private Time arrTime;

    @ManyToOne
    @JoinColumn(name = "route_id")
    @Nullable
    private Route route;

    @ManyToOne
    @JoinColumn(name = "user_id")
    @Nullable
    private User user;

    @ManyToOne
    @JoinColumn(name = "user_info_id")
    @Nullable
    private UserInfo userInfo;

    @ManyToOne
    @JoinColumn(name = "place_id")
    @Nullable
    private Place place;

    @ManyToOne
    @JoinColumn(name = "baggage_id")
    @Nullable
    private Baggage baggage;

    private int finalCost;

    @Nullable
    private boolean paid;

    public Baggage getBaggage() {
        return baggage;
    }

    public Place getPlace() {
        return place;
    }

    public Route getRoute() {
        return route;
    }

    public UserInfo getUserInfo() {
        return userInfo;
    }

    public User getUser() {
        return user;
    }

    public Integer getId() {
        return id;
    }

    public LocalDate getDepDate() {
        return depDate;
    }

    public void setDepDate(LocalDate depDate) {
        this.depDate = depDate;
    }

    public LocalDate getArrDate() {
        return arrDate;
    }

    public void setArrDate(LocalDate arrDate) {
        this.arrDate = arrDate;
    }

    public Time getDepTime() {
        return depTime;
    }

    public void setDepTime(Time depTime) {
        this.depTime = depTime;
    }

    public Time getArrTime() {
        return arrTime;
    }

    public void setArrTime(Time arrTime) {
        this.arrTime = arrTime;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setUserInfo(UserInfo userInfo) {
        this.userInfo = userInfo;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setRoute(@Nullable Route route) {
        this.route = route;
    }

    public boolean isPaid() {
        return paid;
    }

    public void setPaid(boolean paid) {
        this.paid = paid;
    }

    public void setPlace(@Nullable Place place) {
        this.place = place;
    }

    public void setBaggage(@Nullable Baggage baggage) {
        this.baggage = baggage;
    }

    public int getFinalCost() {
        return finalCost;
    }

    public void setFinalCost(int finalCost) {
        this.finalCost = finalCost;
    }

    public String datesFlight() {
        return depDate + " → " + arrDate;
    }

    public String timesFlight() {
        return depTime + " - " + arrTime;
    }

    public String costFlight() {
        return finalCost + " $";
    }
}