/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package cadastroserver;

import cadastroserver.controller.ProdutoJpaController;
import cadastroserver.controller.UsuarioJpaController;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 *
 * @author Filipe
 */
public class CadastroServer {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {

        EntityManagerFactory emf = Persistence.createEntityManagerFactory("CadastroServerPU");
        ProdutoJpaController ctrl = new ProdutoJpaController(emf);
        UsuarioJpaController ctrlUsu = new UsuarioJpaController(emf);

        try (ServerSocket serverSocket = new ServerSocket(4321)) {
            System.out.println("Servidor aguardando conexoes na porta 4321...");
            while (true) {
                Socket socket = serverSocket.accept();
                 System.out.println("Cliente conectado: " + socket.getInetAddress());

                CadastroThread cadastroThread  = new CadastroThread(ctrl, ctrlUsu, socket);
                cadastroThread.start();
                System.out.println("thread iniciado!");

            }
        }  catch (IOException e) {
            e.printStackTrace();
        } finally {
            emf.close();
        }
    }

}
