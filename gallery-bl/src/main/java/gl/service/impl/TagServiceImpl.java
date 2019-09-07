package gl.service.impl;

import gl.repository.TagRepository;

import gl.model.entity.TagEntity;
import gl.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class TagServiceImpl implements TagService {

    @Autowired
    private TagRepository repository;


    public Optional<TagEntity> findByName(String name){ return repository.findByName(name); }

    public Set<TagEntity> resolveInputToTags(String tags, Set<TagEntity> existingTagList) {

        Set<TagEntity> tagSet = existingTagList;
        String[] tagsArray = tags.split(",");
        for (String tag : tagsArray) {
            String name = tag.trim().toLowerCase();
            Optional<TagEntity> tagOptional = findByName(name);
            if (!tagOptional.isPresent()) {
                TagEntity newTag = new TagEntity();
                newTag.setCreatedDate(new Date());
                newTag.setName(name);
                tagSet.add(newTag);
            }
        }
        return tagSet;
    }

    @Override
    public List<TagEntity> findTagEntitiesByNameIn(List<String> names) {
        return repository.findTagEntitiesByNameIn(names);
    }

}
