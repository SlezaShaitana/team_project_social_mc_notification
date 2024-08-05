package com.social.mcnotification.security.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.SignatureException;
import java.util.List;
import java.util.function.Function;

@Component
public class JwtUtils {

    public String getId(String token){
        return Jwts.parser().build()
                .parseSignedClaims(token)
                .getPayload()
                .getId();
    }

    public String getEmail(String token){
        return Jwts.parser()
                .build().parseSignedClaims(token)
                .getPayload()
                .get("email", String.class);
    }

    public List<String> getRoles(String token){
        return Jwts.parser().build()
                .parseSignedClaims(token)
                .getPayload()
                .get("roles", List.class);

    }

    public static Claims getPayload(String token) {
            return Jwts.parser().build()
                    .parseSignedClaims(token)
                    .getPayload();
    }

}
