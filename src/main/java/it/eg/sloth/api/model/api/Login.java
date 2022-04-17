package it.eg.sloth.api.model.api;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Login {

    private String userid;
    private String password;

}
