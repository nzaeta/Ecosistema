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
    ResponseEntity<?> getAllPublications() { return publicationService.getAll();}

    @GetMapping("/get-title")
    ResponseEntity<?> getByTitle(@RequestParam String title){
        return publicationService.getByTitle(title);
    }

    @GetMapping("/get-id")
    ResponseEntity<?> getById(@RequestParam Long id){
        return publicationService.getById(id);
    }

    @GetMapping("/get-not-deleted")
    ResponseEntity<?> getNotDeleted(){
        return publicationService.getByDeletedFalse();
    }

    @GetMapping("/get-user-id")
    ResponseEntity<?> getByUserId (Long user_id){
        return publicationService.getByUsuarioId(user_id);
    }

    @PatchMapping("/increment-view")
    void incrementView(@RequestParam Long id){
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
    ResponseEntity<?> delete(@RequestParam Long id){
        return publicationService.delete(id);
    }

}
