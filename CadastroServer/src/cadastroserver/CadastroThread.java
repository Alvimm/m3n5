/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cadastroserver;

import cadastroserver.controller.ProdutoJpaController;
import cadastroserver.controller.UsuarioJpaController;
import cadastroserver.model.Usuario;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

/**
 *
 * @author Filipe
 */
public class CadastroThread extends Thread{
    private ProdutoJpaController ctrl;
    private UsuarioJpaController ctrlUsu;
    private Socket s1;

    public CadastroThread(ProdutoJpaController ctrl, UsuarioJpaController ctrlUsu, Socket s1) {
        this.ctrl = ctrl;
        this.ctrlUsu = ctrlUsu;
        this.s1 = s1;
    }
    
    @Override
    public void run(){
        try {
            ObjectOutputStream saida = new ObjectOutputStream(s1.getOutputStream());
            ObjectInputStream entrada = new ObjectInputStream(s1.getInputStream());

            String login = (String) entrada.readObject();
            String senha = (String) entrada.readObject();

            if (ctrlUsu != null) {
                Usuario usuario = ctrlUsu.validarUsuario(login, senha);
                if (usuario != null) {
                    saida.writeObject("Conexao estabelecida. Comandos disponiveis: L - Listar produtos");
                    saida.flush();

                    boolean connected = true;
                    while (connected) {
                        String comando = (String) entrada.readObject();
                        
                        switch (comando) {
                            case "L":
                                saida.writeObject(ctrl.findProdutoEntities());
                                saida.flush();
                                break;
                           default:
                                saida.writeObject("Comando inválido!");
                                saida.flush();
                                break;
                        }
                    }
                } else {
                    saida.writeObject("Usuário inválido. Conexão encerrada.");
                    saida.flush();
                }
            } else {
                saida.writeObject("Erro interno. Controlador de usuário não encontrado.");
                saida.flush();
            }

            entrada.close();
            saida.close();
            s1.close();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
    
}
