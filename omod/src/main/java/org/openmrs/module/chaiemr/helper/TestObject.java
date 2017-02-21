package org.openmrs.module.chaiemr.helper;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.openmrs.Concept;
import org.openmrs.ConceptAnswer;
import org.openmrs.ConceptComplex;
import org.openmrs.ConceptNumeric;
import org.openmrs.ConceptSet;
import org.openmrs.Obs;
import org.openmrs.Patient;
import org.openmrs.api.context.Context;
import org.openmrs.module.chaiemr.EmrWebConstants;

public class TestObject {
	
	private static final String unitSeparator = ",";
	public Concept concept;
	// public Integer conceptId;
	public String name;
	public String units = "";
	public String range = "";
	// public String comment = "";
	public String handlerKey;
	public String resultFinding = ""; // finding
	public String resultImpression = ""; // impression
	private String result = "";
	public Obs obs;
	public boolean isRadioloy = false;
	private JSONObject json;
	private Patient patient;

	private static Log logger = LogFactory.getLog(TestObject.class);

	public TestObject(Obs obs, Patient patient) {
		this.patient = patient;
		this.obs = obs;
		this.concept = obs.getValueCoded();
		// this.conceptId = concept.getConceptId();
		if (isNumeric()) {
			ConceptNumeric cn = Context.getConceptService().getConceptNumeric(getConceptId());
			this.units = cn.getUnits();
			range = calculateRange(patient.getGender(), cn);
		} else if (concept.isComplex()) {
			ConceptComplex complex = (ConceptComplex) concept;
			this.handlerKey = complex.getHandler();
		}
		name = concept.getName().getName();
		Concept radiologyParentConcept = Context.getConceptService().getConceptByUuid(EmrWebConstants.RADIOLOGY_PARENT_CONCEPT_UUID);
		for (ConceptAnswer con : radiologyParentConcept.getAnswers()) {
			if (con.getAnswerConcept().getConceptId().equals(concept.getConceptId())) {
				isRadioloy = true;
				break;
			}
		}
	}

	public int getConceptId() {
		return this.concept.getConceptId();
	}

	public Concept getConcept() {
		return this.concept;
	}

	public boolean isDropdown() {
		return units == null ? false : units.contains(",");
	}

	public boolean isDropdown(String unit) {
		return unit.contains(",");
	}

	public String[] getDropDown(String unit) {
		return unit.split(unitSeparator);
	}

	public String[] getDropDown() {
		return units.split(unitSeparator);
	}

	public boolean isNumeric() {
		return concept.getDatatype().isNumeric();
	}

	public String getUnitInfo() {
		if (!isDropdown())
			return "";
		return getDropDown()[0] + ":" + range;
	}

	public String getUnitInfo(Concept concept) {
		ConceptNumeric nConcept = Context.getConceptService().getConceptNumeric(concept.getId());
		System.out.println("Here");
		String unit = nConcept.getUnits();
		if (!isDropdown(unit))
			return "";
		return getDropDown(unit)[0] + ":" + getReferenceRange(concept.getConceptId());
	}

	public boolean isLabSet() {
		boolean result = concept.getConceptClass().getName().toLowerCase().equals("labset");
		return result; 
	}
	
	public boolean isCoded() {
		System.out.println("Date type is :"+concept.getDatatype().getName().toLowerCase());
		boolean result = concept.getDatatype().getName().toLowerCase().equals("coded");
		return result; 
	}

	public String getUnit(int conceptId) {
		ConceptNumeric temp = Context.getConceptService().getConceptNumeric(conceptId);
		return temp == null ? "" : temp.getUnits();
	}

	public String getReferenceRange(int conceptId) {
		ConceptNumeric conceptNumberic = Context.getConceptService().getConceptNumeric(conceptId);
		return calculateRange(patient.getGender(), conceptNumberic);
	}

	public String getComment(int conceptId) {
		return "";
	}
	
	public boolean isWBCGroup(Concept concept){
		
		return false;
	}

	public List<Concept> getAnswerConcept() {
		List<Concept> result = new ArrayList<Concept>();
		for (ConceptSet conceptSet : concept.getConceptSets()) {
			result.add(conceptSet.getConcept());
		}
		return result;
	}

	public String get(String key) {
		JSONObject json = new JSONObject();
		try {
			JSONParser parser = new JSONParser();
			json = (JSONObject) parser.parse(result);
		} catch (Exception e) {
			return "";
		}
		String value = (String) json.get(key);
		return value == null || value.trim().equals("null") ? "" : value;
	}

	public String get(int key) {
		return get(String.valueOf(key));
	}

	public String getJson() {
		return new JSONObject().toJSONString();
	}

	public void setResult(Obs obs) {
		if (obs == null){
			this.result = "";
			return;
		}
		this.result = obs.getValueText();
	}

	public void setResult(Set<Obs> listResultObs) {
		for (Obs resultObs : listResultObs) {
			if (resultObs.getConcept().getConceptId().equals(concept.getConceptId())) {
				json = getJsonValue(resultObs);
				if (isRadioloy) {
					String text = resultObs.getValueText();
					if (text != null) {
						String[] arr = StringUtils.split(text, '|');
						if (arr != null && arr.length == 2) {
							resultFinding = arr[0];
							resultImpression = arr[1];
						}
					}
				} else {
					if (resultObs.getValueText() != null) {
						result = resultObs.getValueText();
					} else {
						result = "";
					}
				}
				break;
			}
		}
	}

	private JSONObject getJsonValue(Obs obs) {
		JSONParser parser = new JSONParser();
		try {
			return (JSONObject) parser.parse(obs.getValueText());
		} catch (Exception e) {
			return new JSONObject();
		}
	}

	public String calculateRange(String gender, ConceptNumeric concept) {
		if (concept == null)
			return " - ";
		System.out.println("here");
		if (gender.equalsIgnoreCase("male")) {
			return "" + concept.getLowCritical() + "-" + concept.getHiCritical();
		} else {
			return "" + concept.getLowNormal() + "-" + concept.getHiNormal();
		}

	}

	public String isSelect(String key, String type) {
		String value = get(key);
		return value != null && value.equals(type) ? "selected" : "";
	}

	public String getResult() {
		System.out.println("Result :"+result);
		return result == null ? "" : result;
	}
}
