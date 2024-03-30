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
        
        Runnable receberGerais = new ReceberMensagensCliente("230.0.0.1", 4321);
        Thread threadRecebeGerais = new Thread(receberGerais);
        threadRecebeGerais.start();
        
		System.out.println("Digite seu nome: ");
		String nome = sc.nextLine();
		
		System.out.println("Você foi conectado ao grupo de avisos gerais. Opções de outros grupo para se juntar: \r\n"
				+ "1- Grupo de avisos de emergências\r\n"
				+ "2- Grupo de Chat para os médicos co hospital\r\n"
				+ "3- Entrar para os dois grupos\r\n"
				+ "4- Não entrar para mais nenhum grupo"); 		
		
		String topico = sc.nextLine();
		
		if(topico.equals("1")) {

	        Runnable receberEmergencia = new ReceberMensagensCliente("230.0.0.2", 4321);
	        Thread threadRecebeEmergencia = new Thread(receberEmergencia);
	        threadRecebeEmergencia.start();
			
		}else if(topico.equals("2")){

	        Runnable receberChat = new ReceberMensagensCliente("230.0.0.3", 4321);
	        Thread threadRecebeChat = new Thread(receberChat);
	        threadRecebeChat.start();
			
		}else if(topico.equals("3")) {
			
			Runnable receberEmergencia = new ReceberMensagensCliente("230.0.0.2", 4321);
	        Thread threadRecebeEmergencia = new Thread(receberEmergencia);
	        Runnable receberChat = new ReceberMensagensCliente("230.0.0.3", 4321);
	        Thread threadRecebeChat = new Thread(receberChat);
	        
	        threadRecebeChat.start();
	        threadRecebeEmergencia.start();        
		}
	}
}

