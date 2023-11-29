package semillero.ecosistema.exception;

public class ProviderSearchException extends RuntimeException {

    public ProviderSearchException () {
        super("Debe ingresar al menos 3 caracteres");
    }
}
