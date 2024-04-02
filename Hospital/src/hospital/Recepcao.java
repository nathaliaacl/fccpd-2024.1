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
    private List<String> gruposSelecionados = new ArrayList<>();
    private InetAddress group;
    private boolean running = true;
    private String nomeRemetente;
    private List<InetAddress> grupos = new ArrayList<>();

    Scanner scanner = new Scanner(System.in);

    public Recepcao() throws IOException {
        this.socket = new MulticastSocket(PORT);
    }
    
    public void entrada() throws IOException{
    	addNome();
    	entrarTopicos(); 
    }

	public void entrarTopicos() throws IOException {	
        for(String grupo : topicosDisponiveis) {
            InetAddress ia = InetAddress.getByName(grupo);
            InetSocketAddress isa = new InetSocketAddress(ia, PORT);
            NetworkInterface ni = NetworkInterface.getByInetAddress(ia);
            
            this.group = InetAddress.getByName(grupo);
            this.socket.joinGroup(isa, ni);
            iniciarRecebimentoMensagens();
        }
        enviarMensagens(scanner);
        
    }

    private void iniciarRecebimentoMensagens() {
        new Thread(() -> {
            while (running) {
                byte[] buffer = new byte[1000];
                DatagramPacket messageIn = new DatagramPacket(buffer, buffer.length);
                try {
                    socket.receive(messageIn);
                    String received = new String(messageIn.getData(), 0, messageIn.getLength());
                    System.out.println("Received: " + received);
                } catch (IOException e) {
                    System.out.println("Erro ao receber mensagem: " + e.getMessage());
                }
            }
        }).start();
    }

    private void enviarMensagens(Scanner scanner) throws IOException {
    	 SimpleDateFormat dateFormat = new SimpleDateFormat("[dd/MM/yyyy - HH:mm]");
         while (true) {
             System.out.println("Escolha o número do grupo para enviar a mensagem:");
             for (int i = 0; i < topicosDisponiveis.size(); i++) {
                 System.out.println((i + 1) + ". " + topicosDisponiveis.get(i));
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

             String mensagemFormatada = dateFormat.format(new Date()) + " " + nomeRemetente + " : " + mensagem;
             byte[] buffer = mensagemFormatada.getBytes();

             DatagramPacket messageOut = new DatagramPacket(buffer, buffer.length, ia, PORT);
             socket.send(messageOut);
         }
    }
    
    private void addNome () {
        System.out.println("Informe seu nome (ou identificador):");
        nomeRemetente = scanner.nextLine();
    }

    /*@SuppressWarnings("deprecation")
	private void sairTopico(InetSocketAddress isa, NetworkInterface ni) throws IOException {
        socket.leaveGroup(isa, ni);;
        System.out.println("Você saiu do tópico.");
        entrarTopicos(); // Dá a opção de escolher outro tópico ou sair
    }*/

    public static void main(String[] args) throws IOException {
        new Recepcao().entrada();
    }
}
