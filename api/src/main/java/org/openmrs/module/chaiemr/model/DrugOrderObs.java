package org.openmrs.module.chaiemr.model;

import org.openmrs.Concept;

public class DrugOrderObs {
	private String drug;
	private String formulation;
	private String strength;
	private String frequency;
	private Concept route;
	private String duration;
	private Integer obsGroupId;

	public String getDrug() {
		return drug;
	}

	public void setDrug(String drug) {
		this.drug = drug;
	}

	public String getFormulation() {
		return formulation;
	}

	public void setFormulation(String formulation) {
		this.formulation = formulation;
	}

	public String getStrength() {
		return strength;
	}

	public void setStrength(String strength) {
		this.strength = strength;
	}

	public String getFrequency() {
		return frequency;
	}

	public void setFrequency(String frequency) {
		this.frequency = frequency;
	}
	
	public Concept getRoute() {
		return route;
	}

	public void setRoute(Concept route) {
		this.route = route;
	}

	public String getDuration() {
		return duration;
	}

	public void setDuration(String duration) {
		this.duration = duration;
	}

	public Integer getObsGroupId() {
		return obsGroupId;
	}

	public void setObsGroupId(Integer obsGroupId) {
		this.obsGroupId = obsGroupId;
	}
}
