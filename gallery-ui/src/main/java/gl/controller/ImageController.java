package gl.controller;

import gl.model.*;
import gl.model.entity.ImageEntity;
import gl.model.entity.QualityImageFileEntity;
import gl.service.ImageService;
import gl.service.QualityImageFileService;
import gl.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
public class ImageController {

    @Autowired
    private ImageService imageService;
    @Autowired
    private TagService tagService;
    @Autowired
    private QualityImageFileService qualityImageFileService;


    //toDo to remake thumbnails generation
    @GetMapping("/images")
    public  List<ImageEntity>  getAllImages() {
        return imageService.getAllImages();
    }

    @GetMapping("/image/{id}")
    public QualityImageFileEntity getQualityFile(@PathVariable Long id) {
        return qualityImageFileService.findByImageId(id);
    }

    @GetMapping("/images/catalog/{id}")
    public List<ImageEntity> getAllImagesByCatalog(
            @PathVariable Long id) {
        return imageService.getImagesByCatalogId(id);
    }

    @GetMapping("/images/catalogs")
    public List<ImageEntity> getAllImagesByCatalogsIds(
            @RequestParam List<Long> ids) {
        return imageService.findImagesByCatalogIds(ids);
    }

    @GetMapping("/images/find")
    public List<ImageEntity> findByMultipleParameters(
            @RequestParam List<Long> catalogIds,
            @RequestParam Set<String> tags,
            @RequestParam String search
    ) {
        return imageService.findByMultipleParameters(catalogIds, tags, search);
    }

//    @PostMapping(value = "/upload")
//    public ResponseEntity<?> upload(
//            @ModelAttribute ImageUploadDTO imageUploadDTO) {
//
//        ImageEntity image = imageService.uploadImage(imageUploadDTO);
//        if (image == null) {
//            return ResponseEntity.badRequest().body("Failed to read file");
//        } else {
//            return ResponseEntity.ok()
//                    .body(image);
//        }
//    }

    @PostMapping(value = "/upload")
    public ImageEntity upload(
            @ModelAttribute ImageUploadEntity imageUploadEntity) {
        ImageEntity image = imageService.uploadImage(imageUploadEntity);
        return image;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/image/{id}")
    public ResponseEntity<?>  deleteImage(@PathVariable Long id) {
        imageService.deleteById(id);
        return ResponseEntity.ok().build();
    }

    // toDo to refactor, ImageDTO is no longer used
    @PutMapping( value = "/image/{id}")
    public ResponseEntity<?>  updateImage(
            @PathVariable Long id,
            @ModelAttribute ImageUploadDTO imageUploadDTO) {

        ImageDTO imageDTO = imageService.updateImage(imageUploadDTO, id);

        if (imageDTO == null) {
            return ResponseEntity.notFound().build();
        } else {
            return ResponseEntity.ok().body(imageDTO);
        }
    }

    //ToDo to refactor - will we need this servide ??
    @PutMapping("/image/{id}/addTags")
    public void updateTags(
            @PathVariable Long id,
            @RequestParam("tag") String tags) {
//        ImageEntity image = imageService.findById(id).get();
//
//        if (!tags.isEmpty()) {
//            image.setTags(tagService.resolveInputToTags(tags, image.getTags()));
//            imageService.save(image);
//        }
    }

    //ToDo to remake into Restful to refactor (logic goes to services!!) - returns ResponseEntity ?
    @DeleteMapping("/image/{id}/tag/{name}")
    public void deleteTag(
            @PathVariable Long id,
            @PathVariable String name) {

//        ImageEntity image = imageService.findById(id).get();
//        TagEntity tag = tagService.findByName(name).get();
//        Set<TagEntity> tagList = image.getTags();
//        tagList.remove(tag);
//        imageService.save(image);
    }
}
