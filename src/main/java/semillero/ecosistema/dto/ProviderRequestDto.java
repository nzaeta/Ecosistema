package semillero.ecosistema.dto;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.URL;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Getter @Setter
public class ProviderRequestDto {

    private String id;
    private String categoryId;
    private String countryId; // pais ID
    private String provinceId; // provincia/estado ID

    @NotEmpty(message = "Campo obligatorio.")
    @Size(min = 2, max = 30, message = "Debe contener entre 2 y 30 caracteres.")
    private String name;

    @NotEmpty(message = "Campo obligatorio.")
    @Size(min = 15, max = 100, message = "Debe contener entre 15 y 100 caracteres.")
    private String description;

    @NotNull(message = "Campo obligatorio.")
    @Pattern(regexp = "^\\+\\d{10,16}$",
            message = "Formato: +(cod país)(cod área)(nro tel) entre 10 y 16 caracteres.")
    private String numberPhone;

    @NotEmpty(message = "Campo obligatorio.")
    @Email(message = "Debe ingresar una dirección de correo válida.")
    @Pattern(regexp = "^[a-zA-Z0-9_!#$%&'*+/=?{|}~^.-]+@[a-zA-Z0-9.-]+$",
            message = "Debe ingresar una dirección de correo válida.")
    private String email;

//    @NotEmpty(message = "Campo obligatorio.")
    @URL(message = "Debe ingresar un website válido.")
    private String facebook;

//    @NotEmpty(message = "Campo obligatorio.")
    @URL(message = "Debe ingresar un website válido.")
    private String instagram;

    @NotEmpty(message = "Campo obligatorio.")
    private String city;

    @NotEmpty(message = "Campo obligatorio.")
    @Size(min = 20, max = 300, message = "Debe contener entre 20 y 300 caracteres.")
    private String about; // descripcion larga del producto

    @Size(min=1, max=3, message = "El proveedor debe contener entre 1 y 3 imágenes.")
    private List<MultipartFile> images;

    private Boolean isNew;
    private Boolean deleted;
    private String status;
    private Boolean openFullImage;
    private String feedBack;

}