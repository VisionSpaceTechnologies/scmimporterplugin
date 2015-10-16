/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.visionspace.scmimporter;

import com.visionspace.scmimporter.scms.SCM;
import hudson.Plugin;
import hudson.model.Descriptor;
import java.io.IOException;
import javax.servlet.ServletException;
import net.sf.json.JSONObject;
import org.kohsuke.stapler.StaplerRequest;

/**
 *
 * @author luis.teixeira
 */
public class SCMImporterPlugin extends Plugin {
    
    private String respositoryURL;
    private SCM scm;
    
    @Override
    public void start() throws Exception{
        super.start();
        /* Do something */
    }
    
    
    @Override
    public void stop() throws Exception {
        /* Do something */
        super.stop();
    }
    
    @Override
    public void configure(StaplerRequest req, JSONObject formData)
                    throws IOException, ServletException, Descriptor.FormException {
        this.respositoryURL = this.scm.createScmUrlFromRequest(req);
    }
    
    public SCM[] getScms() {
        return SCM.values();
    }
   
    public boolean isScmSelected(SCM scm) {
        return this.scm == scm;
    }
    
    public String getScmUrl() {
        if (this.scm != null) {
            return this.scm.extractScmUrlFrom(this.respositoryURL);
        } else { 
            return null;
        }
    }
    
}
