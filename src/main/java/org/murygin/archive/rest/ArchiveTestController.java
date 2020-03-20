package org.murygin.archive.rest;

import org.apache.log4j.Logger;
import org.murygin.archive.service.Document;
import org.murygin.archive.service.DocumentMetadata;
import org.murygin.archive.service.IArchiveService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * REST web service for archive service {@link IArchiveService}.
 * 
 * /archive/upload?file={file}&person={person}&date={date}  Add a document  POST
 *   file: A file posted in a multipart request
 *   person: The name of the uploading person
 *   date: The date of the document
 *
 * /archive/documents?person={person}&date={date}           Find documents  GET
 *   person: The name of the uploading person
 *   date: The date of the document
 *
 * /archive/document/{id}                                   Get a document  GET
 *   id: The UUID of a document
 */
@Controller
@RequestMapping(value = "/archive")
public class ArchiveTestController {

    private static final Logger LOG = Logger.getLogger(ArchiveTestController.class);
    
    @Autowired
    IArchiveService archiveService;

    /**
     * Adds a document to the archive.
     * 
     * Url: /archive/upload?file={file}&person={person}&date={date} [POST]
     *
     * Upload
     * ------
     * Select file: test.csv
     */
    @RequestMapping(value = "/test/upload", method = RequestMethod.POST)
    public @ResponseBody DocumentMetadata fileUpload(@RequestParam(value="file") MultipartFile file) {
        try {
            Document document = new Document(file.getBytes(), file.getOriginalFilename(), new Date(), UUID.randomUUID().toString());
            getArchiveService().save(document);
            return document.getMetadata();
        } catch (RuntimeException e) {
            LOG.error("Error while uploading.", e);
            throw e;
        } catch (Exception e) {
            LOG.error("Error while uploading.", e);
            throw new RuntimeException(e);
        }      
    }

    /**
     * Finds document in the archive. Returns a list of document meta data
     * which does not include the file data. Use getDocument to get the file.
     * Returns an empty list if no document was found.
     *
     * Url: /archive/documents?person={person}&date={date} [GET]
     */
    @RequestMapping(value = "/test/documents", method = RequestMethod.GET)
    public HttpEntity<List<DocumentMetadata>> findDocument(
            @RequestParam(value="person", required=false) String person,
            @RequestParam(value="date", required=false) @DateTimeFormat(pattern="yyyy-MM-dd") Date date) {
        HttpHeaders httpHeaders = new HttpHeaders();
        return new ResponseEntity<List<DocumentMetadata>>(getArchiveService().findDocuments(person,date), httpHeaders,HttpStatus.OK);
    }
    
    /**
     * Returns the document file from the archive with the given UUID.
     * 
     * Url: /archive/document/{id} [GET]
     */
    @RequestMapping(value = "/test/document/{id}", method = RequestMethod.GET)
    public HttpEntity<byte[]> getDocument(@PathVariable String id) {         
        // send it back to the client
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.IMAGE_JPEG);
        return new ResponseEntity<byte[]>(getArchiveService().getDocumentFile(id), httpHeaders, HttpStatus.OK);
    }

    public IArchiveService getArchiveService() {
        return archiveService;
    }

    public void setArchiveService(IArchiveService archiveService) {
        this.archiveService = archiveService;
    }

}
