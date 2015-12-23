package com.visionspace.scmimporter.scms;

import org.kohsuke.stapler.StaplerRequest;

public class ScmSyncGitSCM extends SCM {

	private static final String SCM_URL_PREFIX="scm:git:";
	
	ScmSyncGitSCM(){
            super("Git", "git/config.jelly", "hudson.plugins.git.GitSCM", null);
	}
	
	public String createScmUrlFromRequest(StaplerRequest req) {
            String repoURL = req.getParameter("gitRepositoryUrl");
            if(repoURL == null){
                return null;
            }
            else {
                return SCM_URL_PREFIX+repoURL;
            }
	}
	
	public String extractScmUrlFrom(String scmUrl) {
            return scmUrl.substring(SCM_URL_PREFIX.length());
	}
	
	public SCMCredentialConfiguration extractScmCredentials(String scmUrl) {
            return null;
	}
	
}
