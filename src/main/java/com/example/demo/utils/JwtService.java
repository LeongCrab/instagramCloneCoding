package com.example.demo.utils;


import com.example.demo.common.exceptions.BaseException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

import static com.example.demo.common.response.BaseResponseStatus.EMPTY_JWT;
import static com.example.demo.common.response.BaseResponseStatus.INVALID_JWT;

@Service
public class JwtService {

    @Value("${jwt.secret-key}")
    private String JWT_SECRET_KEY;

    /*
    JWT 생성
    @param userId
    @return String
     */
    public String createJwt(Long id){
        Date now = new Date();
        return Jwts.builder()
                .setHeaderParam("type","jwt")
                .claim("id",id)
                .setIssuedAt(now)
                .setExpiration(new Date(System.currentTimeMillis()+1*(1000*60*60*24)))
                .signWith(SignatureAlgorithm.HS256, JWT_SECRET_KEY)
                .compact();
    }

    /*
    Header에서 Authorization 으로 JWT 추출
    @return String
     */
    public String getJwt(){
        try {
            HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
            return request.getHeader("Authorization").split(" ")[1];
        } catch (Exception exception) {
            throw new BaseException(EMPTY_JWT);
        }
    }

    /*
    JWT에서 id 추출
    @return Long
    @throws BaseException
     */
    public Long getId() throws BaseException{
        //1. JWT 추출
        String accessToken = getJwt();

        // 2. JWT parsing
        Jws<Claims> claims;
        try{
            claims = Jwts.parser()
                    .setSigningKey(JWT_SECRET_KEY)
                    .parseClaimsJws(accessToken);
        } catch (Exception ignored) {
            throw new BaseException(INVALID_JWT);
        }

        // 3. id 추출
        return claims.getBody().get("id",Long.class);
    }

}
