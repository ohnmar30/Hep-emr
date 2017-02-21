<%
	ui.decorateWith("chaiemr", "standardPage", [ layout: "sidebar" ])

	def menuItems = []

	if (user) {
		if (user.retired) {
			menuItems << [ iconProvider: "chaiui", icon: "buttons/enable.png", label: "Enable login", onClick: "ke_onEnableUser(" + user.id + ")" ]
		} else {
			menuItems << [ iconProvider: "chaiui", icon: "buttons/disable.png", label: "Disable login", onClick: "ke_onDisableUser(" + user.id + ")" ]
		}
	}

	menuItems << [ iconProvider: "chaiui", icon: "buttons/back.png", label: "Back to accounts", href: ui.pageLink("chaiemr", "admin/manageAccounts") ]
%>

<script type="text/javascript">
	function ke_onEnableUser(userId) {
		chaiui.openConfirmDialog({
			heading: 'User',
			message: '${ ui.message("chaiemr.confirmReenableUser") }',
			okCallback: function() {
				ui.getFragmentActionAsJson('chaiemr', 'account/accountUtils', 'unretireUser', { userId: userId, reason: 'Admin UI' }, function() {
					ui.reloadPage();
				});
			}
		});
	}
	function ke_onDisableUser(userId) {
		chaiui.openConfirmDialog({
			heading: 'User',
			message: '${ ui.message("chaiemr.confirmDisableUser") }',
			okCallback: function() {
				ui.getFragmentActionAsJson('chaiemr', 'account/accountUtils', 'retireUser', { userId: userId, reason: 'Admin UI' }, function() {
					ui.reloadPage();
				});
			}
		});
	}
</script>

<div class="ke-page-sidebar">
	${ ui.includeFragment("chaiui", "widget/panelMenu", [ heading: "Account", items: menuItems ]) }
</div>

<div class="ke-page-content">
<% if (person) { %>
	${ ui.includeFragment("chaiemr", "account/editAccount") }
<% } else { %>
	${ ui.includeFragment("chaiemr", "account/newAccount") }
<% } %>
</div>