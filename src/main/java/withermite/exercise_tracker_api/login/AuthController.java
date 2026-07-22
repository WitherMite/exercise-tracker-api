package withermite.exercise_tracker_api.login;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import withermite.exercise_tracker_api._util.jwt.JwtUtils;

@RestController
@RequestMapping("/auth")
public class AuthController {
    private final AuthenticationManager authManager;
    private final AuthService service;
    private final JwtUtils jwtUtils;

    public record ResponseToken(String accessToken) {
    }

    public AuthController(AuthenticationManager authManager, AuthService service, JwtUtils jwtUtils) {
        this.authManager = authManager;
        this.service = service;
        this.jwtUtils = jwtUtils;
    }

    @PostMapping
    public ResponseEntity<Object> authenticate(@Valid @RequestBody AuthRequest req) {
        try {
            // run to check that credentials exist,
            // but dont populate token with the return value
            // as it sanitizes the password hash off the returned authentication
            authManager.authenticate(new UsernamePasswordAuthenticationToken(req.username, req.password));
        } catch (BadCredentialsException e) {
            ProblemDetail problem = ProblemDetail.forStatus(HttpStatus.UNAUTHORIZED);
            System.err.println(e);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(problem);
        }

        UserDetails user = service.loadUserByUsername(req.username);
        String token = jwtUtils.generateToken(user);

        return ResponseEntity.ok(new ResponseToken(token));
    }
}
