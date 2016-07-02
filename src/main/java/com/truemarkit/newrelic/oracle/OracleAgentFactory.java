package com.truemarkit.newrelic.oracle;

import com.netradius.commons.lang.StringHelper;
import com.newrelic.agent.deps.org.slf4j.Logger;
import com.newrelic.agent.deps.org.slf4j.LoggerFactory;
import com.newrelic.agent.deps.org.yaml.snakeyaml.Yaml;
import com.newrelic.metrics.publish.Agent;
import com.newrelic.metrics.publish.AgentFactory;
import com.newrelic.metrics.publish.configuration.ConfigurationException;
import com.truemarkit.newrelic.oracle.model.Metric;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * @author Dilip S Sisodia
 * @author Erik R. Jensen
 */
public class OracleAgentFactory extends AgentFactory {

	private static final Logger log = LoggerFactory.getLogger(OracleAgentFactory.class);

	@Override
	public Agent createConfiguredAgent(Map<String, Object> properties) throws ConfigurationException {
		String name = (String) properties.get("name");
		if (StringHelper.isEmpty(name)) {
			throw new ConfigurationException("name may not be empty");
		}
		String host = (String) properties.get("host");
		if (StringHelper.isEmpty(host)) {
			throw new ConfigurationException("host may not be empty");
		}
		String port = (String) properties.get("port");
		if (StringHelper.isEmpty(port)) {
			throw new ConfigurationException("port may not be empty");
		}
		String serviceName = (String) properties.get("service_name");
		String sid = (String) properties.get("sid");
		if (StringHelper.isEmpty(serviceName) && StringHelper.isEmpty(sid)) {
			throw new ConfigurationException("service_name or sid must have a value");
		}
		String username = (String) properties.get("username");
		if (StringHelper.isEmpty(username)) {
			throw new ConfigurationException("username may not be empty");
		}
		String password = (String) properties.get("password");
		if (StringHelper.isEmpty(password)) {
			throw new ConfigurationException("password may not be empty");
		}
		return new OracleAgent(name, host, port, sid, serviceName, username, password, readMetrics());
	}

	public List<Metric> readMetrics() throws ConfigurationException {
		List<Metric> metrics = new ArrayList<>();
		Yaml y = new Yaml();

		try(InputStream in = Main.class.getClass().getResourceAsStream("/metric.yml")) {
			Iterator<Object> lstObj = y.loadAll(in).iterator();
			Object o = new Object();
			while (lstObj.hasNext()) {
				o = lstObj.next();
			}
			metrics = (List<Metric>) o;
		} catch (IOException ex) {
			log.error("Failed to read metrics: " + ex.getMessage());
		}
		if (log.isDebugEnabled()) {
			log.debug("Found [" + metrics.size() + "] metrics");
		}
		return metrics;
	}

}
