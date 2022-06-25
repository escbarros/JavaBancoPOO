package Excessoes;

public class ContaInexistenteException extends Exception{
    public ContaInexistenteException(String mensagem){
        super(mensagem);
    }
}
