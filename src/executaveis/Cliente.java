package executaveis;

/**
 *
 * @author walmi
 */
import jogo.Gekitai;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import jogo.Jogador;
import jogo.Jogadas;
import jogo.Tabuleiro;
import tela.JogoGekitai;



public class Cliente extends Thread{
    private JogoGekitai tabuleiro;
    private JTextField txtNome; 
    public Jogador jogador = new Jogador();
    Tabuleiro tabuleiroMetod = new Tabuleiro();
    Jogadas jogadas = new Jogadas();
    ActionListener listenerActionPerf;
    KeyListener listenerKey;
    MouseListener listenerMouse;
    WindowListener listenerWind;
    private JTextField txtIP;
    private JTextField txtPorta;
    String jogadaIni = "";
    String jogadaFin = "";
    String iniUltimaJog = "";
    String finUltimaJog = "";
    String pecaExcluida = "";
    int pecasAdversario = 8;
    
    public Cliente() throws RemoteException{
        JLabel lblMessage = new JLabel("Digite seu nome");
        txtNome = new JTextField("User");
        JLabel lblHost = new JLabel("Digite o IP do servidor");
        txtIP = new JTextField("127.0.0.1");
        JLabel lblPorta = new JLabel("Digite a porta do servidor");
        txtPorta = new JTextField("12345");
        Object[] texts = {lblMessage, txtNome, lblHost, txtIP, lblPorta, txtPorta};
        JOptionPane.showMessageDialog(null, texts);
        
        this.tabuleiro = new JogoGekitai();
        this.tabuleiro.setVisible(true);
    }       
     
    public static void main(String args[]){
        try {
            final Gekitai interfaceServer = (Gekitai) Naming.lookup("//localhost:12345/ServerTrilha");
            System.out.println("Servidor localizado!");
            
            final Cliente cliente = new Cliente();
            
            Thread thread;
            thread = new Thread(new Runnable(){
                @Override
                public void run() {    
                    try {
                        //Conexão do cliente
                        cliente.jogador.setId(interfaceServer.conectarCliente(cliente.jogador.getNome()));
                        
                        //Inicializar tabuleiro
                        cliente.iniciarCasas(cliente.tabuleiro, cliente.listenerMouse);
                        cliente.iniciarListenerComponents(cliente.tabuleiro, cliente.listenerActionPerf, 
                        cliente.listenerKey, cliente.listenerWind);     
                        cliente.tabuleiroMetod.iniciarTabuleiro();            
                        cliente.tabuleiroMetod.desabilitaBotoes(cliente.tabuleiro);
                        cliente.tabuleiro.getIniciarJogoButton().setEnabled(false);
                        
                        while(cliente.jogador.getFlagAtiva()){
                            try{
                                //Fechamento da janela
                                if(interfaceServer.checkFecharJanela()){
                                    cliente.tabuleiro.getStatusJogoLabel().setText("Seu adversário saiu da sala.\r\n");
                                    cliente.desabilitarListeners(cliente.tabuleiro, cliente.listenerMouse);
                                    cliente.tabuleiroMetod.desabilitaBotoes(cliente.tabuleiro);
                                    cliente.tabuleiro.getIniciarJogoButton().setEnabled(false);
                                }
                                
                                //Conversa chat
                                if(interfaceServer.checkMensagem(cliente.jogador.getId())){
                                String mensagem = interfaceServer.receberMensagemChat();
                                    cliente.tabuleiro.conversaTextArea.append(mensagem + "\r\n");
                                }
                                
                                //Adversário disponível
                                if(interfaceServer.checkAdversarios() && cliente.jogador.getFlagAdversario()){
                                    cliente.tabuleiro.getIniciarJogoButton().setEnabled(true);
                                    cliente.jogador.setFlagAdversario(false);
                                }
                                //Definir cor das peças
                                else if(interfaceServer.getCorPecas(cliente.jogador.getId()).equals("branca") && cliente.jogador.getFlagCor()==0){
                                    cliente.jogador.setCor("branca");
                                    cliente.jogador.setFlagCor(1);

                                    cliente.tabuleiroMetod.iniciarPecas(cliente.tabuleiro, cliente.jogador.getCor());
                                    cliente.desabilitarListeners(cliente.tabuleiro, cliente.listenerMouse);
                                    
                                    cliente.tabuleiro.getIniciarJogoButton().setEnabled(false);
                                    cliente.tabuleiro.getStatusJogoLabel().setText("Seu adversário iniciará o jogo.");
                                }
                                //Identificar jogadas
                                else if(interfaceServer.checkUltimaJogada(cliente.jogador.getId())){
                                    ArrayList<String> movimento = interfaceServer.receberJogada();
                                    cliente.iniUltimaJog = movimento.get(0);
                                    cliente.finUltimaJog = movimento.get(1);
                                    
                                    if(cliente.jogador.getCor().equals("preta")){
                                        cliente.tabuleiroMetod.atualizarTabuleiro(cliente.tabuleiro, "branca", cliente.iniUltimaJog, 
                                                cliente.finUltimaJog, 200);
                                    } else{
                                       cliente.tabuleiroMetod.atualizarTabuleiro(cliente.tabuleiro, "preta", cliente.iniUltimaJog, 
                                                cliente.finUltimaJog, 200); 
                                    }
                                    cliente.jogadas.retomarVez(cliente.tabuleiro);
                                    cliente.habilitarListenersLabel(cliente.tabuleiro, cliente.listenerMouse);
                                }
                                //Peça excluída
                                else if(interfaceServer.checkPecaExcluida(cliente.jogador.getId())){
                                    String peca = interfaceServer.receberExclusao();
                                    cliente.pecaExcluida = peca;
                                    cliente.jogador.setFlagExclusao(true);
                                    
                                    cliente.tabuleiroMetod.retirarPeca(cliente.tabuleiro, peca, 200);
                                    cliente.habilitarListenersLabel(cliente.tabuleiro, cliente.listenerMouse);
                                    cliente.jogadas.retomarVez(cliente.tabuleiro);
                                    cliente.tabuleiroMetod.setQtdPecas(cliente.tabuleiroMetod.getQtdPecas()-1);
                                
                                }
                                //Desistência
                                else if(interfaceServer.checkDesistencia(cliente.jogador.getId())){
                                    cliente.tabuleiro.getStatusJogoLabel().setText("O seu adversário desistiu.");
                                    cliente.desabilitarListeners(cliente.tabuleiro, cliente.listenerMouse);
                                }
                            } catch (RemoteException ex) {
                                Logger.getLogger(Cliente.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        }
                        } catch (RemoteException ex) {
                            Logger.getLogger(Cliente.class.getName()).log(Level.SEVERE, null, ex);
                        }
                }
            });
            
            cliente.listenerActionPerf = new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent arg0) {
                    if(arg0.getActionCommand().equals(cliente.tabuleiro.getEnviarButton().getActionCommand())){
                        if(!cliente.tabuleiro.getMensagemTextField().getText().equals("")){
                            cliente.tabuleiro.conversaTextArea.append("Eu: " + cliente.tabuleiro.getMensagemTextField().getText() + "\r\n");
                            try {
                                interfaceServer.enviarMensagemChat(cliente.jogador.getId(), cliente.tabuleiro.getMensagemTextField().getText());
                            } catch (RemoteException ex) {
                                Logger.getLogger(Cliente.class.getName()).log(Level.SEVERE, null, ex);
                            }
                            cliente.tabuleiro.getMensagemTextField().setText("");
                        }
                    }
                    //Botão iniciar: inicia o jogo no tabuleiro, define a cor das peças.
                    else if(arg0.getActionCommand().equals(cliente.tabuleiro.getIniciarJogoButton().getActionCommand())){
                        try {
                            cliente.jogador.setCor(interfaceServer.definirCor(cliente.jogador.getId()));
                
                            cliente.tabuleiroMetod.iniciarPecas(cliente.tabuleiro, cliente.jogador.getCor());
                            cliente.tabuleiroMetod.habilitaBotoes(cliente.tabuleiro);

                            cliente.tabuleiro.getIniciarJogoButton().setEnabled(false);
                            cliente.tabuleiro.getStatusJogoLabel().setText("Você iniciará o jogo.");

                        } catch (RemoteException ex) {
                            Logger.getLogger(Cliente.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                    //Botão desistir: desiste do jogo.
                    else if(arg0.getActionCommand().equals(cliente.tabuleiro.getDesistirJogoButton().getActionCommand())){
                        int resposta;
                        resposta = JOptionPane.showConfirmDialog(null, "Tem certeza que quer desistir?");

                        if(resposta == JOptionPane.OK_OPTION){
                            cliente.tabuleiro.getStatusJogoLabel().setText("Você desistiu.");
                            cliente.tabuleiroMetod.desabilitaBotoes(cliente.tabuleiro);
                            cliente.desabilitarListeners(cliente.tabuleiro, cliente.listenerMouse);
                            try {
                                interfaceServer.desistirJogo(cliente.jogador.getId());
                            } catch (RemoteException ex) {
                                Logger.getLogger(Cliente.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        }       
                        else{
                            cliente.tabuleiro.getStatusJogoLabel().setText("Faça sua jogada!");
                        }          
                    }
                }
            };
                      
            cliente.listenerMouse = new MouseListener() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    //Seleção da peça a ser movida.
                   if(cliente.jogador.getFlagJogada() == 0 && cliente.jogador.getFlagTrilha() == 0){
                        cliente.jogadaIni = cliente.jogadas.verificarValidadePeca(cliente.tabuleiro, 
                                (JLabel) e.getComponent(), cliente.jogador.getCor(), cliente.tabuleiroMetod.getCasas(), 1);
                        
                        if(!cliente.jogadaIni.equals("")){
                            cliente.jogador.setFlagJogada(1);
                        }    
                    }
                    //Seleção da casa para onde a peça será movida.
                    else if (cliente.jogador.getFlagJogada() == 1 && cliente.jogador.getFlagTrilha() == 0){
                        cliente.jogadaFin = cliente.jogadas.verificarValidadePeca(cliente.tabuleiro, 
                                (JLabel) e.getComponent(), cliente.jogador.getCor(), cliente.tabuleiroMetod.getCasas(), 2);
                        if(!cliente.jogadaFin.equals("")){
                            cliente.jogador.setFlagJogada(0);

                            cliente.tabuleiroMetod.atualizarTabuleiro(cliente.tabuleiro, cliente.jogador.getCor(), 
                            cliente.jogadaIni, cliente.jogadaFin, 100);
                            cliente.tabuleiroMetod.setQtdPecasJogadas(cliente.tabuleiroMetod.getQtdPecasJogadas() + 1);
                            
                            try {
                                interfaceServer.enviarJogada(cliente.jogador.getId(), cliente.jogadaIni, cliente.jogadaFin);
                                cliente.jogador.setFlagExclusao(false);
                            } catch (RemoteException ex) {
                                Logger.getLogger(Cliente.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        }   
                    }   
                    else if (cliente.jogador.getFlagTrilha() == 1){
                        cliente.pecaExcluida = cliente.jogadas.verificarValidadePeca(cliente.tabuleiro, (JLabel) e.getComponent(), 
                                cliente.jogador.getCor(), cliente.tabuleiroMetod.getCasas(), 3);
                        
                        if(!cliente.pecaExcluida.equals("")){
                            cliente.jogador.setFlagTrilha(0);
                            cliente.jogador.setFlagExclusao(true);
                            
                            cliente.tabuleiroMetod.retirarPeca(cliente.tabuleiro, cliente.pecaExcluida, 100);
                            cliente.jogadas.passarVez(cliente.tabuleiro);
                            cliente.desabilitarListeners(cliente.tabuleiro, cliente.listenerMouse);
                            try {
                                interfaceServer.excluirPeca(cliente.jogador.getId(), cliente.pecaExcluida);
                                cliente.jogador.setFlagExclusao(true);
                                cliente.pecasAdversario -= 1;
                            } catch (RemoteException ex) {
                                Logger.getLogger(Cliente.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        }
                    }
                    
                }

                @Override
                public void mousePressed(MouseEvent e) {
                    
                }

                @Override
                public void mouseReleased(MouseEvent e) {
                    
                }

                @Override
                public void mouseEntered(MouseEvent e) {
                    
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    
                }
            };
            
            cliente.listenerWind = new WindowListener (){
                @Override
                public void windowOpened(WindowEvent e) {
                    
                }

                @Override
                public void windowClosing(WindowEvent e) {
                    try {
                        cliente.jogador.setFlagAtiva(false);
                        interfaceServer.fecharJanela();
                        System.exit(0);
                    } catch (RemoteException ex) {
                        Logger.getLogger(Cliente.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    
                }

                @Override
                public void windowClosed(WindowEvent e) {
                    
                }

                @Override
                public void windowIconified(WindowEvent e) {
                    
                }

                @Override
                public void windowDeiconified(WindowEvent e) {
                    
                }

                @Override
                public void windowActivated(WindowEvent e) {
                    
                }

                @Override
                public void windowDeactivated(WindowEvent e) {
                    
                }  
            };
        
            thread.start();
        }catch(NotBoundException | MalformedURLException | RemoteException e){
            e.printStackTrace();
        } 
    }

    public JTextField getTxtNome() {
        return txtNome;
    }

    public void setTxtNome(JTextField txtNome) {
        this.txtNome = txtNome;
    }

    public void iniciarListenerComponents(JogoGekitai tabuleiro, ActionListener listenerA, KeyListener listenerK,
            WindowListener listenerW){
        java.util.List<JButton> botoes = Arrays.asList(tabuleiro.getEnviarButton(), tabuleiro.getIniciarJogoButton(),
                tabuleiro.getDesistirJogoButton());
        
        tabuleiro.getMensagemTextField().addKeyListener(listenerK);
        tabuleiro.addWindowListener(listenerW);
        
        for (JButton botao : botoes) {
            botao.addActionListener(listenerA);
            botao.addKeyListener(listenerK);
        }
    }
    
    void iniciarCasas(JogoGekitai tabuleiro, MouseListener listenerM){
        java.util.List<JLabel> casas = Arrays.asList(tabuleiro.getA1Label(), tabuleiro.getA2Label(), tabuleiro.getA3Label(), tabuleiro.getA4Label(), tabuleiro.getA5Label(), tabuleiro.getA6Label(),
                tabuleiro.getB1Label(), tabuleiro.getB2Label(), tabuleiro.getB3Label(), tabuleiro.getB4Label(), tabuleiro.getB5Label(), tabuleiro.getB6Label(),
                tabuleiro.getC1Label(), tabuleiro.getC2Label(), tabuleiro.getC3Label(), tabuleiro.getC4Label(), tabuleiro.getC5Label(), tabuleiro.getC6Label(),
                tabuleiro.getD1Label(), tabuleiro.getD2Label(), tabuleiro.getD3Label(), tabuleiro.getD4Label(), tabuleiro.getD5Label(), tabuleiro.getD6Label(),
                tabuleiro.getE1Label(), tabuleiro.getE2Label(), tabuleiro.getE3Label(), tabuleiro.getE4Label(), tabuleiro.getE5Label(), tabuleiro.getE6Label(),
                tabuleiro.getF1Label(), tabuleiro.getF2Label(), tabuleiro.getF3Label(), tabuleiro.getF4Label(), tabuleiro.getF5Label(), tabuleiro.getF6Label(),
                
                tabuleiro.getP1Label(), tabuleiro.getP2Label(), tabuleiro.getP3Label(),
                tabuleiro.getP4Label(), tabuleiro.getP5Label(), tabuleiro.getP6Label(),
                tabuleiro.getP7Label(), tabuleiro.getP8Label());
        
        java.util.List<String> nomeLabel =  Arrays.asList("a1Label", "a2Label", "a3Label", "a4Label", "a5Label", "a6Label", 
                "b1Label", "b2Label", "b3Label", "b4Label", "b5Label", "b6Label",
                "c1Label", "c2Label", "c3Label", "c4Label", "c5Label", "c6Label",
                "d1Label", "d2Label", "d3Label", "d4Label", "d5Label", "d6Label",
                "e1Label", "e2Label", "e3Label", "e4Label", "e5Label", "e6Label",
                "f1Label", "f2Label", "f3Label", "f4Label", "f5Label", "f6Label",
                
                "p1Label", "p2Label", "p3Label", "p4Label", "p5Label", "p6Label", "p7Label", "p8Label");
        
        for(int i = 0; i < casas.size(); i++){
            casas.get(i).addMouseListener(listenerM);
            casas.get(i).setName(nomeLabel.get(i));
        }
    }
    
    void habilitarListenersLabel(JogoGekitai tabuleiro, MouseListener listenerM){
        java.util.List<JLabel> pecas = Arrays.asList(tabuleiro.getA1Label(), tabuleiro.getA2Label(), tabuleiro.getA3Label(), tabuleiro.getA4Label(), tabuleiro.getA5Label(), tabuleiro.getA6Label(),
                tabuleiro.getB1Label(), tabuleiro.getB2Label(), tabuleiro.getB3Label(), tabuleiro.getB4Label(), tabuleiro.getB5Label(), tabuleiro.getB6Label(),
                tabuleiro.getC1Label(), tabuleiro.getC2Label(), tabuleiro.getC3Label(), tabuleiro.getC4Label(), tabuleiro.getC5Label(), tabuleiro.getC6Label(),
                tabuleiro.getD1Label(), tabuleiro.getD2Label(), tabuleiro.getD3Label(), tabuleiro.getD4Label(), tabuleiro.getD5Label(), tabuleiro.getD6Label(),
                tabuleiro.getE1Label(), tabuleiro.getE2Label(), tabuleiro.getE3Label(), tabuleiro.getE4Label(), tabuleiro.getE5Label(), tabuleiro.getE6Label(),
                tabuleiro.getF1Label(), tabuleiro.getF2Label(), tabuleiro.getF3Label(), tabuleiro.getF4Label(), tabuleiro.getF5Label(), tabuleiro.getF6Label(),
                
                tabuleiro.getP1Label(), tabuleiro.getP2Label(), tabuleiro.getP3Label(),
                tabuleiro.getP4Label(), tabuleiro.getP5Label(), tabuleiro.getP6Label(),
                tabuleiro.getP7Label(), tabuleiro.getP8Label());
         
         for(JLabel casa : pecas){
             casa.addMouseListener(listenerM);
         }
    }
    
    void desabilitarListeners(JogoGekitai tabuleiro, MouseListener listenerM){
        java.util.List<JLabel> pecas = Arrays.asList(tabuleiro.getA1Label(), tabuleiro.getA2Label(), tabuleiro.getA3Label(), tabuleiro.getA4Label(), tabuleiro.getA5Label(), tabuleiro.getA6Label(),
                tabuleiro.getB1Label(), tabuleiro.getB2Label(), tabuleiro.getB3Label(), tabuleiro.getB4Label(), tabuleiro.getB5Label(), tabuleiro.getB6Label(),
                tabuleiro.getC1Label(), tabuleiro.getC2Label(), tabuleiro.getC3Label(), tabuleiro.getC4Label(), tabuleiro.getC5Label(), tabuleiro.getC6Label(),
                tabuleiro.getD1Label(), tabuleiro.getD2Label(), tabuleiro.getD3Label(), tabuleiro.getD4Label(), tabuleiro.getD5Label(), tabuleiro.getD6Label(),
                tabuleiro.getE1Label(), tabuleiro.getE2Label(), tabuleiro.getE3Label(), tabuleiro.getE4Label(), tabuleiro.getE5Label(), tabuleiro.getE6Label(),
                tabuleiro.getF1Label(), tabuleiro.getF2Label(), tabuleiro.getF3Label(), tabuleiro.getF4Label(), tabuleiro.getF5Label(), tabuleiro.getF6Label(),
                
                tabuleiro.getP1Label(), tabuleiro.getP2Label(), tabuleiro.getP3Label(),
                tabuleiro.getP4Label(), tabuleiro.getP5Label(), tabuleiro.getP6Label(),
                tabuleiro.getP7Label(), tabuleiro.getP8Label());
         
         for(JLabel casa : pecas){
             casa.removeMouseListener(listenerM);
         }
    }    
    
}