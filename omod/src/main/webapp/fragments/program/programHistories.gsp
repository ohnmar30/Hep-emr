<% programs.each { descriptor -> %>
${ ui.includeFragment("chaiemr", "program/programHistory", [ patient: patient, program: descriptor.target, showClinicalData: showClinicalData ]) }
<% } %>