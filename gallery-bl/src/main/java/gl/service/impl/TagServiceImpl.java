package gl.service.impl;

import gl.repository.TagRepository;

import gl.model.entity.TagEntity;
import gl.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class TagServiceImpl implements TagService {

    @Autowired
    private TagRepository repository;


    public Optional<TagEntity> findByName(String name){ return repository.findByName(name); }

    public Set<TagEntity> resolveInputToTags(Set<String> tags) {

        Set<TagEntity> updatedTagSet = findTagEntitiesByNameIn(tags);
        Set<String> existingTagNames = updatedTagSet.stream().map( t -> t.getName()).collect(Collectors.toSet());

        // create new tags
        for (String tag : tags) {
            String name = tag.trim().toLowerCase();
            if (!existingTagNames.contains(tag)){
                TagEntity newTag = new TagEntity();
                newTag.setCreatedDate(new Date());
                newTag.setName(name);
                updatedTagSet.add(newTag);
            }
        }
        return updatedTagSet;
    }

    @Override
    public Set<TagEntity> findTagEntitiesByNameIn(Set<String> names) {
        return repository.findTagEntitiesByNameIn(names);
    }
}
