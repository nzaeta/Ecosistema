package semillero.ecosistema.dto;

import jakarta.validation.constraints.*;
import lombok.*;
import org.hibernate.validator.constraints.URL;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProviderUpdateRequestDto {

    private String id;
    private String name;
    private String description;
    private String numberPhone;
    private String email;
    private String facebook;
    private String instagram;
    private String about; // descripcion larga del producto

    /*@NotEmpty (message = "Campo obligatorio.")
    @Size(min=1, max=3, message = "El proveedor debe contener entre 1 y 3 imágenes.")
    private List<MultipartFile> images;*/

    private List<String> imagenesParaBorrar;
    private List<MultipartFile> imagenesNuevas;
    private String usersId;
    private boolean imagenesBorrarVacio;
    private boolean imagenesNuevasVacio;

    private String categoryId;
    private String countryId; // pais ID
    private String provinceId; // provincia/estado ID
    private String city;

}
