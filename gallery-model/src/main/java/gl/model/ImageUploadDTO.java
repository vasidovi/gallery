package gl.model;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;

@Data
public class ImageUploadDTO {
    private Long id;
    private MultipartFile file;
    private String name;
    private String tags;
    private String description;
    private String catalogs;
    private Date date;
}
