package org.openmrs.module.chaiemr.model;

import java.util.Date;

import org.openmrs.Concept;
import org.openmrs.DrugOrder;
import org.openmrs.Patient;
import org.openmrs.Visit;

public class DrugOrderProcessed implements java.io.Serializable {
	private static final long serialVersionUID = 4757208144130681309L;
	private Integer id;
	private DrugOrder drugOrder;
	private Patient patient;
	private Date createdDate;
	private Date processedDate;
	private Boolean processedStatus = Boolean.FALSE;
	private String dose;
	private String doseRegimen;
	private Integer noOfTablet;
	private Concept route;
	private Integer durationPreProcess;
	private Date discontinuedDate;
	private Concept discontinuedReason;
	private String discontinuedReasonNonCoded;
	private Integer quantityPostProcess;
	private String drugRegimen;
	private String regimenChangeType;
	private String typeOfRegimen;
	private Date startDate;
	private Visit visit;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public DrugOrder getDrugOrder() {
		return drugOrder;
	}

	public void setDrugOrder(DrugOrder drugOrder) {
		this.drugOrder = drugOrder;
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

	public Boolean getProcessedStatus() {
		return processedStatus;
	}

	public void setProcessedStatus(Boolean processedStatus) {
		this.processedStatus = processedStatus;
	}

	public String getDose() {
		return dose;
	}

	public void setDose(String dose) {
		this.dose = dose;
	}
	
	public String getDoseRegimen() {
		return doseRegimen;
	}

	public void setDoseRegimen(String doseRegimen) {
		this.doseRegimen = doseRegimen;
	}

	public Integer getNoOfTablet() {
		return noOfTablet;
	}

	public void setNoOfTablet(Integer noOfTablet) {
		this.noOfTablet = noOfTablet;
	}

	public Concept getRoute() {
		return route;
	}

	public void setRoute(Concept route) {
		this.route = route;
	}

	public Integer getDurationPreProcess() {
		return durationPreProcess;
	}

	public void setDurationPreProcess(Integer durationPreProcess) {
		this.durationPreProcess = durationPreProcess;
	}

	public Date getDiscontinuedDate() {
		return discontinuedDate;
	}

	public void setDiscontinuedDate(Date discontinuedDate) {
		this.discontinuedDate = discontinuedDate;
	}
	
	public Concept getDiscontinuedReason() {
		return discontinuedReason;
	}

	public void setDiscontinuedReason(Concept discontinuedReason) {
		this.discontinuedReason = discontinuedReason;
	}

	public String getDiscontinuedReasonNonCoded() {
		return discontinuedReasonNonCoded;
	}

	public void setDiscontinuedReasonNonCoded(String discontinuedReasonNonCoded) {
		this.discontinuedReasonNonCoded = discontinuedReasonNonCoded;
	}

	public Integer getQuantityPostProcess() {
		return quantityPostProcess;
	}

	public void setQuantityPostProcess(Integer quantityPostProcess) {
		this.quantityPostProcess = quantityPostProcess;
	}

	public String getDrugRegimen() {
		return drugRegimen;
	}

	public void setDrugRegimen(String drugRegimen) {
		this.drugRegimen = drugRegimen;
	}

	public String getTypeOfRegimen() {
		return typeOfRegimen;
	}

	public void setTypeOfRegimen(String typeOfRegimen) {
		this.typeOfRegimen = typeOfRegimen;
	}

	public String getRegimenChangeType() {
		return regimenChangeType;
	}

	public void setRegimenChangeType(String regimenChangeType) {
		this.regimenChangeType = regimenChangeType;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Visit getVisit() {
		return visit;
	}

	public void setVisit(Visit visit) {
		this.visit = visit;
	}
}
