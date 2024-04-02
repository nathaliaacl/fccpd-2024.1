package hospital;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.MulticastSocket;
import java.net.NetworkInterface;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

public class Recepcao {
    private static final int PORT = 4446;
    private static List<String> topicosDisponiveis = Arrays.asList(
        "230.0.0.1", // Tópico 1
        "230.0.0.2", // Tópico 2
        "230.0.0.3" // Tópico 3
    );
    private MulticastSocket socket;
    private boolean running = true;
    private String nomeRemetente = "Recepção Hospital";
    private InetAddress ia;
    private ArrayList<String> listaClientesAvisos = new ArrayList<>();
    private ArrayList<String> listaClientesEmergencias = new ArrayList<>();
    private ArrayList<String> listaClientesChat = new ArrayList<>();

    Scanner scanner = new Scanner(System.in);

    public Recepcao() throws IOException {
        this.socket = new MulticastSocket(PORT);
    }
    
    public void entrada() throws IOException{
    	System.out.println("Servidor conectado!");
    	entrarTopicos(); 
    }

	public void entrarTopicos() throws IOException {	
        for(String grupo : topicosDisponiveis) {
            this.ia = InetAddress.getByName(grupo);
            InetSocketAddress isa = new InetSocketAddress(ia, PORT);
            NetworkInterface ni = NetworkInterface.getByInetAddress(ia);
            
            this.socket.joinGroup(isa, ni);
            
            Thread thread = new Thread(new ReceberMensagens());
            thread.start();
        }
        enviarMensagens(scanner);
        
    }

    private void enviarMensagens(Scanner scanner) throws IOException {
    	 SimpleDateFormat dateFormat = new SimpleDateFormat("[dd/MM/yyyy - HH:mm]");
         while (true) {
             System.out.println("Escolha o número do grupo para enviar a mensagem:");
             for (int i = 0; i < topicosDisponiveis.size(); i++) {
                 System.out.println((i + 1) + ". " + traduzir(topicosDisponiveis.get(i)));
             }
             int escolha = scanner.nextInt();
             scanner.nextLine(); 

             if (escolha < 1 || escolha > topicosDisponiveis.size()) {
                 System.out.println("Escolha inválida. Tente novamente.");
                 continue;
             }
             String enderecoTopico = topicosDisponiveis.get(escolha - 1);
             
             InetAddress ia = InetAddress.getByName(enderecoTopico);

             System.out.println("Digite a mensagem:");
             String mensagem = scanner.nextLine();

             String mensagemFormatada = traduzir(enderecoTopico) + dateFormat.format(new Date()) + " " + nomeRemetente + " : " + mensagem;
             byte[] buffer = mensagemFormatada.getBytes();

             DatagramPacket messageOut = new DatagramPacket(buffer, buffer.length, ia, PORT);
             socket.send(messageOut);
         }
    }
    
    public class ReceberMensagens implements Runnable{
    	@Override
		public void run() {
			while (running) {
                byte[] buffer = new byte[1000];
                DatagramPacket messageIn = new DatagramPacket(buffer, buffer.length);
                try {
                    socket.receive(messageIn);
                    String received = new String(messageIn.getData(), 0, messageIn.getLength());
                    String palavras[] = received.split("\\s+"); 
                    if(palavras[0].equals("entrada")) {
                    	if(palavras[1].equals("230.0.0.1")) {
                    		listaClientesAvisos.add(palavras[2]);                    		
                    		enviarConfirmacao(palavras[2], palavras[1], socket);
                    	}else if(palavras[1].equals("230.0.0.2")) {
                    		listaClientesEmergencias.add(palavras[2]);
                    		enviarConfirmacao(palavras[2], palavras[1], socket);
                    	}else if(palavras[1].equals("230.0.0.3")) {
                    		listaClientesChat.add(palavras[2]);
                    		enviarConfirmacao(palavras[2], palavras[1], socket);
                    	}
                    }else if(palavras[0].equals("saida")) {
                    	if(palavras[1].equals("230.0.0.1")) {
                    		listaClientesAvisos.remove(palavras[2]);                    		
                    		enviarSaida(palavras[2], palavras[1], socket);
                    	}else if(palavras[1].equals("230.0.0.2")) {
                    		listaClientesEmergencias.remove(palavras[2]);
                    		enviarSaida(palavras[2], palavras[1], socket);
                    	}else if(palavras[1].equals("230.0.0.3")) {
                    		listaClientesChat.remove(palavras[2]);
                    		enviarSaida(palavras[2], palavras[1], socket);
                    	}
                	}else {
                        System.out.println(received);
                    }
                } catch (IOException e) {
                    System.out.println("Erro ao receber mensagem: " + e.getMessage());
                }			
			}
		}
    }
    
    public static void enviarConfirmacao (String nome, String endereco, MulticastSocket socket) throws IOException {

		String confirmacao = nome + " se conectou ao grupo " + traduzir(endereco);
		
		InetAddress ia = InetAddress.getByName(endereco);
		 
    	byte[] buffer1 = confirmacao.getBytes();
    	DatagramPacket messageOut = new DatagramPacket(buffer1, buffer1.length, ia, PORT);
        socket.send(messageOut); 
    }
    
    public static void enviarSaida(String nome, String endereco, MulticastSocket socket) throws IOException {
    	String saida = nome + " se desconectou do grupo " + traduzir(endereco);
    	
    	InetAddress ia = InetAddress.getByName(endereco);
		 
    	byte[] buffer1 = saida.getBytes();
    	DatagramPacket messageOut = new DatagramPacket(buffer1, buffer1.length, ia, PORT);
        socket.send(messageOut); 
    }
    
    private static String traduzir(String endereco) {
 	   String nomeGrupo = null; 
 	   if(endereco.equals("230.0.0.1")) {
 		   nomeGrupo = "Avisos Gerais";
 	   }else if(endereco.equals("230.0.0.2")){
 		   nomeGrupo = "Avisos de Emergência";
 	   }else if(endereco.equals("230.0.0.3")) {
 		   nomeGrupo = "Chat";		   
 	   }
 	   return nomeGrupo; 
    }

    public static void main(String[] args) throws IOException {
        new Recepcao().entrada();
    }
}
