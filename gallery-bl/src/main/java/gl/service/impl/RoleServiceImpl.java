package gl.service.impl;

import gl.model.entity.RoleEntity;
import gl.repository.RoleRepository;
import gl.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RoleServiceImpl implements RoleService {

    @Autowired
    private RoleRepository repository;

    @Override
    public RoleEntity findByRole(String role) {
        return repository.findByRole(role).get();
    }
}
