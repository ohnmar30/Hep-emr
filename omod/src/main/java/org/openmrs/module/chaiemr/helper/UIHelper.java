package org.openmrs.module.chaiemr.helper;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.Concept;
import org.openmrs.Obs;
import org.openmrs.Patient;

public class UIHelper {

	public static final int CXR = 12;

	public static final int sputumSmearMicroscopy = 307;
	private Patient mPatient;

	private static Log logger = LogFactory.getLog(UIHelper.class);

	private List<TestObject> mNormalTestObjectList;

	public UIHelper(Patient patient) {
		mNormalTestObjectList = new ArrayList<TestObject>();
		// mTableTestObjectList = new ArrayList<TestObject>();
		mPatient = patient;
	}

	public TestObject addToForm(Obs obs) {
		TestObject testObject = new TestObject(obs, mPatient);
		if (obs.getValueCoded().getId() == CXR || obs.getValueCoded().getId() == sputumSmearMicroscopy)
			return testObject;
		mNormalTestObjectList.add(testObject);
		return testObject;
	}
	
	private boolean isMultiUnit(Obs obs){
		Concept concept = obs.getValueCoded();
		return false;
	}

	public static boolean isSpecial(TestObject testObject) {
		if (testObject.concept.getConceptId() == CXR) {
			return true;
		}
		if (testObject.concept.getConceptId() == sputumSmearMicroscopy) {
			return true;
		}
		return false;
	}

	public static String getName(TestObject testObject) {
		// String name = testObject.concept.getName().getName();
		//
		// return name.replaceAll(" ", "");
		if (testObject.concept.getConceptId() == CXR)
			return "cxr";
		if (testObject.concept.getConceptId() == sputumSmearMicroscopy)
			return "sputumSmearMicroscopy";
		return "";
	}

	public List<TestObject> getNormalTestResult() {
		return mNormalTestObjectList;
	}

	// public List<TestObject> getTableElements() {
	// return mTableTestObjectList;
	// }
}
