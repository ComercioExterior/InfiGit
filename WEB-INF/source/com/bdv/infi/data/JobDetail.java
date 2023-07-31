/**
 * 
 */
package com.bdv.infi.data;


/**
 *
 */
public class JobDetail {

	/**
	 * Nombre de la Tarea a ejecutar por el scheduler
	 */
	private String name;
	/**
	 * Grupo al que pertenece la tarea
	 */
	private String group;
	/**
	 * Descripcion de la tarea
	 */
	private String description;
	/**
	 * Clase a ejecutar por el Scheduler
	 */
	private String jobClass;
	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}
	/**
	 * @param description the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}
	/**
	 * @return the group
	 */
	public String getGroup() {
		return group;
	}
	/**
	 * @param group the group to set
	 */
	public void setGroup(String group) {
		this.group = group;
	}
	/**
	 * @return the jobClass
	 */
	public String getJobClass() {
		return jobClass;
	}
	/**
	 * @param jobClass the jobClass to set
	 */
	public void setJobClass(String jobClass) {
		this.jobClass = jobClass;
	}
	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}
}
