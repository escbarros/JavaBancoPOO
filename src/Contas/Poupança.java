package Contas;
import Contas.Enums.TipoOperacao;
import Contas.Interfaces.Remunerada;
import Excessoes.CpfException;

public class Poupança extends Conta implements Remunerada{

    public Poupança(String nome, String cpf) throws CpfException {
        super(nome, cpf);
    }
    public void aplicaCorrecao(float correcao) {
        float taxa = this.saldo * correcao;
        this.saldo += taxa;
        listaOperacoes.add(new Operacao(TipoOperacao.CORRECAO, taxa, this.saldo));
    }
}
