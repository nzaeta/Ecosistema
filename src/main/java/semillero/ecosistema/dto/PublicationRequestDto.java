package semillero.ecosistema.Dto;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;
import semillero.ecosistema.entity.ImageEntity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Getter
@Setter
public class PublicationRequestDto {
    private String id;
    private String user_id;
    private String title;
    private String content;
    private boolean hidden;
    private Date date;
    private List<MultipartFile> images;
    private int visualizations;
}
