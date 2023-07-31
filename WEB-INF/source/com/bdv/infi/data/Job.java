/**
 * 
 */
package com.bdv.infi.data;

/**
 *
 */
public class Job {
	
	/**
	 * Job Details
	 */
	private JobDetail jobDetail;
	
	/**
	 *Trigger asociados a la tarea 
	 */
	private Trigger trigger;
	/**
	 * @return the jobDetail
	 */
	public JobDetail getJobDetail() {
		return jobDetail;
	}

	/**
	 * @param jobDetail the jobDetail to set
	 */
	public void setJobDetail(JobDetail jobDetail) {
		this.jobDetail = jobDetail;
	}

	/**
	 * @return the trigger
	 */
	public Trigger getTrigger() {
		return trigger;
	}

	/**
	 * @param trigger the trigger to set
	 */
	public void setTrigger(Trigger trigger) {
		this.trigger = trigger;
	}
}
