# Encurtador de URL

Aplicação web desenvolvida em Java para criação de URLs curtas.

O usuário informa uma URL original e, opcionalmente, um alias personalizado. A aplicação gera uma URL curta que, ao ser acessada, redireciona para o endereço original.

## Tecnologias

- Java 8
- Java EE 7
- JAX-RS
- CDI
- JPA / Hibernate
- H2 Database
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

O projeto foi separado em camadas para manter as responsabilidades simples e facilitar a manutenção e os testes.

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
- `repository`: realiza o acesso aos dados;
- `domain`: contém as entidades da aplicação;
- `dto`: representa os dados de entrada e saída da API;
- `exception`: concentra as exceções e o tratamento dos erros da aplicação.

O fluxo principal da aplicação é:

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
H2
```

## Funcionalidades

A aplicação permite:

- cadastrar uma URL original;
- informar um alias personalizado;
- gerar automaticamente um código quando o alias não é informado;
- validar o alias informado;
- impedir a utilização de aliases já cadastrados;
- localizar a URL original através do código;
- redirecionar uma URL curta para a URL original;
- utilizar uma interface web simples para criação dos links.

## API

### Criar uma URL curta

```http
POST /url-encurtador/api/urls
Content-Type: application/json
```

Exemplo utilizando alias:

```json
{
  "url": "https://www.google.com",
  "alias": "google"
}
```

O alias é opcional.

Exemplo sem alias:

```json
{
  "url": "https://www.google.com"
}
```

Quando o alias não é informado, a aplicação gera automaticamente um código para a URL.

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

A aplicação procura o código cadastrado e redireciona para a URL original utilizando HTTP `302`.

Exemplo:

```text
http://localhost:8080/url-encurtador/google
                  |
                  | HTTP 302
                  v
https://www.google.com
```

## Interface web

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

# Como executar

A aplicação pode ser executada de duas formas:

1. utilizando Docker;
2. utilizando Java, Maven e WildFly instalados localmente.

A execução com Docker é a opção mais simples, pois não exige a instalação manual do Java, Maven e WildFly.

---

## Opção 1 - Docker

### Pré-requisitos

É necessário ter Docker e Docker Compose instalados.

### Windows

Instale o Docker Desktop:

https://docs.docker.com/desktop/setup/install/windows-install/

Depois da instalação, abra PowerShell, Prompt de Comando ou Git Bash e confirme:

```bash
docker --version
```

```bash
docker compose version
```

Abra o terminal na raiz do projeto e execute:

```bash
docker compose up --build
```

Aguarde a inicialização do WildFly.

Depois acesse:

```text
http://localhost:8080/url-encurtador/
```

### Linux

A instalação do Docker Engine está documentada em:

https://docs.docker.com/engine/install/

Depois da instalação, confirme:

```bash
docker --version
```

```bash
docker compose version
```

Na raiz do projeto execute:

```bash
docker compose up --build
```

Depois da inicialização do WildFly, acesse:

```text
http://localhost:8080/url-encurtador/
```

### Executar em segundo plano

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

### Como funciona o build Docker

O `Dockerfile` utiliza build multi-stage.

Na primeira etapa, Maven e Java 8 são utilizados para compilar o projeto:

```text
Código fonte
     |
     v
Maven + Java 8
     |
     v
mvn clean package
     |
     v
url-encurtador.war
```

Na segunda etapa, somente o WAR gerado é enviado para a imagem do WildFly:

```text
url-encurtador.war
       |
       v
WildFly 10.1.0.Final
       |
       v
Aplicação :8080
```

Isso permite executar a aplicação sem configurar manualmente um WildFly local.

---

## Opção 2 - Execução manual

Para executar o projeto sem Docker são necessários:

- JDK 8;
- Apache Maven;
- WildFly 10.1.0.Final.

### Downloads

#### JDK 8

Oracle JDK 8:

https://www.oracle.com/java/technologies/javase/javase8-archive-downloads.html

Também pode ser utilizada uma distribuição OpenJDK compatível com Java 8.

#### Apache Maven

https://maven.apache.org/download.cgi

#### WildFly 10.1.0.Final

https://www.wildfly.org/downloads/

Utilize a versão:

```text
10.1.0.Final
```

## Windows

### 1. Verificar o Java

Abra PowerShell, Prompt de Comando ou Git Bash:

```bash
java -version
```

Verifique também o compilador:

```bash
javac -version
```

O projeto foi desenvolvido para Java 8.

### 2. Verificar o Maven

```bash
mvn -version
```

O comando mostra a versão do Maven e o Java utilizado por ele.

### 3. Compilar o projeto

Na raiz:

```bash
mvn clean package
```

O WAR será criado em:

```text
target\url-encurtador.war
```

### 4. Instalar o WildFly

Extraia o WildFly para um diretório.

Exemplo:

```text
C:\wildfly-10.1.0.Final
```

### 5. Fazer o deploy

Copie:

```text
target\url-encurtador.war
```

para:

```text
C:\wildfly-10.1.0.Final\standalone\deployments\
```

### 6. Iniciar o WildFly

Execute:

```cmd
C:\wildfly-10.1.0.Final\bin\standalone.bat
```

Aguarde a mensagem informando que o servidor foi iniciado.

### 7. Acessar a aplicação

Abra:

```text
http://localhost:8080/url-encurtador/
```

A API estará disponível em:

```text
http://localhost:8080/url-encurtador/api/urls
```

---

## Linux

### 1. Verificar o Java

```bash
java -version
```

```bash
javac -version
```

### 2. Verificar o Maven

```bash
mvn -version
```

### 3. Compilar

Na raiz do projeto:

```bash
mvn clean package
```

O WAR será criado em:

```text
target/url-encurtador.war
```

### 4. Configurar o WildFly

Depois de baixar e extrair o WildFly, por exemplo em:

```text
/opt/wildfly-10.1.0.Final
```

é possível definir:

```bash
export WILDFLY_HOME=/opt/wildfly-10.1.0.Final
```

### 5. Fazer o deploy

```bash
cp target/url-encurtador.war \
   $WILDFLY_HOME/standalone/deployments/
```

### 6. Iniciar

```bash
$WILDFLY_HOME/bin/standalone.sh
```

### 7. Acessar

Abra:

```text
http://localhost:8080/url-encurtador/
```

# Banco de dados

A aplicação utiliza **H2 Database** com **JPA/Hibernate**.

O H2 foi escolhido por ser leve e suficiente para o escopo do desafio. Dessa forma, quem executar o projeto não precisa instalar e configurar PostgreSQL, MySQL ou outro servidor de banco de dados apenas para avaliar a aplicação.

O acesso aos dados é feito através de JPA/Hibernate e fica concentrado na camada `repository`.

Essa separação evita que detalhes de persistência sejam espalhados pelas regras de negócio.

Para o contexto do desafio, essa solução mantém a infraestrutura simples e permite concentrar a implementação no comportamento do encurtador.

Em um ambiente de produção, eu utilizaria um banco de dados externo, como PostgreSQL, principalmente para ter uma persistência independente da aplicação e uma estrutura mais adequada para backup, disponibilidade e execução com múltiplas instâncias.

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

Ao acessar o endereço, o navegador será redirecionado para:

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
  -Body '{
    "url":"https://www.google.com",
    "alias":"google"
  }'
```

## Testando geração automática

Para deixar a aplicação gerar o código, envie apenas a URL:

### Linux / Git Bash

```bash
curl -i \
  -X POST \
  -H "Content-Type: application/json" \
  -d '{"url":"https://www.google.com"}' \
  http://localhost:8080/url-encurtador/api/urls
```

### PowerShell

```powershell
Invoke-RestMethod `
  -Uri "http://localhost:8080/url-encurtador/api/urls" `
  -Method Post `
  -ContentType "application/json" `
  -Body '{
    "url":"https://www.google.com"
  }'
```

## Testando o redirecionamento

```bash
curl -I http://localhost:8080/url-encurtador/google
```

A resposta deve ser semelhante a:

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

- geração do código curto;
- criação de URLs;
- geração automática;
- utilização de alias personalizado;
- validações de entrada;
- alias já existente;
- tratamento de exceções;
- comportamento dos endpoints;
- redirecionamento.

A ideia foi concentrar os testes principalmente nas regras que podem causar regressões no comportamento da aplicação.

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

## Processamento sincronizado

O requisito do desafio determina que o motor de geração processe apenas uma solicitação por vez.

Por esse motivo, a criação da URL foi implementada de forma sincronizada na camada de serviço.

A sincronização ficou no serviço porque considero que ela faz parte da regra de processamento da criação e não é uma responsabilidade do endpoint HTTP.

Essa abordagem atende ao cenário de uma única instância da aplicação.

Em um ambiente com várias instâncias, o `synchronized` não seria suficiente, porque cada JVM teria seu próprio controle. Nesse cenário seria necessário utilizar outro mecanismo de coordenação ou tratar a concorrência através da persistência.

## Alias personalizado

O alias é opcional.

Quando informado, a aplicação verifica se o código já está sendo utilizado antes de concluir a criação.

Também existe validação do formato recebido.

A unicidade deve continuar sendo garantida pela persistência, evitando depender somente da consulta realizada pela aplicação.

## Geração automática do código

Quando nenhum alias é informado, um código é gerado automaticamente.

A geração utiliza valores aleatórios e a aplicação verifica se o código já existe.

Mesmo sendo pequena a possibilidade de colisão, ela é considerada durante a criação.

## Banco de dados e persistência

Foi utilizado **H2 Database**, com acesso através de **JPA/Hibernate**.

A escolha do H2 foi feita para manter a solução autocontida e simples de executar. Para o tamanho do problema não considerei necessário exigir a instalação de um servidor de banco externo.

JPA/Hibernate permite manter as regras de negócio separadas dos detalhes de persistência.

O acesso aos dados fica concentrado no `repository`.

Em produção, essa escolha seria revista e provavelmente seria utilizado um banco externo, como PostgreSQL.

## API REST

JAX-RS foi utilizado para disponibilizar a API.

O resource recebe as requisições HTTP e delega as regras para a camada de serviço.

Isso evita colocar regras de negócio diretamente nos endpoints.

## Injeção de dependências

CDI é utilizado para gerenciar as dependências da aplicação.

Isso evita a criação manual das dependências e também facilita a substituição de implementações durante os testes.

## Tratamento de erros

As exceções da aplicação são convertidas para respostas HTTP através de `ExceptionMapper`.

Dessa forma, o tratamento fica centralizado e os resources permanecem focados no fluxo da requisição.

## Frontend

O frontend utiliza HTML, CSS e JavaScript.

Não foi utilizado React, Angular ou outro framework porque a interface necessária é pequena e não justificaria adicionar mais dependências e configuração.

A intenção foi manter a interface funcional sem tirar o foco da implementação Java.

## Docker

Docker foi incluído para simplificar a execução do projeto.

Foi utilizado build multi-stage: Maven gera o WAR na primeira etapa e o artefato é copiado para a imagem do WildFly na segunda.

Com isso, quem quiser apenas executar a aplicação não precisa configurar manualmente Java, Maven e WildFly.

# Trade-offs

Algumas decisões foram tomadas procurando manter a solução proporcional ao tamanho do desafio.

## Sincronização em uma única JVM

O uso de `synchronized` atende diretamente ao requisito de processar uma solicitação por vez e mantém a implementação simples.

A limitação é que esse controle funciona dentro de uma única JVM.

Com várias instâncias seria necessária outra estratégia.

## H2

O H2 reduz a infraestrutura necessária para executar o desafio e evita exigir um banco externo.

Para o volume e objetivo desta aplicação ele é suficiente.

Em produção, eu utilizaria um banco externo, como PostgreSQL, deixando o ciclo de vida dos dados independente da aplicação e utilizando uma estrutura mais adequada para backup e disponibilidade.

## JPA/Hibernate

Para uma aplicação desse tamanho seria possível implementar a persistência de uma forma ainda mais simples.

Optei por JPA/Hibernate por ser compatível com a stack proposta e por permitir separar o acesso aos dados das regras do encurtador.

## Geração aleatória

A geração aleatória mantém a implementação simples.

Existe a possibilidade de colisão, por isso a disponibilidade do código é verificada antes de concluir o cadastro.

## Frontend sem framework

Um framework JavaScript poderia ser utilizado, mas aumentaria a quantidade de dependências e configuração para uma interface pequena.

HTML, CSS e JavaScript atendem ao objetivo da tela.

## WildFly 10

Foi utilizada a versão 10.1.0.Final para manter o projeto compatível com a stack proposta.

Mesmo existindo versões mais recentes de Java e WildFly, atualizar essas tecnologias mudaria desnecessariamente o contexto do desafio.

# O que faria diferente com mais tempo

Em um cenário de produção, alguns pontos poderiam evoluir:

- substituir o H2 por um banco externo, como PostgreSQL;
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

Além dos requisitos principais, foram adicionados testes automatizados, uma interface web simples e suporte a Docker para facilitar a execução e a validação do projeto.