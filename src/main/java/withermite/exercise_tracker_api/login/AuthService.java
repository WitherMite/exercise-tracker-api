package withermite.exercise_tracker_api.login;

import java.util.List;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class AuthService implements UserDetailsService {
    private final AuthRepository repository;

    public AuthService(AuthRepository repository) {
        this.repository = repository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        AuthRequest user = repository.getOne(username);
        if (user == null)
            throw new UsernameNotFoundException("User not found");

        List<SimpleGrantedAuthority> roles = List.of(new SimpleGrantedAuthority(user.role));
        return new User(user.username, user.password, roles);
    }

}
