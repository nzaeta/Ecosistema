package semillero.ecosistema.exception;

public class PublicationExistException extends RuntimeException{
    public PublicationExistException(){
        super("La publicacion ya esta Creada");
    }
}
