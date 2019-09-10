package gl.model.entity;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Data
@Entity
@Table(name = "image")
public class ImageEntity {

    @Id
    @GeneratedValue
    @Column(name = "id")
    private Long id;

    @Lob
    private byte[] file;

    private String description;

    private String name;

    @Temporal(TemporalType.DATE)
    private Date date;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "creation_datetime")
    private Date creationDateTime;

    @Column(name = "image_format")
    private String imageFormat;

    @ManyToMany(cascade = {
            CascadeType.PERSIST,
            CascadeType.MERGE
    })
    @JoinTable(name = "image_tag",
            joinColumns = @JoinColumn(name = "image_id"),
            inverseJoinColumns = @JoinColumn(name = "tag_id")
    )
    private Set<TagEntity> tags = new HashSet<>();

    @ManyToMany(cascade =
            {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(name = "image_catalog",
            joinColumns = @JoinColumn(name = "image_id"),
            inverseJoinColumns = @JoinColumn(name = "catalog_id")
    )
    private Set<CatalogEntity> catalogs = new HashSet<>();

    @OneToOne(mappedBy = "image")
//    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
//    @JoinTable(name = "image_file_image",
//            joinColumns = @JoinColumn(name = "image_id"),
//            inverseJoinColumns =   @JoinColumn(name = "image_file_id")
//    )
    @Transient
    private QualityImageFileEntity qualityImageFile;

    public ImageEntity(){};
    public ImageEntity(byte[] bytes, String imageFormat){
        this.file = bytes;
        this.imageFormat = imageFormat;
    }

    public void addTag(TagEntity tag) {
        tags.add(tag);
        tag.getImages().add(this);
    }

    public void removeTag(TagEntity tag) {
        tags.remove(tag);
        tag.getImages().remove(this);
    }

    public void addCatalog(CatalogEntity catalog) {
        catalogs.add(catalog);
        catalog.getImages().add(this);
    }

    public void removeCatalog(CatalogEntity catalog) {
        catalogs.remove(catalog);
        catalog.getImages().remove(this);
    }




}
