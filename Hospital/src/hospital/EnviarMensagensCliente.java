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
		
		LocalDateTime agora = LocalDateTime.now();
		DateTimeFormatter formato = DateTimeFormatter.ofPattern("dd/MM/yyyy - HH:mm:ss");
	    String dataHoraFormatada = agora.format(formato);		
	    
		String header = "entrada " + nome + " " + dataHoraFormatada;
		
		envio = header.getBytes();
		
		DatagramPacket pacoteHeader = new DatagramPacket(envio, envio.length, ia, 4321);
		
		socket.send(pacoteHeader);
		
		while(true) {
			//print: escreva mensgem 
			String mensagem = sc.nextLine();
	
			envio = mensagem.getBytes(); 
			
			DatagramPacket pacote = new DatagramPacket(envio, envio.length, ia, 4321);
			socket.send(pacote);
		}
	}
}
