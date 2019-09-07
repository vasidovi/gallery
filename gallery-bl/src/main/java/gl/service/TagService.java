package gl.service;

import gl.model.entity.TagEntity;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface TagService {

    Optional<TagEntity> findByName(String name);
    Set<TagEntity> resolveInputToTags(String tags, Set<TagEntity> existingTagList);
    List<TagEntity> findTagEntitiesByNameIn(List<String> name);

}
