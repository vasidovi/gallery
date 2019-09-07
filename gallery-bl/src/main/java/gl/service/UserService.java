package gl.service;

import gl.model.entity.UserEntity;

public interface UserService {

    UserEntity save(UserEntity user);
    void delete(long id);
    UserEntity findOne(String username);
    UserEntity findById(Long id);
}
