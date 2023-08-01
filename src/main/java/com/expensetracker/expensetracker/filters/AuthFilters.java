package com.expensetracker.expensetracker.filters;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
import org.springframework.web.filter.GenericFilterBean;

import com.expensetracker.Constants;

import java.io.IOException;

public class AuthFilters extends GenericFilterBean {
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) servletRequest;
        HttpServletResponse httpResponse = (HttpServletResponse) servletResponse;

        String authHeader = httpRequest.getHeader("Authorization");
        if(authHeader != null) {
            String[] authHeaderArr = authHeader.split("Bearer ");
            if(authHeaderArr.length > 1 && authHeaderArr[1] != null) {
                String token = authHeaderArr[1];
                System.out.println("token " + token);
                try {
                    System.out.println("Body  "+Jwts.parser().setSigningKey(Constants.API_SECRET_KEY)
                            .parseClaimsJws(token).getBody());
                    Claims claims = Jwts.parser().setSigningKey(Constants.API_SECRET_KEY)
                            .parseClaimsJws(token).getBody();
                            System.out.println("Claims" +claims);
                            
                            System.out.println("User Id : "+claims.get("user_id"));
                            System.out.println("User First Name" + claims.get("firstName"));
                            System.out.println("User Last Name : "+ claims.get("lastName"));
                    // httpRequest.setAttribute("userId", Integer.parseInt(claims.get("user_id").toString()));
                    Object userIdObj = claims.get("user_id");
                    if (userIdObj != null && userIdObj instanceof Integer) {
                        int userId = (int) userIdObj;
                        httpRequest.setAttribute("user_id", userId);
                    } else {
                        httpResponse.sendError(HttpStatus.FORBIDDEN.value(), "Invalid token: Missing or invalid userId claim");
                        return;
                    }
                }catch (Exception e) {
                    System.out.println("Exception Occured" + e);
                    httpResponse.sendError(HttpStatus.FORBIDDEN.value(), "invalid/expired token");
                    return;
                }
            } else {
                httpResponse.sendError(HttpStatus.FORBIDDEN.value(), "Authorization token must be Bearer [token]");
                return;
            }
        } else {
            httpResponse.sendError(HttpStatus.FORBIDDEN.value(), "Authorization token must be provided");
            return;
        }
        filterChain.doFilter(servletRequest, servletResponse);
    }
}