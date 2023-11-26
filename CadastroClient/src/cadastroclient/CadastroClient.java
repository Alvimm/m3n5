/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package cadastroclient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.List;

/**
 *
 * @author Filipe
 */
public class CadastroClient {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        try {
            Socket socket = new Socket("localhost", 4321);
            ObjectOutputStream saida = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream entrada = new ObjectInputStream(socket.getInputStream());
            ThreadClient threadClient = new ThreadClient(in);
          threadClient.start();
            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
            System.out.println("------------------------------");
            System.out.print("Login: ");
            String login = reader.readLine();
            System.out.print("Senha: ");
            String senha = reader.readLine();

            saida.writeObject(login);
            saida.writeObject(senha);
            saida.writeObject("Mensagem do cliente para o servidor.");

            saida.writeObject("L");
            saida.flush();
            saida.close();
            entrada.close();
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
