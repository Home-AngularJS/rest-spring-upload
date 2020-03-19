REST Document Archive
=====================

Простой архив документов с интерфейсом REST.
* Вы можете прочитать больше об этом проекте в моем блоге: [A REST web service, file uploads & Spring Boot](https://murygin.wordpress.com/2014/10/13/rest-web-service-file-uploads-spring-boot)
* Репозиторий: https://github.com/murygin/rest-document-archive
* Open to see the AngularJS frontend: [http://localhost:8080](http://localhost:8080)

REST API
--------

* **Add a document**
 
   */archive/upload?file={file}&person={person}&date={date} POST*

  * file: A file posted in a multipart request
  * person: The name of the uploading person
  * date: The date of the document
   
* **Find documents**

   */archive/documents?person={person}&date={date} GET*

  * person: The name of the uploading person
  * date: The date of the document
   
* **Get a document**  

   */archive/document/{id} GET*                                  

  * id: The UUID of a document

Documentation
-------------

* REST Service Controller
  * [ArchiveController.java](https://github.com/murygin/rest-document-archive/blob/master/src/main/java/org/murygin/archive/rest/ArchiveController.java)
   
   Executes incoming request and defines URL to service method mappings. All remote call are delegated to the archive service.
* Service
  * Interface: [IArchiveService.java](https://github.com/murygin/rest-document-archive/blob/master/src/main/java/org/murygin/archive/service/IArchiveService.java)
  * Implementation: [ArchiveService.java](https://github.com/murygin/rest-document-archive/blob/master/src/main/java/org/murygin/archive/service/ArchiveService.java)
   
   A service to save, find and get documents from an archive. 
* Data access object
  * Interface: [IDocumentDao.java](https://github.com/murygin/rest-document-archive/blob/master/src/main/java/org/murygin/archive/dao/IDocumentDao.java)
  * Implementation: [FileSystemDocumentDao.java](https://github.com/murygin/rest-document-archive/blob/master/src/main/java/org/murygin/archive/dao/FileSystemDocumentDao.java)  
   
   Объект доступа к данным для вставки, поиска и загрузки документов. FileSystemDocumentDao сохраняет документы в файловой системе. База данных не участвует. Для каждого документа создается папка. Папка содержит документ и файлы свойств с метаданными документа. Каждый документ в архиве имеет универсальный уникальный идентификатор (UUID). Имя папки для документов - это UUID документа.

* Client
   
   [ArchiveServiceClient.java](https://github.com/murygin/rest-document-archive/blob/master/src/main/java/org/murygin/archive/client/ArchiveServiceClient.java)
   
   A client for the document archive which is using the REST service.

* Web client
   
   [src/main/resources/static](https://github.com/murygin/rest-document-archive/tree/master/src/main/resources/static)

   A web client made with [AngularJS](https://angularjs.org/).
