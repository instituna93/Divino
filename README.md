# MngmtInstituna

## Environment Setup

Antes de começar a trabalhar no projeto é necessário instalar:

- JDK17 - https://www.oracle.com/java/technologies/javase/jdk17-archive-downloads.html
- Node.js e npm - https://nodejs.org/en/download

```
choco install nodejs-lts -y
node -v
npm -v
```

- JHipster - https://www.jhipster.tech/installation/

```
npm install -g generator-jhipster
```

## Project Startup

Para arrancar o projeto correr este comando:

```
mvnw
```

Ele cria automaticamente uma base de dados local de testes em H2: `\target\h2db`
E arranca o projeto em `http://localhost:8080/`

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

## Static data/Test data

Atualmente o faker est disabled, para ativar, pode-se alterar o campo `skipFakeData` no ficheiro `.yo-rc.json`
Quando est ativo, o faker cria uma tabela de dados ficticios que pode ser util para fazer testes, sem ter que estar sempre a adicionar dados BD de arranque (`\src\main\resources\config\liquibase\fake-data\teste.csv`)

O fake data no deve ser usado para dados estticos (Como por exemplo roles, ou tags), nesse caso deve-se adicionar uma tabela de dados aqui (`\src\main\resources\config\liquibase\data\*`)
Ao adicionar essa tabela de dados, necessrio fazer o carregamento desses dados para a tabela correspondente: (`\src\main\resources\config\liquibase\changelog\99999999999999_added_data.xml`)
Dados estticos que sejam relacionados, tm que ser inseridos em changesets diferentes.

Quando fizerem alteraes aos ficheiros Liquibase, podem ter que apagar a BD local de testes: `\target\h2db`

## Project Structure

Node is required for generation and recommended for development. `package.json` is always generated for a better development experience with prettier, commit hooks, scripts and so on.

- `npmw` - wrapper to use locally installed npm.
  JHipster installs Node and npm locally using the build tool by default. This wrapper makes sure npm is installed locally and uses it avoiding some differences different versions can cause. By using `./npmw` instead of the traditional `npm` you can configure a Node-less environment to develop or test your application.
- `/src/main/docker` - Docker configurations for the application and services that the application depends on

## Development

You will only need to run this command when dependencies change in [package.json](package.json).

```
npm install
```

We use npm scripts and [Angular CLI][] with [Webpack][] as our build system.

Run the following commands in two separate terminals to create a blissful development experience where your browser
auto-refreshes when files change on your hard drive.

```
./mvnw
npm start
```

Npm is also used to manage CSS and JavaScript dependencies used in this application. You can upgrade dependencies by
specifying a newer version in [package.json](package.json). You can also run `npm update` and `npm install` to manage dependencies.
Add the `help` flag on any command to see how you can use it. For example, `npm help update`.

The `npm run` command will list all of the scripts available to run for this project.

### PWA Support

JHipster ships with PWA (Progressive Web App) support, and it's turned off by default. One of the main components of a PWA is a service worker.

The service worker initialization code is disabled by default. To enable it, uncomment the following code in `src/main/webapp/app/app.module.ts`:

```typescript
ServiceWorkerModule.register('ngsw-worker.js', { enabled: false }),
```

### Managing dependencies

For example, to add [Leaflet][] library as a runtime dependency of your application, you would run following command:

```
npm install --save --save-exact leaflet
```

To benefit from TypeScript type definitions from [DefinitelyTyped][] repository in development, you would run following command:

```
npm install --save-dev --save-exact @types/leaflet
```

Then you would import the JS and CSS files specified in library's installation instructions so that [Webpack][] knows about them:
Edit [src/main/webapp/app/app.module.ts](src/main/webapp/app/app.module.ts) file:

```
import 'leaflet/dist/leaflet.js';
```

Edit [src/main/webapp/content/scss/vendor.scss](src/main/webapp/content/scss/vendor.scss) file:

```
@import 'leaflet/dist/leaflet.css';
```

Note: There are still a few other things remaining to do for Leaflet that we won't detail here.

For further instructions on how to develop with JHipster, have a look at [Using JHipster in development][].

### Using Angular CLI

You can also use [Angular CLI][] to generate some custom client code.

For example, the following command:

```
ng generate component my-component
```

will generate few files:

```
create src/main/webapp/app/my-component/my-component.component.html
create src/main/webapp/app/my-component/my-component.component.ts
update src/main/webapp/app/app.module.ts
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

## Testing

To launch your application's tests, run:

```
./mvnw verify
```

### Client tests

Unit tests are run by [Jest][]. They're located in [src/test/javascript/](src/test/javascript/) and can be run with:

```
npm test
```

For more information, refer to the [Running tests page][].

### Code quality

Sonar is used to analyse code quality. You can start a local Sonar server (accessible on http://localhost:9001) with:

```
docker compose -f src/main/docker/sonar.yml up -d
```

Note: we have turned off forced authentication redirect for UI in [src/main/docker/sonar.yml](src/main/docker/sonar.yml) for out of the box experience while trying out SonarQube, for real use cases turn it back on.

You can run a Sonar analysis with using the [sonar-scanner](https://docs.sonarqube.org/display/SCAN/Analyzing+with+SonarQube+Scanner) or by using the maven plugin.

Then, run a Sonar analysis:

```
./mvnw -Pprod clean verify sonar:sonar -Dsonar.login=admin -Dsonar.password=admin
```

If you need to re-run the Sonar phase, please be sure to specify at least the `initialize` phase since Sonar properties are loaded from the sonar-project.properties file.

```
./mvnw initialize sonar:sonar -Dsonar.login=admin -Dsonar.password=admin
```

Additionally, Instead of passing `sonar.password` and `sonar.login` as CLI arguments, these parameters can be configured from [sonar-project.properties](sonar-project.properties) as shown below:

```
sonar.login=admin
sonar.password=admin
```

For more information, refer to the [Code quality page][].

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

You can also fully dockerize your application and all the services that it depends on.
To achieve this, first build a docker image of your app by running:

```
npm run java:docker
```

Or build a arm64 docker image when using an arm64 processor os like MacOS with M1 processor family running:

```
npm run java:docker:arm64
```

Then run:

```
docker compose -f src/main/docker/app.yml up -d
```

When running Docker Desktop on MacOS Big Sur or later, consider enabling experimental `Use the new Virtualization framework` for better processing performance ([disk access performance is worse](https://github.com/docker/roadmap/issues/7)).

For more information refer to [Using Docker and Docker-Compose][], this page also contains information on the docker-compose sub-generator (`jhipster docker-compose`), which is able to generate docker configurations for one or several JHipster applications.

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
