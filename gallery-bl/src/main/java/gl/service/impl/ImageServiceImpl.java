package gl.service.impl;


import gl.model.*;
import gl.model.entity.CatalogEntity;
import gl.model.entity.ImageEntity;
import gl.model.entity.QualityImageFileEntity;
import gl.model.entity.TagEntity;
import gl.repository.ImageRepository;
import gl.repository.QualityImageFileRepository;
import gl.service.CatalogService;
import gl.service.ImageService;
import gl.service.TagService;
import gl.util.CreateImageThumbnail;
import org.springframework.stereotype.Service;
import javax.persistence.EntityManager;
import javax.persistence.criteria.*;
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


    public ImageServiceImpl( EntityManager entityManager,
                             ImageRepository repository,
                             TagService tagService,
                             CatalogService catalogService,
                             CreateImageThumbnail createImageThumbnail,
                             QualityImageFileRepository qualityImageFileRepository) {
        this.entityManager = entityManager;
        this.repository = repository;
        this.tagService = tagService;
        this.catalogService = catalogService;
        this.createImageThumbnail = createImageThumbnail;
        this.qualityImageFileRepository = qualityImageFileRepository;
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


        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<ImageEntity> criteriaQuery = criteriaBuilder.createQuery(ImageEntity.class);
        Root<ImageEntity> image = criteriaQuery.from(ImageEntity.class);

        Join<ImageEntity, CatalogEntity> catalogs = image.join("catalogs", JoinType.LEFT);
        Join<ImageEntity, TagEntity> tags = image.join("tags", JoinType.LEFT);

        criteriaQuery.select(image).distinct(true);

        Predicate finalPredicate = null;
        Predicate catalogPredicate = null;
        Predicate tagsPredicate = null;
        Predicate searchPredicate = null;

        if (catalogIds.size() > 0) {
            catalogPredicate = criteriaBuilder.equal(catalogs.get("id"), catalogIds.get(0));
            if (catalogIds.size() > 1) {
                for (int i = 1; i < catalogIds.size(); i++) {
                    Predicate currentPredicate = criteriaBuilder.equal(catalogs.get("id"), catalogIds.get(i));
                    catalogPredicate = criteriaBuilder.or(catalogPredicate, currentPredicate);
                }
            }
        }

        if (tagSet.size() > 0) {
            List<String> tagsList = new ArrayList<>(tagSet);
            tagsPredicate = criteriaBuilder.equal(tags.get("name"), tagsList.get(0));
            if (tagsList.size() > 1) {
                for (int i = 1; i < tagsList.size(); i++) {
                    Predicate currentPredicate = criteriaBuilder.equal(tags.get("name"), tagsList.get(i));
                    tagsPredicate = criteriaBuilder.or(tagsPredicate, currentPredicate);
                }
            }
        }

        String searchLowercase = search.trim().toLowerCase();

        if (!searchLowercase.isEmpty()) {
            Predicate namePredicate = criteriaBuilder.like(image.get("name"), "%" + searchLowercase + "%");
            Predicate descriptionPredicate = criteriaBuilder.like(image.get("description"), "%" + searchLowercase + "%");
            searchPredicate = criteriaBuilder.or(namePredicate, descriptionPredicate);
        }

        if (catalogPredicate != null) {
            finalPredicate = catalogPredicate;
        }

        if (tagsPredicate != null) {

            if (finalPredicate != null) {
                finalPredicate = criteriaBuilder.and(finalPredicate, tagsPredicate);
            } else {
                finalPredicate = tagsPredicate;
            }
        }

        if (searchPredicate != null) {

            if (finalPredicate != null) {
                finalPredicate = criteriaBuilder.and(finalPredicate, searchPredicate);
            } else {
                finalPredicate = searchPredicate;
            }
        }

        if (finalPredicate != null) {
            criteriaQuery.where(finalPredicate);
            return entityManager.createQuery(criteriaQuery).getResultList();
        } else {
            return null;
        }
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
