package gl.model.entity;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "image_file")
public class QualityImageFileEntity {
    @Id
    @GeneratedValue
    @Column(name = "id")
    private Long id;

    @Lob
    private byte[] file;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinTable(name = "image_file_image",
            joinColumns = @JoinColumn(name = "image_file_id"),
            inverseJoinColumns = @JoinColumn(name = "image_id")
    )

    private ImageEntity image;
}
