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
		byte[] envio = new byte[1024];
		
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
        
        Runnable receberGerais = new ReceberMensagensCliente("230.0.0.1", 4321, "Avisos Gerais");
        Thread threadRecebeGerais = new Thread(receberGerais);
        threadRecebeGerais.start();
        
		System.out.println("Digite seu nome: ");
		String nome = sc.nextLine();
		
		enviarHeader(socket, nome, ia1); 
	
		System.out.println("Você foi conectado ao grupo de avisos gerais. Opções de outros grupo para se juntar: \r\n"
				+ "1- Grupo de avisos de emergências\r\n"
				+ "2- Grupo de Chat para os médicos co hospital\r\n"
				+ "3- Entrar para os dois grupos\r\n"
				+ "4- Não entrar para mais nenhum grupo\r\n"); 		
		
		String topico = sc.nextLine();
		
		if(topico.equals("1")) {
			
	        Runnable receberEmergencia = new ReceberMensagensCliente("230.0.0.2", 4321, "Emergências");
	        Thread threadRecebeEmergencia = new Thread(receberEmergencia);
	        threadRecebeEmergencia.start();	        

			enviarHeader(socket, nome, ia2);
			
		}else if(topico.equals("2")){

	        Runnable receberChat = new ReceberMensagensCliente("230.0.0.3", 4321, "Chat");
	        Thread threadRecebeChat = new Thread(receberChat);
	        threadRecebeChat.start();
            
			enviarHeader(socket, nome, ia3); 
	        
	        while(true) {
	            System.out.println("Digite sua mensagem para enviar ao grupo de chat:");
	            String mensagem = nome +": " +sc.nextLine();
		
				envio = mensagem.getBytes(); 
				
				DatagramPacket pacote = new DatagramPacket(envio, envio.length, ia3, 4321);
				socket.send(pacote);
	        }
			
		}else if(topico.equals("3")) {
			
			Runnable receberEmergencia = new ReceberMensagensCliente("230.0.0.2", 4321, "Emergências");
	        Thread threadRecebeEmergencia = new Thread(receberEmergencia);
	         
	        Runnable receberChat = new ReceberMensagensCliente("230.0.0.3", 4321, "Chat");
	        Thread threadRecebeChat = new Thread(receberChat);
	        
	        threadRecebeChat.start();
	        threadRecebeEmergencia.start(); 

			enviarHeader(socket, nome, ia2);
			enviarHeader(socket, nome, ia3);    
	        
	        while(true) {
	            System.out.println("Digite sua mensagem para enviar ao grupo de chat:");
	            String mensagem = nome +": " +sc.nextLine();
		
				envio = mensagem.getBytes(); 
				
				DatagramPacket pacote = new DatagramPacket(envio, envio.length, ia3, 4321);
				socket.send(pacote);
	        }
		}
	}
	
	public static void enviarHeader(MulticastSocket socket, String nome, InetAddress ia) throws IOException {
		byte[] envio = new byte[1024];
		System.out.println("entro");
	
		String header = "entrada " + nome;
		
		envio = header.getBytes();
		
		DatagramPacket pacoteHeader = new DatagramPacket(envio, envio.length, ia, 4321);
		
		socket.send(pacoteHeader);
	}
}
