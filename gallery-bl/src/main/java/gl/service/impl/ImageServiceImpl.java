package gl.service.impl;


import gl.model.*;
import gl.model.entity.ImageEntity;
import gl.model.entity.QualityImageFileEntity;
import gl.repository.ImageRepository;
import gl.repository.QualityImageFileRepository;
import gl.service.CatalogService;
import gl.service.ImageService;
import gl.service.TagService;
import gl.specification.ImageSpecification;
import gl.util.CreateImageThumbnail;
import org.springframework.stereotype.Service;
import javax.persistence.EntityManager;
import java.io.IOException;
import java.util.*;

@Service
public class ImageServiceImpl implements ImageService {

    private EntityManager entityManager;
    private ImageRepository repository;
    private TagService tagService;
    private CatalogService catalogService;
    private CreateImageThumbnail createImageThumbnail;
    private QualityImageFileRepository qualityImageFileRepository;
    private ImageSpecification imageSpecification;


    public ImageServiceImpl( EntityManager entityManager,
                             ImageRepository repository,
                             TagService tagService,
                             CatalogService catalogService,
                             CreateImageThumbnail createImageThumbnail,
                             QualityImageFileRepository qualityImageFileRepository,
                             ImageSpecification imageSpecification) {
        this.entityManager = entityManager;
        this.repository = repository;
        this.tagService = tagService;
        this.catalogService = catalogService;
        this.createImageThumbnail = createImageThumbnail;
        this.qualityImageFileRepository = qualityImageFileRepository;
        this.imageSpecification = imageSpecification;
    }

    public List<ImageEntity> getAllImages() {
        return repository.findAll();
    }

    public ImageEntity updateImage(ImageUploadEntity imageUploadEntity, Long id) {

        Optional<ImageEntity> imageDAOOptional = repository.findById(id);

        if (imageDAOOptional.isPresent()) {
            ImageEntity image = imageDAOOptional.get();
             _updateImageMetadata(imageUploadEntity, image);
            repository.save(image);
            return image;
        }
        return null;
    }

    private void _updateImageMetadata(ImageUploadEntity imageUploadEntity, ImageEntity imageEntity) {

        String name = imageUploadEntity.getName().trim().toLowerCase();
        imageEntity.setName(name);

        String description = imageUploadEntity.getDescription().trim().toLowerCase();
        imageEntity.setDescription(description);

        Set<String> catalogs = imageUploadEntity.getCatalogs();
        imageEntity.setCatalogs(catalogService.findAllByNames(catalogs));

        Set<String> tags = imageUploadEntity.getTags();
        imageEntity.setTags(tagService.resolveInputToTags(tags));

    }

    @Override
    public ImageEntity uploadImage(ImageUploadEntity imageUploadEntity) {

        try {
            ImageEntity image = new ImageEntity(
                    imageUploadEntity.getFile().getBytes(),
                    imageUploadEntity.getFile().getContentType()
            );
            image.setDate(new Date());
            image.setCreationDateTime(new Date());

            _updateImageMetadata(imageUploadEntity, image);

            image.setFile(createImageThumbnail.createThumbnail(image));

            QualityImageFileEntity qualityImageFile = new QualityImageFileEntity();
            qualityImageFile.setFile(imageUploadEntity.getFile().getBytes());
            qualityImageFile.setImage(image);
            qualityImageFileRepository.save(qualityImageFile);

            return image;

        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public List<ImageEntity> findByMultipleParameters(List<Long> catalogIds,
                                                      Set<String> tagSet,
                                                      String search) {

        return repository.findAll(imageSpecification.getImagesByFilterParameters(search, tagSet, catalogIds));
    }

    @Override
    public void deleteById(Long id) {
        repository.deleteById(id);
    }

    @Override
    public ImageEntity findByImageId(Long id) {

        Optional<ImageEntity> image = repository.findById(id);
        return image.orElse(null);
    }
}
