package gl.service;


import gl.model.ImageUploadEntity;
import gl.model.entity.ImageEntity;
import java.util.List;
import java.util.Set;

public interface ImageService {

//    void deleteById(Long id);

    List<ImageEntity> getAllImages();
    List<ImageEntity> getImagesByCatalogId(Long id);
    List<ImageEntity> findImagesByCatalogIds(List<Long> ids);

    ImageEntity uploadImage(ImageUploadEntity imageUploadEntity);
    ImageEntity updateImage(ImageUploadEntity imageUploadEntity, Long id);

    List<ImageEntity> findByMultipleParameters(List<Long> catalogIds, Set<String> tags, String search);


    ImageEntity findByImageId(Long id);
}
