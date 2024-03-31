package hospital;

import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.NetworkInterface;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ReceberMensagensCliente implements Runnable {
    private String multicastGroup;
    private int port;
    private MulticastSocket socket;
    private String nomeGrupo;

    public ReceberMensagensCliente(String multicastGroup, int port, String nomeGrupo) {
        this.multicastGroup = multicastGroup;
        this.port = port;
        this.nomeGrupo = nomeGrupo; 
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
                
                System.out.println(received);
                
                if(palavras[0].equals("entrada")) {
                	System.out.println("entrou");
                	continue;
                }else {
                	String mensagemFormatada = formatMessage(received, nomeGrupo);
                	
                	System.out.println(mensagemFormatada);
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
    
    private String formatMessage(String receivedMessage, String nomeGrupo) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy - HH:mm");
        String dataHora = dateFormat.format(new Date());

        String[] parts = receivedMessage.split(":", 2);
        String senderName = parts[0].trim();
        String message = parts[1].trim();

        String formattedMessage = "[" + nomeGrupo + " - " + dataHora + "] " + senderName + ": " + message;

        return formattedMessage;
    }
}