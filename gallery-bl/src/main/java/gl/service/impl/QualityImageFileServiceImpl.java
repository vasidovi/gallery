package gl.service.impl;

import gl.model.entity.ImageEntity;
import gl.model.entity.QualityImageFileEntity;
import gl.repository.ImageRepository;
import gl.repository.QualityImageFileRepository;
import gl.service.QualityImageFileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class QualityImageFileServiceImpl implements QualityImageFileService {

    @Autowired
    ImageRepository imageRepository;
    @Autowired
    private QualityImageFileRepository repository;

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
            repository.delete( repository.findByImage(image.get()).get());
            return image.get();
        } else {
            return null;
        }
    }

}
