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
import java.util.stream.Collectors;

@Service
public class TagServiceImpl implements TagService {

    @Autowired
    private TagRepository repository;


    public Optional<TagEntity> findByName(String name){ return repository.findByName(name); }

    public Set<TagEntity> resolveInputToTags(Set<String> tags) {

        Set<TagEntity> tagsSet = findTagEntitiesByNameIn(tags);
        Set<String> existingTagNames = tagsSet.stream().map( t -> t.getName()).collect(Collectors.toSet());
        for (String tag : tags) {
            String name = tag.trim().toLowerCase();
            if (!existingTagNames.contains(tag)){
                TagEntity newTag = new TagEntity();
                newTag.setCreatedDate(new Date());
                newTag.setName(name);
                tagsSet.add(newTag);
            }
        }
        return tagsSet;
    }

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
    public Set<TagEntity> findTagEntitiesByNameIn(Set<String> names) {
        return repository.findTagEntitiesByNameIn(names);
    }

}
