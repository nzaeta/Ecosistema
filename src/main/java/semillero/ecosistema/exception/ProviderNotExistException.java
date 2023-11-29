package semillero.ecosistema.exception;

public class ProviderNotExistException extends RuntimeException {
    public ProviderNotExistException () {
        super("El proveedor no existe");
    }
}