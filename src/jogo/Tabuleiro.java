package jogo;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JButton;
import javax.swing.JLabel;
import tela.JogoGekitai;

/**
 *
 * @author walmi
 */
public class Tabuleiro {
    private Map<String,Casa> casas;
    private int qtdPecas;
    private int qtdPecasJogadas;
    java.util.List<String> nomeCasa =  Arrays.asList("a1Label", "a2Label", "a3Label", "a4Label", "a5Label", "a6Label", 
                "b1Label", "b2Label", "b3Label", "b4Label", "b5Label", "b6Label",
                "c1Label", "c2Label", "c3Label", "c4Label", "c5Label", "c6Label",
                "d1Label", "d2Label", "d3Label", "d4Label", "d5Label", "d6Label",
                "e1Label", "e2Label", "e3Label", "e4Label", "e5Label", "e6Label",
                "f1Label", "f2Label", "f3Label", "f4Label", "f5Label", "f6Label");
    java.util.List<String> nomePeca = Arrays.asList("p1Label", "p2Label", "p3Label", "p4Label", "p5Label",
            "p6Label", "p7Label", "p8Label");
    
    public Map<String, Casa> getCasas(){
        return casas;
    }
    public int getQtdPecas(){ return this.qtdPecas; }
    public void setQtdPecas(int qtdPecas){ this.qtdPecas = qtdPecas; }
    public int getQtdPecasJogadas(){ return this.qtdPecasJogadas; }
    public void setQtdPecasJogadas(int qtdPecasJogadas){ this.qtdPecasJogadas = qtdPecasJogadas; }
    
    public Tabuleiro(){
        this.casas = new HashMap();
        this.qtdPecas = 8;
        this.qtdPecasJogadas = 0;
    }
    
    public void retirarPeca(JogoGekitai tabuleiro, String casa, int cod){ 
        java.util.List<JLabel> pecas = Arrays.asList(tabuleiro.getA1Label(), tabuleiro.getA2Label(), tabuleiro.getA3Label(), tabuleiro.getA4Label(), tabuleiro.getA5Label(), tabuleiro.getA6Label(),
                tabuleiro.getB1Label(), tabuleiro.getB2Label(), tabuleiro.getB3Label(), tabuleiro.getB4Label(), tabuleiro.getB5Label(), tabuleiro.getB6Label(),
                tabuleiro.getC1Label(), tabuleiro.getC2Label(), tabuleiro.getC3Label(), tabuleiro.getC4Label(), tabuleiro.getC5Label(), tabuleiro.getC6Label(),
                tabuleiro.getD1Label(), tabuleiro.getD2Label(), tabuleiro.getD3Label(), tabuleiro.getD4Label(), tabuleiro.getD5Label(), tabuleiro.getD6Label(),
                tabuleiro.getE1Label(), tabuleiro.getE2Label(), tabuleiro.getE3Label(), tabuleiro.getE4Label(), tabuleiro.getE5Label(), tabuleiro.getE6Label(),
                tabuleiro.getF1Label(), tabuleiro.getF2Label(), tabuleiro.getF3Label(), tabuleiro.getF4Label(), tabuleiro.getF5Label(), tabuleiro.getF6Label());
        
        java.util.List<JLabel> pecasIniciais = Arrays.asList(tabuleiro.getP1Label(), tabuleiro.getP2Label(), tabuleiro.getP3Label(),
                tabuleiro.getP4Label(), tabuleiro.getP5Label(), tabuleiro.getP6Label(),
                tabuleiro.getP7Label(), tabuleiro.getP8Label());
        
        if(cod == 100){ //minha jogada
            casas.get(casa).setCor("vazia");
            char pLetra = casa.charAt(0);
            
            if(pLetra == 'p'){
                for(JLabel peca : pecasIniciais){
                    if(peca.getName().equals(casa)){
                        peca.setIcon(null);
                    } 
                }
            }
            else{
               for(JLabel peca : pecas){
                    if(peca.getName().equals(casa)){
                        peca.setIcon(null);
                    } 
                }
            }
        }
        else{ // jogada adversário
            for(JLabel peca : pecas){
                if(peca.getName().equals(casa)){
                    peca.setIcon(null);
                    casas.get(casa).setCor("vazia");
                }
            }
        }
    }

    public void adicionarPeca(JogoGekitai tabuleiro, String peca, String cor){
        java.util.List<JLabel> pecas = Arrays.asList(tabuleiro.getA1Label(), tabuleiro.getA2Label(), tabuleiro.getA3Label(), tabuleiro.getA4Label(), tabuleiro.getA5Label(), tabuleiro.getA6Label(),
                tabuleiro.getB1Label(), tabuleiro.getB2Label(), tabuleiro.getB3Label(), tabuleiro.getB4Label(), tabuleiro.getB5Label(), tabuleiro.getB6Label(),
                tabuleiro.getC1Label(), tabuleiro.getC2Label(), tabuleiro.getC3Label(), tabuleiro.getC4Label(), tabuleiro.getC5Label(), tabuleiro.getC6Label(),
                tabuleiro.getD1Label(), tabuleiro.getD2Label(), tabuleiro.getD3Label(), tabuleiro.getD4Label(), tabuleiro.getD5Label(), tabuleiro.getD6Label(),
                tabuleiro.getE1Label(), tabuleiro.getE2Label(), tabuleiro.getE3Label(), tabuleiro.getE4Label(), tabuleiro.getE5Label(), tabuleiro.getE6Label(),
                tabuleiro.getF1Label(), tabuleiro.getF2Label(), tabuleiro.getF3Label(), tabuleiro.getF4Label(), tabuleiro.getF5Label(), tabuleiro.getF6Label(),
                
                tabuleiro.getP1Label(), tabuleiro.getP2Label(), tabuleiro.getP3Label(),
                tabuleiro.getP4Label(), tabuleiro.getP5Label(), tabuleiro.getP6Label(),
                tabuleiro.getP7Label(), tabuleiro.getP8Label());
        
        casas.get(peca).setCor(cor);
        
        if(cor.equals("preta")){
            for(JLabel casa : pecas){
                if(casa.getName().equals(peca)){
                    casa.setIcon(new javax.swing.ImageIcon(getClass().getResource("/tela/peca_preta.png")));
                }
            }
        }
        else{
            for(JLabel casa : pecas){
                if(casa.getName().equals(peca)){
                    casa.setIcon(new javax.swing.ImageIcon(getClass().getResource("/tela/peca_branca.png")));
                }
            }
        }
    
    }
    
    public void atualizarTabuleiro(JogoGekitai tabuleiro, String cor, String partida, String chegada, int cod){
        retirarPeca(tabuleiro, partida, cod);
        adicionarPeca(tabuleiro, chegada, cor);
    }
    
    public void iniciarPecas(JogoGekitai tabuleiro, String cor){
        java.util.List<JLabel> pecasIniciais = Arrays.asList(tabuleiro.getP1Label(), tabuleiro.getP2Label(), tabuleiro.getP3Label(),
                tabuleiro.getP4Label(), tabuleiro.getP5Label(), tabuleiro.getP6Label(),
                tabuleiro.getP7Label(), tabuleiro.getP8Label());
        
        if(cor.equals("preta")){
            for(JLabel peca : pecasIniciais){
                casas.put(peca.getName(), new Casa(peca.getName()));
                casas.get(peca.getName()).setCor(cor);
                peca.setIcon(new javax.swing.ImageIcon(getClass().getResource("/tela/peca_preta.png")));
            }
        }
        else{
           for(JLabel peca : pecasIniciais){
                casas.put(peca.getName(), new Casa(peca.getName()));
                casas.get(peca.getName()).setCor(cor);
                peca.setIcon(new javax.swing.ImageIcon(getClass().getResource("/tela/peca_branca.png")));
            } 
        }
    }
     
    public void iniciarTabuleiro(){
        String corInicial = "vazia";
        
        for(int i = 0; i < nomeCasa.size(); i++){
            casas.put(nomeCasa.get(i), new Casa(nomeCasa.get(i)));
            casas.get(nomeCasa.get(i)).setCor(corInicial);
        }        
    }
             
    public void desabilitaBotoes(JogoGekitai tabuleiro){
        java.util.List<JButton> botoes = Arrays.asList(tabuleiro.getDesistirJogoButton());
         
        for (JButton botao : botoes) {
           botao.setEnabled(false);
        }
    }
     
    public void habilitaBotoes(JogoGekitai tabuleiro){
        java.util.List<JButton> botoes = Arrays.asList(tabuleiro.getDesistirJogoButton());
         
        for (JButton botao : botoes) {
            botao.setEnabled(true);
        } 
    }
}
