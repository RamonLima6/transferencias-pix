package br.com.ramon.pix.models.entities;

import br.com.ramon.pix.models.enums.TipoChave;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Embeddable
@Data
public class DestinoPagamento {
    @NotNull(message = "A Chave Pix é obrigatória!")
    private String chavePix;

    @Enumerated(EnumType.STRING)
    private TipoChave tipoChavePix;
}
