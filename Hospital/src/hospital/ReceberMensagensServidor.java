package hospital;

import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.NetworkInterface;
import java.util.ArrayList;

public class ReceberMensagensServidor implements Runnable {
    private String multicastGroup;
    private int port;
    private MulticastSocket socket;
    private String nomeGrupo;
    private ArrayList<String> nomes = new ArrayList<>();
    private int quantidadeClientes;
    private byte[] envio = new byte[1024];
    private InetAddress ia;

    public ReceberMensagensServidor(String multicastGroup, int port, String nomeGrupo, InetAddress ia) {
        this.multicastGroup = multicastGroup;
        this.port = port;
        this.nomeGrupo = nomeGrupo; 
        this.ia = ia;
    }

    @Override
    public void run() {
        try {
            socket = new MulticastSocket(port);
            NetworkInterface networkInterface = NetworkInterface.getByInetAddress(InetAddress.getLocalHost());
            
            socket.joinGroup(new java.net.InetSocketAddress(multicastGroup, port), networkInterface);

            while (true) {
                byte[] buffer = new byte[1024];
                DatagramPacket packet = new DatagramPacket(buffer, buffer.length);

                socket.receive(packet);

                String received = new String(packet.getData(), 0, packet.getLength());
                
                String[] palavras = received.split("\\s+");
                
                if(palavras[0].equals("entrada")) {
                	nomes.add(palavras[1]);
                	quantidadeClientes += 1; 
                	
                	String mensagem = "Servidor: " + palavras[1] + " se conectou ao grupo " + nomeGrupo + "!"; 

                	envio = mensagem.getBytes();
            		
            		DatagramPacket pacote = new DatagramPacket(envio, envio.length, ia, 4321);
            		socket.send(pacote);
                }else {
                	System.out.println(received + "\n");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (socket != null && !socket.isClosed()) {
                socket.close();
            }
        }
    }
}