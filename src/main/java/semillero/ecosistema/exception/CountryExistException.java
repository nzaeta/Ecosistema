package semillero.ecosistema.exception;

public class CountryExistException extends RuntimeException {
    public CountryExistException(){
        super("El pais ya se encuentra registado");
    }
}
