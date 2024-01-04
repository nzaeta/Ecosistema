package semillero.ecosistema.exception;

public class ImagenesPorPublicacionException extends RuntimeException{

    public ImagenesPorPublicacionException () {super ("La publicación debe tener entre una y tres imágenes");}
}
