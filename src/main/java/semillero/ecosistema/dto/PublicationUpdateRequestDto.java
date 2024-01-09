package semillero.ecosistema.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;
import java.util.List;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PublicationUpdateRequestDto {
    private String id;
    private String user_id;
    private String title;
    private String content;
    private boolean hidden;
    @DateTimeFormat(pattern= "yyyy-MM-dd")
    private Date date;
    private List<String> imagenesParaBorrar;
    private List<MultipartFile> imagenesNuevas;
    private int visualizations;
}
