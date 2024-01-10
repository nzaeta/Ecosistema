package semillero.ecosistema.exception;

public class ExcessImageSizeException extends RuntimeException{
    public ExcessImageSizeException () {
        super("El tamaño de la imagen excele 1M");
    }
}
