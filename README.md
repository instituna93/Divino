# MngmtInstituna

## Environment Setup

Antes de começar a trabalhar no projeto é necessário instalar:

- JDK17 - https://www.oracle.com/java/technologies/javase/jdk17-archive-downloads.html
- Node.js e npm - https://nodejs.org/en/download

```
choco install nodejs -y
node -v
npm -v
```

- JHipster - https://www.jhipster.tech/installation/

```
npm install -g generator-jhipster
```

## Project Startup

Copiar o projeto e instalar dependencias:

```
mkdir /var/mngmt-instituna
cd /var/mngmt-instituna
git clone https://github.com/instituna93/mngmt-instituna.git .
npm install
```

Para arrancar o projeto correr este comando:

```
mvnw
```

Ele cria automaticamente uma base de dados local de testes em H2: `\target\h2db`
E arranca o projeto em `http://localhost:8080/`

```
----------------------------------------------------------
        Application 'MngmtInstituna' is running! Access URLs:
        Local:          http://localhost:8080/
----------------------------------------------------------
```

Atualmente existem 3 contas de utilizador criadas no site, para usar e fazer testes, cada uma com permissões diferentes:

- Administrador (utilizador="admin" e palavra-passe="admin")
- Direcao (utilizador="direcao" e palavra-passe="direcao")
- Utilizador (utilizador="user" e palavra-passe="user").

## Database Structure Changes

Para fazer alterações á BD, pode-se seguir o modelo de JDL: `jhipster-jdl_db.jdl`

Deve-se usar um novo ficheiro com as tabelas a serem criadas, pois usando o mesmo, ele volta a criar as entidades que já foram criadas e alteradas, dando assim rollback as alterações que já foram feitas ao projeto.
Exemplo:

```
entity Teste {
	istoEUmTeste String
}
paginate Teste with infinite-scroll
service Teste with serviceImpl
dto Teste with mapstruct
```

Aps criao de um novo ficheiro JDL, ele pode ser importado com o seguinte comando: `jhipster jdl jhipster-jdl_TesteTable.jdl`

Com isto, o JHipster cria automaticamente varios documentos:

- Estrutura da tabela (`\.jhipster\Teste.json`)
- Script criação da tabela em Liquibase (`\src\main\resources\config\liquibase\changelog\YYYYMMDDHHmmSS_added_entity_Teste.xml`)
- Inclusao do Script Liquibase no master do Liquibase (`\src\main\resources\config\liquibase\master.xml`)
- Modelo Java (`\src\main\java\com\instituna\domain\Teste.java`)
- Helpers SQL (`\src\main\java\com\instituna\repository\TesteSqlHelper.java` & `\src\main\java\com\instituna\repository\rowmapper\TesteRowMapper.java`)
- Repositorios/CRUD DB (`\src\main\java\com\instituna\repository\TesteRepository.java` & `\src\main\java\com\instituna\repository\TesteRepositoryInternalImpl.java`)
- Serviço Controller (`\src\main\java\com\instituna\service\TesteService.java` & `\src\main\java\com\instituna\service\impl\TesteServiceImpl.java`)
- DTO e Mapper (`\src\main\java\com\instituna\service\dto\TesteDTO.java` & `\src\main\java\com\instituna\service\mapper\TesteMapper.java`)
- Serviço REST (`\src\main\java\com\instituna\web\rest\TesteResource.java`)
- Testes Java
- Angular - Cria tambem um conjunto de paginas e controladores em angular para listar, criar, editar e apagar esta entidade (`\src\main\webapp\app\entities\teste\*`)

## Data - Static/Test

Atualmente o faker est disabled, para ativar, pode-se alterar o campo `skipFakeData` no ficheiro `.yo-rc.json`
Quando est ativo, o faker cria uma tabela de dados ficticios que pode ser util para fazer testes, sem ter que estar sempre a adicionar dados BD de arranque (`\src\main\resources\config\liquibase\fake-data\teste.csv`)

O fake data no deve ser usado para dados estticos (Como por exemplo roles, ou tags), nesse caso deve-se adicionar uma tabela de dados aqui (`\src\main\resources\config\liquibase\data\*`)
Ao adicionar essa tabela de dados, necessrio fazer o carregamento desses dados para a tabela correspondente: (`\src\main\resources\config\liquibase\changelog\99999999999999_added_data.xml`)
Dados estticos que sejam relacionados, tm que ser inseridos em changesets diferentes.

Quando fizerem alteraes aos ficheiros Liquibase, podem ter que apagar a BD local de testes: `\target\h2db`

## Project Structure

If you change any project dependencies, you will need do run the following to update project dependencies.

```
npm install
```

If you wish to have angular live-reload, you can start a separate instance of the front-end with the following command:

```
npm start
```

Ele arranca uma instancia do angular com live-reload ligado á instancia de back-end em `http://localhost:9000`

```
 -------------------------------------
       Local: http://localhost:9000
 -------------------------------------
```

## OTHER Development informations, irrelevant for now

- `/src/main/docker` - Docker configurations for the application and services that the application depends on

The `npm run` command will list all of the scripts available to run for this project.

Sonar is used to analyse code quality. You can start a local Sonar server (accessible on http://localhost:9001) with:

```
docker compose -f src/main/docker/sonar.yml up -d
```

### PWA Support

JHipster ships with PWA (Progressive Web App) support, and it's turned off by default. One of the main components of a PWA is a service worker.

The service worker initialization code is disabled by default. To enable it, uncomment the following code in `src/main/webapp/app/app.module.ts`:

```typescript
ServiceWorkerModule.register('ngsw-worker.js', { enabled: false }),
```

### JHipster Control Center

JHipster Control Center can help you manage and control your application(s). You can start a local control center server (accessible on http://localhost:7419) with:

```
docker compose -f src/main/docker/jhipster-control-center.yml up
```

## Building for production

### Packaging as jar

To build the final jar and optimize the MngmtInstituna application for production, run:

```
./mvnw -Pprod clean verify
```

This will concatenate and minify the client CSS and JavaScript files. It will also modify `index.html` so it references these new files.
To ensure everything worked, run:

```
java -jar target/*.jar
```

Then navigate to [http://localhost:8080](http://localhost:8080) in your browser.

Refer to [Using JHipster in production][] for more details.

### Packaging as war

To package your application as a war in order to deploy it to an application server, run:

```
./mvnw -Pprod,war clean verify
```

## Using Docker to simplify development (optional)

You can use Docker to improve your JHipster development experience. A number of docker-compose configuration are available in the [src/main/docker](src/main/docker) folder to launch required third party services.

For example, to start a postgresql database in a docker container, run:

```
docker compose -f src/main/docker/postgresql.yml up -d
```

To stop it and remove the container, run:

```
docker compose -f src/main/docker/postgresql.yml down
```

Then run:

```
docker compose -f src/main/docker/app.yml up -d
```

## Continuous Integration (optional)

To configure CI for your project, run the ci-cd sub-generator (`jhipster ci-cd`), this will let you generate configuration files for a number of Continuous Integration systems. Consult the [Setting up Continuous Integration][] page for more information.

[JHipster Homepage and latest documentation]: https://www.jhipster.tech
[JHipster 8.0.0-beta.2 archive]: https://www.jhipster.tech/documentation-archive/v8.0.0-beta.2
[Using JHipster in development]: https://www.jhipster.tech/documentation-archive/v8.0.0-beta.2/development/
[Using Docker and Docker-Compose]: https://www.jhipster.tech/documentation-archive/v8.0.0-beta.2/docker-compose
[Using JHipster in production]: https://www.jhipster.tech/documentation-archive/v8.0.0-beta.2/production/
[Running tests page]: https://www.jhipster.tech/documentation-archive/v8.0.0-beta.2/running-tests/
[Code quality page]: https://www.jhipster.tech/documentation-archive/v8.0.0-beta.2/code-quality/
[Setting up Continuous Integration]: https://www.jhipster.tech/documentation-archive/v8.0.0-beta.2/setting-up-ci/
[Node.js]: https://nodejs.org/
[NPM]: https://www.npmjs.com/
[Webpack]: https://webpack.github.io/
[BrowserSync]: https://www.browsersync.io/
[Jest]: https://facebook.github.io/jest/
[Leaflet]: https://leafletjs.com/
[DefinitelyTyped]: https://definitelytyped.org/
[Angular CLI]: https://cli.angular.io/
