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
            return repository.findByCatalogIdNative(id);
        }
        return null;
    }

    public List<ImageEntity> getAllImages() {
        return repository.findAll();
    }

    private String _imageModelToString(ImageEntity image) {
        return "data:" +
                image.getImageFormat() +
                ";base64," +
                Base64.getEncoder().encodeToString(image.getFile());
    }

    private ImageDTO _mapImageDAOToImageDTO(ImageEntity imageEntity) {
        ImageDTO imageDTO = new ImageDTO();
        imageDTO.setId(imageEntity.getId());
        imageDTO.setFile(_imageModelToString(imageEntity));
        imageDTO.setDate(imageEntity.getDate());
        imageDTO.setDescription(imageEntity.getDescription());
        imageDTO.setName(imageEntity.getName());

        Set<String> DTOTags = new HashSet<>();

        for (TagEntity tag : imageEntity.getTags()) {
            DTOTags.add(tag.getName());
        }

        Set<String> DTOCatalogs = new HashSet<>();

        for (CatalogEntity catalog : imageEntity.getCatalogs()) {
            DTOTags.add(catalog.getName());
        }

        imageDTO.setCatalogs(DTOCatalogs);
        imageDTO.setTags(DTOTags);

        return imageDTO;
    }

    public ImageDTO updateImage(ImageUploadDTO imageUploadDTO, Long id) {

        Optional<ImageEntity> imageDAOOptional = repository.findById(id);

        if (imageDAOOptional.isPresent()) {
            ImageEntity image = imageDAOOptional.get();
            image = _updateImageMetadata(imageUploadDTO, image);
            repository.save(image);

            return _mapImageDAOToImageDTO(image);

        }
        return null;
    }

    private ImageEntity _updateImageMetadata(ImageUploadDTO imageUploadDTO, ImageEntity imageEntity) {

        String name = imageUploadDTO.getName();
        if (name != null && !name.isEmpty()) {
            imageEntity.setName(name);
        }

        String catalogs = imageUploadDTO.getCatalogs();
        if (catalogs != null && !catalogs.isEmpty()) {
            // parse catalogs ?? how are we choosing catalogs
            // should not be able to create catalog that easily
        }
        String tags = imageUploadDTO.getTags();
        if (tags != null && !tags.isEmpty()) {
            imageEntity.setTags(tagService.resolveInputToTags(tags, imageEntity.getTags()));
        }

        String description = imageUploadDTO.getDescription();
        if (description != null && !description.isEmpty()) {
            imageEntity.setDescription(description);
        }

        return imageEntity;
    }

    public ImageEntity uploadImage(ImageUploadDTO imageUploadDTO) {

        try {
            ImageEntity image = new ImageEntity(
                    imageUploadDTO.getFile().getBytes(),
                    imageUploadDTO.getFile().getContentType()
            );
            image.setDate(new Date());
            image.setCreationDateTime(new Date());

            image = _updateImageMetadata(imageUploadDTO, image);
            image.setFile(createImageThumbnail.createThumbnail(image));

            QualityImageFileEntity qualityImageFile = new QualityImageFileEntity();
            qualityImageFile.setFile(imageUploadDTO.getFile().getBytes());
            qualityImageFile.setImage(image);

            qualityImageFileRepository.save(qualityImageFile);

            return null;

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

    @Override
    public List<ImageEntity> findByMultipleParameters(List<Long> catalogIds,
                                                      List<String> tags,
                                                      String search) {

        Session session = entityManager.unwrap(Session.class);
        StringBuilder sb = new StringBuilder();

        sb.append("select distinct image from ImageEntity image " +
                "left outer join image.catalogs c " +
                "left outer join image.tags t where ");

        if (tags != null & tags.size() > 0) {
            List<Long> tagIds = tagService.findTagEntitiesByNameIn(tags)
                    .stream().map( tag -> tag.getId()).collect(Collectors.toList());
            if (tagIds.size() > 0){
                sb.append("t.id in ( ");
                        for (Long id : tagIds) {
                            sb.append(id + ", ");
                        }
                        sb.delete(sb.length()-2, sb.length()-1);
                        sb.append(") and ");
            }
        }

        if (catalogIds != null && catalogIds.size() > 0) {
            sb.append("c.id in ( ");
            for (Long id : catalogIds) {
                sb.append(id + ", ");
            }
            sb.delete(sb.length()-2, sb.length()-1);
            sb.append(") and ");
        }
        String searchLowercase = search.trim().toLowerCase();

        if (!searchLowercase.isEmpty()) {
            sb.append("image.name like :name or image.description like :description and ");
        }

        // at least one condition was met
        if (sb.toString().contains("and")) {

             String queryReady  = sb.substring(0, sb.length() - 5);
             Query<ImageEntity> query = session.createQuery(queryReady);


             if (!searchLowercase.isEmpty()) {
                query.setParameter("name", "%" + searchLowercase+ "%");
                query.setParameter("description", "%" + searchLowercase + "%");
            }

            List<ImageEntity> images = query.getResultList();

            return images;

        } else {
            return null;
        }
    }

    public void deleteById(Long id) {
        repository.deleteById(id);
    }

}

//  Deprecated methods

//    @Override
//    public List<ImageDTO> returnThumbnailsByCatalogs(CatalogEntity catalogs) {
//        List<ImageEntity> images = repository.findAllByCatalogs(catalogs);
//        List<ImageDTO> imageDTOS = new ArrayList<>();
//        for (ImageEntity imageEntity : images){
//            try {
//                imageEntity.setFile(createImageThumbnail.createThumbnail(imageEntity).getFile());
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//            ImageDTO imageDTO = _mapImageDAOToImageDTO(imageEntity);
//            imageDTOS.add(imageDTO);
//        }
//        return imageDTOS;
//    }

//    public ImageDTO uploadFile(MultipartFile file) {
//        try {
//            ImageEntity image = new ImageEntity(
//                    file.getBytes(),
//                    file.getContentType()
//            );
//            repository.save(image);
//            createImageThumbnail.createThumbnail(image);
//            return _mapImageDAOToImageDTO(image);
//
//        } catch (IOException e) {
//            e.printStackTrace();
//            return null;
//        }
//    }
