package jogo;

import java.util.Arrays;
import java.util.Map;
import javax.swing.JLabel;
import tela.JogoGekitai;

/**
 *
 * @author walmi
 */
public class Jogadas {
    
    java.util.List<String> nomeCasa =  Arrays.asList("a1Label", "a2Label", "a3Label", "a4Label", "a5Label", "a6Label", 
                "b1Label", "b2Label", "b3Label", "b4Label", "b5Label", "b6Label",
                "c1Label", "c2Label", "c3Label", "c4Label", "c5Label", "c6Label",
                "d1Label", "d2Label", "d3Label", "d4Label", "d5Label", "d6Label",
                "e1Label", "e2Label", "e3Label", "e4Label", "e5Label", "e6Label",
                "f1Label", "f2Label", "f3Label", "f4Label", "f5Label", "f6Label");
    
    java.util.List<String> nomePeca = Arrays.asList("p1Label", "p2Label", "p3Label", "p4Label", "p5Label",
            "p6Label", "p7Label", "p8Label");
    
    public void passarVez(JogoGekitai tabuleiro){
        tabuleiro.getStatusJogoLabel().setText("Seu adversário está jogando");
    }
    
    public void retomarVez(JogoGekitai tabuleiro){
        tabuleiro.getStatusJogoLabel().setText("Agora é a sua vez");
    }
    
    public String verificarValidadePeca(JogoGekitai tabuleiro, JLabel peca, String minhaCor, Map<String, Casa> pecas, int pos){
        String casa ="";
        
        if(pos == 1){ // Seleciona a peça
            tabuleiro.getSelecaoPecaLabel().setText("Peça selecionada");
            
            if(minhaCor.equals(pecas.get(peca.getName()).getCor())){
                casa = peca.getName();
            }
            else if(pecas.get(peca.getName()).getCor().equals("vazia")){    
                tabuleiro.getStatusJogoLabel().setText("Selecione uma peça válida");
            }
        }
        else if (pos == 2){ //Seleciona a casa
            tabuleiro.getSelecaoPecaLabel().setText("Casa selecionada");
            
            if(pecas.get(peca.getName()).getCor().equals("vazia")){
                casa = peca.getName();
            }
            else{
                tabuleiro.getStatusJogoLabel().setText("Essa casa está ocupada");
            }
        }        
        return casa;
    } 
}
