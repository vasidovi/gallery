package gl.repository;

import gl.model.entity.ImageEntity;
import gl.model.entity.QualityImageFileEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface QualityImageFileRepository  extends JpaRepository<QualityImageFileEntity, Long> {

    Optional<QualityImageFileEntity> findByImage(ImageEntity imageEntity);

}

