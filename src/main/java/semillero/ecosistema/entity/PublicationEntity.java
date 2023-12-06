package semillero.ecosistema.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "publicaciones")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PublicationEntity {
    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid",strategy = "uuid2")
    private String id;
    @Column(name="titulo")
    private String title;
    @Column(name="descripcion", length = 2500)
    private String content;
    @Column(name="deleted")
    private boolean hidden;
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="fecha_creacion")
    private Date date;
    @ElementCollection
    @Column(name="imagenes")
    private List<String> images = new ArrayList<>();
    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserEntity usuarioCreador;
    @Column(name="cant_visualizaciones")
    private int visualizations;
}
