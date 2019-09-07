package gl.model;

import gl.model.entity.CatalogEntity;
import lombok.Data;

import java.util.HashSet;
import java.util.Set;

@Data
public class CatalogSetDTO {
    private Set<CatalogEntity> catalogEntitySet = new HashSet<>();
}
