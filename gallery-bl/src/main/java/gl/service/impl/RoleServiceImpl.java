package gl.service.impl;

import gl.model.entity.RoleEntity;
import gl.repository.RoleRepository;
import gl.service.RoleService;
import org.springframework.stereotype.Service;

@Service
public class RoleServiceImpl implements RoleService {

    private RoleRepository repository;

    public RoleServiceImpl(RoleRepository repository) {
        this.repository = repository;
    }

    @Override
    public RoleEntity findByRole(String role) {
        return repository.findByRole(role).get();
    }
}
