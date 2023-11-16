package semillero.ecosistema.exception;

public class CountryNotExistException extends RuntimeException{
    public CountryNotExistException(){
        super("El pais no se encuentra registrado");
    }
}
