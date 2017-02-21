package org.openmrs.module.chaiemr.fragment.controller.patient;


import org.openmrs.Patient;
import org.openmrs.module.chaiui.ChaiUiUtils;
import org.openmrs.ui.framework.UiUtils;
import org.openmrs.ui.framework.annotation.FragmentParam;
import org.openmrs.ui.framework.annotation.SpringBean;
import org.openmrs.ui.framework.fragment.FragmentModel;
import org.openmrs.ui.framework.page.PageRequest;

public class PatientWhiteCardFragmentController {
	public void controller(@FragmentParam("patient") Patient patient,
			   @SpringBean ChaiUiUtils chaiUi,
			   PageRequest pageRequest,
			   UiUtils ui,
			   FragmentModel model) {

model.addAttribute("patient", patient);
}

}
