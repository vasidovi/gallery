package gl.controller;

import gl.model.entity.UserEntity;
import gl.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
public class UserController {

    private UserService userService;

    public  UserController(UserService userService){
        this.userService = userService;
    }

    @PostMapping("/signup")
    public ResponseEntity<?> saveUser(@RequestBody UserEntity user) {

        UserEntity newUser = userService.save(user);
        if (newUser == null) {
            return ResponseEntity.badRequest().body(user.getUsername() + " already exists, choose different username");
        } else {
            return ResponseEntity.ok().body(newUser);
        }
    }


}

