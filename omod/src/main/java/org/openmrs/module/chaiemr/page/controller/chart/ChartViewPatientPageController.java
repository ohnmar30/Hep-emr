/**
 * The contents of this file are subject to the OpenMRS Public License
 * Version 1.0 (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 * http://license.openmrs.org
 *
 * Software distributed under the License is distributed on an "AS IS"
 * basis, WITHOUT WARRANTY OF ANY KIND, either express or implied. See the
 * License for the specific language governing rights and limitations
 * under the License.
 *
 * Copyright (C) OpenMRS, LLC.  All Rights Reserved.
 */

package org.openmrs.module.chaiemr.page.controller.chart;

import org.apache.commons.lang3.StringUtils;
import org.openmrs.Encounter;
import org.openmrs.Form;
import org.openmrs.Patient;
import org.openmrs.PatientProgram;
import org.openmrs.Program;
import org.openmrs.Visit;
import org.openmrs.api.context.Context;
import org.openmrs.module.appframework.domain.AppDescriptor;
import org.openmrs.module.chaicore.form.FormDescriptor;
import org.openmrs.module.chaicore.form.FormManager;
import org.openmrs.module.chaicore.program.ProgramDescriptor;
import org.openmrs.module.chaicore.program.ProgramManager;
import org.openmrs.module.chaiemr.EmrConstants;
import org.openmrs.module.chaiemr.EmrWebConstants;
import org.openmrs.module.chaiemr.api.ChaiEmrService;
import org.openmrs.module.chaiui.ChaiUiUtils;
import org.openmrs.module.chaiui.annotation.AppPage;
import org.openmrs.ui.framework.SimpleObject;
import org.openmrs.ui.framework.UiUtils;
import org.openmrs.ui.framework.annotation.SpringBean;
import org.openmrs.ui.framework.page.PageModel;
import org.openmrs.ui.framework.page.PageRequest;
import org.openmrs.ui.framework.session.Session;
import org.openmrs.util.OpenmrsUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestParam;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Viewing a patient's record, in the chart app
 */
@AppPage(EmrConstants.APP_CHART)
public class ChartViewPatientPageController {

	public void controller(
			@RequestParam(required = false, value = "visitId") Visit visit,
			@RequestParam(required = false, value = "formUuid") String formUuid,
			@RequestParam(required = false, value = "programId") Program program,
			@RequestParam(required = false, value = "section") String section,
			@RequestParam(required = false, value = "startDate") String startDate,
			@RequestParam(required = false, value = "endDate") String endDate,
			PageModel model, UiUtils ui, Session session,
			PageRequest pageRequest, @SpringBean ChaiUiUtils chaiUi,
			@SpringBean FormManager formManager,
			@SpringBean ProgramManager programManager) {

		if ("".equals(formUuid)) {
			formUuid = null;
		}

		Patient patient = (Patient) model
				.getAttribute(EmrWebConstants.MODEL_ATTR_CURRENT_PATIENT);
		recentlyViewed(patient, session);

		AppDescriptor thisApp = chaiUi.getCurrentApp(pageRequest);

		List<FormDescriptor> oneTimeFormDescriptors = formManager
				.getCommonFormsForPatient(thisApp, patient);
		List<SimpleObject> oneTimeForms = new ArrayList<SimpleObject>();
		for (FormDescriptor formDescriptor : oneTimeFormDescriptors) {
			Form form = formDescriptor.getTarget();
			oneTimeForms.add(ui.simplifyObject(form));
		}
		model.addAttribute("oneTimeForms", oneTimeForms);

		Collection<ProgramDescriptor> progams = programManager
				.getPatientPrograms(patient);

		model.addAttribute("programs", progams);
		model.addAttribute("programSummaries",
				programSummaries(patient, progams, programManager, chaiUi));

		/**
		 * For filtered visit
		 * */
		List<Visit> filterVisitList = null;
		if (endDate != "" && startDate != "") {
			Date sd = null, ed = null;
			DateFormat formatter;
			formatter = new SimpleDateFormat("dd-MMM-yyyy");
			try {
				sd = (Date) formatter.parse(startDate);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try {
				ed = (Date) formatter.parse(endDate);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			Date startOfDay = OpenmrsUtil.firstSecondOfDay(sd);
			Date endOfDay = OpenmrsUtil.getLastMomentOfDay(ed);

			model.addAttribute(
					"visits",
					Context.getVisitService().getVisits(null,
							Collections.singleton(patient), null, null, null,
							endOfDay, startOfDay, null, null, true, false));
			model.addAttribute(
					"visitsCount",
					Context.getVisitService()
							.getVisits(null, Collections.singleton(patient),
									null, null, null, endOfDay, startOfDay,
									null, null, true, false).size());

			SimpleDateFormat dateformat = new SimpleDateFormat("dd-MMM-yyyy");
			String sdate_to_string = dateformat.format(sd);
			String edate_to_string = dateformat.format(ed);
			model.addAttribute("period", sdate_to_string + " to "
					+ edate_to_string);
			model.addAttribute("filter", "filterVisit");
		} else {
			model.addAttribute("visits", Context.getVisitService()
					.getVisitsByPatient(patient));
			model.addAttribute("visitsCount", Context.getVisitService()
					.getVisitsByPatient(patient).size());
			model.addAttribute("period", "All visit");
			model.addAttribute("filter", "");
		}

		Form form = null;
		String selection = null;
		if (visit != null) {
			selection = "visit-" + visit.getVisitId();
		} else if (formUuid != null) {
			selection = "form-" + formUuid;
			form = Context.getFormService().getFormByUuid(formUuid);
			List<Encounter> encounters = Context.getEncounterService()
					.getEncounters(patient, null, null, null,
							Collections.singleton(form), null, null, null,
							null, false);
			Encounter encounter = encounters.size() > 0 ? encounters.get(0)
					: null;
			model.addAttribute("encounter", encounter);
		} else if (program != null) {
			selection = "program-" + program.getProgramId();
		} else {
			if (StringUtils.isEmpty(section)) {
				section = "overview";
			}
			selection = "section-" + section;
		}

		model.addAttribute("form", form);
		model.addAttribute("visit", visit);
		model.addAttribute("program", program);
		model.addAttribute("section", section);
		model.addAttribute("selection", selection);
		model.addAttribute("endDate", endDate);
		model.addAttribute("startDate", startDate);
	}

	/**
	 * Adds this patient to the user's recently viewed list
	 * 
	 * @param patient
	 *            the patient
	 * @param session
	 *            the session
	 */
	private void recentlyViewed(Patient patient, Session session) {
		String attrName = EmrConstants.APP_CHART + ".recentlyViewedPatients";

		LinkedList<Integer> recent = session.getAttribute(attrName,
				LinkedList.class);
		if (recent == null) {
			recent = new LinkedList<Integer>();
			session.setAttribute(attrName, recent);
		}
		recent.removeFirstOccurrence(patient.getPatientId());
		recent.add(0, patient.getPatientId());
		while (recent.size() > 10)
			recent.removeLast();
	}

	/**
	 * Creates a one line summary for each program
	 * 
	 * @return the map of programs to summaries
	 */
	private Map<Program, String> programSummaries(Patient patient,
			Collection<ProgramDescriptor> programs,
			ProgramManager programManager, ChaiUiUtils chaiUi) {
		Map<Program, String> summaries = new HashMap<Program, String>();

		for (ProgramDescriptor descriptor : programs) {
			Program program = descriptor.getTarget();
			List<PatientProgram> allEnrollments = programManager
					.getPatientEnrollments(patient, program);
			PatientProgram lastEnrollment = allEnrollments.get(allEnrollments
					.size() - 1);
			String summary = lastEnrollment.getActive() ? "Enrolled on "
					+ chaiUi.formatDate(lastEnrollment.getDateEnrolled())
					: "Completed on "
							+ chaiUi.formatDate(lastEnrollment
									.getDateCompleted());

			summaries.put(descriptor.getTarget(), summary);
		}

		return summaries;
	}
}