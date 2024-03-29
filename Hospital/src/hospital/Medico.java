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
        

        socket.joinGroup(avisosGerais, ni1);
        
        Runnable receberGerais = new ReceberMensagensCliente(socket, "Avisos Gerais");
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
			entrarNoGrupoEmergencia(socket);
			
		}else if(topico.equals("2")){
			entrarNoGrupoChat(socket);
			
		}else if(topico.equals("3")) {
			entrarNoGrupoEmergencia(socket);
			entrarNoGrupoChat(socket);
		}
	}
	
    private static void entrarNoGrupoEmergencia(MulticastSocket socket) throws IOException {
        InetAddress ia2 = InetAddress.getByName("230.0.0.2");
        InetSocketAddress avisosEmergencia = new InetSocketAddress(ia2, 4321);
        NetworkInterface ni2 = NetworkInterface.getByInetAddress(ia2);
        socket.joinGroup(avisosEmergencia, ni2);

        Runnable receberEmergencia = new ReceberMensagensCliente(socket, "Emergências");
        Thread threadRecebeEmergencia = new Thread(receberEmergencia);
        threadRecebeEmergencia.start();
    }

    private static void entrarNoGrupoChat(MulticastSocket socket) throws IOException {
        InetAddress ia3 = InetAddress.getByName("230.0.0.3");
        InetSocketAddress grupoChat = new InetSocketAddress(ia3, 4321);
        NetworkInterface ni3 = NetworkInterface.getByInetAddress(ia3);
        socket.joinGroup(grupoChat, ni3);

        Runnable receberChat = new ReceberMensagensCliente(socket, "Chat");
        Thread threadRecebeChat = new Thread(receberChat);
        
        threadRecebeChat.start();
    }
}

