package executaveis;

/**
 *
 * @author walmi
 */
import jogo.Flags;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.logging.Level;
import java.util.logging.Logger;


public class Servidor{
    Flags server;
    Registry registry;
    
    public Servidor() throws RemoteException{
        try{
            server = new Flags();
            registry = LocateRegistry.createRegistry(12345);
            registry.rebind("ServerTrilha", server);
            System.out.println("Servidor registrado");
        } catch (Exception e){
            e.printStackTrace();
        }
        
    }
    
    public static void main(String args[]) {
        try {
            Servidor servidor = new Servidor();
        } catch (RemoteException ex) {
            Logger.getLogger(Servidor.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
}