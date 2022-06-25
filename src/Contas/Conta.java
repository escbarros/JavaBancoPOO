package Contas;

import java.util.ArrayList;
import java.util.Random;

import Contas.Enums.TipoOperacao;
import Excessoes.*;
public abstract class Conta {

    public String numeroConta;

    public String nomeCliente;

    protected String cpfCliente;

    protected ArrayList<Operacao> listaOperacoes = new ArrayList<Operacao>();

    protected float saldo = 0;
    public Conta(String nome, String cpf) throws CpfException{
        geraNumConta();
        this.nomeCliente = nome;
        cpf = cpf.replaceAll("\\D+","");
        if(cpfValido(cpf)) this.cpfCliente = String.format("%c%c%c.%c%c%c.%c%c%c-%c%c", cpf.charAt(0), cpf.charAt(1), cpf.charAt(2), cpf.charAt(3), cpf.charAt(4), cpf.charAt(5), cpf.charAt(6), cpf.charAt(7), cpf.charAt(8), cpf.charAt(9), cpf.charAt(10));
        else throw new CpfException("Cpf inválido");
    }



    private boolean cpfValido(String cpf) {
        if(cpf.length() != 11) return false;
        else{
            char dig10, dig11;
            int sm, i, r, num, peso;
            sm = 0;
            peso = 10;
            for (i = 0; i < 9; i++) {
                num = (int) (cpf.charAt(i) - 48);
                sm = sm + (num * peso);
                peso = peso - 1;
            }

            r = 11 - (sm % 11);
            if ((r == 10) || (r == 11)) dig10 = '0';
            else dig10 = (char) (r + 48);
            sm = 0;
            peso = 11;
            for (i = 0; i < 10; i++) {
                num = (int) (cpf.charAt(i) - 48);
                sm = sm + (num * peso);
                peso = peso - 1;
            }

            r = 11 - (sm % 11);
            if ((r == 10) || (r == 11))
                dig11 = '0';
            else
                dig11 = (char) (r + 48);

            if ((dig10 == cpf.charAt(9)) && (dig11 == cpf.charAt(10)))
                return (true);
            else
                return (false);
        }
    }

    public void depositar(float deposito){
        this.saldo+= deposito;
        listaOperacoes.add(new Operacao(TipoOperacao.DEPOSITO, deposito, this.saldo));
    }

    public void sacar(float saque) throws SaldoInsuficiente{
        if (saque >= this.saldo) throw new SaldoInsuficiente("Seu saldo é inválido para realizar essa operação!\nSeu saldo: "+ this.saldo +"\nValor a sacar: " + saque);
        else{
            this.saldo -= saque;
            listaOperacoes.add(new Operacao(TipoOperacao.SAQUE, saque, this.saldo));
        }
    }
    private void geraNumConta(){
        Random rd = new Random();
        this.numeroConta = String.format("%d.%d%d%d-%d", rd.nextInt(10), rd.nextInt(10), rd.nextInt(10), rd.nextInt(10), rd.nextInt(10) );
    }

    @Override
    public String toString() {
        return Cores.CIANO + "Nome: " + Cores.AZUL + this.nomeCliente +
                Cores.CIANO + "\nCPF: " + Cores.AZUL + this.cpfCliente +
                Cores.CIANO + "\nNúmero conta: " + Cores.AZUL + this.numeroConta +
                Cores.CIANO + "\nSaldo: " + Cores.AZUL + this.saldo + Cores.RESET;
    }

    public String getNum() {
        return numeroConta;
    }

    public float getSaldo() {
        return this.saldo;
    }


    public ArrayList<Operacao> getExtrato() {
        return listaOperacoes;
    }
}
