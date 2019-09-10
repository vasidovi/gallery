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
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.criteria.*;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ImageServiceImpl implements ImageService {

    @Autowired
    EntityManager entityManager;
    @Autowired
    private ImageRepository repository;
    @Autowired
    private TagService tagService;
    @Autowired
    private CatalogService catalogService;
    @Autowired
    private CreateImageThumbnail createImageThumbnail;
    @Autowired
    private QualityImageFileRepository qualityImageFileRepository;

    public List<ImageEntity> getImagesByCatalogId(Long id) {

        if (catalogService.findById(id) != null) {
            return repository.findByCatalogId(id);
        }
        return null;
    }

    public List<ImageEntity> getAllImages() {
        return repository.findAll();
    }


    public ImageEntity updateImage(ImageUploadEntity imageUploadEntity, Long id) {

        Optional<ImageEntity> imageDAOOptional = repository.findById(id);

        if (imageDAOOptional.isPresent()) {
            ImageEntity image = imageDAOOptional.get();
            image = _updateImageMetadata(imageUploadEntity, image);
            repository.save(image);
            return image;
        }
        return null;
    }

    private ImageEntity _updateImageMetadata(ImageUploadEntity imageUploadEntity, ImageEntity imageEntity) {

        String name = imageUploadEntity.getName().trim().toLowerCase();
        imageEntity.setName(name);

        String description = imageUploadEntity.getDescription().trim().toLowerCase();
        imageEntity.setDescription(description);

        Set<String> catalogs = imageUploadEntity.getCatalogs();
        imageEntity.setCatalogs(catalogService.findAllByNames(catalogs));

        Set<String> tags = imageUploadEntity.getTags();
        imageEntity.setTags(tagService.resolveInputToTags(tags));

        return imageEntity;
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

            image = _updateImageMetadata(imageUploadEntity, image);

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

    public List<ImageEntity> findImagesByCatalogIds(List<Long> ids) {

        Session session = entityManager.unwrap(Session.class);
        StringBuilder sb = new StringBuilder();
        sb.append("select distinct img from ImageEntity " +
                "as img left outer join img.catalogs as cats where ");
        for (Long x : ids) {
            sb.append("cats.id=:category" + x + " or ");

        }

        String queryReady = sb.substring(0, sb.length() - 3);
        Query<ImageEntity> query = session.createQuery(queryReady);
        for (Long x : ids) {
            query.setParameter("category" + x, x);
        }
        List<ImageEntity> images = query.getResultList();
        return images;
    }

    /*  Filter in native

        select distinct image.* from image
        left outer join
        image_catalog as img_cat on img_cat.image_id  = image.id
        left outer join
        image_tag as img_tag on img_tag.image_id = image.id
        left outer join
        tag on tag.id = img_tag.tag_id

        !!! only if catalog id list length > 0
        where catalog.id in ( ..,.. )
        and
        !!! only  if tag.name list length > 0
        tag.name in (..,..)
        and
        image.name like '% image.name %'
        and
        image.description like '% image.description %'

    */
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

            List<ImageEntity> images = entityManager.createQuery(criteriaQuery).getResultList();

            return images;

        } else {
            return null;
        }
    }


    public List<ImageEntity> findByMultipleParametersOldVersion(List<Long> catalogIds,
                                                                Set<String> tags,
                                                                String search) {

        Session session = entityManager.unwrap(Session.class);
        StringBuilder sb = new StringBuilder();

        sb.append("select distinct image from ImageEntity image " +
                "left outer join image.catalogs c " +
                "left outer join image.tags t where ");

        if (tags != null & tags.size() > 0) {
            List<Long> tagIds = tagService.findTagEntitiesByNameIn(tags)
                    .stream().map(tag -> tag.getId()).collect(Collectors.toList());
            if (tagIds.size() > 0) {
                sb.append("t.id in ( ");
                for (Long id : tagIds) {
                    sb.append(id + ", ");
                }
                sb.delete(sb.length() - 2, sb.length() - 1);
                sb.append(") and ");
            }
        }

        if (catalogIds != null && catalogIds.size() > 0) {
            sb.append("c.id in ( ");
            for (Long id : catalogIds) {
                sb.append(id + ", ");
            }
            sb.delete(sb.length() - 2, sb.length() - 1);
            sb.append(") and ");
        }
        String searchLowercase = search.trim().toLowerCase();

        if (!searchLowercase.isEmpty()) {
            sb.append("image.name like :name or image.description like :description and ");
        }

        // at least one condition was met
        if (sb.toString().contains("and")) {

            String queryReady = sb.substring(0, sb.length() - 5);
            Query<ImageEntity> query = session.createQuery(queryReady);


            if (!searchLowercase.isEmpty()) {
                query.setParameter("name", "%" + searchLowercase + "%");
                query.setParameter("description", "%" + searchLowercase + "%");
            }

            List<ImageEntity> images = query.getResultList();

            return images;

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

        if (image.isPresent()) {
            return image.get();
        } else {
            return null;
        }
    }

}
