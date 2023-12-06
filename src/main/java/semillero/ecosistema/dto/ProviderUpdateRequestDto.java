package semillero.ecosistema.Dto;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.URL;

@Getter
@Setter
public class ProviderUpdateRequestDto {

    private Long id;
    private Long categoryId;
    private Long countryId; // pais ID
    private Long provinceId; // provincia/estado ID

    @NotEmpty(message = "Campo obligatorio.")
    @Size(min = 2, max = 30, message = "Debe contener entre 2 y 30 caracteres.")
    private String name;

    @NotEmpty (message = "Campo obligatorio.")
    @Size(min = 20, max = 100, message = "Debe contener entre 20 y 100 caracteres.")
    private String description;

    @NotNull(message = "Campo obligatorio.")
    @Pattern(regexp = "^\\+\\d{10,16}$",
            message = "Formato: +(cod país)(cod área)(nro tel) entre 10 y 16 caracteres.")
    private String numberPhone;

    @NotEmpty (message = "Campo obligatorio.")
    @Email(message = "Debe ingresar una dirección de correo válida.")
    @Pattern(regexp = "^[a-zA-Z0-9_!#$%&'*+/=?{|}~^.-]+@[a-zA-Z0-9.-]+$",
            message = "Debe ingresar una dirección de correo válida.")
    private String email;

    @NotEmpty (message = "Campo obligatorio.")
    @URL(message = "Debe ingresar un website válido.")
    private String facebook;

    @NotEmpty (message = "Campo obligatorio.")
    @URL (message = "Debe ingresar un website válido.")
    private String instagram;

    @NotEmpty(message = "Campo obligatorio.")
    private String city;

    @NotEmpty (message = "Campo obligatorio.")
    @Size(min = 20, max = 300, message = "Debe contener entre 20 y 300 caracteres.")
    private String about; // descripcion larga del producto

    private String image; // falta para que guarde 3 imagenes
    private Boolean isNew;
    private Boolean deleted;
    private String status;
    private Boolean openFullImage;

    private String feedBack;
    private Long usersId;
}
