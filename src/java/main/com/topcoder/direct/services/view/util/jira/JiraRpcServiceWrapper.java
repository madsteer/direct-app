/*
 * Copyright (C) 2011 - 2012 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.direct.services.view.util.jira;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.atlassian.jira.rpc.soap.client.JiraSoapService;
import com.atlassian.jira.rpc.soap.client.RemoteAuthenticationException;
import com.atlassian.jira.rpc.soap.client.RemoteFieldValue;
import com.atlassian.jira.rpc.soap.client.RemoteIssue;
import com.atlassian.jira.rpc.soap.client.RemoteResolution;
import com.atlassian.jira.rpc.soap.client.RemoteStatus;
import com.atlassian.jira_soapclient.SOAPSession;
import com.topcoder.direct.services.configs.ConfigUtils;
import com.topcoder.direct.services.view.dto.TcJiraIssue;
import com.topcoder.direct.services.view.dto.contest.ContestBriefDTO;

/**
 * <p>The class provides a wrapper for the JiraSoapService. And it provides various methods to get issue data using
 * JiraSoapService.</p>
 *
 * <p>Version 1.1 TC Cockpit Bug Tracking R1 Cockpit Project Tracking version 1.0 change notes:
 * - Added {@link #getIssuesForDirectProject(List<? extends ContestBriefDTO>)} method
 * - Added {@link #getIssuesFromJQLQuery(String)} method
 * </p>
 * 
 * <p>
 * Version 1.2 (TC Direct Issue Tracking Tab Update Assembly 1) change notes:
 *   <ol>
 *     <li>Added {@link #createIssue(RemoteIssue, Long)} method to create a new JIRA issue with security level.</li>
 *     <li>Added {@link #getIssueById(String)} method to retrieve JIRA issue by id.</li>
 *     <li>Added {@link #updateIssue(String, RemoteFieldValue[])} method to update an existing JIRA issue.</li>
 *     <li>Added {@link #getSecurityLevelId(String)} method to retrieve the security level id of the JIRA issue.</li>
 *     <li>Added {@link #getResolutionNames()} method to retrieve the issue resolution names.<li>
 *     <li>Added {@link #getIssueStatusNames()} method to retrieve the issue status names.<li>
 *     <li>Updated {@link #getIssuesForContest(long)} method to fix the bug of not retrieving issues from JIRA.</li>
 *   </ol>
 * </p>
 *
 * @author Veve, TCSASSEMBER
 * @version 1.2 (TC Cockpit Bug Tracking R1 Cockpit Project Tracking assembly)
 */
public class JiraRpcServiceWrapper {

    /**
     * Logger for this class
     */
    private static final Logger logger = Logger.getLogger(JiraRpcServiceWrapper.class);

    /**
     * The soap session between client and Jira RPC soap service.
     */
    private static SOAPSession soapSession;

    /**
     * The map used to store the mapping of resolution id to resolution name.
     */
    private static Map<String, String> RESOLUTION_NAMES;

    /**
     * The map used to store the mapping of issue status id to issue status name.
     */
    private static Map<String, String> ISSUE_STATUS_NAMES;

    /**
     * The static field used to count the number of authentication failure. The value will be reset to 0 after a
     * successful authentication.
     */
    private static int retryAttemptCount = 0;


    /**
     * Private constructor prevents from initialization.
     */
    private JiraRpcServiceWrapper() {
        // do nothing
    }

    /**
     * Initialize the soap session before calling the methods of RPC service.
     *
     * @throws Exception if any error occurs.
     */
    private static void initializeSoapSession() throws Exception {

        // Get configurations
        String jiraServiceUrl = ConfigUtils.getIssueTrackingConfig().getJiraRpcURL();
        String user = ConfigUtils.getIssueTrackingConfig().getUser();
        String password = ConfigUtils.getIssueTrackingConfig().getPassword();

        // create a soap session from the configured tc jira service soap url
        soapSession = new SOAPSession(new URL(jiraServiceUrl));
        soapSession.connect(user, password);

        // the JIRA SOAP Service and authentication token are used to make authentication calls
        JiraSoapService jiraSoapService = soapSession.getJiraSoapService();
        String authToken = soapSession.getAuthenticationToken();

        // initialize mappings of resolution names if needed
        if (RESOLUTION_NAMES == null) {
            RemoteResolution[] resolutions = jiraSoapService.getResolutions(authToken);
            RESOLUTION_NAMES = new HashMap<String, String>();
            for (RemoteResolution r : resolutions) {
                RESOLUTION_NAMES.put(r.getId(),r.getName());
            }
        }

        // initialize mappings of status names if needed
        if (ISSUE_STATUS_NAMES == null) {
            RemoteStatus[] statuses = jiraSoapService.getStatuses(authToken);
            ISSUE_STATUS_NAMES = new HashMap<String, String>();
            for (RemoteStatus s : statuses) {
                ISSUE_STATUS_NAMES.put(s.getId(), s.getName());
            }
        }
        
    }

    /**
     * <p>Create a new JIRA issue.</p>
     *
     * @param project the JIRA project the new issue belongs to.
     * @param issueTypeId the type id of the new created issue.
     * @param summary the summary of the issue.
     * @param description the description of the issue.
     * @param reporter the reporter of the issue.
     * @throws Exception if any error occurs.
     */
    public static void createIssue(String project, int issueTypeId, String summary, String description, String reporter) throws Exception {
        // if soap session is not established, initialize a soap session first
        if (soapSession == null) {
            initializeSoapSession();
        }

        JiraSoapService service = soapSession.getJiraSoapService();
        String token = soapSession.getAuthenticationToken();
        RemoteIssue issue = new RemoteIssue();
        issue.setProject(project);
        issue.setType(String.valueOf(issueTypeId));
        issue.setSummary(summary);
        issue.setDescription(description);
        issue.setReporter(reporter);
        service.createIssue(token, issue);
    }

    /**
     * <p>Creates a new JIRA issue with security level.</p>
     * 
     * @param issue the JIRA issue to be created.
     * @param securityLevelId the security level id.
     * @return the new created JIRA issue.
     * @throws Exception if any error occurs.
     * @since 1.2
     */
    public static TcJiraIssue createIssue(RemoteIssue issue, Long securityLevelId)   throws Exception {
        // if soap session is not established, initialize a soap session first
        if (soapSession == null) {
            initializeSoapSession();
        }

        JiraSoapService service = soapSession.getJiraSoapService();
        String token = soapSession.getAuthenticationToken();
        return new TcJiraIssue(service.createIssueWithSecurityLevel(token, issue, securityLevelId));
    }
    
    /**
     * Gets all the issues for specified contest id.
     *
     * @param contestId the id of the contest.
     * @return the list of all the issues of the contest.
     * @throws Exception if any error occurs.
     */
    public static List<TcJiraIssue> getIssuesForContest(long contestId) throws Exception {
        // throw IllegalArgumentException when the contest id is not positive
        if (contestId <= 0 ) {
            throw new IllegalArgumentException("contest id should be positive.");
        }

        // build the JQL query first
        String softwareQuery = ConfigUtils.getIssueTrackingConfig().getSoftwareContestJQLQuery();
        String jqlQuery = (softwareQuery) + contestId;

        List<TcJiraIssue> result = getIssuesFromJQLQuery(jqlQuery);

        return result;
    }

    /**
     * Gets TcJiraIssues of the given list of contests.
     *
     * @param contests a list of contests.
     * @return a list of TcJiraIssues.
     * @throws Exception if an unexpected error occurs.
     * @since 1.1
     */
    public static List<TcJiraIssue> getIssuesForDirectProject(List<? extends ContestBriefDTO> contests) throws Exception {
        long time = System.currentTimeMillis();

        // when the input is null or empty, return an empty result
        if (contests == null || contests.size() == 0 ) {
            return  new ArrayList<TcJiraIssue>();
        }

        // build the JQL query first
        String softwareQuery = ConfigUtils.getIssueTrackingConfig().getSoftwareContestJQLQuery();

        StringBuffer jqlQueryBuilder = new StringBuffer();

        for(ContestBriefDTO contest : contests) {
            jqlQueryBuilder.append((softwareQuery ) + contest.getId());
            jqlQueryBuilder.append(" OR ");
        }

        // remove the last " OR " which is not needed
        String jqlQuery = jqlQueryBuilder.substring(0, jqlQueryBuilder.length() - 3) + " order by Created DESC";

        List<TcJiraIssue> result = getIssuesFromJQLQuery(jqlQuery);


        return result;
    }

    /**
     * <p>Gets the JIRA issue by issue id.</p>
     * 
     * @param issueId the issue id.
     * @return the retrieved JIRA issue.
     * @throws Exception if an unexpected error occurs.
     * @since 1.2
     */
    public static TcJiraIssue getIssueById(String issueId) throws Exception {
        try {
            if (soapSession == null) {
                initializeSoapSession();
            }
            
            JiraSoapService service = soapSession.getJiraSoapService();
            String token = soapSession.getAuthenticationToken();
            return new TcJiraIssue(service.getIssueById(token, issueId));
        } catch (Exception ex) {
            logger.error("Error when executing method getIssueById" + ", issue id is :" + issueId, ex);
            throw ex;
        }
    }
    
    /**
     * <p>Updates a JIRA issue.</p>
     * 
     * @param issueKey the key of the issue which to be updated.
     * @param filedValues the field values to be updated.
     * @throws Exception if an unexpected error occurs.
     * @since 1.2
     */
    public static void updateIssue(String issueKey, RemoteFieldValue[] filedValues) throws Exception {
        try {
            if (soapSession == null) {
                initializeSoapSession();
            }
            
            JiraSoapService service = soapSession.getJiraSoapService();
            String token = soapSession.getAuthenticationToken();
            service.updateIssue(token, issueKey, filedValues);
        } catch (Exception ex) {
            logger.error("Error when executing method updateIssue" + ", issue key is :" + issueKey, ex);
            throw ex;
        }
    }

    /**
     * <p>Gets security level id for a JIRA issue.</p>
     * 
     * @param issueKey the JIRA issue key.
     * @return the security level id of a JIRA issue.
     * @throws Exception if an unexpected error occurs.
     * @since 1.2
     */
    public static Long getSecurityLevelId(String issueKey) throws Exception {
        if (soapSession == null) {
            initializeSoapSession();
        }
        
        JiraSoapService service = soapSession.getJiraSoapService();
        String token = soapSession.getAuthenticationToken();
        return Long.parseLong(service.getSecurityLevel(token, issueKey).getId());
    }
    
    /**
     * Gets a list of TcJiraIssue by calling remote service with the specified JQL query.
     *
     * @param jqlQuery the JQL query.
     * @return a list of TcJiraIssue.
     * @throws Exception if an unexpected error occurs.
     * @since 1.1
     */
    private static List<TcJiraIssue> getIssuesFromJQLQuery(String jqlQuery) throws Exception {

        // List to store the final result
        final List<TcJiraIssue> result = new ArrayList<TcJiraIssue>();

        try {

            // if soap session is not established, initialize a soap session first
            if (soapSession == null) {
                initializeSoapSession();
            }


            JiraSoapService service = soapSession.getJiraSoapService();
            String token = soapSession.getAuthenticationToken();

            List<Long> includeIssueTypeIds = ConfigUtils.getIssueTrackingConfig().getIncludedIssueTypeIds();

            // gets issues from the RPC service
            RemoteIssue[] issues = service.getIssuesFromJqlSearch(token, jqlQuery, ConfigUtils.getIssueTrackingConfig().getMaxResultNumber());

            for (RemoteIssue issue : issues) {

                TcJiraIssue tcJiraIssue = new TcJiraIssue((issue));

                tcJiraIssue.setResolutionName(RESOLUTION_NAMES.get(tcJiraIssue.getResolutionId()));
                tcJiraIssue.setStatusName(ISSUE_STATUS_NAMES.get(tcJiraIssue.getStatusId()));

                // check issue type
                if (includeIssueTypeIds.contains(Long.parseLong(issue.getType()))) {
                    result.add(tcJiraIssue);
                }
            }

        } catch (RemoteAuthenticationException authEx) {
            // got authentication exception, try authentication again until max error reached
            if (retryAttemptCount < ConfigUtils.getIssueTrackingConfig().getMaxAuthRetry()) {
                retryAttemptCount++;
                initializeSoapSession();
                return getIssuesFromJQLQuery(jqlQuery);
            }
            else {
                // wrap into JiraRpcServiceAuthenticationException and rethrow
                // throw new JiraRpcServiceAuthenticationException("Failed to authenticate with Jira RPC Service.", authEx);
                logger.error("Error when executing method getIssuesFromJQLQuery" + ", JQL Query is :" + jqlQuery + "Error message is :" + authEx.getMessage(), authEx);
            }

        } catch (Exception ex) {
            // all the other exception will be wrapped into JiraRpcServiceProcessingException
            // throw new JiraRpcServiceProcessingException("Error occurs when calling Jira RPC Service.", ex);
            logger.error("Error when executing method getIssuesFromJQLQuery" + ", JQL Query is :" + jqlQuery + "Error message is :" + ex.getMessage(), ex);
        }

        // successfully get data, reset retry attempts
        retryAttemptCount = 0;

        return result;
    }
    
    /**
     * <p>Gets the map used to store the mapping of resolution id to resolution name.</p>
     * 
     * @return the map used to store the mapping of resolution id to resolution name.
     * @since 1.2
     */
    public static Map<String, String> getResolutionNames() {
        return RESOLUTION_NAMES;
    }
    
    /**
     * <p>Gets the map used to store the mapping of issue status id to issue status name.</p>
     * 
     * @return the map used to store the mapping of issue status id to issue status name.
     * @since 1.2
     */
    public static Map<String, String> getIssueStatusNames() {
        return ISSUE_STATUS_NAMES;
    }
}
