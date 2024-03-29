package hospital;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.util.Scanner;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class EnviarMensagensServidor implements Runnable {
	private MulticastSocket socket;
	private Scanner sc = new Scanner(System.in);
	private InetAddress ia; 
	private int flag;
	private String nome;
	byte[] envio = new byte[1024];
	
	public EnviarMensagensServidor(MulticastSocket socket, InetAddress ia, int flag) {
        this.socket = socket;
        this.ia = ia;
        this.flag = flag; 
    }
	
	public EnviarMensagensServidor(MulticastSocket socket, InetAddress ia, int flag, String nome) {
        this.socket = socket;
        this.ia = ia;
        this.flag = flag; 
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
		if(flag == 1) {
			while(true) {
				System.out.println("\n[Servidor] Digite o conteúdo da mensagem:");
				String mensagem = sc.nextLine();
		
				envio = mensagem.getBytes(); 
				
				DatagramPacket pacote = new DatagramPacket(envio, envio.length, ia, 4321);
				socket.send(pacote);
			}
		}else {
			String mensagem = nome + " se conectou ao grupo!";
			envio = mensagem.getBytes();
			DatagramPacket pacote = new DatagramPacket(envio, envio.length, ia, 4321);
			socket.send(pacote);
		}
	}
}
