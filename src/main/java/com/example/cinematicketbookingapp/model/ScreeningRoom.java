package com.example.cinematicketbookingapp.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Set;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
@Data
public class ScreeningRoom {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToMany(mappedBy = "screeningRoom")
    private Set<Screening> screenings;

    @OneToMany(mappedBy = "screeningRoom")
    private Set<Seat> seats;
}
