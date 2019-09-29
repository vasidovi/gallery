package gl.service.impl;

import gl.model.entity.ImageEntity;
import gl.model.entity.QualityImageFileEntity;
import gl.repository.ImageRepository;
import gl.repository.QualityImageFileRepository;
import gl.service.QualityImageFileService;
import lombok.extern.java.Log;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Log
public class QualityImageFileServiceImpl implements QualityImageFileService {

    private ImageRepository imageRepository;
    private QualityImageFileRepository repository;

    public QualityImageFileServiceImpl(ImageRepository imageRepository,
                                       QualityImageFileRepository repository) {
        this.imageRepository = imageRepository;
        this.repository = repository;
    }

    @Override
    public QualityImageFileEntity findByImageId(Long id) {
        QualityImageFileEntity result = null;

        Optional<ImageEntity> image = imageRepository.findById(id);

        if (image.isPresent()) {
            Optional<QualityImageFileEntity> qualityImageFileEntityOptional = repository.findByImage(image.get());
            if (qualityImageFileEntityOptional.isPresent()) {
                result = qualityImageFileEntityOptional.get();
            }
        }
        log.info("Failed to find quality image file, because mo image with id " + id  + " found");
        return result;
    }

    @Override
    public ImageEntity deleteByImageId(Long id) {

        Optional<ImageEntity> image = imageRepository.findById(id);
        if (image.isPresent()) {
            repository.delete(repository.findByImage(image.get()).get());
            return image.get();
        } else {
            log.info("Failed to delete image, because no image with id " + id  + " found");
            return null;
        }
    }

}
