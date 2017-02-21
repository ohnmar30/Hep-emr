ChaiEMR Distribution
=====================================
<a href="http://ci.chaiemr.org/viewType.html?buildTypeId=bt2"><img src="http://ci.chaiemr.org/app/rest/builds/buildType:bt2/statusIcon"/></a>

Overview
--------
Initial I-TECH work on an OpenMRS-based EMR for the Myanmar MOH.

Project homepage: https://sites.google.com/site/chaihealthinformatics/

Developer documentation: https://wiki.openmrs.org/display/projects/ChaiEMR+Distribution

Installation
------------
To create a distribution zip of all required modules, build as follows:

	mvn clean package -DbuildDistro

This can then be extracted into your OpenMRS modules repository directory, e.g.

	MODULE_DIR=/usr/share/tomcat6/.OpenMRS/modules/
	rm $MODULE_DIR/*.omod
	unzip -oj distro/target/chaiemr-13.3-distro.zip -d $MODULE_DIR

Accreditation
-------------
* Highcharts graphing library by Highsoft used under Creative Commons Licence 3.0 (http://www.highcharts.com/)
* Pretty Office Icons used with permission from CustomIconDesign (http://www.customicondesign.com)