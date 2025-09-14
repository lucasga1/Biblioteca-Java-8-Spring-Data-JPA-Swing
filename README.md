
Biblioteca Digital – Tecnologias e Funcionamento

1. Visão Geral do Sistema
O sistema Biblioteca Digital foi desenvolvido em Java, visando a gestão de livros, incluindo cadastro, atualização, busca e exclusão. Ele integra funcionalidades de persistência de dados, interface gráfica para interação do usuário e consumo de APIs externas. O sistema também prioriza transações seguras, tratamento de exceções e experiência amigável na interface.


2. Tecnologias, Frameworks e Bibliotecas
- Java 8: Linguagem base utilizada para desenvolvimento do sistema, aproveitando recursos como Streams, Optional e expressões lambda.

- Spring Boot: Framework principal utilizado para criar a aplicação backend. Ele fornece:
  - Injeção de Dependência: Gerenciamento automático de instâncias de classes, facilitando testes e manutenção.
  - Controllers e Services: Separação de responsabilidades entre lógica de negócio e endpoints da API.
  - Spring Data JPA: Integração com banco de dados para persistência de entidades, utilizando repositórios e consultas simplificadas.

- JPA (Java Persistence API): Responsável pela persistência de dados das entidades, como Livro e LivroDTO. Facilita operações de CRUD, mapeamento objeto-relacional e gerenciamento de transações.

- Jackson: Biblioteca para conversão entre objetos Java e JSON, usada para:
  - Serialização: Transformar objetos Java em JSON para APIs.
  - Desserialização: Transformar JSON recebido em objetos Java.

- OpenLibraryClient: Cliente HTTP personalizado para consumir APIs externas de livros, transformando os resultados JSON em objetos LivroDTO.

- Swing: Biblioteca gráfica utilizada para criar a interface do usuário, incluindo:
  - JTable: Para exibição de listas de livros.
  - JOptionPane: Para exibição de mensagens, confirmações e erros.
  - JFileChooser: Para seleção de arquivos no sistema de arquivos.

- MaskFormatter: Utilizado para formatação de campos de entrada, garantindo a padronização de dados como datas e ISBN.

- Streams e Lambdas: Facilitaram operações com coleções, como filtragem, mapeamento e ordenação de listas de livros.

- Transações e Rollback: Implementadas usando Spring, garantindo que operações de exclusão ou atualização que falharem não comprometam a integridade do banco de dados.

3. Estrutura do Sistema
- Modelos (Entities): Classes que representam os livros (Livro) e dados de transporte (LivroDTO).
- Repositórios: Interfaces que estendem JPA Repository para persistência de dados.
- Serviços: Contêm a lógica de negócio, validação de dados e integração com APIs externas.
- Controllers: Expostos como endpoints REST, permitindo acesso aos dados via JSON.
- Cliente HTTP: OpenLibraryClient para comunicação com APIs externas.
- Interface Gráfica: Formulários e tabelas para interação do usuário, incluindo carregamento de dados, busca e filtros.

4. Funcionalidades Principais
- Cadastro e atualização de livros.
- Busca por filtros como título, autor ou categoria.
- Exclusão de livros com rollback em caso de falha.
- Consumo de APIs externas para obter dados adicionais sobre livros.
- Exportação e importação de arquivos CSV para gerenciamento em lote.

5. Observações Técnicas
- A aplicação prioriza o tratamento de exceções customizadas para erros de cadastro, busca ou exclusão de livros.
- A arquitetura segue princípios de separação de responsabilidades e boas práticas de desenvolvimento Spring Boot.
- O uso de PageResponse permite paginação eficiente ao exibir listas grandes de livros.
