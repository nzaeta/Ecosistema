package semillero.ecosistema.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "proveedores")
@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProviderEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

//    private Long userId; // QUTAR

    private String name;
    private String description;
    private String numberPhone;
    private String email;

    /**
     * Variables extra
     */
    private String facebook;
    private String instagram;

    private String city;
    private String image;
    private Boolean active;
    private Boolean deleted;
    private String status;

    /**
     * variables extra
     */
    private String feedBack;

    /**
     * RELACIONES o VARIABLES ENTRE TABLAS SEGUN UML *
     *
     *
     * private Pais pais
     * private Provincia provincia
     * private Categoria categoria
     *
     */

    @ManyToOne()
    @JoinColumn(name = "userId") // insertable = false, updatable = false
    private UserEntity users;

}