package semillero.ecosistema.exception;

public class ProvinceExistException extends RuntimeException {
    public ProvinceExistException(){
        super("La pronvincia ya existe");
    }
}
