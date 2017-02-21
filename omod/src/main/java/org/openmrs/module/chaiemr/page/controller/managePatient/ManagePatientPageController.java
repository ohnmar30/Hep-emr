package org.openmrs.module.chaiemr.page.controller.managePatient;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.module.chaiemr.EmrConstants;
import org.openmrs.module.chaiemr.fragment.controller.SearchFragmentController;
import org.openmrs.module.chaiui.annotation.AppPage;
import org.openmrs.ui.framework.UiUtils;
import org.openmrs.ui.framework.page.PageModel;

@AppPage(EmrConstants.APP_MANAGE_PATIENT)
public class ManagePatientPageController {
	
	protected static final Log log = LogFactory.getLog(ManagePatientPageController.class);
	
	public String controller(UiUtils ui,
            PageModel model) {

		return null;
	}


}
