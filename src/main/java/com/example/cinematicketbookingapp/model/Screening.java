package com.example.cinematicketbookingapp.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Data
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
public class Screening {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Movie movie;

    private LocalDateTime startDateTime;

    private LocalDateTime endDateTime;

    @ManyToOne
    @JoinColumn(name = "screening_room_id")
    private ScreeningRoom screeningRoom;

    @OneToMany(mappedBy = "screening")
    private Set<Reservation> reservations;
}
