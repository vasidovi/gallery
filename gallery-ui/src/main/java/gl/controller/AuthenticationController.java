package gl.controller;

import gl.model.AuthToken;
import gl.model.LoginUser;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/token")
public class AuthenticationController {

    private AuthenticationManager authenticationManager;

    public AuthenticationController(AuthenticationManager authenticationManager
//                                    TokenProvider jwtTokenUtil
    ) {
        this.authenticationManager = authenticationManager;
//        this.jwtTokenUtil = jwtTokenUtil;
    }

    @PostMapping("/generate-token")
    public ResponseEntity<?> signIn(@RequestBody LoginUser loginUser) throws AuthenticationException {

        final Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginUser.getUsername(),
                        loginUser.getPassword()
                )
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
        final String token = null;
//                jwtTokenUtil.generateToken(authentication);
        return ResponseEntity.ok(new AuthToken(token));
    }
}
