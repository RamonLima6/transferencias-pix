package br.com.ramon.pix.models.entities;

import br.com.ramon.pix.models.enums.FrequenciaRecorrencia;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.FutureOrPresent;
import lombok.Data;

import java.time.LocalDate;

@Data
@Embeddable
public class Recorrencia {
    @Enumerated(EnumType.STRING)
    private FrequenciaRecorrencia frequencia;

    @FutureOrPresent(message = "A data final da recorrência precisa ser futura!")
    private LocalDate dataFinal;
}
