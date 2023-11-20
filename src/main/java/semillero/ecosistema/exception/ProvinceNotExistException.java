package semillero.ecosistema.exception;

public class ProvinceNotExistException extends RuntimeException{
    public ProvinceNotExistException(){
        super("La provincia no se encuentra");
    }
}
