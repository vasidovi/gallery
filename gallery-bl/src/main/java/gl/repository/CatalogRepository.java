package gl.repository;

import gl.model.entity.CatalogEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Set;

public interface CatalogRepository extends JpaRepository<CatalogEntity, Long> {

    @Query( "select c from CatalogEntity c where c.name in :names" )
    Set<CatalogEntity> findByNameIn(Set<String> names);
}
