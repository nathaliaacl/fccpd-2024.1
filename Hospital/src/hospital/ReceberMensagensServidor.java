package hospital;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.util.ArrayList;

class ReceberMensagensServidor implements Runnable {
    private MulticastSocket socket;
    private String nomeGrupo;
    private ArrayList<String> nomes = new ArrayList<>(); 
    private int quantidadeClientes;
	private InetAddress ia; 

    public ReceberMensagensServidor(MulticastSocket socket, String nomeGrupo, InetAddress ia) {
        this.socket = socket;
        this.nomeGrupo = nomeGrupo;
        this.ia = ia;
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
        	
            byte[] buffer = new byte[1024];
            DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
            socket.receive(packet);

            String msg = new String(packet.getData(), 0, packet.getLength());
            
            String[] palavras = msg.split("\\s+");
            
            if(palavras[0].equals("entrada")) {
            	nomes.add(palavras[1]);
            	quantidadeClientes += 1; 
            	
            	Runnable enviar = new EnviarMensagensServidor(socket, ia, 0, palavras[1]);
            	Thread thread = new Thread(enviar);
            	thread.start();      	
            }else {
            	System.out.println("servidor: " + msg + "\n");
            }
            
        }
    }
}