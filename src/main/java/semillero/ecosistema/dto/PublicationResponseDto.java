package semillero.ecosistema.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class PublicationResponseDto {
    private Long id;
    private String userName;
    private String titulo;
    private String descripcion;
    private boolean deleted;
    private Date fechaCreacion;
    private String imagenes;
    private int cantVisualizaciones;

}
