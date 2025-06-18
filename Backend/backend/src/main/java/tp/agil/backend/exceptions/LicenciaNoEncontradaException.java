package tp.agil.backend.exceptions;

public class LicenciaNoEncontradaException extends RuntimeException {
    public LicenciaNoEncontradaException(String mensaje) {
        super(mensaje);
    }
}