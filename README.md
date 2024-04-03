# fccpd-2024.1
Fundamentos da Computação Concorrente Paralela e Distribuída 

Este projeto é um sistema de chat desenvolvido em Java, utilizando sockets Multicast para a troca de mensagens e tem como objetivo fornecer uma plataforma de comunicação baseada em tópicos, onde os usuários podem se juntar a diferentes grupos de discussão de acordo com seus interesses. Nosso contexto escolhido foi de um hospital, onde iremos oferecer os seguintes tópicos para troca de mensagens: 
- Avisos Gerais 
- Emergência
- Chat

## Funcionalidades
- **Troca de Mensagens:** Utiliza sockets Multicast para a troca de mensagens, permitindo a comunicação eficiente em grupo.
- **Seleção de Tópicos:** Os usuários podem escolher entre diferentes tópicos para participar.
- **Entrada e Saída de Tópicos:** Os usuários podem entrar e sair dos tópicos a qualquer momento. A saída do usuário é notificada aos demais participantes.

## Configuração e Execução
Com o java já instalado na sua máquina: 
1. Clone o repositório do projeto para sua máquina local. <br>
   ```git clone url_do_projeto```
2. Abra um terminal e navegue até a pasta do projeto. <br>
  ```cd nome_do_arquivo```
3. Execute primeiro o servidor, representado pela classe Recepcao.java
4. Abra um novo terminal e execute agora um ou mais clientes, representados pela classe Medico.java

