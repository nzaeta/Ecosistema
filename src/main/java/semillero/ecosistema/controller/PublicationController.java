package semillero.ecosistema.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;
import semillero.ecosistema.Dto.PublicationRequestDto;
import semillero.ecosistema.Dto.PublicationResponseDto;
import semillero.ecosistema.entity.PublicationEntity;
import semillero.ecosistema.service.MailService;
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
    ResponseEntity<?> getAllPublications() { return publicationService.getAll();}

    @GetMapping("/get-title")
    ResponseEntity<?> getByTitle(@RequestParam String title){
        return publicationService.getByTitle(title);
    }

    @GetMapping("/get-id")
    ResponseEntity<?> getById(@RequestParam String id){
        return publicationService.getById(id);
    }

    @GetMapping("/get-not-deleted")
    ResponseEntity<?> getNotDeleted(){
        return publicationService.getByDeletedFalse();
    }

    @GetMapping("/get-user-id")
    ResponseEntity<?> getByUserId (String user_id){
        return publicationService.getByUsuarioId(user_id);
    }

    @PatchMapping("/increment-view")
    void incrementView(@RequestParam String id){
        publicationService.incrementViewCount(id);
    }

    @Secured("ADMIN")
    @PostMapping("/save")
    ResponseEntity<?> save(@RequestBody PublicationRequestDto publicationRequestDto){
        return publicationService.save(publicationRequestDto);
    }

    @Secured("ADMIN")
    @PatchMapping("/update")
    ResponseEntity<?> update(@RequestBody PublicationRequestDto publicationRequestDto){
        return publicationService.update(publicationRequestDto);
    }

    @Secured("ADMIN")
    @PatchMapping("/delete")
    ResponseEntity<?> delete(@RequestParam String id){
        return publicationService.delete(id);
    }

    @Secured("ADMIN")
    @PatchMapping("/active")
    ResponseEntity<?> active(@RequestParam String id){
        return publicationService.active(id);
    }

    @GetMapping("/mail")
    void sendEmail(){

    }

}
