package semillero.ecosistema.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import java.util.List;

@Entity
@Table(name = "proveedores")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProviderEntity {

    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid",strategy = "uuid2")
    private String id;

    private String name;
    private String description;
    private String numberPhone;
    private String email;
    private String facebook;
    private String instagram;
    private String city;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "providerId")
    private List<ImageEntity> imagenes;


    private Boolean deleted;
    private String feedBack;

    private Boolean isNew;           // ESTO PARA QUÉ ES??
    private String status;           // ESTO PARA QUÉ ES??
    private Boolean openFullImage;   // ESTO PARA QUÉ ES??

    @Column(length = 300)
    private String about; // descripcion larga del producto


    @ManyToOne()
    @JoinColumn(name = "countryId") //, insertable = false, updatable = false)
    @JsonIgnore
    private CountryEntity country;

    @ManyToOne()
    @JoinColumn(name = "provinceId") //, insertable = false, updatable = false)
    @JsonIgnore
    private ProvinceEntity province;

    @ManyToOne()
    @JoinColumn(name = "userId")
    @JsonIgnore
    private UserEntity user;

    @ManyToOne()
    @JoinColumn(name = "categoryId") //, insertable = false, updatable = false)
    @JsonIgnore
    private CategoryEntity category;

}