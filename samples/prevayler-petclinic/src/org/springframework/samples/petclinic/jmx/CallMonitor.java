package org.springframework.samples.petclinic.jmx;

/**
 * Simple interface for a call monitor.
 * To be exposed for management via JMX.
 * 
 * @author Rob Harrop
 * @since 1.2
 */
public interface CallMonitor {

	void setEnabled(boolean isEnabled);

	boolean isEnabled();

	void reset();

	int getCallCount();

	long getCallTime();

}
