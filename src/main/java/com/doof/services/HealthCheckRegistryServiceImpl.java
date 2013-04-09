package com.doof.services;

import java.util.Map;

import org.apache.tapestry5.ioc.annotations.InjectService;
import org.hibernate.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.yammer.metrics.health.HealthCheck;
import com.yammer.metrics.health.HealthCheck.Result;
import com.yammer.metrics.health.HealthCheckRegistry;

public class HealthCheckRegistryServiceImpl implements HealthCheckRegistryService {
	
	private static final HealthCheckRegistry healthChecks = new HealthCheckRegistry();
	private Session session;
	Logger logger = LoggerFactory.getLogger(HealthCheckRegistryServiceImpl.class);
	

	public HealthCheckRegistryServiceImpl(@InjectService("Session") Session session) {
		super();
		if (session == null){
	    	logger.error(" SESSION IS NULL!!!!");

		}
		this.session = session;


	}



	public void register(String name, HealthCheck healthCheck) {
		healthChecks.register(name, healthCheck);

	}



	public Map<String, Result> runHealthChecks() {
		return healthChecks.runHealthChecks();
	}



	public void initRegistries() {
		healthChecks.register("hibernate", new DatabaseHealthCheckImpl(session));
	}


}
