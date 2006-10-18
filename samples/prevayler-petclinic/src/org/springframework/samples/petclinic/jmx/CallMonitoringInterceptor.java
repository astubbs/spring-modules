package org.springframework.samples.petclinic.jmx;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

import org.springframework.util.StopWatch;

/**
 * Simple interceptor that monitors call count and call invocation time.
 * Implements the CallMonitor management interface.
 * 
 * @author Rob Harrop
 * @author Juergen Hoeller
 * @since 1.2
 */
public class CallMonitoringInterceptor implements CallMonitor, MethodInterceptor {

	private boolean isEnabled = true;

	private int callCount = 0;

	private long accumulatedCallTime = 0;


	public void setEnabled(boolean enabled) {
		isEnabled = enabled;
	}

	public boolean isEnabled() {
		return isEnabled;
	}

	public void reset() {
		this.callCount = 0;
		this.accumulatedCallTime = 0;
	}

	public int getCallCount() {
		return callCount;
	}

	public long getCallTime() {
		return (this.callCount > 0 ? this.accumulatedCallTime / this.callCount : 0);
	}


	public Object invoke(MethodInvocation invocation) throws Throwable {
		if (this.isEnabled) {
			StopWatch sw = new StopWatch(invocation.getMethod().getName());

			sw.start("invoke");
			try {
				return invocation.proceed();
			}
			finally {
				sw.stop();
				synchronized (this) {
					this.callCount++;
					this.accumulatedCallTime += sw.getTotalTimeMillis();
				}
			}
		}

		else {
			return invocation.proceed();
		}
	}

}
