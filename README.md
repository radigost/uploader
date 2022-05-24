Project that lets creating new notes in Obsidian Daily Notes with Telegram bot.

It is more convinient as it is more easy to post smth in the chat with bot than to go to Obsidian application

Currently supports text messages and pictures (creates file with picture and inserts link to it in the file).

Files are created with date format YYYYMMDDHHMM

Current plan is to implement support of
- Audio messages
- Forwarded messages
- Convert Audio messages to text and include text too(?)


---

Links
- https://www.dropbox.com/developers/documentation - Dropbox documentation
- https://obsidian.md/ - Obsidian Application
---

# Using the project personally

1. You need to use env variables for local run or for docker containers

```shell
DROPBOX_FOLDER=folder_to_store_notes/Apps/Obsidian/Daily Notes;
DROPBOX_DATA_FOLDER=folder_where_to_put_files_from_notes/Apps/Obsidian/data;
DROPBOX_APP_KEY=your_dropbox_app_key;
DROPBOX_APP_SECRET=your_dropbox_app_secret;
BOT_TOKEN=your_telegram_bot_key;
```

2. You can use `build.sh` to build docker container
3. You can push to your own docker repository with `publish.sh` - just change docker repo (my own is private)

---
### Reference Documentation
For further reference, please consider the following sections:

* [Official Apache Maven documentation](https://maven.apache.org/guides/index.html)
* [Spring Boot Maven Plugin Reference Guide](https://docs.spring.io/spring-boot/docs/3.0.0-M2/maven-plugin/reference/html/)
* [Create an OCI image](https://docs.spring.io/spring-boot/docs/3.0.0-M2/maven-plugin/reference/html/#build-image)
* [Spring Data JPA](https://docs.spring.io/spring-boot/docs/3.0.0-M2/reference/htmlsingle/#boot-features-jpa-and-spring-data)
* [Spring Data Neo4j](https://docs.spring.io/spring-boot/docs/3.0.0-M2/reference/htmlsingle/#boot-features-neo4j)
* [Spring Boot DevTools](https://docs.spring.io/spring-boot/docs/3.0.0-M2/reference/htmlsingle/#using-boot-devtools)
* [Spring Configuration Processor](https://docs.spring.io/spring-boot/docs/3.0.0-M2/reference/htmlsingle/#configuration-metadata-annotation-processor)
* [Spring Boot Actuator](https://docs.spring.io/spring-boot/docs/3.0.0-M2/reference/htmlsingle/#production-ready)
* [Spring Web](https://docs.spring.io/spring-boot/docs/3.0.0-M2/reference/htmlsingle/#boot-features-developing-web-applications)
* [Rest Repositories](https://docs.spring.io/spring-boot/docs/3.0.0-M2/reference/htmlsingle/#howto-use-exposing-spring-data-repositories-rest-endpoint)
* [Spring Security](https://docs.spring.io/spring-boot/docs/3.0.0-M2/reference/htmlsingle/#boot-features-security)

### Guides
The following guides illustrate how to use some features concretely:

* [Accessing Data with JPA](https://spring.io/guides/gs/accessing-data-jpa/)
* [Accessing Data with Neo4j](https://spring.io/guides/gs/accessing-data-neo4j/)
* [Building a RESTful Web Service with Spring Boot Actuator](https://spring.io/guides/gs/actuator-service/)
* [Building a RESTful Web Service](https://spring.io/guides/gs/rest-service/)
* [Serving Web Content with Spring MVC](https://spring.io/guides/gs/serving-web-content/)
* [Building REST services with Spring](https://spring.io/guides/tutorials/bookmarks/)
* [Accessing JPA Data with REST](https://spring.io/guides/gs/accessing-data-rest/)
* [Accessing Neo4j Data with REST](https://spring.io/guides/gs/accessing-neo4j-data-rest/)
* [Accessing MongoDB Data with REST](https://spring.io/guides/gs/accessing-mongodb-data-rest/)
* [Securing a Web Application](https://spring.io/guides/gs/securing-web/)
* [Spring Boot and OAuth2](https://spring.io/guides/tutorials/spring-boot-oauth2/)
* [Authenticating a User with LDAP](https://spring.io/guides/gs/authenticating-ldap/)

