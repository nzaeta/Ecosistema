package semillero.ecosistema.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;
import semillero.ecosistema.dto.PublicationRequestDto;
import semillero.ecosistema.dto.PublicationResponseDto;
import semillero.ecosistema.entity.PublicationEntity;
import semillero.ecosistema.service.contracts.PublicationService;

import java.util.List;

@RestController
@RequestMapping(path="/Publications")
public class PublicationController {
    private final PublicationService publicationService;


    public PublicationController(PublicationService publicationService) {
        this.publicationService = publicationService;
    }

    @Secured("ADMIN")
    @GetMapping
    public List<PublicationResponseDto> getAllPublications() { return publicationService.getAll();
    }

    @Secured("ADMIN")
    @GetMapping("/get-titulo")
    public PublicationResponseDto getByTitulo(@RequestParam String titulo){
        return publicationService.getByTitulo(titulo);
    }

    @Secured("ADMIN")
    @GetMapping("/get-id")
    public PublicationResponseDto getById(@RequestParam Long id){
        return publicationService.getById(id);
    }

    @Secured("ADMIN")
    @GetMapping("/get-not-deleted")
    public List<PublicationResponseDto> getNotDeleted(){
        return publicationService.getByDeletedFalse();
    }

    @Secured("ADMIN")
    @GetMapping("/get-user-id")
    public List<PublicationResponseDto> getByUserId (Long user_id){
        return publicationService.getByUsuarioId(user_id);
    }

    @Secured("ADMIN")
    @PatchMapping("/increment-view")
    public void incrementView(@RequestParam Long id){
        publicationService.incrementViewCount(id);
    }

    @Secured("ADMIN")
    @PostMapping("/save")
    public PublicationResponseDto save(@RequestBody PublicationRequestDto publicationRequestDto){
        return publicationService.save(publicationRequestDto);
    }

    @Secured("ADMIN")
    @PatchMapping("/update")
    public PublicationResponseDto update(@RequestBody PublicationRequestDto publicationRequestDto){
        return publicationService.update(publicationRequestDto);
    }

    @Secured("ADMIN")
    @PatchMapping("/delete")
    public void delete(@RequestParam Long id){
        publicationService.delete(id);
    }

}
