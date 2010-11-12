/**
 * NOTE: This copyright does *not* cover user programs that use Hyperic
 * program services by normal system calls through the application
 * program interfaces provided as part of the Hyperic Plug-in Development
 * Kit or the Hyperic Client Development Kit - this is merely considered
 * normal use of the program, and does *not* fall under the heading of
 * "derived work".
 *
 *  Copyright (C) [2010], VMware, Inc.
 *  This file is part of Hyperic.
 *
 *  Hyperic is free software; you can redistribute it and/or modify
 *  it under the terms version 2 of the GNU General Public License as
 *  published by the Free Software Foundation. This program is distributed
 *  in the hope that it will be useful, but WITHOUT ANY WARRANTY; without
 *  even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 *  PARTICULAR PURPOSE. See the GNU General Public License for more
 *  details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program; if not, write to the Free Software
 *  Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307
 *  USA.
 *
 */
package org.hyperic.hq.plugin.rabbitmq.configure;

import org.hyperic.hq.plugin.rabbitmq.collect.MetricConstants;
import org.hyperic.hq.plugin.rabbitmq.core.DetectorConstants;
import org.hyperic.hq.product.PluginException;
import org.hyperic.util.config.ConfigResponse;

import java.util.Properties;

/**
 * Configuration
 * @author Helena Edelson
 */
public class Configuration {

    private String nodename;
    private String virtualHost;
    private String authentication;
    private String defaultVirtualHost = "/";

    @Override
    public String toString() {
        return "[nodename="+nodename+" virtualHost="+virtualHost+" authentication="+authentication+"]";
    }

    public boolean isDefaultVirtualHost() {
        return this.virtualHost.equalsIgnoreCase(defaultVirtualHost);
    }

    /**
     * Explicitly set the virtual host as the default
     * when we initialize for the first time in order to
     * collect virtualHosts.
     * @param doSet
     */
    public void setDefaultVirtualHost(boolean doSet) {
        if (doSet && this.virtualHost == null) {
            this.virtualHost = defaultVirtualHost;
        }
    }

    public String getDefaultVirtualHost() {
        return defaultVirtualHost;
    }

    public boolean isMatch(Configuration comparableKey) {
        return comparableKey != null && this.getVirtualHost().equalsIgnoreCase(comparableKey.getVirtualHost())
                && this.getNodename().equalsIgnoreCase(comparableKey.getNodename());
    }

    /**
     * Call before creating ApplicationContext
     * Log which one failed. 
     * @return
     * @throws org.hyperic.hq.product.PluginException
     *
     */
    public boolean isConfigured() throws PluginException {
        if (nodename == null) {
            throw new PluginException("This resource requires the node name of the broker.");
        }

        if (authentication == null) {
            throw new PluginException("Erlang cookie value is not set yet. Please insure the Agent has permission to read the Erlang cookie.");
        }

        return true;
    }

    /**
     * Call before validating OtpConnection
     * @return true if has values
     */
    public boolean isConfiguredOtpConnection() {
        return authentication != null && nodename != null;
    }

    public String getNodename() {
        return nodename;
    }

    public void setNodename(String nodename) {
        this.nodename = nodename != null && nodename.length() > 0 ? nodename.trim() : null;
    }

    public String getAuthentication() {
        return authentication;
    }

    public void setAuthentication(String authentication) {
        this.authentication = authentication != null && authentication.length() > 0 ? authentication.trim() : null;
    }

    public String getVirtualHost() {
        return virtualHost;
    }

    public void setVirtualHost(String virtualHost) {
        this.virtualHost = virtualHost;
    }

    public static Configuration toConfiguration(Properties props) {
        Configuration conf = new Configuration();
        conf.setNodename(props.getProperty(DetectorConstants.NODE));
        conf.setVirtualHost(props.getProperty(MetricConstants.VHOST));
        conf.setAuthentication(props.getProperty(DetectorConstants.AUTHENTICATION));

        return conf;
    }

    public static Configuration toConfiguration(ConfigResponse configResponse) {
        return toConfiguration(configResponse.toProperties());
    }
}
