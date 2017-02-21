package org.openmrs.module.chaiemr.model;

public class DrugDetails {
	private String drug;
	private String strength;
	private String formulation;
	private String frequency;
	private String duration;
	private String quantity;

	public String getDrug() {
		return drug;
	}

	public void setDrug(String drug) {
		this.drug = drug;
	}
	
	public String getStrength() {
		return strength;
	}

	public void setStrength(String strength) {
		this.strength = strength;
	}

	public String getFormulation() {
		return formulation;
	}

	public void setFormulation(String formulation) {
		this.formulation = formulation;
	}

	public String getFrequency() {
		return frequency;
	}

	public void setFrequency(String frequency) {
		this.frequency = frequency;
	}

	public String getDuration() {
		return duration;
	}

	public void setDuration(String duration) {
		this.duration = duration;
	}

	public String getQuantity() {
		return quantity;
	}

	public void setQuantity(String quantity) {
		this.quantity = quantity;
	}
}
