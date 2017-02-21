package org.openmrs.module.chaiemr.fragment.controller.field;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.openmrs.api.context.Context;
import org.openmrs.module.chaiemr.api.ChaiEmrService;
import org.openmrs.module.chaiemr.model.DrugInfo;
import org.openmrs.ui.framework.UiUtils;
import org.springframework.web.bind.annotation.RequestParam;

public class DrugInfoFragmentController {
	public JSONObject drugDetails(@RequestParam("drugNames") String drugCodes,
			UiUtils ui){
	JSONObject drugsInfoDetailsJson = new JSONObject();
	JSONArray drugsInfoDetailsJsonArray = new JSONArray();
	ChaiEmrService chaiEmrService = (ChaiEmrService) Context.getService(ChaiEmrService.class);
	   for (String drugCode: drugCodes.split("/")){
		   JSONObject drugInfoDetailsJson = new JSONObject();
		   DrugInfo drugInfo=chaiEmrService.getDrugInfo(drugCode);  
		   if(drugInfo!=null){
			   drugInfoDetailsJson.put("drugName", drugInfo.getDrugName());
			   drugInfoDetailsJson.put("toxicity", drugInfo.getToxicity());
			   drugInfoDetailsJson.put("riskFactor", drugInfo.getRiskFactor());
			   drugInfoDetailsJson.put("suggestedManagement", drugInfo.getSuggestedManagement());
			   drugInfoDetailsJson.put("drugInteraction", drugInfo.getDrugInteraction());
			   drugInfoDetailsJson.put("suggestedManagementInteraction", drugInfo.getSuggestedManagementInteraction());
				}
				else{
					drugInfoDetailsJson.put("drugName", "");
					drugInfoDetailsJson.put("toxicity", "");
					drugInfoDetailsJson.put("riskFactor", "");
					drugInfoDetailsJson.put("suggestedManagement", "");
					drugInfoDetailsJson.put("drugInteraction", "");
					drugInfoDetailsJson.put("suggestedManagementInteraction", "");
				}
		   drugsInfoDetailsJsonArray.add(drugInfoDetailsJson);
	   }
	   drugsInfoDetailsJson.put("drugsInfoDetailsJson", drugsInfoDetailsJsonArray);
	
	return drugsInfoDetailsJson;	
}

}
