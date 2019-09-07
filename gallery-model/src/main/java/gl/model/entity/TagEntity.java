package gl.model.entity;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Data
@Entity
@Table(name = "tag")
public class TagEntity {

    @Id
    @GeneratedValue
    private Long id;
    @Column(name ="NAME", nullable = false, unique = true)
    private String name;
    @Column(name = "CREATED_DATE")
    private Date createdDate;

    @ManyToMany(mappedBy = "tags")
    @Transient
    private Set<ImageEntity> images = new HashSet<>();



}
