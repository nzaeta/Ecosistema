package semillero.ecosistema.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import semillero.ecosistema.entity.ImageEntity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@NoArgsConstructor
public class PublicationResponseDto {
    private String id;
    private String userName;
    private String title;
    private String content;
    private boolean hidden;
    private Date date;
    private List<ImageDto> images;
    private int visualizations;


}

