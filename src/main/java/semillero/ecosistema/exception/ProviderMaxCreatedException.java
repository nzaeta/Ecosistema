package semillero.ecosistema.exception;

public class ProviderMaxCreatedException extends RuntimeException {
    public ProviderMaxCreatedException () {
        super("Has superado el limite de 3 proveedores creados por usuario");
    }
}
