package gl.model;

import gl.model.entity.CatalogEntity;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.util.Set;

@Data
public class ImageUploadEntity {
    private Long id;
    private MultipartFile file;
    private String name;
    private Set<String> tags;
    private String description;
    private Set<String> catalogs;
}
