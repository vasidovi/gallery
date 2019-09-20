package gl.controller;

import gl.model.*;
import gl.model.entity.ImageEntity;
import gl.model.entity.QualityImageFileEntity;
import gl.model.entity.TagEntity;
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

    @GetMapping("/images")
    public  List<ImageEntity>  getAllImages() {
        return imageService.getAllImages();
    }

    @GetMapping("/tags")
    public  List<TagEntity>  getAllTags() {
        return tagService.getAllTags();
    }

    @GetMapping("/image/metadata/{id}")
    public  ImageEntity  getImageById(@PathVariable Long id) {
        return imageService.findByImageId(id);
    }

    @GetMapping("/image/{id}")
    public QualityImageFileEntity getQualityFile(@PathVariable Long id) {
        return qualityImageFileService.findByImageId(id);
    }

    //path no longer used up for removal
    @GetMapping("/images/catalog/{id}")
    public List<ImageEntity> getAllImagesByCatalog(
            @PathVariable Long id) {
        return imageService.getImagesByCatalogId(id);
    }

    // path no longer used up for removal
    @GetMapping("/images/catalogs")
    public List<ImageEntity> getAllImagesByCatalogsIds(
            @RequestParam List<Long> ids) {
        return imageService.findImagesByCatalogIds(ids);
    }

    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @PutMapping( value = "/image/{id}")
    public ResponseEntity<?>  updateImage(
            @PathVariable Long id,
            @ModelAttribute ImageUploadEntity imageUploadEntity) {

        ImageEntity imageEntity = imageService.updateImage(imageUploadEntity, id);

        if (imageEntity == null) {
            return ResponseEntity.notFound().build();
        } else {
            return ResponseEntity.ok().body(imageEntity);
        }
    }

    @GetMapping("/images/find")
    public List<ImageEntity> findByMultipleParameters(
            @RequestParam List<Long> catalogIds,
            @RequestParam Set<String> tags,
            @RequestParam String search
    ) {
        return imageService.findByMultipleParameters(catalogIds, tags, search);
    }

    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @PostMapping(value = "/upload")
    public ImageEntity upload(
            @ModelAttribute ImageUploadEntity imageUploadEntity) {

        ImageEntity image = imageService.uploadImage(imageUploadEntity);
        return image;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/image/{id}")
    public ResponseEntity<?>  deleteImage(@PathVariable Long id) {
        qualityImageFileService.deleteByImageId(id);
        return ResponseEntity.ok().build();
    }
}
