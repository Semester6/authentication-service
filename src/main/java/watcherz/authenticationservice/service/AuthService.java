package watcherz.authenticationservice.service;

import lombok.AllArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import watcherz.authenticationservice.exceptions.*;
import watcherz.authenticationservice.model.Login;
import watcherz.authenticationservice.model.RegisterUser;
import watcherz.authenticationservice.model.Role;
import watcherz.authenticationservice.model.User;
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

    public void signUp(RegisterUser registerUser) throws CouldNotSaveUserException {
        String password = EncodePassword(registerUser.getPassword());
        User user = new User(UUID.randomUUID(), registerUser.getEmail(), password, Role.ROLE_USER);
        try{
            authRepository.save(user);
        }
        catch (Exception e) {
            throw new CouldNotSaveUserException();
        }
       //return user.getUserId();
    }
}
