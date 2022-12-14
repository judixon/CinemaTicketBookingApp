package com.example.cinematicketbookingapp.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Set;

@Entity
@Data
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
public class Seat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int rowNumber;

    private int seatNumber;

    @ManyToOne
    @JoinColumn(name="screening_room_id")
    private ScreeningRoom screeningRoom;

    @ManyToMany
    @JoinTable(
            name="seat_reservation",
            joinColumns = @JoinColumn(name = "seat_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name="reservation_id", referencedColumnName = "id")
    )
    private Set<Reservation> reservations;
}
