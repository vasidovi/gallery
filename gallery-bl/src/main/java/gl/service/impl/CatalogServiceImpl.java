package gl.service.impl;

import gl.model.entity.CatalogEntity;
import gl.repository.CatalogRepository;
import gl.service.CatalogService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class CatalogServiceImpl implements CatalogService {

    private CatalogRepository catalogRepository;

    public CatalogServiceImpl(CatalogRepository catalogRepository) {
        this.catalogRepository = catalogRepository;
    }

    @Override
    public List<CatalogEntity> findAll() {
        return catalogRepository.findAll();
    }

    @Override
    public CatalogEntity findById(Long id) {
        Optional<CatalogEntity> catalogDAOOptional = catalogRepository.findById(id);
        if (catalogDAOOptional.isPresent()) {
            return catalogDAOOptional.get();
        } else {
            return null;
        }
    }

    @Override
    public Set<CatalogEntity> findAllByNames(Set<String> names) {
        return catalogRepository.findByNameIn(names);
    }
}
