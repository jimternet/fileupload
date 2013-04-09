package com.doof.services;

import java.util.Map;
import java.util.Map.Entry;

import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.ioc.annotations.InjectService;
import org.hibernate.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.yammer.metrics.health.HealthCheck;

public class DatabaseHealthCheckImpl extends HealthCheck implements DatabaseHealthCheck  {


	Logger logger = LoggerFactory.getLogger(DatabaseHealthCheckImpl.class);
			
//	@Inject
	private Session session;
	
	@Inject
	private HealthCheckRegistryService healthChecks;
	

    public DatabaseHealthCheckImpl(@InjectService("Session") Session session) {
		super();
		if (session == null){
	    	logger.error(" SESSION IS NULL!!!!");

		}
		this.session = session;
    }



	@Override
    protected Result check() throws Exception {
        if (session.isOpen()) {
            return Result.healthy();
        }
        return Result.unhealthy("Hiberante session is closed!");
    }



	public Map<String, Result> runHealthChecks() {
		
		
		final Map<String, HealthCheck.Result> results = healthChecks.runHealthChecks();
		for (Entry<String, HealthCheck.Result> entry : results.entrySet()) {
		    if (entry.getValue().isHealthy()) {
		    	logger.info(entry.getKey() + " is healthy");
		    } else {
		    	logger.error(entry.getKey() + " is UNHEALTHY: " + entry.getValue().getMessage());
		        final Throwable e = entry.getValue().getError();
		        if (e != null) {
		            e.printStackTrace();
		        }
		    }
		}
		return results;
	}


}
