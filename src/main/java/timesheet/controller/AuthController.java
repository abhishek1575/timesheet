package timesheet.controller;


import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.graphql.GraphQlProperties;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import timesheet.config.JwtUtil;
import timesheet.dto.AuthResponse;
import timesheet.entity.AuthRequest;
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
    public ResponseEntity<?> login(@RequestBody AuthRequest authRequest) {
        try {
            // Debugging: Log incoming request
            System.out.println("Login attempt for email: " + authRequest.getEmail());

            // 1. Verify user exists first
            User user = userRepository.findByEmail(authRequest.getEmail())
                    .orElseThrow(() -> {
                        System.out.println("User not found in database");
                        return new UsernameNotFoundException("Invalid credentials");
                    });

            // 2. Debug password verification
            System.out.println("Stored password hash: " + user.getPassword());
            if (!passwordEncoder.matches(authRequest.getPassword(), user.getPassword())) {
                System.out.println("Password mismatch for user: " + authRequest.getEmail());
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body("Invalid credentials");
            }

            // 3. Authenticate using Spring Security
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            authRequest.getEmail(),
                            authRequest.getPassword()
                    )
            );

            // 4. Generate JWT token
            final String jwt = jwtUtil.generateToken(authRequest.getEmail(), user.getRole());

            // 5. Build response
            AuthResponse authResponse = new AuthResponse(
                    user.getId(),
                    user.getName(),
                    user.getEmail(),
                    user.getRole().toString(),
                    jwt
            );

            // Debugging: Log successful login
            System.out.println("Successful login for user: " + authRequest.getEmail());

            return ResponseEntity.ok(authResponse);

        } catch (UsernameNotFoundException e) {
            System.out.println("Login failed - user not found: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Invalid credentials");
        } catch (BadCredentialsException e) {
            System.out.println("Login failed - bad credentials: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Invalid credentials");
        } catch (Exception e) {
            System.out.println("Unexpected login error: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An error occurred during login");
        }
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody User user) {
        System.out.println("Raw password: " + user.getPassword()); // Debug
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        System.out.println("Encoded password: " + user.getPassword()); // Debug
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
