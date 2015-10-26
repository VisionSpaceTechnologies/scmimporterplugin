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
        super.configure(req, formData);
    }
    
    public SCM[] getScms() {
        return SCM.values();
    }
    
}