<% dataPoints.each { dataPoint -> %>
${ ui.includeFragment("chaiui", "widget/dataPoint", [ label: dataPoint.key, value: dataPoint.value, showDateInterval: true ]) }
<% } %>