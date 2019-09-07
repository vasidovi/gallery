package gl.service;


import gl.model.entity.ImageEntity;
import gl.model.ImageDTO;
import gl.model.ImageUploadDTO;

import java.util.List;

public interface ImageService {

    void deleteById(Long id);

    List<ImageEntity> getAllImages();
    List<ImageEntity> getImagesByCatalogId(Long id);
    List<ImageEntity> findImagesByCatalogIds(List<Long> ids);

    ImageEntity uploadImage(ImageUploadDTO imageUploadDTO);
    ImageDTO updateImage(ImageUploadDTO imageUploadDTO, Long id);

    List<ImageEntity> findByMultipleParameters(List<Long> catalogIds, List<String> tags, String search);



}
