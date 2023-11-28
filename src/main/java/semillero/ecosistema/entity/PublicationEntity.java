package semillero.ecosistema.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Entity
@Table(name = "publicaciones")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PublicationEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String titulo;
    @Column(length = 2500)
    private String descripcion;
    private boolean deleted;
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaCreacion;
    private String imagenes;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserEntity usuarioCreador;
    private int cantVisualizaciones;
}
