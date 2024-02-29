package com.cdd.config.filter;

import com.cdd.mapper.UserMapper;
import com.cdd.pojo.User;
import com.cdd.utils.JwtUtils;
import com.cdd.utils.UserDetailsImpl;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.security.SignatureException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Configuration
public class JwtAuthenticationTokenFilter extends OncePerRequestFilter {
    @Autowired
    UserMapper userMapper;
    @Autowired
    JwtUtils jwtUtils;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String token = request.getHeader("Authorization");
        if(token == null || !token.startsWith("Bearer ")) { // 没有 token 就不进行以下验证
            filterChain.doFilter(request, response);
            return;
        }
        token = token.substring(7);
        int userId;
        try {
            userId = Integer.parseInt(jwtUtils.getPayloadByToken(token).getSubject());
        } catch (SignatureException e) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid token");
            return;
        } catch (ExpiredJwtException e) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Token expired");
            return;
        }
        User user = userMapper.selectById(userId);
        if(user == null) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "User not found in jwt");
            return;
        }
        UserDetailsImpl userDetails = new UserDetailsImpl(user);
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userDetails, null, null);
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);

        filterChain.doFilter(request, response);
    }
}
