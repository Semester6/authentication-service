package watcherz.authenticationservice.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import watcherz.authenticationservice.exceptions.*;
import watcherz.authenticationservice.messaging.RabbitMQSender;
import watcherz.authenticationservice.model.Login;
import watcherz.authenticationservice.model.RabbitUser;
import watcherz.authenticationservice.model.RegisterUser;
import watcherz.authenticationservice.service.AuthService;

@RestController
@AllArgsConstructor
@RequestMapping("auth")
public class AuthController {

    private final AuthService authService;
    private final RabbitMQSender rabbitMQSender;

    @PostMapping("/signIn")
    public ResponseEntity<String> signIn(@RequestBody Login login) throws NotAuthenticatedException, CouldNotCreateTokenException, EmailDoesNotExistException {
        return new ResponseEntity<String>(authService.signIn(login), HttpStatus.OK);
    }

    @PostMapping("/signUp")
    public ResponseEntity<Void> signUp(@RequestBody RegisterUser registerUser) throws CouldNotSaveUserException {
        RabbitUser rabbitUser = authService.signUp(registerUser);
        rabbitMQSender.send(rabbitUser);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }
}
