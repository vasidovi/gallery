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

        Optional<ImageEntity> image = imageRepository.findById(id);

        if (image.isPresent()) {

            Optional<QualityImageFileEntity> qualityImageFileEntityOptional = repository.findByImage(image.get());
            if (qualityImageFileEntityOptional.isPresent()) {
                return qualityImageFileEntityOptional.get();
            } else {
                return null;
            }
        } else {
            return null;
        }
    }

}
