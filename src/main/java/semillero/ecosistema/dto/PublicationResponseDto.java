package semillero.ecosistema.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import semillero.ecosistema.entity.PublicationEntity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class PublicationResponseDto {
    private Long id;
    private String userName;
    private String title;
    private String content;
    private boolean hidden;
    private Date date;
    private List<ArrayList> images;
    private int visualizations;


}

