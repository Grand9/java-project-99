package hexlet.code.controller;

import hexlet.code.dto.AuthRequest;
import hexlet.code.util.JWTUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/login")
public class AuthenticationController {

    private final JWTUtils jwtUtils;
    private final AuthenticationManager authenticationManager;

    @PostMapping
    public ResponseEntity<String> create(@RequestBody AuthRequest authRequest) {
        try {
            var authentication = new UsernamePasswordAuthenticationToken(
                    authRequest.getUsername(), authRequest.getPassword());

            authenticationManager.authenticate(authentication);

            String token = jwtUtils.generateToken(authRequest.getUsername());

            return ResponseEntity.ok(token);
        } catch (AuthenticationException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Invalid username or password");
        }
    }
}
