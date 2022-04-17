package it.eg.sloth.api.model.prova;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Car {

    private String make;
    private int numberOfSeats;
    private String type;
    private LocalDateTime date;
    private String flag;
    private LocalDate localDate;
}
