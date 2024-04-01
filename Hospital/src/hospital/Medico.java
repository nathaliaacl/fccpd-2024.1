package hospital;

import java.io.IOException;
import java.util.Scanner;
import java.net.MulticastSocket;
import java.net.NetworkInterface;
import java.net.InetAddress;
import java.net.DatagramPacket;
import java.net.InetSocketAddress;

public class Medico {
	public static void main(String[] args) throws IOException {
		Scanner sc = new Scanner(System.in);
		byte[] envio = new byte[1024];

		MulticastSocket socket = new MulticastSocket(4321);

		InetAddress ia1 = InetAddress.getByName("230.0.0.1");
		InetAddress ia2 = InetAddress.getByName("230.0.0.2");
		InetAddress ia3 = InetAddress.getByName("230.0.0.3");

		ReceberMensagensCliente instanciaReceberGerais = new ReceberMensagensCliente("230.0.0.1", 4321, "Avisos Gerais");
		Thread threadRecebeGerais = new Thread(instanciaReceberGerais);
		threadRecebeGerais.start();

		System.out.println("Digite seu nome: ");
		String nome = sc.nextLine();

		enviarHeader(socket, nome, ia1);

		System.out.println("Você foi conectado ao grupo de avisos gerais. Opções de outros grupo para se juntar: \r\n"
				+ "1- Grupo de avisos de emergências\r\n"
				+ "2- Grupo de Chat para os médicos do hospital\r\n"
				+ "3- Entrar para os dois grupos\r\n"
				+ "4- Não entrar para mais nenhum grupo\r\n");

		String topico = sc.nextLine();
		ReceberMensagensCliente instanciaReceberEmergencia = null;
		ReceberMensagensCliente instanciaReceberChat = null;

		if (topico.equals("1")) {
			instanciaReceberEmergencia = new ReceberMensagensCliente("230.0.0.2", 4321, "Emergências");
			Thread threadRecebeEmergencia = new Thread(instanciaReceberEmergencia);
			threadRecebeEmergencia.start();

			enviarHeader(socket, nome, ia2);

			while (true) {
				System.out.println("\nOpções:");
				System.out.println("1 - Sair de um grupo específico");
				System.out.println("2 - Sair de todos os grupos");
				System.out.println("Digite a opção que você quer:");

				String opcao = sc.nextLine();

				switch (opcao) {
					case "1":
						System.out.println("Digite o número do grupo do qual deseja sair:");
						System.out.println("1 - Avisos Gerais");
						System.out.println("2 - Emergências");
						System.out.println("3 - Chat");
						String grupo = sc.nextLine();

						try {
							if ("1".equals(grupo) && instanciaReceberGerais != null) {
								instanciaReceberGerais.sairGrupo();
								instanciaReceberGerais = null;
								System.out.println(nome + "saiu do grupo de Avisos Gerais.");
							} else if ("2".equals(grupo) && instanciaReceberEmergencia != null) {
								instanciaReceberEmergencia.sairGrupo();
								instanciaReceberEmergencia = null;
								System.out.println(nome + "saiu do grupo de Emergências.");
							} else if ("3".equals(grupo) && instanciaReceberChat != null) {
								instanciaReceberChat.sairGrupo();
								instanciaReceberChat = null;
								System.out.println(nome + "saiu do grupo de Chat.");
							} else {
								System.out.println("Você não está nesse grupo");
							}
						} catch (IOException e) {
							e.printStackTrace();
						}
						break;
					case "2":
						try {
							if (instanciaReceberGerais != null) {
								instanciaReceberGerais.sairGrupo();
								instanciaReceberGerais = null;
							}
							if (instanciaReceberEmergencia != null) {
								instanciaReceberEmergencia.sairGrupo();
								instanciaReceberEmergencia = null;
							}
							if (instanciaReceberChat != null) {
								instanciaReceberChat.sairGrupo();
								instanciaReceberChat = null;
							}
							System.out.println(nome + "saiu de todos os grupos.");
							//sc.close();
							//socket.close();
							return;
						} catch (IOException e) {
							System.out.println(nome + "Erro saindo dos grupos...");
						}
						break;
					default:
						System.out.println("Opção inválida.");
						break;
				}
			}
		} else if (topico.equals("2")) {
			instanciaReceberChat = new ReceberMensagensCliente("230.0.0.3", 4321, "Chat");
			Thread threadRecebeChat = new Thread(instanciaReceberChat);
			threadRecebeChat.start();

			enviarHeader(socket, nome, ia3);

			while (true) {
				System.out.println("\nOpções:");
				System.out.println("1 - Sair de um grupo específico");
				System.out.println("2 - Sair de todos os grupos");
				System.out.println("3 - Enviar mensagem no Chat");
				System.out.println("Digite a opção que você quer:");

				String opcao = sc.nextLine();

				switch (opcao) {
					case "1":
						System.out.println("Digite o número do grupo do qual deseja sair:");
						System.out.println("1 - Avisos Gerais");
						System.out.println("2 - Emergências");
						System.out.println("3 - Chat");
						String grupo = sc.nextLine();

						try {
							if ("1".equals(grupo) && instanciaReceberGerais != null) {
								instanciaReceberGerais.sairGrupo();
								instanciaReceberGerais = null;
								System.out.println(nome + "saiu do grupo de Avisos Gerais.");
							} else if ("2".equals(grupo) && instanciaReceberEmergencia != null) {
								instanciaReceberEmergencia.sairGrupo();
								instanciaReceberEmergencia = null;
								System.out.println(nome + "saiu do grupo de Emergências.");
							} else if ("3".equals(grupo) && instanciaReceberChat != null) {
								instanciaReceberChat.sairGrupo();
								instanciaReceberChat = null;
								System.out.println(nome + "saiu do grupo de Chat.");
							} else {
								System.out.println("Você não está nesse grupo");
							}
						} catch (IOException e) {
							e.printStackTrace();
						}
						break;
					case "2":
						try {
							if (instanciaReceberGerais != null) {
								instanciaReceberGerais.sairGrupo();
								instanciaReceberGerais = null;
							}
							if (instanciaReceberEmergencia != null) {
								instanciaReceberEmergencia.sairGrupo();
								instanciaReceberEmergencia = null;
							}
							if (instanciaReceberChat != null) {
								instanciaReceberChat.sairGrupo();
								instanciaReceberChat = null;
							}
							System.out.println(nome + "saiu de todos os grupos.");
							//sc.close();
							//socket.close();
							return;
						} catch (IOException e) {
							System.out.println(nome + "Erro saindo dos grupos...");
						}
						break;
					case "3":
						if (instanciaReceberChat != null) {
							System.out.println("Digite sua mensagem para enviar ao grupo de chat:");
							String mensagem = nome + ": " + sc.nextLine();
							envio = mensagem.getBytes();
							DatagramPacket pacote = new DatagramPacket(envio, envio.length, ia3, 4321);
							socket.send(pacote);
						} else {
							System.out.println("Você não está no grupo de Chat.");
						}
						break;
					default:
						System.out.println("Opção inválida.");
						break;
				}
			}
		} else if (topico.equals("3")) {
			instanciaReceberEmergencia = new ReceberMensagensCliente("230.0.0.2", 4321, "Emergências");
			Thread threadRecebeEmergencia = new Thread(instanciaReceberEmergencia);
			threadRecebeEmergencia.start();

			instanciaReceberChat = new ReceberMensagensCliente("230.0.0.3", 4321, "Chat");
			Thread threadRecebeChat = new Thread(instanciaReceberChat);
			threadRecebeChat.start();

			enviarHeader(socket, nome, ia2);
			enviarHeader(socket, nome, ia3);

			while (true) {
				System.out.println("\nOpções:");
				System.out.println("1 - Sair de um grupo específico");
				System.out.println("2 - Sair de todos os grupos");
				System.out.println("3 - Enviar mensagem no Chat");
				System.out.println("Digite a opção que você quer:");

				String opcao = sc.nextLine();

				switch (opcao) {
					case "1":
						System.out.println("Digite o número do grupo do qual deseja sair:");
						System.out.println("1 - Avisos Gerais");
						System.out.println("2 - Emergências");
						System.out.println("3 - Chat");
						String grupo = sc.nextLine();

						try {
							if ("1".equals(grupo) && instanciaReceberGerais != null) {
								instanciaReceberGerais.sairGrupo();
								instanciaReceberGerais = null;
								System.out.println(nome + "saiu do grupo de Avisos Gerais.");
							} else if ("2".equals(grupo) && instanciaReceberEmergencia != null) {
								instanciaReceberEmergencia.sairGrupo();
								instanciaReceberEmergencia = null;
								System.out.println(nome + "saiu do grupo de Emergências.");
							} else if ("3".equals(grupo) && instanciaReceberChat != null) {
								instanciaReceberChat.sairGrupo();
								instanciaReceberChat = null;
								System.out.println(nome + "saiu do grupo de Chat.");
							} else {
								System.out.println("Você não está nesse grupo");
							}
						} catch (IOException e) {
							e.printStackTrace();
						}
						break;
					case "2":
						try {
							if (instanciaReceberGerais != null) {
								instanciaReceberGerais.sairGrupo();
								instanciaReceberGerais = null;
							}
							if (instanciaReceberEmergencia != null) {
								instanciaReceberEmergencia.sairGrupo();
								instanciaReceberEmergencia = null;
							}
							if (instanciaReceberChat != null) {
								instanciaReceberChat.sairGrupo();
								instanciaReceberChat = null;
							}
							System.out.println(nome + "saiu de todos os grupos.");
							//sc.close();
							//socket.close();
							return;
						} catch (IOException e) {
							System.out.println(nome + "Erro saindo dos grupos...");
						}
						break;
					case "3":
						if (instanciaReceberChat != null) {
							System.out.println("Digite sua mensagem para enviar ao grupo de chat:");
							String mensagem = nome + ": " + sc.nextLine();
							envio = mensagem.getBytes();
							DatagramPacket pacote = new DatagramPacket(envio, envio.length, ia3, 4321);
							socket.send(pacote);
						} else {
							System.out.println("Você não está no grupo de Chat.");
						}
						break;
					default:
						System.out.println("Opção inválida.");
						break;
				}
			}
		}
	}

	public static void enviarHeader(MulticastSocket socket, String nome, InetAddress ia) throws IOException {
		byte[] envio = ("entrada " + nome).getBytes();
		DatagramPacket pacoteHeader = new DatagramPacket(envio, envio.length, ia, 4321);
		socket.send(pacoteHeader);
	}
}