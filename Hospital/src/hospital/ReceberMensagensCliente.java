package hospital;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.MulticastSocket;

class ReceberMensagensCliente implements Runnable {
    private MulticastSocket socket;
    private String nomeGrupo;

    public ReceberMensagensCliente(MulticastSocket socket, String nomeGrupo) {
        this.socket = socket;
        this.nomeGrupo = nomeGrupo;
    }

    @Override
    public void run() {
        try {
            receberMensagens();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void receberMensagens() throws IOException {

        while (true) {
        	System.out.println("Esperando por mensagem Multicast do grupo...");
        	
            byte[] buffer = new byte[1024];
            DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
            socket.receive(packet);

            String msg = new String(packet.getData(), 0, packet.getLength());
            
            String[] palavras = msg.split("\\s+");
            
            if(palavras[0].equals("entrada")) {
            	continue; 
            }else {
            	System.out.println( nomeGrupo + ": " + msg + "\n");
            }
            
        }
    }
}