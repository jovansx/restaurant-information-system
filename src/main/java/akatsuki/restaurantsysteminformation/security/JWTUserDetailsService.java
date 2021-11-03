package akatsuki.restaurantsysteminformation.security;

import akatsuki.restaurantsysteminformation.registereduser.RegisteredUser;
import akatsuki.restaurantsysteminformation.registereduser.RegisteredUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class JWTUserDetailsService implements UserDetailsService {

    @Autowired
    private RegisteredUserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String emailAddress) throws UsernameNotFoundException {
        Optional<RegisteredUser> registeredUser = userRepository.findByEmailAddress(emailAddress);
        if (registeredUser.isEmpty()) {
            throw new UsernameNotFoundException("User not found");
        } else {
            return new JWTUserDetails(registeredUser.get());
        }
    }
}
