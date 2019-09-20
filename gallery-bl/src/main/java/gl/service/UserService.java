package gl.service;

import gl.model.entity.UserEntity;

public interface UserService {

    UserEntity save(UserEntity user);
    UserEntity findOne(String username);
}
