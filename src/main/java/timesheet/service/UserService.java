package timesheet.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import timesheet.entity.User;
import timesheet.repository.UserRepository;

import java.util.List;
import java.util.Optional;

@Service
public class UserService implements UserDetailsService {
    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));
        return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(),
                List.of(new SimpleGrantedAuthority(user.getRole().name())));
    }

    public User findUserByEmail(String email){
        Optional<User> user =userRepository.findByEmail(email);
        return user.get();
    }

    public String addUser(User user){
     Optional<User> user1 = userRepository.findByEmail(user.getEmail());
        if (user1.isPresent()){
            return "User Already Present";
        }else{
            userRepository.save(user);
            return "User Added Successfully";
        }
    }
}
