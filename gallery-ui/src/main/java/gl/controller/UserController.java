package gl.controller;

import gl.model.entity.UserEntity;
import gl.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@RestController
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/signup")
    public UserEntity saveUser(@RequestBody UserEntity user){
        return userService.save(user);
    }

}

