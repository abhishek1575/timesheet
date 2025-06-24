package timesheet.controller;


import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.graphql.GraphQlProperties;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import timesheet.config.JwtUtil;
import timesheet.dto.AuthResponse;
import timesheet.entity.AuthRequest;
import timesheet.entity.Role;
import timesheet.entity.User;
import timesheet.repository.UserRepository;
import timesheet.service.UserService;

import java.util.Optional;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserService userService;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserRepository userRepository;

    @PostMapping("/login")
public ResponseEntity<?> login(@RequestBody AuthRequest authRequest){
       try{
           Authentication authenticate = authenticationManager.authenticate(
                   new UsernamePasswordAuthenticationToken(authRequest.getEmail(), authRequest.getPassword()));
           if (authenticate.isAuthenticated()){
                final UserDetails userDetails = userService.loadUserByUsername(authRequest.getEmail());
                User user = userService.findUserByEmail(authRequest.getEmail());

                final String jwt = jwtUtil.generateToken(authRequest.getEmail(), user.getRole());

                String role = user.getRole().toString();

               AuthResponse authResponse = new AuthResponse(
                       user.getId(),
                       user.getName(),
                       user.getEmail(),
                       role,
                       jwt
               );

                return  ResponseEntity.ok(authResponse);
           }else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid User Request");
           }
       } catch (Exception e) {
          return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                   .body("Invalid User");
       }
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
        return ResponseEntity.ok("User registered successfully");
    }


    @PostMapping("/forgotPassword")
    public ResponseEntity<?> forgotPassword (@RequestParam String email, String password){
        email = email.trim();
        Optional<User> userInfo = userRepository.findByEmail(email);
        if(userInfo.isPresent()){
            User user =userInfo.get();
            try{
                user.setPassword(passwordEncoder.encode(password));
                userRepository.save(user);
                return  ResponseEntity.ok("Password change Successfully");
            } catch (Exception e) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body("Unexpected Error");
            }
        }else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("User Not Found ");
        }
    }
}
