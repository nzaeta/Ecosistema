package semillero.ecosistema.Dto;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class ProviderResponseDto {
    private Long id;

    private String categoryName;

    private String countryName; // nombre pais

    private String provinceName; // nombre provincia/estado

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


    private Boolean deleted;

    private String status;

    private Boolean openFullImage;

    private String feedBack;
}

