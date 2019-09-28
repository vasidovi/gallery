package gl.model.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Data
@Entity
@Table(name = "image")
@NoArgsConstructor
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
    @Transient
    private QualityImageFileEntity qualityImageFile;

    public ImageEntity(byte[] bytes, String imageFormat){
        this.file = bytes;
        this.imageFormat = imageFormat;
    }
}
