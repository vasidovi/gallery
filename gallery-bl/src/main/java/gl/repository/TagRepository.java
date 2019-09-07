package gl.repository;

import gl.model.entity.TagEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface TagRepository extends JpaRepository<TagEntity, Long> {

    @Query("Select t from TagEntity t where t.name=:name")
    Optional<TagEntity> findByName(@Param("name") String name);

    @Query("Select t from TagEntity t where t.name in :names")
    Set<TagEntity> findTagEntitiesByNameIn(Set<String> names);

}
