package semillero.ecosistema.exception;

public class PublicationNotExistException extends RuntimeException{
    public PublicationNotExistException(){
        super("La publicacion no existe");
    }
}
