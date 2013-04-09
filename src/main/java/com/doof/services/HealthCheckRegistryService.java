package com.doof.services;

import java.util.Map;

import com.yammer.metrics.health.HealthCheck;
import com.yammer.metrics.health.HealthCheck.Result;

public interface HealthCheckRegistryService {
	
	void register(String name, HealthCheck healthCheck);

	Map<String, Result> runHealthChecks();

	void initRegistries();



}
