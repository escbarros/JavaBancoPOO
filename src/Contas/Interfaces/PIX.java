package Contas.Interfaces;

import Excessoes.ContaInexistenteException;
import Excessoes.PixJaExistenteException;
import Excessoes.SaldoInsuficiente;

public interface PIX {
    void cadastraPix() throws PixJaExistenteException;
    void efetuaPix(String cpf, float valor) throws ContaInexistenteException, SaldoInsuficiente;
    void recebePix(String cpf, float valor);

}
