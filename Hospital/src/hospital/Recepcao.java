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
		String mensagem;
		
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
        
        socket.joinGroup(avisosGerais, ni1);
        socket.joinGroup(avisosEmergencia, ni2);
        socket.joinGroup(grupoChat, ni3);
		
        while(true) {
        	System.out.print("[Servidor] Selecione para que grupo deseja enviar mensagem:\r\n"
        			+ "1- Enviar mensagem para o grupo de avisos gerais\r\n"
        			+ "2- Enviar mensagem para o grupo de aviso de emergência\r\n"
        			+ "3- Enviar mensagem para o grupo chat.");
        	topico = sc.nextLine();
        	System.out.print("[Servidor] Digite o conteúdo da mensagem:");
        	conteudo = sc.nextLine();
        	System.out.println(topico);   
        	
        	mensagem = conteudo; 

    		//formatar a mensgem direitinho
        	if(topico.equals("1")) {
        		envio = mensagem.getBytes();
        		
        		DatagramPacket pacote = new DatagramPacket(envio, envio.length, ia1, 4321);
        		socket.send(pacote);
        		
        	}else if(topico.equals("2")) {
        		envio = mensagem.getBytes();
        		
        		DatagramPacket pacote = new DatagramPacket(envio, envio.length, ia2, 4321);
        		socket.send(pacote);
        	}else if(topico.equals("3")) {
        		envio = mensagem.getBytes();
        		
        		DatagramPacket pacote = new DatagramPacket(envio, envio.length, ia3, 4321);
        		socket.send(pacote);
        	}
        }
		
	}
}
