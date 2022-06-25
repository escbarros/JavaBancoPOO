import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Objects;
import java.util.Scanner;
import Contas.*;
import Contas.Enums.TipoConta;
import Excessoes.ContaInexistenteException;
import Excessoes.CpfException;
import Excessoes.PixJaExistenteException;
import Excessoes.SaldoInsuficiente;

public class Banco {
    enum criterio{NENHUM, CORRENTE, POUPANCA}
    static Scanner input = new Scanner(System.in);
    static ArrayList<Conta> todasContas = new ArrayList<Conta>();

    public static void main(String[] args) {
        int opcao = 0;
        boolean fecharMenu = false;
        try{
            contasProntas();
            while(!fecharMenu){
                System.out.println(Cores.CIANO +"\t\tBem vindo ao menu do banco!\n" + Cores.AZUL + "Digite a operação respectiva aos números abaixo:\n"+ Cores.ROXO +"\t\t\t0 - "+ Cores.BRANCO +"Sair\n"+Cores.ROXO +"\t\t\t1 -" +Cores.BRANCO+" Criar conta Corrente" +
                        Cores.ROXO +"\n\t\t\t2 - "+ Cores.BRANCO +"Criar conta poupança"+ Cores.ROXO +"\n\t\t\t3 - "+ Cores.BRANCO +"Efetuar depósito"+ Cores.ROXO +"\n\t\t\t4 - "+ Cores.BRANCO +"Efetuar saque"+ Cores.ROXO +"\n\t\t\t5 - "+ Cores.BRANCO +"Aplicar correção"+ Cores.ROXO +"\n\t\t\t6 - "+ Cores.BRANCO +"Cadastrar PIX"+ Cores.ROXO +"\n\t\t\t7 - "+ Cores.BRANCO +"Efetuar PIX" +
                        Cores.ROXO +"\n\t\t\t8 - "+ Cores.BRANCO +"Consultar extrato");
                System.out.print(Cores.ROXO +"Digite uma das opções acima: "+ Cores.RESET);
                opcao = input.nextInt();
                switch (opcao){
                    case 0:
                        fecharMenu = true;
                        break;
                    case 1:
                        criaConta(TipoConta.CORRENTE);
                        break;
                    case 2:
                        criaConta(TipoConta.POUPANCA);
                        break;
                    case 3:
                        if(houveContaCriada(criterio.NENHUM)) deposita();
                        else System.out.println(Cores.AMARELO + "\n\tNenhuma conta foi criada ainda\n" + Cores.RESET);
                        break;
                    case 4:
                        if(houveContaCriada(criterio.NENHUM)) saque();
                        else System.out.println(Cores.AMARELO + "\n\tNenhuma conta foi criada ainda\n" + Cores.RESET);
                        break;
                    case 5:
                        if(houveContaCriada(criterio.POUPANCA)) corrige();
                        else System.out.println(Cores.AMARELO + "\n\tNenhuma conta poupança foi criada ainda\n" + Cores.RESET);
                        break;
                    case 6:
                        if(houveContaCriada(criterio.CORRENTE)) cadastraPix();
                        else System.out.println(Cores.AMARELO + "\n\tNão existe nenhuma conta corrente\n" + Cores.RESET);
                        break;
                    case 7:
                        efetuaPix();
                        break;
                    case 8:
                        if(houveContaCriada(criterio.NENHUM)) mostraExtrato();
                        else System.out.println(Cores.AMARELO + "Nenhuma conta foi criada até agora" + Cores.RESET);
                        break;
                    default:
                        System.out.println("Opção inválida");
                        break;

                }
            }
        }catch(Exception e){
            System.out.println(Cores.VERMELHO + "Algo deu errado" + Cores.RESET);
        }
    }

    static void contasProntas(){
        try {
            Corrente contaC1 = new Corrente("Eduardo", "36940175008");
            Corrente contaC2 = new Corrente("Luciana", "57426868036");
            Poupança contaP1 = new Poupança("Lucas", "53392058009");
            todasContas.add(contaC1);
            todasContas.add(contaC2);
            todasContas.add(contaP1);


            contaC1.depositar(120);
            contaC1.cadastraPix();
            contaC2.cadastraPix();
            contaC1.efetuaPix(contaC2.getCpf(), 110f);
            contaP1.depositar(300);
            contaP1.aplicaCorrecao(0.25f);
            contaP1.sacar(150);

        } catch (CpfException | PixJaExistenteException | SaldoInsuficiente e) {
            e.printStackTrace();
        }
    }

    static void criaConta(TipoConta tipo){
        String nomeCliente;
        String cpfCliente = null;
        boolean opcoesCorretas = false;
        while(!opcoesCorretas){
            try{
                System.out.println(Cores.CIANO + "Para criar sua conta, digite suas informações:\n");
                System.out.print(Cores.ROXO + "\tSeu nome: " );
                nomeCliente = input.next();
                System.out.print("\n\tDigite seu CPF: " + Cores.RESET);
                cpfCliente = input.next();
                if (tipo.ordinal() == TipoConta.CORRENTE.ordinal()) todasContas.add(new Corrente(nomeCliente, cpfCliente));
                else todasContas.add(new Poupança(nomeCliente, cpfCliente));
                System.out.println(Cores.VERDE + "Conta criada com sucesso!" + Cores.RESET);
                opcoesCorretas = true;

            }catch(CpfException  e) {
                System.out.println(Cores.VERMELHO + "\n\tO cpf que você digitou é invalido!\nExemplo Correto: XXX.XXX.XXX-XX ou XXXXXXXXXXX\n"+ Cores.RESET);
            }
        }
    }

    static boolean houveContaCriada(criterio tipo){
        if(tipo.ordinal() == criterio.NENHUM.ordinal()){
            return todasContas.size() > 0;
        }else{
            for (Conta conta : todasContas){
                if(criterio.CORRENTE.ordinal() == tipo.ordinal() && conta instanceof Corrente || criterio.POUPANCA.ordinal() == tipo.ordinal() && conta instanceof Poupança) return true;
            }
            return false;
        }
    }

    static void deposita()  {
        Conta contaSelecionada = selecionaConta(criterio.NENHUM);
        contaSelecionada.depositar(valor());
        System.out.println(Cores.VERDE + "Depósito realizado com sucesso!" + Cores.RESET);
    }
    static void saque(){
        Conta contaSelecionada = selecionaConta(criterio.NENHUM);
        float saque=0;
        try{
            saque = valor();
            contaSelecionada.sacar(saque);
            System.out.println(Cores.VERDE + "Saque realizado com sucesso!" + Cores.RESET);
        }catch(SaldoInsuficiente e){
            System.out.println(Cores.VERMELHO + "\n\tSaldo insuficiente.\n\tSeu saldo: " + contaSelecionada.getSaldo() + "\n\tQuanto você tentou sacar: " + saque + "\n" + Cores.RESET);
        }
    }
    static Conta selecionaConta(criterio tipo){
        String numConta;
        Conta contaSelecionada = null;
        while(contaSelecionada ==null){
            if(tipo.ordinal() == criterio.NENHUM.ordinal()){
                for (Conta conta : todasContas){
                    System.out.println("\n"+conta);
                }
            }else{
                for (Conta conta : todasContas){
                    if(conta instanceof Corrente) System.out.println("\n"+conta);
                }
            }
            try {
                System.out.print(Cores.ROXO + "Digite o número da conta: " + Cores.RESET);
                numConta = input.next();
                for (Conta conta : todasContas) if (conta.getNum().replaceAll("\\D+", "").equals(numConta.replaceAll("\\D+", ""))) contaSelecionada = conta;
                if (contaSelecionada == null) throw new ContaInexistenteException("Conta não existe");
            }catch (ContaInexistenteException e){
                System.out.println(Cores.VERMELHO + "\n\t Essa conta não existe\n" + Cores.RESET);
            }
        }
        return contaSelecionada;
    }
    static float valor(){
        float valor = 0;
        while(valor <= 0){
            try{
                System.out.print(Cores.ROXO + "Digite o valor: " + Cores.RESET);
                valor = input.nextFloat();
            }catch(InputMismatchException e){
                System.out.println(Cores.VERMELHO + "\n\tValor inválido\n\t" + Cores.RESET);
            }
        }
        return valor;
    }

    static void corrige(){
        float correcao = 0;
        System.out.print(Cores.ROXO + "Digite a taxa de correção: " + Cores.RESET);
        correcao = input.nextFloat();
        for (Conta conta : todasContas){
            if(conta instanceof Poupança) ((Poupança) conta).aplicaCorrecao(correcao);
        }
        System.out.println(Cores.VERDE + "Correção aplicada com sucesso!" + Cores.RESET);
    }

    private static void cadastraPix() {
        try{
            Corrente contaSelecionada = (Corrente) selecionaConta(criterio.CORRENTE);
            contaSelecionada.cadastraPix();
            System.out.println(Cores.VERDE + "\nPIX CRIADO COM SUCESSO!\n" + Cores.RESET);
        }catch(ClassCastException e){
            System.out.println(Cores.VERMELHO + "\n\tA conta com esse número nao é do tipo corrente!\n" + Cores.RESET);
        } catch (PixJaExistenteException e) {
            System.out.println(Cores.VERMELHO + "\n\tJá existe um PIX com esse CPF\n" + Cores.RESET);;
        }
    }
    private static void efetuaPix() {
        Corrente contaAPagar = null;
        String cpf;
        ArrayList<Corrente> contasPix = Corrente.getContasPix();
        if(contasPix.size() < 2){
            System.out.println(Cores.AMARELO + "\n\tPara efetuar um PIX é necessário, no mínimo, de 2 contas cadastradas no PIX\n" + Cores.RESET);
            return;
        }
        System.out.println(Cores.CIANO + "Selecione sua Conta" + Cores.RESET);
        for (Corrente conta : contasPix) System.out.println("\n" + conta + "\n");//Mostra as contas
        System.out.print(Cores.ROXO+"Digite o CPF:"+ Cores.RESET);
        cpf = input.next();
        for (Corrente conta : contasPix) if(Objects.equals(conta.getCpf().replaceAll("\\D+", ""), cpf.replaceAll("\\D+", ""))) contaAPagar = conta; //verifica a conta
        if(contaAPagar == null){
            System.out.println(Cores.VERMELHO + "\n\tConta inexistente\n" + Cores.RESET);
            return;
        }
        else{
            cpf = null;
            Corrente recebePix = null;
            System.out.println(Cores.CIANO + "Selecione a conta que vai receber o pix" + Cores.RESET);
            //Seleciona 2 conta
            for (Corrente conta : contasPix) if(conta.getCpf() != contaAPagar.getCpf())System.out.println(conta);
            System.out.print(Cores.ROXO + "Digite o CPF:" + Cores.RESET);
            cpf = input.next();
            //verifica segunda conta
            for (Corrente conta : contasPix) if(Objects.equals(conta.getCpf().replaceAll("\\D+", ""), cpf.replaceAll("\\D+", ""))){recebePix = conta;}
            if(contaAPagar == recebePix){
                System.out.println(Cores.VERMELHO+"\n\tVocê não pode enviar um pix a si mesmo\n\t"+Cores.RESET);
                return;
            }else if (recebePix == null){
                System.out.println(Cores.VERMELHO + "\n\tConta inexistente\n" + Cores.RESET);
            }else{
                float valor = valor();
                if(valor < 0.01){
                    System.out.println(Cores.VERMELHO  + "O valor do pix deve ser maior que 1 centavo" + Cores.RESET);
                    return;
                }else{
                    try {
                        contaAPagar.efetuaPix(recebePix.getCpf(), valor);
                        System.out.println(Cores.VERDE + "\n\tPIX EFETUADO COM SUCESSO!\n" + Cores.RESET);
                    } catch (SaldoInsuficiente e) {
                        System.out.println(Cores.VERMELHO +"\n\tSaldo insuficiente!\n"+Cores.RESET);
                    }
                }
            }
        }

    }

    private static void mostraExtrato() {
        System.out.println(Cores.CIANO + "Selecione a conta que você deseja ver o extrato" + Cores.RESET);
        Conta contaSelecionada = selecionaConta(criterio.NENHUM);
        ArrayList<Operacao> operacoes = contaSelecionada.getExtrato();
        Operacao operacaoAnterior = null;
        String dataAnterior=null;
        for(Operacao operacao : operacoes){
            if(operacaoAnterior == null){
                System.out.println(operacao.getOperacao(true));
                operacaoAnterior = operacao;
                dataAnterior = operacaoAnterior.getData();
            }
            else if(!(dataAnterior).equals(operacao.getData())) {
                System.out.println(Cores.PRETO + "=-=-=-=-=-=-=-=-=-=-=-=-=-=-="+ Cores.RESET+"\n\t\t\tSALDO: " + Cores.AZUL + operacaoAnterior.getSaldo()+"\n");
                System.out.println(operacao.getOperacao(true));
                operacaoAnterior = operacao;
                dataAnterior = operacaoAnterior.getData();
            }else{
                System.out.println(operacao.getOperacao(false));
            }
        }
        System.out.println(Cores.PRETO + "=-=-=-=-=-=-=-=-=-=-=-=-=-=-="+ Cores.RESET+"\n\t\t\tSALDO: " + Cores.AZUL + contaSelecionada.getSaldo()+"\n");
    }










}
