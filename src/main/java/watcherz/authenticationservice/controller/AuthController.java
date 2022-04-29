package watcherz.authenticationservice.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import watcherz.authenticationservice.exceptions.*;
import watcherz.authenticationservice.model.Login;
import watcherz.authenticationservice.model.RegisterUser;
import watcherz.authenticationservice.model.User;
import watcherz.authenticationservice.service.AuthService;
import watcherz.authenticationservice.service.JwtService;

import java.io.UnsupportedEncodingException;

@RestController
@AllArgsConstructor
@RequestMapping("auth")
public class AuthController {

    private final AuthService authService;
    private final JwtService jwtService;

    @PostMapping("/signIn")
    public ResponseEntity<String> signIn(@RequestBody Login login) throws NotAuthenticatedException, CouldNotCreateTokenException, EmailDoesNotExistException {
        return new ResponseEntity<String>(authService.signIn(login), HttpStatus.OK);
    }

    @PostMapping("/signUp")
    public ResponseEntity<Void> signUp(@RequestBody RegisterUser user) throws CouldNotSaveUserException {
        authService.signUp(user);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }
}
