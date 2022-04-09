package it.eg.sloth.api.model;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Login {

    private String userid;
    private String password;

}
