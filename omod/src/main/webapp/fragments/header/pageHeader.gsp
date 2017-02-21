<div class="ke-page-header">
	<table width="100%">
		<tr>
			<td>
				<div style="float: left; padding-right: 3px">
					<a href="/${ contextPath }/index.htm?<% if (config.context) { %>${ config.context }<% } %>">
						<!--<br />-->
						<!--<img src="${ ui.resourceLink("chaiemr", "images/logos/Red_ribbon.png") }" width="48" height="48" />-->
					</a>
				</div>
				<div style="float: centre; text-align: left; font-size: 13px; font-weight: bold; padding: 39px 57px 0 0; color: #7f7b72;">
					${ ui.message("chaiemr.image") }
				</div>
				<div style="float: left"></div>
			</td>
	<!-- 	<span style="font-size: 24px;">${ ui.message("chaiemr.title") }</span>
		<span style="font-size: 10px; color: #7f7b72;">${ moduleVersion }, powered by OpenMRS</span>
		<br/>
		<% if (systemLocation) { %>
			<span style="font-weight: bold; margin-left: 12px; border-top: 1px gray solid;">${ ui.format(systemLocation) }</span>
			<span style="font-size: 10px;">(${ systemLocationCode })</span>
		<% } %> -->
			<td>
				<div style="float: right; text-align: right">
					<img src="${ ui.resourceLink("chaiemr", "images/logos/MOH_logo.png") }" width="48" height="48" />
				</div>
				<div style="float: right; text-align: right; font-size: 13px; font-weight: bold; padding: 9px 5px 0 0; color: #7f7b72;">
					${ ui.message("chaiemr.subtitle") }
				</div>
				<div style="clear: both"></div>
			</td>
		</tr>
	</table>
	<table width="100%" >
		<tr>
			<td>
				<div class="ke-page-content">
					<% apps.eachWithIndex { app, i ->
						def onClick = "ui.navigate('/" + contextPath + "/" + app.url + (currentPatient ? ("?patientId=" + currentPatient.id) : "") + "')"
						def iconTokens = app.icon.split(":")
						def iconProvider, icon
						if (iconTokens.length == 2) {
							iconProvider = iconTokens[0]
							icon = "images/" + iconTokens[1]
						}
					%>
					<div style="float: left; margin: 5px;" >
						<%	if(currentApp ) { %>
							<% if(currentApp.label==app.label) {%>
							<button type="button" style="background-color: #afeeee;" class="ke-app" onclick="${ onClick }"><img src="${ ui.resourceLink(iconProvider, icon) }" />${ app.label }</button>
						<% }  else { %>
						<button type="button" class="ke-app" onclick="${ onClick }"><img src="${ ui.resourceLink(iconProvider, icon) }" />${ app.label }</button>
							<%} } else { %>
							<button type="button" class="ke-app" onclick="${ onClick }"><img src="${ ui.resourceLink(iconProvider, icon) }" />${ app.label }</button>
						<% }  %>
					</div>
					<% } %>
				</div>
			</td>
		</tr>
	</table>
</div>