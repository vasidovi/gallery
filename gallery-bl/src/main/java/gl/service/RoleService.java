package gl.service;

import gl.model.entity.RoleEntity;

public interface RoleService {

    RoleEntity findByRole(String role);
}
