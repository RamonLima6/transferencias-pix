package br.com.ramon.pix.validation;

import br.com.ramon.pix.models.entities.Pagamento;
import br.com.ramon.pix.models.enums.TipoChave;

public class ChaveValidation {
    public static void inferirTipoChave(Pagamento pagamento) {
        if(pagamento.getDestino().getChavePix().contains("@")){
            pagamento.getDestino().setTipoChavePix(TipoChave.EMAIL);
        }else if(pagamento.getDestino().getChavePix().length() == 11){
            long chavePixInt = Long.parseLong(pagamento.getDestino().getChavePix());
            chavePixInt = somarAlgarismos(chavePixInt);
            if(chavePixInt == 33 || chavePixInt == 44 || chavePixInt == 55 || chavePixInt == 66){
                pagamento.getDestino().setTipoChavePix(TipoChave.CPF);
            }else{
                pagamento.getDestino().setTipoChavePix(TipoChave.TELEFONE);
            }
        }else if(pagamento.getDestino().getChavePix().matches("[0-9a-fA-F\\-]{36}")){
            pagamento.getDestino().setTipoChavePix(TipoChave.ALEATORIA);
        }else{
            throw new IllegalArgumentException("Chave pix inválida.");
        }
    }
    public static int somarAlgarismos(long numero) {
        String numeroStr = String.valueOf(numero); // Converte para String
        int soma = 0;

        for (char c : numeroStr.toCharArray()) {
            soma += Character.getNumericValue(c); // Converte o caractere para número e soma
        }

        return soma;
    }
}
