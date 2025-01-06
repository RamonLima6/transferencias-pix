package br.com.ramon.pix.exception;

import java.io.Serial;

public class ResourceNotFoundException extends RuntimeException {
    @Serial
    private static final long serialVersionUID = 1L;

    public ResourceNotFoundException(){
        super("Pagamento não encontrado");
    }
}
