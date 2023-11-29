package semillero.ecosistema.Dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProviderUpdateRequestDto {
    private Long id;
    private Long usersId;
    private Long categoryId;
    private Long countryId; // pais ID
    private Long provinceId; // provincia/estado ID
    private String name;
    private String description;
    private String numberPhone;
    private String email;
    private String facebook;
    private String instagram;
    private String city;
    private String about; // descripcion larga del producto
    private String image; // falta para que guarde 3 imagenes
    private Boolean isNew;
    private Boolean active;
    private Boolean deleted;
    private String status;
    private Boolean openFullImage;
    private String feedBack;
}
