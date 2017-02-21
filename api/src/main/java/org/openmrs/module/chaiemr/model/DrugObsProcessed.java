package org.openmrs.module.chaiemr.model;

import java.util.Date;

import org.openmrs.Obs;
import org.openmrs.Patient;

public class DrugObsProcessed implements java.io.Serializable {
	private static final long serialVersionUID = 1L;
	private Integer id;
	private Obs obs;
	private Patient patient;
	private Date createdDate;
	private Date processedDate;
	private Integer quantityPostProcess;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Obs getObs() {
		return obs;
	}

	public void setObs(Obs obs) {
		this.obs = obs;
	}
	
	public Patient getPatient() {
		return patient;
	}

	public void setPatient(Patient patient) {
		this.patient = patient;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}
	
	public Date getProcessedDate() {
		return processedDate;
	}

	public void setProcessedDate(Date processedDate) {
		this.processedDate = processedDate;
	}

	public Integer getQuantityPostProcess() {
		return quantityPostProcess;
	}

	public void setQuantityPostProcess(Integer quantityPostProcess) {
		this.quantityPostProcess = quantityPostProcess;
	}
}
