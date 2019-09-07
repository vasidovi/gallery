package gl.repository;

import gl.model.entity.ImageEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface ImageRepository extends JpaRepository<ImageEntity, Long> {

    @Query(value = "select image from ImageEntity image join image.catalogs c where c.id = :id")
    List<ImageEntity> findByCatalogIdNative(@Param("id") Long Id);

}


