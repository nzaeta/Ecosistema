package semillero.ecosistema.Dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProviderUpdateStatusRequestDto {
    private Long providerId;
    private String newStatus;
    private String newFeedBack;
}
