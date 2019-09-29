package gl.service;

import gl.model.entity.CatalogEntity;

import java.util.List;
import java.util.Set;

public interface CatalogService {

    List<CatalogEntity> findAll();
    Set<CatalogEntity> findAllByNames(Set<String> names);
}
