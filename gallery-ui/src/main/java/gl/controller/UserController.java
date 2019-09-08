package gl.controller;

import gl.model.entity.UserEntity;
import gl.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
public class UserController {

    @Autowired
    private UserService userService;

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

