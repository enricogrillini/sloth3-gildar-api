package it.eg.sloth.api.controller;

import it.eg.sloth.api.model.api.Jwt;
import it.eg.sloth.api.model.api.Login;
import it.eg.sloth.core.token.JwtUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.PrivateKey;

@RestController
@RequestMapping("/api/v1/security")
@Slf4j
public class SecurityController implements SecurityApi, InitializingBean {

    @Value("${security.private_key_jwt}")
    String certificatePath;

    PrivateKey privateKey;

    @Override
    public void afterPropertiesSet() throws Exception {
        privateKey = JwtUtil.getPrivateKey(certificatePath);
    }

    @Override
    @PostMapping(path = "/login", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Jwt login(Login login) {
        Jwt jwt = new Jwt();
        jwt.setToken(JwtUtil.createJWT("GildarApi.Security", login.getUserid(), "GildarApi", (long) 3600 * 100, null, privateKey));

        return jwt;
    }


}




