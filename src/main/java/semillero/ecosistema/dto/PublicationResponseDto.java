package semillero.ecosistema.Dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
    private List<String> images;
    private int visualizations;


}

