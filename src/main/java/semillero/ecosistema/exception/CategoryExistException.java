package semillero.ecosistema.exception;

public class CategoryExistException extends RuntimeException {
    public CategoryExistException () {
        super("La categoria ingresada ya se encuentra registrada");
    }
}