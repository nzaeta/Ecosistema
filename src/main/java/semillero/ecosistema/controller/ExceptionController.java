package semillero.ecosistema.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import semillero.ecosistema.Dto.ErrorValidationDto;

import java.util.HashMap;

@ControllerAdvice
public class ExceptionController {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> fallaValidacion(MethodArgumentNotValidException ex){
        HashMap<String, String> errores = new HashMap<>();
        ex.getFieldErrors()
                .forEach(field-> errores.put(field.getField(),field.getDefaultMessage()));
        return new ResponseEntity<>(new ErrorValidationDto(400,errores), HttpStatus.BAD_REQUEST);
    }
}
