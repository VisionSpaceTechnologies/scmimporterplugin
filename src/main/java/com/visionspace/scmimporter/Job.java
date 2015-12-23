/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.visionspace.scmimporter;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

/**
 *
 * @author luis.teixeira
 */
public class Job {
    
    public enum ImportStatus {
        NOT_SELECTED,
        IMPORT_SUCCESSFUL,
        IMPORT_FAILED
    }
    
    private String name;
    private String description;
    private File configFile;
    private ImportStatus importStatus;
    
    public Job(String name, File configFile) {
        this.name = name;
        this.configFile = configFile;
        setDescriptionFromFile();
        this.importStatus = ImportStatus.NOT_SELECTED;
    }
    
    public String getName() {
        return name;
    }
    
    public String getDescription() {
        return description;
    }
    
    public File getConfigFile() {
        return configFile;
    }
    
    public ImportStatus getImportStatus() {
        return this.importStatus;
    }
    
    public void setImportStatus(ImportStatus importStatus) {
        this.importStatus = importStatus;
    }
    
    private void setDescriptionFromFile() {
        try {
            Node node = getConfigDocument().getElementsByTagName("description").item(0);
            this.description = (node != null) ? node.getTextContent() : "";
        } catch (ParserConfigurationException ex) {
            Logger.getLogger(Job.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SAXException ex) {
            Logger.getLogger(Job.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Job.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private Document getConfigDocument() throws ParserConfigurationException, SAXException, IOException  {
        DocumentBuilderFactory documentFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder documentBuilder = documentFactory.newDocumentBuilder();
        Document document = documentBuilder.parse(configFile);
        return document;
    }
}
