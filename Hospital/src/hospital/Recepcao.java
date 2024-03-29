package hospital;

import java.io.IOException;
import java.util.Scanner;
import java.net.MulticastSocket;
import java.net.NetworkInterface;
import java.net.InetAddress;
import java.net.DatagramPacket;
import java.net.InetSocketAddress;

public class Recepcao {
	public static void main(String[] args) throws IOException{
		byte[] envio = new byte[1024];
		String topico;
		String conteudo; 
		
		Scanner sc = new Scanner(System.in); 
		
		MulticastSocket socket = new MulticastSocket(4321);
		
		InetAddress ia = InetAddress.getByName("230.0.0.1");
        InetSocketAddress grupoTeste = new InetSocketAddress(ia, 4321);
        NetworkInterface ni = NetworkInterface.getByInetAddress(ia);
        socket.joinGroup(grupoTeste, ni);
		
        while(true) {
        	System.out.print("[Servidor] Digite o código do tópico da mensagem:");
        	topico = sc.nextLine();
        	System.out.print("[Servidor] Digite o conteúdo da mensagem:");
        	conteudo = sc.nextLine();
        	System.out.println(topico);
        }
		
	}
}
