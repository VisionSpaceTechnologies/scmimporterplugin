/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.visionspace.scmimporter;

import hudson.Extension;
import hudson.model.RootAction;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import org.apache.maven.scm.CommandParameters;
import org.apache.maven.scm.ScmException;
import javax.servlet.ServletException;
import jenkins.model.Jenkins;
import org.apache.maven.scm.ScmFileSet;
import org.apache.maven.scm.log.ScmLogDispatcher;
import org.apache.maven.scm.manager.BasicScmManager;
import org.apache.maven.scm.manager.NoSuchScmProviderException;
import org.apache.maven.scm.manager.ScmManager;
import org.apache.maven.scm.repository.ScmRepository;
import org.apache.maven.scm.repository.ScmRepositoryException;
import org.kohsuke.stapler.StaplerRequest;
import org.kohsuke.stapler.StaplerResponse;
import org.apache.maven.scm.provider.git.gitexe.GitExeScmProvider;
import org.apache.maven.scm.provider.git.gitexe.command.checkout.GitCheckOutCommand;
/**
 *
 * @author luis.teixeira
 */
@Extension
public class SCMImportAction implements RootAction{
    
    private static final String TMP_DIRECTORY = "/tmp/jenkins";
    
    private String repositoryUrl;
    private String username, password;
    private Map<String, Job> jobs = new HashMap<String, Job>();

    public void doImport(final StaplerRequest request, final StaplerResponse response) throws ServletException,
      IOException,
      InterruptedException {
        
        if(isJobsAvailable()) {
            boolean overwrites = false;
            if(request.hasParameter("overwrite") ){
                overwrites = true;
            }
            if(request.hasParameter("jobName")) {
                for (final String jobName : Arrays.asList(request.getParameterValues("jobName"))) {
                    Job job = jobs.get(jobName);
                    try {
                        Jenkins.getInstance().createProjectFromXML(jobName, new FileInputStream(job.getConfigFile()));
                        job.setImportStatus(Job.ImportStatus.IMPORT_SUCCESSFUL);
                    }catch(IllegalArgumentException e) {
                        if(overwrites){
                            try {
                                Jenkins.getInstance().getItem(jobName).delete();
                                Jenkins.getInstance().createProjectFromXML(jobName, new FileInputStream(job.getConfigFile()));
                                job.setImportStatus(Job.ImportStatus.IMPORT_SUCCESSFUL);
                            }catch(Exception ex){
                                job.setImportStatus(Job.ImportStatus.IMPORT_FAILED);
                            }
                        }else {
                            job.setImportStatus(Job.ImportStatus.IMPORT_FAILED);
                        }
                    } catch(Exception e) {
                        job.setImportStatus(Job.ImportStatus.IMPORT_FAILED);
                    }
                }
            }
        }
        response.forwardToPreviousPage(request);
    }
   
    public void doQuery(final StaplerRequest request, final StaplerResponse response) throws ServletException,
      IOException {
        repositoryUrl = request.getParameter("_.repositoryUrl");
        username = request.getParameter("_.username");
        password = request.getParameter("_.password");
        
        File baseDir  = new File(TMP_DIRECTORY);
        ScmRepository repository= null;
        ScmManager scmManager = new BasicScmManager();
        scmManager.setScmProvider("git", new GitExeScmProvider());
        try{
            String scmUrl = "scm:git:" + repositoryUrl;
            repository = scmManager.makeScmRepository(scmUrl);
            repository.getProviderRepository().setUser(username);
            repository.getProviderRepository().setPassword(password);
            
            GitCheckOutCommand checkoutCommand = new GitCheckOutCommand();
            checkoutCommand.setLogger(new ScmLogDispatcher());
            
            CommandParameters parameters = new CommandParameters();
            checkoutCommand.executeCommand(repository.getProviderRepository(), new ScmFileSet(baseDir), parameters);
            createJobsList();
        }catch (ScmRepositoryException e) {
            e.printStackTrace();
        }catch (NoSuchScmProviderException e) {
            e.printStackTrace();
        }catch (ScmException e) {
            e.printStackTrace();
        }
        response.forwardToPreviousPage(request);
    }
   
    private void createJobsList() {
        jobs.clear();
        
        File jobsRepo = new File(TMP_DIRECTORY + "/jobs");
        
        for(File f : jobsRepo.listFiles()) {
            File configFile = new File(f.getAbsolutePath() + "/config.xml");
            jobs.put(f.getName(), new Job(f.getName(), configFile));
        }
    }
    
    public boolean isJobsAvailable() {
        return jobs.size() > 0;
    }
    
    public Collection<Job> getJobs() {
        return jobs.values();
    }
    
    @Override
    public String getIconFileName() {
        return "/images/32x32/setting.png";
    }

    @Override
    public String getDisplayName() {
        return "SCM Import plugin";
    }

    @Override
    public String getUrlName() {
        return "/scm-import";
    }
    
   
}
