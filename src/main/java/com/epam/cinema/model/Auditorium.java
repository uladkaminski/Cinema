package com.epam.cinema.model;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "AUDITORIUMS")
@Access(AccessType.FIELD)
@SequenceGenerator(name = "auditorium_sequence", initialValue = 100, allocationSize = 100)
@NamedQueries({
        @NamedQuery(name = "findAllAuditoriums", query = "SELECT a from Auditorium a")
})
public class Auditorium {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "auditorium_sequence")
    private Long id;
    @Column(name = "name")
    private String name;
    @OneToMany(mappedBy = "auditorium", cascade=CascadeType.ALL)
    private List<Seat> seats;

    public Auditorium() {
    }

    public Auditorium(Long id, String name, List<Seat> seats) {
        this.id = id;
        this.name = name;
        this.seats = seats;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Seat> getSeats() {
        return seats;
    }

    public void setSeats(List<Seat> seats) {
        this.seats = seats;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Auditorium that = (Auditorium) o;

        if (id != null ? !id.equals(that.id) : that.id != null) return false;
        if (name != null ? !name.equals(that.name) : that.name != null) return false;
        return seats != null ? seats.equals(that.seats) : that.seats == null;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (seats != null ? seats.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Auditorium{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", seats=" + seats +
                '}';
    }
}
