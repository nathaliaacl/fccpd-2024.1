package hospital;

import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.NetworkInterface;

public class ReceberMensagensCliente implements Runnable {
    private String multicastGroup;
    private int port;
    private MulticastSocket socket;

    public ReceberMensagensCliente(String multicastGroup, int port) {
        this.multicastGroup = multicastGroup;
        this.port = port;
    }

    @Override
    public void run() {
        try {
            // Criação do socket multicast
            socket = new MulticastSocket(port);

            // Obter a interface de rede para se juntar ao grupo multicast
            NetworkInterface networkInterface = NetworkInterface.getByInetAddress(InetAddress.getLocalHost());

            // Juntando-se ao grupo multicast especificado
            socket.joinGroup(new java.net.InetSocketAddress(multicastGroup, port), networkInterface);

            // Loop infinito para escutar o grupo multicast
            while (true) {
                byte[] buffer = new byte[1024];
                DatagramPacket packet = new DatagramPacket(buffer, buffer.length);

                // Recebendo pacote
                socket.receive(packet);

                // Convertendo os dados para String e exibindo na saída padrão
                String received = new String(packet.getData(), 0, packet.getLength());
                System.out.println("Mensagem recebida no grupo " + multicastGroup + ": " + received);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // Fechando o socket ao finalizar
            if (socket != null && !socket.isClosed()) {
                socket.close();
            }
        }
    }
}
