package hu.me.domain;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name="_4_Time")
public class Time {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private LocalDate time_date;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "time_date")
    private List<Seat> seats;
    @ManyToOne
    private Movie movie;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getTime() {
        return time_date;
    }

    public void setTime(LocalDate time) {
        this.time_date = time;
    }
}
