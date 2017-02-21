package org.openmrs.module.chaiemr.fragment.controller.intake;

import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpSession;

import org.apache.commons.lang.math.NumberUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.Concept;
import org.openmrs.Encounter;
import org.openmrs.EncounterType;
import org.openmrs.Obs;
import org.openmrs.Visit;
import org.openmrs.api.EncounterService;
import org.openmrs.api.context.Context;
import org.openmrs.module.chaicore.form.FormManager;
import org.openmrs.module.chaiemr.helper.TestObject;
import org.openmrs.module.chaiemr.helper.UIHelper;
import org.openmrs.module.chaiui.ChaiUiUtils;
import org.openmrs.ui.framework.SimpleObject;
import org.openmrs.ui.framework.UiUtils;
import org.openmrs.ui.framework.annotation.SpringBean;
import org.openmrs.ui.framework.fragment.FragmentActionRequest;
import org.openmrs.ui.framework.page.PageModel;
import org.openmrs.ui.framework.resource.ResourceFactory;
import org.springframework.web.bind.annotation.RequestParam;

public class EnterLabResultFragmentController {

	private static Log logger = LogFactory.getLog(EnterLabResultFragmentController.class);

	public void controller(@RequestParam(required = false, value = "encounterId") Encounter encounter, @RequestParam(required = false, value = "returnUrl") String returnUrl, UiUtils ui, PageModel model) {
		Set<Obs> listResultObs = null;
		Encounter resultEncounter = null;
		Visit visit = encounter.getVisit();
		if (visit != null) {
			EncounterType encType = Context.getEncounterService().getEncounterType("Lab Results");
			List<Encounter> encs = Context.getEncounterService().getEncounters(visit.getPatient(), null, null, null, null, Collections.singleton(encType), null, null, Collections.singleton(visit), true);
			if (encs != null && !encs.isEmpty()) {
				resultEncounter = encs.get(encs.size() - 1);
			}

			if (resultEncounter != null) {
				listResultObs = resultEncounter.getAllObs();
			}
		}
		model.addAttribute("resultEncounter", resultEncounter);
		model.addAttribute("listResultObs", listResultObs);
		model.addAttribute("visit", visit);
		model.addAttribute("returnUrl", returnUrl);

		// List<TestObject> listTests = new ArrayList<TestObject>();
		// List<TestObject> tableElements = new ArrayList<TestObject>();
		Map<String, Obs> obsMap = toObsMap(listResultObs);
		UIHelper uiHelper = new UIHelper(visit.getPatient());
		model.addAttribute("cxr", false);
		model.addAttribute("sputumSmearMicroscopy", false);
		model.addAttribute("mgit", false);
		model.addAttribute("lj",false);
		model.addAttribute("lpa",false);
		model.addAttribute("dst",false);
		if (encounter != null) {
			for (Obs obs : encounter.getAllObs()) {
				if (obs.getValueCoded() == null)
					continue;
				TestObject testObject = uiHelper.addToForm(obs);
				if (UIHelper.isSpecial(testObject)) {
					model.addAttribute(UIHelper.getName(testObject), testObject);
				}
				testObject.setResult(obsMap.get(testObject.concept.getUuid()));
			}
		}
		model.addAttribute("listTests", uiHelper.getNormalTestResult());
		model.addAttribute("confirmed", resultEncounter != null && resultEncounter.isVoided() ? true : false);
		model.addAttribute("patientGender", encounter.getPatient().getGender());
		model.addAttribute("patientAge", encounter.getPatient().getAge());
	}

	public SimpleObject submit(@RequestParam(value = "visitId", required = false) Visit visit, @RequestParam(value = "conceptIds", required = true) String[] conceptIds, @RequestParam(value = "encounterId", required = false) Encounter encounter, @RequestParam(value = "confirmed", required = false) Boolean confirm, @SpringBean ResourceFactory resourceFactory, @SpringBean ChaiUiUtils chaiUi, @SpringBean FormManager formManager, UiUtils ui, HttpSession session,
			FragmentActionRequest actionRequest) throws Exception {
		EncounterService encService = Context.getService(EncounterService.class);

		Date curDate = new Date();

		if (encounter != null) {
			// Set<Obs> listObs = resultEncounter.getAllObs();
			Map<String, Obs> obsMap = toObsMap(encounter.getAllObs());
			boolean changed = false;
			for (String submittedConceptId : conceptIds) {
				String value = getInputValue(actionRequest, submittedConceptId);
				Concept concept = Context.getConceptService().getConcept(NumberUtils.toInt(submittedConceptId));
				Obs obs = obsMap.get(concept.getUuid());
				if (obs != null) {
					// value has been changed
					obs.setDateChanged(curDate);
					changed = true;
				} else {
					// save new Obs
					obs = new Obs();
					obs.setConcept(concept);
					obs.setEncounter(encounter);
					obs.setDateCreated(curDate);
					encounter.addObs(obs);
					changed = true;
				}
				updateObsValue(obs, value);
			} // end loop for conceptIds
			if (changed) {
				if (confirm)
					confirmEncounter(encounter);
				encounter.setEncounterDatetime(encounter.getEncounterDatetime());
				encounter.setChangedBy(Context.getUserContext().getAuthenticatedUser());
				Context.getEncounterService().saveEncounter(encounter);
			}
		} else {
			// save new
			encounter = new Encounter();
			encounter.setDateCreated(visit.getDateCreated());
			encounter.setEncounterType(encService.getEncounterType("Lab Results"));
			encounter.setVisit(visit);
			encounter.setPatient(visit.getPatient());
			encounter.setEncounterDatetime(visit.getStartDatetime());
			if (confirm) 
				confirmEncounter(encounter);

			for (String conceptId : conceptIds) {
				String value = actionRequest.getParameter(conceptId + "_value");
				Concept concept = Context.getConceptService().getConcept(conceptId);
				Obs obs = new Obs();
				obs.setConcept(concept);
				obs.setEncounter(encounter);
				obs.setDateCreated(curDate);
				obs.setValueText(value);
				encounter.addObs(obs);
			}
			encounter.setChangedBy(Context.getUserContext().getAuthenticatedUser());
			encService.saveEncounter(encounter);
		}

		return SimpleObject.create("success", true);
	}
	
	private void confirmEncounter(Encounter encounter){
		encounter.setVoided(true);
		encounter.setVoidReason("Result Confirmed");
		encounter.setVoidedBy(Context.getAuthenticatedUser());
		encounter.setDateChanged(new Date());
	}

	private void updateObsValue(Obs obs, String value) {
		if(obs ==null || obs.getConcept()==null)
			return;
		Concept concept = obs.getConcept();
		if (value != null && value != "") {
			obs.setValueText(value);
			if (concept.getConceptId() == 856 || concept.getConceptId() == 5497 || concept.getConceptId() == 730) {
				obs.setValueNumeric(Double.parseDouble(value));
			}
		} else {
			obs.setValueText("");
		}
	}

	private Map<String, Obs> toObsMap(Set<Obs> obsSet) {
		Map<String, Obs> obsMap = new HashMap<String, Obs>();
		if(obsSet==null || obsSet.isEmpty())
			return obsMap;
		for (Obs obs : obsSet) {
			obsMap.put(obs.getConcept().getUuid(), obs);
		}
		return obsMap;
	}

	private String getInputValue(FragmentActionRequest actionRequest, String submittedConceptId) {
		return actionRequest.getParameter(submittedConceptId + "_value");
	}

}
