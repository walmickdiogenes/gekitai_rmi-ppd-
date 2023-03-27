
package jogo;

import jogo.Gekitai;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JOptionPane;
import jogo.Jogador;
import jogo.Tabuleiro;

/**
 *
 * @author walmi
 */
public class Flags extends UnicastRemoteObject implements Gekitai{
    
    private Map<Integer, Jogador> jogadores; 
    Tabuleiro tabuleiroMetod = new Tabuleiro();
    ArrayList<String> ultimaJogada;
    int idUltimoJogador;
    String pecaExcluida = "";
    int idUltimoChat;
    String mensagemChat="";
    
    //Flags
    int flagJogadaEnviada;
    int flagPecaExcluida;
    int flagDesistencia;
    int flagMensagem;
    int flagFecharJanela;
    
    public void setJogadores(Map<Integer,Jogador> jogadores){ this.jogadores = jogadores;}
      
    public Flags() throws RemoteException{
      super();
      this.jogadores = new HashMap();
      this.ultimaJogada = new ArrayList();
      this.flagJogadaEnviada = 0;
      this.flagPecaExcluida = 0;
      this.flagDesistencia = 0;
      this.flagMensagem = 0;
      this.flagFecharJanela = 0;
    }
    
    @Override
    public int conectarCliente(String nome) throws RemoteException {
        int idAux = jogadores.size();
        
        jogadores.put(idAux, new Jogador());
        jogadores.get(idAux).setId(idAux);
        jogadores.get(idAux).setNome(nome);
        jogadores.get(idAux).setCor("");
        
        System.out.println(jogadores.get(idAux).getId() + " - " + jogadores.get(idAux).getNome() + " se conectou.");
     
        return idAux;
    }
    
    @Override
    public boolean checkAdversarios() throws RemoteException {
        return jogadores.size()>1;
    }

    @Override
    public String definirCor(int id) throws RemoteException {
        String cor = "preta";
        jogadores.get(id).setCor(cor);
        
        for(int i=0; i<jogadores.size(); i++){
            if(id != jogadores.get(i).getId()){
                jogadores.get(i).setCor("branca");
            }
        }
        return cor;
    }
    
    @Override
    public String getCorPecas(int id) throws RemoteException {
        return jogadores.get(id).getCor();
    }
    
    @Override
    public void enviarJogada(int id, String casaInicial, String casaFinal) throws RemoteException {
        idUltimoJogador = id;
        flagJogadaEnviada = 1;
        ultimaJogada.add(0, casaInicial);
        ultimaJogada.add(1, casaFinal);
    }
    
    @Override
    public boolean checkUltimaJogada(int id) throws RemoteException {
        return !(idUltimoJogador == id || flagJogadaEnviada == 0);
    }
    
    @Override
    public ArrayList<String> receberJogada() throws RemoteException { 
        flagJogadaEnviada = 0;
        return ultimaJogada;
    } 
    
    @Override
    public void excluirPeca(int id, String casa) throws RemoteException {
        idUltimoJogador = id;
        flagPecaExcluida = 1;
        pecaExcluida = casa;
    }
    
    @Override
    public boolean checkPecaExcluida(int id) throws RemoteException {
        return !(idUltimoJogador == id || flagPecaExcluida == 0);
    }
    
    /**Recebe informação sobre a peça excluída
     * 
     * @return String
     * @throws RemoteException 
     */
    @Override
    public String receberExclusao() throws RemoteException {
        flagPecaExcluida = 0;
        return pecaExcluida;
    }

    @Override
    public void desistirJogo(int id) throws RemoteException {
        idUltimoJogador = id;
        flagDesistencia = 1;
    }
    
    @Override
    public boolean checkDesistencia(int id) throws RemoteException {
        return !(idUltimoJogador == id || flagDesistencia == 0);
    }
    

    @Override
    public void fecharJanela() throws RemoteException {
        flagFecharJanela = 1;
    }
    

    @Override
    public boolean checkFecharJanela() throws RemoteException {
        return flagFecharJanela == 1;
    }


    @Override
    public void enviarMensagemChat(int id, String mensagem) throws RemoteException {
        idUltimoChat = id;
        flagMensagem = 1;
        mensagemChat= mensagem;
    }
    

    @Override
    public boolean checkMensagem(int id) throws RemoteException {
       return !(idUltimoChat == id || flagMensagem == 0);
    }


    @Override
    public String receberMensagemChat() throws RemoteException {
        flagMensagem = 0;
        String nome = jogadores.get(idUltimoChat).getNome();
        return nome + ": " + mensagemChat;
    }  
}