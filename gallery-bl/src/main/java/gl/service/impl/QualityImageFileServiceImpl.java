package gl.service.impl;

import gl.model.entity.ImageEntity;
import gl.model.entity.QualityImageFileEntity;
import gl.repository.ImageRepository;
import gl.repository.QualityImageFileRepository;
import gl.service.QualityImageFileService;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
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

        return result;
    }

    @Override
    public ImageEntity deleteByImageId(Long id) {

        Optional<ImageEntity> image = imageRepository.findById(id);
        if (image.isPresent()) {
            repository.delete(repository.findByImage(image.get()).get());
            return image.get();
        } else {
            return null;
        }
    }

}
