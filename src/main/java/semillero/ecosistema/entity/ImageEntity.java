package semillero.ecosistema.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ImageEntity {


        @Id
        @GeneratedValue(generator ="uuid")
        @GenericGenerator(name = "uuid",strategy = "uuid2")
        private String id;
        private String name;
        private String imagenUrl;
        private String imagenId; // Es el id de Cloudinary



}
