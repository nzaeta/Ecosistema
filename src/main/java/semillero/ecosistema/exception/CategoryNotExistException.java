package semillero.ecosistema.exception;

public class CategoryNotExistException extends RuntimeException {
    public CategoryNotExistException () {
        super("La categoria ingresado no existe");
    }
}