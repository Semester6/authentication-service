package watcherz.authenticationservice.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class RegisterUser {

    String email;
    String password;
    //Role role;
}
