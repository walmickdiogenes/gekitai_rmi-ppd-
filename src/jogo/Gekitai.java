package jogo;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;

/**
 *
 * @author walmi
 */
public interface Gekitai extends Remote{
    public int conectarCliente(String nome) throws RemoteException;
    public boolean checkAdversarios() throws RemoteException;
    
    public String definirCor(int id) throws RemoteException;
    public String getCorPecas(int id) throws RemoteException;
    
    public boolean checkUltimaJogada(int id) throws RemoteException;
    public void enviarJogada(int id, String casaInicial, String casaFinal) throws RemoteException;
    public ArrayList<String> receberJogada() throws RemoteException;
    
    public void excluirPeca(int id, String casa) throws RemoteException;
    public boolean checkPecaExcluida(int id) throws RemoteException;
    public String receberExclusao() throws RemoteException;
    
    public void desistirJogo(int id) throws RemoteException;
    public boolean checkDesistencia(int id) throws RemoteException;
    
    public void fecharJanela() throws RemoteException;
    public boolean checkFecharJanela() throws RemoteException; 
    
    public void enviarMensagemChat(int id, String mensagem) throws RemoteException;
    public boolean checkMensagem(int id) throws RemoteException;
    public String receberMensagemChat() throws RemoteException;
}