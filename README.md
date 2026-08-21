# Encurtador de URL

Aplicação web desenvolvida em Java para criação e utilização de URLs curtas.

O usuário informa uma URL original e, opcionalmente, um alias personalizado. A aplicação gera uma URL curta que, ao ser acessada, redireciona para o endereço original.

## Tecnologias

- Java 8
- Java EE 7
- JAX-RS
- CDI
- JPA 2.1 / Hibernate
- JTA
- H2 Database
- DataSource gerenciado pelo WildFly
- WildFly 10.1.0.Final
- Maven
- JUnit
- Mockito
- HTML
- CSS
- JavaScript
- Docker
- Docker Compose

## Estrutura do projeto

O projeto foi separado em camadas para manter as responsabilidades bem definidas e facilitar a manutenção e os testes.

```text
src/main/java/br/com/topaz/encurtador
├── domain
├── dto
├── exception
├── repository
├── resource
└── service
```

As principais responsabilidades são:

- `resource`: disponibiliza os endpoints REST e recebe as requisições HTTP;
- `service`: concentra as regras de negócio do encurtador;
- `repository`: realiza o acesso e persistência dos dados;
- `domain`: contém as entidades da aplicação;
- `dto`: representa os dados utilizados na entrada e saída da API;
- `exception`: concentra as exceções e o tratamento dos erros da aplicação.

O fluxo principal pode ser representado da seguinte forma:

```text
Cliente
   |
   v
JAX-RS Resource
   |
   v
Service
   |
   v
Repository
   |
   v
JPA / Hibernate
   |
   v
DataSource / JTA
   |
   v
H2
```

## Funcionalidades

A aplicação permite:

- cadastrar uma URL original;
- informar um alias personalizado;
- gerar automaticamente um código quando o alias não é informado;
- validar os dados recebidos;
- impedir a utilização de aliases já cadastrados;
- localizar a URL original através do código;
- redirecionar uma URL curta para a URL original;
- utilizar uma interface web simples para criação dos links.

# API

## Criar uma URL curta

```http
POST /url-encurtador/api/urls
Content-Type: application/json
```

Exemplo utilizando um alias personalizado:

```json
{
  "url": "https://www.google.com",
  "alias": "google"
}
```

O alias é opcional.

Também é possível enviar somente a URL:

```json
{
  "url": "https://www.google.com"
}
```

Nesse caso, a aplicação gera automaticamente um código para a URL curta.

Exemplo de resposta:

```json
{
  "urlCurta": "http://localhost:8080/url-encurtador/google",
  "codigo": "google",
  "urlOriginal": "https://www.google.com"
}
```

## Redirecionamento

Depois da criação, a URL curta pode ser acessada diretamente:

```text
http://localhost:8080/url-encurtador/google
```

A aplicação procura o código cadastrado e responde com um redirecionamento HTTP `302` para a URL original.

Exemplo:

```text
http://localhost:8080/url-encurtador/google
                  |
                  | HTTP 302
                  v
https://www.google.com
```

# Interface web

Também foi criada uma interface web simples para consumir a API.

Depois de iniciar a aplicação, acesse:

```text
http://localhost:8080/url-encurtador/
```

Na tela é possível:

1. informar a URL original;
2. informar um alias, caso desejado;
3. gerar o link curto;
4. copiar o endereço gerado;
5. acessar o endereço curto e ser redirecionado.

O frontend foi mantido simples porque o foco principal do desafio está na implementação Java e nas regras do encurtador.

# Banco de dados

A aplicação utiliza **H2 Database** através do datasource `ExampleDS` disponibilizado pelo WildFly.

A unidade de persistência não configura diretamente uma conexão JDBC. A aplicação utiliza o datasource gerenciado pelo servidor:

```xml
<jta-data-source>java:jboss/datasources/ExampleDS</jta-data-source>
```

A unidade de persistência está configurada para utilizar transações JTA:

```xml
<persistence-unit
    name="urlEncurtadorPU"
    transaction-type="JTA">
```

Dessa forma, o gerenciamento da conexão e das transações fica sob responsabilidade do servidor de aplicação.

O H2 foi utilizado porque é leve e suficiente para o escopo do desafio. Isso permite executar e avaliar a aplicação sem exigir a instalação e configuração de PostgreSQL, MySQL ou outro servidor de banco de dados.

O acesso aos dados é realizado através de JPA/Hibernate e fica concentrado na camada `repository`, evitando que detalhes de persistência sejam espalhados pelas regras de negócio.

Em um ambiente de produção, eu utilizaria um banco externo, como PostgreSQL, configurando um datasource específico no servidor de aplicação.

## Criação e atualização do schema

Para facilitar a execução do desafio, o Hibernate está configurado com:

```text
hibernate.hbm2ddl.auto=update
```

Essa configuração permite que o Hibernate crie ou atualize as estruturas necessárias no banco a partir das entidades da aplicação.

Para o contexto do desafio isso simplifica bastante a inicialização do ambiente.

Em produção, eu utilizaria migrations versionadas para ter maior controle sobre alterações no schema e permitir rastrear a evolução do banco de dados.

# Como executar

A aplicação pode ser executada de duas formas:

1. utilizando Docker;
2. utilizando Java, Maven e WildFly instalados localmente.

A execução com Docker é a opção recomendada por exigir menos configuração do ambiente.

---

# Opção 1 - Docker

## Pré-requisitos

É necessário ter Docker e Docker Compose instalados.

## Windows

Instale o Docker Desktop:

https://docs.docker.com/desktop/setup/install/windows-install/

Depois da instalação, abra PowerShell, Prompt de Comando ou Git Bash e confirme:

```bash
docker --version
```

```bash
docker compose version
```

Na raiz do projeto, execute:

```bash
docker compose up --build
```

Aguarde a inicialização do WildFly.

Depois acesse:

```text
http://localhost:8080/url-encurtador/
```

A API estará disponível em:

```text
http://localhost:8080/url-encurtador/api/urls
```

## Linux

A instalação do Docker Engine está disponível na documentação oficial:

https://docs.docker.com/engine/install/

Depois da instalação, confirme:

```bash
docker --version
```

```bash
docker compose version
```

Na raiz do projeto:

```bash
docker compose up --build
```

Depois da inicialização do WildFly, acesse:

```text
http://localhost:8080/url-encurtador/
```

## Executar em segundo plano

No Windows ou Linux:

```bash
docker compose up --build -d
```

Para verificar os containers:

```bash
docker compose ps
```

Para acompanhar os logs:

```bash
docker compose logs -f
```

Para encerrar:

```bash
docker compose down
```

## Como funciona o build Docker

O `Dockerfile` utiliza build multi-stage.

Na primeira etapa, Maven e Java são utilizados para compilar o projeto:

```text
Código fonte
     |
     v
Maven + Java
     |
     v
mvn clean package
     |
     v
url-encurtador.war
```

Na segunda etapa, o WAR gerado é implantado no WildFly:

```text
url-encurtador.war
       |
       v
WildFly 10.1.0.Final
       |
       v
Aplicação :8080
```

Dessa forma, quem utiliza Docker não precisa instalar e configurar manualmente um WildFly local.

---

# Opção 2 - Execução manual

Para executar sem Docker são necessários:

- JDK 8;
- Apache Maven;
- WildFly 10.1.0.Final.

## Downloads

### JDK 8

Oracle JDK 8:

https://www.oracle.com/java/technologies/javase/javase8-archive-downloads.html

Também pode ser utilizada uma distribuição OpenJDK compatível com Java 8.

### Apache Maven

https://maven.apache.org/download.cgi

### WildFly 10.1.0.Final

https://www.wildfly.org/downloads/

Utilize a versão:

```text
10.1.0.Final
```

---

# Execução manual no Windows

## 1. Verificar o Java

Abra PowerShell, Prompt de Comando ou Git Bash:

```bash
java -version
```

Verifique também o compilador:

```bash
javac -version
```

O projeto utiliza Java 8.

## 2. Verificar o Maven

```bash
mvn -version
```

O comando mostra a versão do Maven e também qual Java está sendo utilizado.

## 3. Compilar o projeto

Na raiz:

```bash
mvn clean package
```

O WAR será criado em:

```text
target\url-encurtador.war
```

## 4. Instalar o WildFly

Extraia o WildFly para um diretório.

Exemplo:

```text
C:\wildfly-10.1.0.Final
```

## 5. Fazer o deploy

Copie:

```text
target\url-encurtador.war
```

para:

```text
C:\wildfly-10.1.0.Final\standalone\deployments\
```

## 6. Iniciar o WildFly

Execute:

```cmd
C:\wildfly-10.1.0.Final\bin\standalone.bat
```

Aguarde a mensagem informando que o servidor foi iniciado.

## 7. Acessar

Abra:

```text
http://localhost:8080/url-encurtador/
```

A API estará disponível em:

```text
http://localhost:8080/url-encurtador/api/urls
```

---

# Execução manual no Linux

## 1. Verificar o Java

```bash
java -version
```

```bash
javac -version
```

## 2. Verificar o Maven

```bash
mvn -version
```

## 3. Compilar

Na raiz:

```bash
mvn clean package
```

O WAR será criado em:

```text
target/url-encurtador.war
```

## 4. Configurar o WildFly

Depois de baixar e extrair o WildFly, por exemplo:

```text
/opt/wildfly-10.1.0.Final
```

é possível definir:

```bash
export WILDFLY_HOME=/opt/wildfly-10.1.0.Final
```

## 5. Fazer o deploy

```bash
cp target/url-encurtador.war \
   $WILDFLY_HOME/standalone/deployments/
```

## 6. Iniciar

```bash
$WILDFLY_HOME/bin/standalone.sh
```

## 7. Acessar

Abra:

```text
http://localhost:8080/url-encurtador/
```

# Como testar

## Pela interface web

Abra:

```text
http://localhost:8080/url-encurtador/
```

Informe, por exemplo:

```text
URL original:
https://www.google.com

Alias:
google
```

Gere o link.

O resultado será semelhante a:

```text
http://localhost:8080/url-encurtador/google
```

Ao acessar esse endereço, o navegador será redirecionado para:

```text
https://www.google.com
```

## Testando a API

### Linux / Git Bash

```bash
curl -i \
  -X POST \
  -H "Content-Type: application/json" \
  -d '{"url":"https://www.google.com","alias":"google"}' \
  http://localhost:8080/url-encurtador/api/urls
```

### Windows PowerShell

```powershell
Invoke-RestMethod `
  -Uri "http://localhost:8080/url-encurtador/api/urls" `
  -Method Post `
  -ContentType "application/json" `
  -Body '{"url":"https://www.google.com","alias":"google"}'
```

## Testando a geração automática

Para deixar a aplicação gerar o código, envie somente a URL.

### Linux / Git Bash

```bash
curl -i \
  -X POST \
  -H "Content-Type: application/json" \
  -d '{"url":"https://www.google.com"}' \
  http://localhost:8080/url-encurtador/api/urls
```

### Windows PowerShell

```powershell
Invoke-RestMethod `
  -Uri "http://localhost:8080/url-encurtador/api/urls" `
  -Method Post `
  -ContentType "application/json" `
  -Body '{"url":"https://www.google.com"}'
```

## Testando o redirecionamento

```bash
curl -I http://localhost:8080/url-encurtador/google
```

A resposta deverá ser semelhante a:

```text
HTTP/1.1 302 Found
Location: https://www.google.com
```

# Testes automatizados

Foram adicionados testes automatizados para as principais regras e fluxos da aplicação.

Para executar:

```bash
mvn clean test
```

Os testes cobrem principalmente:

- criação de URLs;
- geração automática do código;
- utilização de alias personalizado;
- validações de entrada;
- tentativa de utilização de alias já existente;
- tratamento de exceções;
- comportamento dos endpoints;
- redirecionamento.

A ideia foi concentrar os testes nas regras que podem causar regressões no comportamento principal da aplicação.

# Build

Para executar os testes e gerar o WAR:

```bash
mvn clean package
```

O artefato será criado em:

```text
target/url-encurtador.war
```

# Decisões técnicas

## Separação em camadas

A aplicação foi dividida entre `resource`, `service` e `repository`.

O objetivo foi manter as responsabilidades separadas sem criar uma arquitetura maior do que o problema exige.

Os endpoints ficam responsáveis pela comunicação HTTP, o serviço concentra as regras de negócio e o repository realiza o acesso aos dados.

## Processamento sincronizado

O requisito do desafio determina que o motor de geração processe apenas uma solicitação por vez.

Por esse motivo, a criação da URL foi implementada de forma sincronizada na camada de serviço.

A sincronização ficou no serviço porque ela faz parte da regra de processamento da criação e não é uma responsabilidade do endpoint HTTP.

Essa abordagem atende ao cenário de uma única instância da aplicação.

Em um ambiente com várias instâncias, o `synchronized` não seria suficiente, pois cada JVM teria seu próprio controle. Nesse cenário seria necessário utilizar outro mecanismo de coordenação ou tratar a concorrência através da persistência.

## Alias personalizado

O alias é opcional.

Quando informado, a aplicação verifica se o código já está sendo utilizado antes de concluir a criação.

Também são realizadas as validações necessárias sobre os dados recebidos.

A persistência continua sendo responsável por garantir a consistência dos dados, evitando depender somente das verificações realizadas pela aplicação.

## Geração automática do código

Quando nenhum alias é informado, um código é gerado automaticamente.

A aplicação verifica a disponibilidade do código antes de concluir o cadastro.

Mesmo sendo pequena a possibilidade de colisão, esse cenário é considerado durante a geração.

## Banco de dados e persistência

Foi utilizado H2 Database com JPA/Hibernate.

A aplicação utiliza uma unidade de persistência JTA e obtém a conexão através do datasource:

```text
java:jboss/datasources/ExampleDS
```

gerenciado pelo WildFly.

Essa decisão mantém a aplicação separada dos detalhes da conexão JDBC e aproveita o gerenciamento de conexões e transações fornecido pelo servidor de aplicação.

O H2 atende bem ao objetivo do desafio porque não exige a instalação de um servidor de banco separado.

Em um ambiente de produção, eu utilizaria um banco externo, como PostgreSQL, configurando um datasource específico no WildFly.

## Gerenciamento do schema

Para simplificar a execução do projeto, foi utilizado:

```text
hibernate.hbm2ddl.auto=update
```

Isso permite que o Hibernate prepare as estruturas necessárias para a execução da aplicação.

Em produção, eu substituiria essa estratégia por migrations versionadas.

## API REST

JAX-RS foi utilizado para disponibilizar a API.

Os resources recebem as requisições HTTP e delegam as regras para a camada de serviço.

Isso evita concentrar regras de negócio nos endpoints.

## Injeção de dependências

CDI é utilizado para gerenciar as dependências da aplicação.

Isso evita a criação manual das dependências e facilita a substituição delas durante os testes.

## Tratamento de erros

As exceções da aplicação são convertidas para respostas HTTP através de `ExceptionMapper`.

Dessa forma, o tratamento fica centralizado e os resources permanecem focados no fluxo das requisições.

## Frontend

O frontend utiliza HTML, CSS e JavaScript.

Não foi utilizado um framework JavaScript porque a interface necessária é pequena e não justificaria adicionar mais dependências e configuração ao projeto.

A intenção foi manter a interface funcional sem tirar o foco da implementação Java.

## Docker

Docker foi incluído para simplificar a execução do projeto.

O build utiliza múltiplas etapas: Maven gera o WAR e, posteriormente, o artefato é implantado no WildFly.

Assim, quem quiser apenas avaliar a aplicação pode executá-la sem configurar manualmente todo o ambiente.

# Trade-offs

Algumas decisões foram tomadas procurando manter a solução proporcional ao tamanho e ao objetivo do desafio.

## Sincronização em uma única JVM

O uso de `synchronized` atende diretamente ao requisito de processar uma solicitação por vez e mantém a implementação simples.

A principal limitação é que esse controle funciona somente dentro da mesma JVM.

Caso a aplicação fosse executada em várias instâncias, seria necessária outra estratégia de sincronização.

## H2

O H2 reduz a infraestrutura necessária para executar o desafio e evita exigir um servidor de banco externo.

Para desenvolvimento, demonstração e avaliação da solução ele atende bem ao objetivo.

Em produção, eu utilizaria um banco externo, como PostgreSQL, deixando o ciclo de vida dos dados independente da aplicação e utilizando uma estrutura mais adequada para backup, disponibilidade e múltiplas instâncias.

## `hibernate.hbm2ddl.auto`

A utilização de `update` facilita a inicialização do projeto e reduz a configuração necessária para executar o desafio.

Por outro lado, não oferece o mesmo controle e rastreabilidade de migrations versionadas.

Por esse motivo, em produção eu utilizaria uma ferramenta de migration.

## JPA/Hibernate

Para uma aplicação pequena seria possível implementar a persistência de uma forma mais simples.

Optei por JPA/Hibernate por ser compatível com a stack utilizada e permitir separar o acesso aos dados das regras do encurtador.

## Geração automática

A geração automática mantém o fluxo simples para o usuário.

Como códigos gerados podem eventualmente colidir, a aplicação verifica sua disponibilidade antes de concluir a criação.

## Frontend sem framework

Um framework JavaScript poderia oferecer mais recursos, mas também aumentaria a quantidade de dependências e configuração para uma interface pequena.

HTML, CSS e JavaScript atendem ao objetivo da tela.

## WildFly 10

Foi utilizada a versão 10.1.0.Final para manter o projeto compatível com a stack proposta para o desafio.

Mesmo existindo versões mais recentes de Java e WildFly, atualizar essas tecnologias mudaria desnecessariamente o contexto da implementação.

# O que faria diferente com mais tempo

Em um cenário de produção, alguns pontos poderiam evoluir:

- substituir o datasource H2 por um datasource PostgreSQL;
- utilizar migrations versionadas para evolução do banco;
- melhorar o controle de concorrência para múltiplas instâncias;
- permitir configurar expiração das URLs;
- adicionar métricas de acesso aos links;
- disponibilizar uma área para gerenciamento das URLs;
- adicionar paginação nas consultas;
- ampliar os testes de integração;
- adicionar logs estruturados;
- adicionar métricas e monitoramento;
- configurar HTTPS;
- configurar domínio próprio;
- automatizar build e testes através de integração contínua.

Esses itens não foram incluídos no fluxo principal para manter a implementação focada no escopo solicitado.

# Considerações finais

A implementação procurou manter a solução simples e próxima da stack proposta no desafio.

As responsabilidades foram separadas entre API, regras de negócio e persistência, evitando adicionar componentes que não fossem necessários para resolver o problema.

O H2 e o datasource fornecido pelo WildFly simplificam a execução do ambiente, enquanto JPA/Hibernate e JTA mantêm a persistência separada das regras da aplicação.

Além dos requisitos principais, foram adicionados testes automatizados, uma interface web simples e suporte a Docker para facilitar a execução e a validação do projeto.