package it.eg.sloth.api.model.prova;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;
import java.util.Date;

@Data
@NoArgsConstructor
public class CarBff {
 
    private String make;
    private int seatCount;
    private String type;
    private OffsetDateTime date;
    private Boolean flag;
}