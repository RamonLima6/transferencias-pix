package br.com.ramon.pix.validation;

import br.com.ramon.pix.models.enums.TipoChave;

public class ChaveValidation {
    public static TipoChave inferirTipoChave(String chavePix){
        if(chavePix.contains("@")){
            return TipoChave.EMAIL;
        }else if(chavePix.length() == 11){
            long chavePixInt = Long.parseLong(chavePix);
            chavePixInt = somarAlgarismos(chavePixInt);
            if(chavePixInt == 33 || chavePixInt == 44 || chavePixInt == 55 || chavePixInt == 66){
                return TipoChave.CPF;
            }else{
                return TipoChave.TELEFONE;
            }
        }else{
            return TipoChave.ALEATORIA;
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
