package gl.service;

import gl.model.entity.CatalogEntity;

import java.util.List;
import java.util.Set;

public interface CatalogService {
    List<CatalogEntity> findAll();

    CatalogEntity findById(Long id);
    Set<CatalogEntity> findAllByNames(Set<String> names);

}
