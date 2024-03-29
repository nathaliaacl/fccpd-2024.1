package hospital;

import java.io.IOException;
import java.util.Scanner;
import java.net.MulticastSocket;
import java.net.NetworkInterface;
import java.net.InetAddress;
import java.net.DatagramPacket;
import java.net.InetSocketAddress;

public class Medico {
	public static void main(String[] args) throws IOException{
		Scanner sc = new Scanner(System.in); 
		
		MulticastSocket socket = new MulticastSocket(4321);
		
		InetAddress ia1 = InetAddress.getByName("230.0.0.1");
        InetSocketAddress avisosGerais = new InetSocketAddress(ia1, 4321);
        NetworkInterface ni1 = NetworkInterface.getByInetAddress(ia1);
        
        InetAddress ia2 = InetAddress.getByName("230.0.0.2");
        InetSocketAddress avisosEmergencia = new InetSocketAddress(ia2, 4321);
        NetworkInterface ni2 = NetworkInterface.getByInetAddress(ia2);
        
        InetAddress ia3 = InetAddress.getByName("230.0.0.3");
        InetSocketAddress grupoChat = new InetSocketAddress(ia3, 4321);
        NetworkInterface ni3 = NetworkInterface.getByInetAddress(ia3);
        
		System.out.println("Digite seu nome: ");
		String nome = sc.nextLine();
		
		System.out.println("Escolha uma dos grupos a seguir para se juntar: \r\n"
				+ "1- Grupo de avisos gerais do hospital\r\n"
				+ "2- Grupo de avisos de emergências\r\n"
				+ "3- Grupo de chat para os médicos do hospital ");
		String topico = sc.nextLine(); 
		
		//entrar em avisos gerais direto. Dar opção para emergencia e chat 
		System.out.println("oi");
		
		switch(topico) {
		case "1": 
			socket.joinGroup(avisosGerais, ni1);
			
			Runnable receber1 = new ReceberMensagensCliente(socket, "Avisos Gerais");
			Thread threadRecebe1 = new Thread(receber1);
			
			threadRecebe1.start();
		case "2":
			socket.joinGroup(avisosEmergencia, ni2);
			
			Runnable receber2 = new ReceberMensagensCliente(socket, "Emergências");
			Thread threadRecebe2 = new Thread(receber2);
			
			threadRecebe2.start();
		case "3": 
			socket.joinGroup(grupoChat, ni3);
			
			Runnable receber3 = new ReceberMensagensCliente(socket, "Chat");
			Thread threadRecebe3 = new Thread(receber3);
			
			Runnable enviar = new EnviarMensagensCliente(socket, ia3, nome);
			Thread threadEnvia = new Thread(enviar);
						
			threadRecebe3.start();
			threadEnvia.start();
		}
	}
}
