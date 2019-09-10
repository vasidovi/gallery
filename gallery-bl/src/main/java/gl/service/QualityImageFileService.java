package gl.service;

import gl.model.entity.ImageEntity;
import gl.model.entity.QualityImageFileEntity;

public interface QualityImageFileService {

    QualityImageFileEntity findByImageId(Long id);

    ImageEntity deleteByImageId(Long id);
}
