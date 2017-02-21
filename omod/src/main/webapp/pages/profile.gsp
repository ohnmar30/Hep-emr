<%
	ui.decorateWith("chaiemr", "standardPage", [ layout: "sidebar" ])
%>
<script type="text/javascript">
	function showChangePasswordDialog() {
		chaiui.openPanelDialog({ templateId: 'change_password_form' });
	}
	function showSecretQuestionDialog() {
		chaiui.openPanelDialog({ templateId: 'change_secret_question_form' });
	}

	<% if (tempPassword) { %>
	jq(function() {
		showChangePasswordDialog();
	});
	<% } %>
</script>

<div class="ke-page-sidebar">
	${ ui.includeFragment("chaiui", "widget/panelMenu", [
			heading: "My Profile",
			items: [
					[ label: "Change Password", iconProvider: "chaiui", icon: "buttons/profile_password.png", onClick: "showChangePasswordDialog()" ],
					[ label: "Change Secret Question", iconProvider: "chaiui", icon: "buttons/profile_secret_question.png", onClick: "showSecretQuestionDialog()" ]
			]
	]) }
</div>

<div class="ke-page-content">
	${ ui.includeFragment("chaiemr", "account/personDetails", [ person: person ]) }

	${ ui.includeFragment("chaiemr", "profileLoginDetails", [ tempPassword: tempPassword ]) }
</div>