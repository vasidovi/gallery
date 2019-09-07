package gl.service;

import gl.model.entity.CatalogEntity;

import java.util.List;

public interface CatalogService {
    List<CatalogEntity> findAll();

    CatalogEntity findById(Long id);
}
