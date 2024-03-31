package hospital;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.util.Scanner;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class EnviarMensagensCliente implements Runnable {
	private MulticastSocket socket;
	private Scanner sc = new Scanner(System.in);
	private InetAddress ia; 
	private String nome;
	byte[] envio = new byte[1024];
	
	public EnviarMensagensCliente(MulticastSocket socket, InetAddress ia, String nome) {
        this.socket = socket;
        this.ia = ia;
        this.nome = nome;
    }
	
	@Override
	public void run() {
		try {
			enviarMensagens();
		}catch(IOException e) {
			e.printStackTrace();
		}
	}
	
	private void enviarMensagens() throws IOException{
		
		while(true) {
            System.out.println("Digite sua mensagem para enviar ao grupo de chat:");
            String mensagem = nome +": " +sc.nextLine();
	
			envio = mensagem.getBytes(); 
			
			DatagramPacket pacote = new DatagramPacket(envio, envio.length, ia, 4321);
			socket.send(pacote);
		}
	}
}
