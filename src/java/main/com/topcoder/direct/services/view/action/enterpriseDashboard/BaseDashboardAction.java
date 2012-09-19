/*
 * Copyright (C) 2012 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.direct.services.view.action.enterpriseDashboard;

import com.topcoder.direct.services.project.metadata.entities.dao.DirectProjectMetadataKey;
import com.topcoder.direct.services.view.action.contest.launch.BaseDirectStrutsAction;
import com.topcoder.direct.services.view.util.DataProvider;
import com.topcoder.direct.services.view.util.DirectUtils;
import com.topcoder.security.TCSubject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 *  The base action for the new enterprise dashboard, it acts as the entry point as the JSP pages of enterprise
 *  dashboard.
 * </p>
 *
 * @author TCSASSEMBLER
 * @version 1.0
 */
public class BaseDashboardAction extends BaseDirectStrutsAction {

    /**
     * The default start year of the timeline filter.
     */
    private static final int DEFAULT_START_YEAR = 2005;

    /**
     * The months label.
     */
    private static final String[] MONTHS = {"JAN", "FEB", "MAR", "APR", "MAY", "JUN", "JUL", "AUG", "SEP", "OCT", "NOV", "DEC"};

    /**
     * The clients displayed in the filter.
     */
    private Map<Long, String> clients;

    /**
     * The project status displayed in the filter.
     */
    private Map<Long, String> directProjectStatus;

    /**
     * The client projects displayed in the filter.
     */
    private Map<Long, String> clientProjects;

    /**
     * The client metadata keys displayed in the filter.
     */
    private Map<Long, String> clientMetadataKeys;

    /**
     * Gets the clients map.
     *
     * @return the clients map.
     */
    public Map<Long, String> getClients() {
        return clients;
    }

    /**
     * Sets the client map.
     *
     * @param clients the clients map.
     */
    public void setClients(Map<Long, String> clients) {
        this.clients = clients;
    }

    /**
     * Gets the direct project status.
     *
     * @return the direct project status.
     */
    public Map<Long, String> getDirectProjectStatus() {
        return directProjectStatus;
    }

    /**
     * Sets the direct project status.
     *
     * @param directProjectStatus the direct project status.
     */
    public void setDirectProjectStatus(Map<Long, String> directProjectStatus) {
        this.directProjectStatus = directProjectStatus;
    }

    /**
     * Gets the client projects.
     *
     * @return the client projects.
     */
    public Map<Long, String> getClientProjects() {
        return clientProjects;
    }

    /**
     * Sets the client projects.
     *
     * @param clientProjects the client projects.
     */
    public void setClientProjects(Map<Long, String> clientProjects) {
        this.clientProjects = clientProjects;
    }

    /**
     * Gets the client metadata keys.
     *
     * @return the client metadata keys.
     */
    public Map<Long, String> getClientMetadataKeys() {
        return clientMetadataKeys;
    }

    /**
     * Sets the client metadata keys.
     *
     * @param clientMetadataKeys the client metadata keys.
     */
    public void setClientMetadataKeys(Map<Long, String> clientMetadataKeys) {
        this.clientMetadataKeys = clientMetadataKeys;
    }

    /**
     * Gets the default client to display when first enters the JSP page.
     *
     * @return the default client to display when first enters the JSP page.
     */
    public String getDefaultClient() {

        for(Map.Entry<Long, String> client : getClients().entrySet()) {
            return client.getValue();
        }
        return "None";
    }

    /**
     * Gets the month options to display on the filter panel.
     *
     * @return the month options to display on the filter panel.
     */
    public List<String> getMonthOptions() {
        int startYear =  DEFAULT_START_YEAR;
        int endYear = Calendar.getInstance().get(Calendar.YEAR);

        List<String> monthOptions = new ArrayList<String>();

        for(int year = startYear; year <= endYear; year++) {
            for(String m : MONTHS) {
                monthOptions.add(m + "'" + String.valueOf(year).substring(2));
            }
        }

        return monthOptions;
    }

    /**
     * Prepares the data for displaying the enterprise dashboard page and filter panel.
     *
     * @throws Exception if there is error
     */
    @Override
    protected void executeAction() throws Exception {
        final TCSubject currentUser = DirectUtils.getTCSubjectFromSession();
        // prepare the available clients for the filter panel
        setClients(DirectUtils.getAllClients(currentUser));

        // prepare the available project status for the filter panel
        setDirectProjectStatus(new LinkedHashMap<Long, String>());
        getDirectProjectStatus().put(0L, "All Project Status");
        final Map<Long, String> allDirectProjectStatus = DataProvider.getAllDirectProjectStatus();
        for(Long statusId : allDirectProjectStatus.keySet()) {
            getDirectProjectStatus().put(statusId, allDirectProjectStatus.get(statusId));
        }

        setClientProjects(new LinkedHashMap<Long, String>());
        setClientMetadataKeys(new LinkedHashMap<Long, String>());

        if(getClients().size() > 0) {
            for(Map.Entry<Long, String> client : getClients().entrySet()) {
                // get projects for the first client
                getClientProjects().put(0L, "All Projects");
                final Map<Long, String> projectsForClient = DirectUtils.getProjectsForClient(currentUser, client.getKey());
                for(Long projectId : projectsForClient.keySet()) {
                    getClientProjects().put(projectId, projectsForClient.get(projectId));
                }

                // get metadata keys for the first client
                getClientMetadataKeys().put(0L, "None");
                final List<DirectProjectMetadataKey> clientProjectMetadataKeys =
                        getMetadataKeyService().getClientProjectMetadataKeys(client.getKey(), true);
                for(DirectProjectMetadataKey key : clientProjectMetadataKeys) {
                    getClientMetadataKeys().put(key.getId(), key.getName());
                }

                break;
            }

        }
    }
}