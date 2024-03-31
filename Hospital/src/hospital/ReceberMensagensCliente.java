package hospital;

import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.NetworkInterface;

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
            
            System.out.println("esperando por mensagem no grupo" + multicastGroup);

            while (true) {
                byte[] buffer = new byte[1024];
                DatagramPacket packet = new DatagramPacket(buffer, buffer.length);

                socket.receive(packet);

                String received = new String(packet.getData(), 0, packet.getLength());
                System.out.println("Mensagem recebida no grupo " + nomeGrupo + ": " + received);
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