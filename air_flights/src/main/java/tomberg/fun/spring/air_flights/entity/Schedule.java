package tomberg.fun.spring.air_flights.entity;

import org.springframework.lang.Nullable;
import tomberg.fun.spring.air_flights.entity.time.Day;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "schedule")
public class Schedule {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "route_id")
    private Route route;

    @Nullable
    @ManyToMany
    private Set<Day> periodical;

    public Schedule() {
    }

    public Route getRoute() {
        return route;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setRoute(Route route) {
        this.route = route;
    }

    public Set<Day> getPeriodical() {
        return periodical;
    }

    public void setPeriodical(Set<Day> periodical) {
        this.periodical = periodical;
    }
}