package gl.specification;

import gl.model.entity.CatalogEntity;
import gl.model.entity.ImageEntity;
import gl.model.entity.TagEntity;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Component
public class ImageSpecification  {


    public Specification<ImageEntity> getImagesByFilterParameters(String search,
                                                                  Set<String> tagSet,
                                                                  List<Long> catalogIds) {

            String searchLowercase = search.trim().toLowerCase();
            Specification<ImageEntity> finalSpecification =  getImagesByNameLike(searchLowercase)
                    .or(getImagesByDescriptionLike(searchLowercase));

            if (tagSet.size() > 0) {
                    finalSpecification = finalSpecification.and(getImagesByTagsNamesEqual(tagSet));
            }

            if (catalogIds.size() > 0) {
                if (finalSpecification != null) {
                    finalSpecification = (finalSpecification).and(getImagesByCatalogIdsEqual(catalogIds));
                } else {
                    finalSpecification = getImagesByCatalogIdsEqual(catalogIds);
                }
            }
            return finalSpecification;
        }

    private Specification<ImageEntity> getImagesByNameLike(String name) {
        return (root, query, criteriaBuilder) ->{
            return criteriaBuilder.like(root.get("name"), "%" + name + "%");
        };
    }

    private Specification<ImageEntity> getImagesByDescriptionLike(String description) {
        return (root, query, criteriaBuilder) -> {
            return criteriaBuilder.like(root.get("description"), "%" + description + "%");
        };
    }

    private Specification<ImageEntity> getImagesByTagsNamesEqual(Set<String> tagSet) {
        return (root, query, criteriaBuilder) -> {
            query.distinct(true);
            Join<ImageEntity, TagEntity> tagJoin = root.join("tags", JoinType.LEFT);
            List<String> tagsList = new ArrayList<>(tagSet);

            Predicate tagPredicate = criteriaBuilder.equal(tagJoin.get("name"), tagsList.get(0));
                if (tagsList.size() > 1) {
                    for (int i = 1; i < tagsList.size(); i++) {
                        Predicate currentPredicate = criteriaBuilder.equal(tagJoin.get("name"), tagsList.get(i));
                        tagPredicate = criteriaBuilder.or(tagPredicate, currentPredicate);
                    }
                }

            return tagPredicate;
        };
    }

    private Specification<ImageEntity> getImagesByCatalogIdsEqual(List<Long> catalogIds) {
        return (root, query, criteriaBuilder) -> {

            query.distinct(true);
            Join<ImageEntity, CatalogEntity> catalogs = root.join("catalogs", JoinType.LEFT);

            Predicate catalogPredicate = criteriaBuilder.equal(catalogs.get("id"), catalogIds.get(0));

            if (catalogIds.size() > 1) {
                    for (int i = 1; i < catalogIds.size(); i++) {
                        Predicate currentPredicate = criteriaBuilder.equal(catalogs.get("id"), catalogIds.get(i));
                        catalogPredicate = criteriaBuilder.or(catalogPredicate, currentPredicate);
                    }
                }

            return catalogPredicate;
        };
    }

}
