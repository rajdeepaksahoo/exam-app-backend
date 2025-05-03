package com.online.exam.config.jwt;

import com.online.exam.config.customUserDetails.CustomUserDetails;
import com.online.exam.exception.InvalidUsernameException;
import com.online.exam.model.UserModel;
import com.online.exam.repository.UserModelRepository;
import io.jsonwebtoken.*;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

@Configuration
@RequiredArgsConstructor
public class JwtConfig {
    public static Long TOKEN_VALIDITY = (long) (5*60*60);
    private static String SECRET_KEY = "afafasfafafasfasfasfafacasdasfasxASFACASDFACASDFASFASFDAFASFASDAADSCSDFADCVSGCFVADXCcadwavfsfarvf";
    private final UserModelRepository userModelRepository;
    private PasswordEncoder passwordEncoder;

    @Autowired
    public void setPasswordEncoder(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    //Get Username From Token
    public String getUsernameFromToken(String token){
        String userName = getAllClaims(token, Claims::getSubject);
        return userName;
    }
    //Get Expiration From Token
    public Date getExpirationFromToken(String token){
        return getAllClaims(token,Claims::getExpiration);
    }
    public static <T> T getAllClaims(String token, Function<Claims, T> claimResolver) {
        Claims claims =  Jwts.parser()
                .setSigningKey(SECRET_KEY)
                .parseClaimsJws(token).getBody();
        System.out.println(claims);
        return claimResolver.apply(claims);
    }

    public String generateToken(CustomUserDetails details){
        Optional<UserModel> byUsername = userModelRepository.findByUsername(details.getUsername());
        if(byUsername.isPresent()){
            if(passwordEncoder.matches(details.getPassword(), byUsername.get().getPassword())){
                Map<String,Object> claims= new HashMap<>();
                System.out.println(details);
                System.out.println(details.getUsername());
                return doGenerateToken(claims,details.getUsername());
            }else{
                throw new BadCredentialsException("Incorrect Password !!!");
            }
        }else{
            throw new InvalidUsernameException("Invalid Username");
        }

    }

    private String doGenerateToken(Map<String,Object> claims,String subject) {
        return Jwts
                .builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis()+TOKEN_VALIDITY * 1000))
                .signWith(SignatureAlgorithm.HS512, SECRET_KEY)
                .compact();
    }
    public boolean isTokenExpired(String token){
        return getExpirationFromToken(token).before(new Date(System.currentTimeMillis()));
    }
    public boolean validateToken(String token,UserDetails details){
        String usernameFromToken = getUsernameFromToken(token);
        return (!isTokenExpired(token) && details.getUsername().equals(usernameFromToken));
    }
}
