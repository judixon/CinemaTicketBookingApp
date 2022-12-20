package com.example.cinematicketbookingapp.model;

import com.example.cinematicketbookingapp.validation.Name;
import com.example.cinematicketbookingapp.validation.OnePartSurname;
import com.example.cinematicketbookingapp.validation.TwoPartSurname;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OptimisticLock;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class Reservation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "screening_id")
    private Screening screening;

    @ManyToMany
    @OptimisticLock(excluded = false)
    @JoinTable(
            name = "seat_reservation",
            joinColumns = @JoinColumn(name = "reservation_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "seat_id", referencedColumnName = "id")
    )
    @NotEmpty
    private Set<Seat> seats;

    @NotNull
    @Name(message = "Name should be at least 3 characters long and start with capital letter")
    private String ownerName;

    @NotNull
    @OnePartSurname(message = "Surname should be at least 3 characters long and start with capital letter")
    @TwoPartSurname(message = "Both parts of surname should be at least 3 characters long and start with capital letter")
    private String ownerSurname;

    private LocalDateTime creationDateTime;
}
