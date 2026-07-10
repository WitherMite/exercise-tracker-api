package withermite.exercise_tracker_api._util.jwt;

import java.io.IOException;

import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.auth0.jwt.interfaces.DecodedJWT;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import withermite.exercise_tracker_api.login.AuthService;

@Component
public class JwtRequestFilter extends OncePerRequestFilter {
    private final AuthService service;
    private final JwtUtils jwtUtils;

    public JwtRequestFilter(AuthService service, JwtUtils jwtUtils) {
        this.service = service;
        this.jwtUtils = jwtUtils;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        // look for Bearer auth header
        final String header = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (header == null || !header.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        final String token = header.substring(7);
        final DecodedJWT jwt = jwtUtils.decodeToken(token);
        if (jwt == null || jwt.getSubject() == null) {
            // validation failed or token expired
            filterChain.doFilter(request, response);
            return;
        }

        final UserDetails userDetails;
        try {
            userDetails = service.loadUserByUsername(jwt.getSubject());
        } catch (final UsernameNotFoundException userNotFoundEx) {
            // user not found
            filterChain.doFilter(request, response);
            return;
        }

        final UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                userDetails,
                null,
                userDetails.getAuthorities());

        authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        // set user details on spring security context
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // continue with authenticated user
        filterChain.doFilter(request, response);
    }
}
