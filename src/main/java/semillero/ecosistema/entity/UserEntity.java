package semillero.ecosistema.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table(name = "usuarios")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nombre;
    private String apellido;
    private String email;
    private String password;
    private boolean deleted;
    private String rol;
    private String telefono;

    @OneToMany(mappedBy = "users", cascade = CascadeType.ALL)
    private List<ProviderEntity> providerEntityList;
}