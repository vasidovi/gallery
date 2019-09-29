package gl.controller;

import gl.model.*;
import gl.model.entity.ImageEntity;
import gl.model.entity.QualityImageFileEntity;
import gl.model.entity.TagEntity;
import gl.service.ImageService;
import gl.service.QualityImageFileService;
import gl.service.TagService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
public class ImageController {

    private ImageService imageService;
    private TagService tagService;
    private QualityImageFileService qualityImageFileService;

    public ImageController(ImageService imageService,
                           TagService tagService,
                           QualityImageFileService qualityImageFileService) {
        this.imageService = imageService;
        this.tagService = tagService;
        this.qualityImageFileService = qualityImageFileService;
    }

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

    @GetMapping("/imageSrc/{id}")
    public ResponseEntity<byte[]> getQualityFileSrc(@PathVariable Long id) {
        QualityImageFileEntity file = qualityImageFileService.findByImageId(id);
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.IMAGE_JPEG);
            return new ResponseEntity<>(file.getFile(), headers, HttpStatus.OK);
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
        System.out.println("we go here");
        return imageService.findByMultipleParameters(catalogIds, tags, search);
    }

    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @PostMapping(value = "/upload")
    public ImageEntity upload(
            @ModelAttribute ImageUploadEntity imageUploadEntity) {

       return imageService.uploadImage(imageUploadEntity);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/image/{id}")
    public ResponseEntity<?>  deleteImage(@PathVariable Long id) {
        qualityImageFileService.deleteByImageId(id);
        return ResponseEntity.ok().build();
    }
}
