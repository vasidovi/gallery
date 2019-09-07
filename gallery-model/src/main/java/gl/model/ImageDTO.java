package gl.model;

import lombok.Data;

import java.util.Date;
import java.util.Set;

@Data
public class ImageDTO {
    private Long id;
    private String file;
    private String name;
    private Set<String> tags;
    private Set<String> catalogs;
    private String description;
    private Date date;
}
