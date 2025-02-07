package com.bal.demo_reddit.security;

import com.bal.demo_reddit.exceptions.SpringRedditException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import jakarta.annotation.PostConstruct;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;
import java.io.IOException;
import java.io.InputStream;
import java.security.*;
import java.security.cert.CertificateException;

import static io.jsonwebtoken.Jwts.parser;


@Service
public class JwtProvider {



    private KeyStore keyStore;

    @PostConstruct
    public void init() {
        try {
            keyStore = KeyStore.getInstance("JKS");
            InputStream resourceAsStream = getClass().getClassLoader().getResourceAsStream("springblog.jks");
            keyStore.load(resourceAsStream, "secret".toCharArray());
        } catch (KeyStoreException | CertificateException | NoSuchAlgorithmException | IOException e) {
            throw new SpringRedditException("Exception occurred while loading keystore.");
        }
    }

    public String generateToken(Authentication authentication) {
        User principal = (User) authentication.getPrincipal();
        return Jwts.builder()
                .setSubject(principal.getUsername())
                .signWith(getPrivateKey())
                .compact();
    }

    private PrivateKey getPrivateKey() {
        try {
            return (PrivateKey) keyStore.getKey("springblog", "secret".toCharArray());
        } catch (KeyStoreException | NoSuchAlgorithmException | UnrecoverableKeyException e) {
            throw new SpringRedditException("Exception occurred while retrieving public key from keystore");
        }
    }

    public boolean validateToken(String token) {
        try {
            JwtParser jwtParser = Jwts.parserBuilder()
                    .setSigningKey(getPublicKey())
                    .build();

            jwtParser.parseClaimsJws(token);
            return true;
        } catch (Exception e) {
           throw new SpringRedditException("Exception occurred while validation token.");
        }
    }

    private PublicKey getPublicKey() {
        try {
            return keyStore.getCertificate("springblog").getPublicKey();
        } catch (KeyStoreException e) {
            throw new SpringRedditException("Exception occurred while retrieving public key from the keystore");
        }
    }

    public String getUserNameFromJwt(String token) {
        JwtParser jwtParser = Jwts.parserBuilder()
                .setSigningKey(getPublicKey())
                .build();

        Claims claims = jwtParser.parseClaimsJws(token).getBody();
        return claims.getSubject();
    }


}
