package Contas;

import Contas.Enums.TipoOperacao;

import java.time.format.DateTimeFormatter;
import java.time.LocalDateTime;

public class Operacao {
    TipoOperacao tipoOperacao;
    final DateTimeFormatter formatador = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    String data;
    float valor;
    float saldo;

    public Operacao(TipoOperacao tipoOperacao, float valor, float saldo){
        this.tipoOperacao = tipoOperacao;
        this.data =formatador.format(LocalDateTime.now());
        this.valor = valor;
        this.saldo = saldo;
    }
    public float getSaldo(){
        return this.saldo;
    }
    public String getOperacao(Boolean mostraData) {
        String corOperacao = this.tipoOperacao.ordinal() == TipoOperacao.PIX_OUT.ordinal() || this.tipoOperacao.ordinal() == TipoOperacao.SAQUE.ordinal() ? Cores.VERMELHO : Cores.VERDE;
        if(mostraData){
            return Cores.VERDE+this.data+"\t"+Cores.RESET+this.tipoOperacao+corOperacao + "\t...\t"+this.valor + Cores.RESET;
        }else{
            return "        \t"+Cores.RESET+this.tipoOperacao+ corOperacao + "\t...\t"+this.valor + Cores.RESET;
        }
    }

    public String getData() {
        return data;
    }
}
