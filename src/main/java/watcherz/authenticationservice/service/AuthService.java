package watcherz.authenticationservice.service;

import lombok.AllArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import watcherz.authenticationservice.exceptions.*;
import watcherz.authenticationservice.model.*;
import watcherz.authenticationservice.repository.AuthRepository;

import java.util.UUID;

@Service @AllArgsConstructor
public class AuthService {

    private final JwtService jwtService;
    private final AuthRepository authRepository;

    public String signIn(Login login) throws NotAuthenticatedException, CouldNotCreateTokenException, EmailDoesNotExistException {
        User user;
        try{
            user = authRepository.findByEmail(login.getEmail());
            if (MatchPassword(login.getPassword(), user.getPassword())) {
                return createToken(user);
            }
            else{
                throw new NotAuthenticatedException();
            }
        } catch (Exception e){
            throw new EmailDoesNotExistException();
        }
    }

    public String createToken(User user) throws CouldNotCreateTokenException {
        String token;
        try {
            token = jwtService.createToken(user);
            if(token.isEmpty()){
                throw new TokenIsEmptyException();
            }
            return token;
        }
        catch(Exception e){
            throw new CouldNotCreateTokenException();
        }
    }

    public String EncodePassword(String password){
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String encodedPassword = passwordEncoder.encode(password);
        return encodedPassword;
    }

    public boolean MatchPassword(String loginPassword, String userPassword){
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        return passwordEncoder.matches(loginPassword, userPassword);
    }

    public RabbitUser signUp(RegisterUser registerUser) throws CouldNotSaveUserException {
        String password;
        User user;
        RabbitUser rabbitUser;
        try{
            password = EncodePassword(registerUser.getPassword());
            user = new User(UUID.randomUUID(), registerUser.getEmail(), password, Role.ROLE_USER);
            authRepository.save(user);
            rabbitUser = new RabbitUser(user.getUserId(), user.getEmail());
            return rabbitUser;
        }
        catch (Exception e) {
            throw new CouldNotSaveUserException();
        }
    }
}
