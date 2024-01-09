package semillero.ecosistema.entity;

import jakarta.persistence.*;
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
        private String cloudinaryId; // Es el id de Cloudinary
        @ManyToOne
        @JoinColumn(name = "providerId")
        private ProviderEntity provider;

        @ManyToOne
        @JoinColumn(name = "publicacionId")
        private PublicationEntity publicationEntity;


}
