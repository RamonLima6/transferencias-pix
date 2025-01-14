package br.com.ramon.pix.models.entities;

import br.com.ramon.pix.models.enums.StatusPagamento;
import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.io.Serial;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Data
@Entity
@Table(name = "pix")
public class Pagamento implements java.io.Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue
    @Column(columnDefinition = "UUID")
    private UUID id;

    @Enumerated(EnumType.STRING)
    private StatusPagamento status;

    private LocalDate dataInclusao;

    @FutureOrPresent(message = "A data do pagamento deve ser atual ou futura!")
    @NotNull(message = "A data do pagamento é obrigatória!")
    private LocalDate dataPagamento;

    @DecimalMin(value = "0.01", message = "O valor deve ser maior que 0.")
    @NotNull(message = "O valor é obrigatório!")
    private BigDecimal valor;

    private String descricao;

    @Embedded
    private DestinoPagamento destino;

    @Embedded
    private Recorrencia recorrencia;
}