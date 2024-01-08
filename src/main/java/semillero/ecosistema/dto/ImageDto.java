package semillero.ecosistema.dto;

import lombok.Getter;
import lombok.Setter;
import semillero.ecosistema.entity.ProviderEntity;
import semillero.ecosistema.entity.PublicationEntity;

@Getter @Setter
public class ImageDto {
    private String id;
    private String name;
    private String imagenUrl;
    private String cloudinartId;
    private String provider;
    private String publication;

    public void setPublication (PublicationEntity publication){
        this.publication = publication.getTitle();
    }

    public void setProvider(ProviderEntity provider) {
        if(provider != null){
            this.provider = provider.getName();
        }
    }
}



