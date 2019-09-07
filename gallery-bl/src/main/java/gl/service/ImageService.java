package gl.service;


import gl.model.ImageUploadEntity;
import gl.model.entity.ImageEntity;
import gl.model.ImageDTO;
import gl.model.ImageUploadDTO;

import java.util.List;
import java.util.Set;

public interface ImageService {

    void deleteById(Long id);

    List<ImageEntity> getAllImages();
    List<ImageEntity> getImagesByCatalogId(Long id);
    List<ImageEntity> findImagesByCatalogIds(List<Long> ids);

    ImageEntity uploadImage(ImageUploadEntity imageUploadEntity);
    ImageDTO updateImage(ImageUploadDTO imageUploadDTO, Long id);

    List<ImageEntity> findByMultipleParameters(List<Long> catalogIds, Set<String> tags, String search);



}
