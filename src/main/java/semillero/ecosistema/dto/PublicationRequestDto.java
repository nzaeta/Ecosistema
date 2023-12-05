package semillero.ecosistema.Dto;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Getter
@Setter
public class PublicationRequestDto {
    private Long id;
    private Long user_id;
    private String title;
    private String content;
    private boolean hidden;
    private Date date;
    private List<String> images;
    private int visualizations;
}
