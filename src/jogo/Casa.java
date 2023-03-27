package jogo;

/**
 *
 * @author walmi
 */
public class Casa {
    private String nome = "";
    private String cor = "";
    
    public String getNome() { return this.nome; }
    public String getCor() { return this.cor; }
    public void setCor(String cor) { this.cor = cor; }

    public Casa(String nome) {
        this.nome = nome;
    }

}