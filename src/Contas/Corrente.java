package Contas;

import Contas.Enums.TipoOperacao;
import Contas.Interfaces.PIX;
import Excessoes.ContaInexistenteException;
import Excessoes.CpfException;
import Excessoes.PixJaExistenteException;
import Excessoes.SaldoInsuficiente;

import java.util.ArrayList;


public class Corrente extends Conta implements PIX {

    private static ArrayList<Corrente> aceitaPix = new ArrayList<Corrente>();

    public static ArrayList<Corrente> getContasPix(){
        return aceitaPix;
    }

    public Corrente(String nome, String cpf) throws CpfException {
        super(nome, cpf);
    }

    @Override
    public void cadastraPix() throws PixJaExistenteException{ //falar pro professor que adicionei na interface o throws
        boolean jaExiste = false;
        for (Corrente conta : aceitaPix) if (this.cpfCliente == conta.cpfCliente) throw new PixJaExistenteException("Um pix com esse cpf já foi criado");
        aceitaPix.add(this);

    }

    @Override
    public void efetuaPix(String cpf, float valor) throws SaldoInsuficiente {
        if (valor > this.saldo) throw new SaldoInsuficiente("Saldo Insuficiente");
        Corrente contaSelecionada = null;
        for (Corrente conta : aceitaPix){
            if (conta.cpfCliente == cpf) contaSelecionada = conta;
        }

        contaSelecionada.recebePix(this.cpfCliente, valor);
        this.saldo -= valor;
        this.listaOperacoes.add(new Operacao(TipoOperacao.PIX_OUT, valor, this.saldo));
    }

    @Override
    public void recebePix(String cpf, float valor) {
        this.saldo += valor;
        this.listaOperacoes.add(new Operacao(TipoOperacao.PIX_IN, valor, this.saldo));
    }

    public String getCpf(){
        return this.cpfCliente;
    }


}
