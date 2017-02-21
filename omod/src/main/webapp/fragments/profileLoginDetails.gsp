<%
	ui.decorateWith("chaiui", "panel", [ heading: "Login Settings" ])

	def checkCurrentPassword = !config.tempPassword;

	def changePasswordProps = checkCurrentPassword ? [ "oldPassword", "newPassword", "confirmNewPassword" ] : [ "newPassword", "confirmNewPassword" ];
	def changePasswordHiddenProps = checkCurrentPassword ? [] : [ "oldPassword" ]
%>

${ ui.includeFragment("chaiui", "widget/dataPoint", [ label: "Username", value: user.username ]) }
${ ui.includeFragment("chaiui", "widget/dataPoint", [ label: "Secret question", value: user.secretQuestion ]) }

${ ui.includeFragment("chaiui", "widget/dialogForm", [
		id: "change_password",
		dialogConfig: [ heading: "Change Password" ],
		fragmentProvider: "chaiemr",
		fragment: "profileLoginDetails",
		action: "changePassword",
		prefix: "changePasswordForm",
		commandObject: changePasswordForm,
		properties: changePasswordProps,
		propConfig: [
				oldPassword: [ type: "password" ],
				newPassword: [ type: "password" ],
				confirmNewPassword: [ type: "password" ]
		],
		hiddenProperties: changePasswordHiddenProps,
		submitLabel: "Update",
		cancelLabel: !config.tempPassword ? "Cancel" : null,
		onSuccessCallback:  "ui.reloadPage();"
]) }

${ ui.includeFragment("chaiui", "widget/dialogForm", [
		id: "change_secret_question",
		dialogConfig: [ heading: "Change Secret Question" ],
		fragmentProvider: "chaiemr",
		fragment: "profileLoginDetails",
		action: "changeSecretQuestion",
		prefix: "changeSecretQuestionForm",
		commandObject: changeSecretQuestionForm,
		properties: [ "currentPassword", "secretQuestion", "newSecretAnswer" ],
		propConfig: [
				currentPassword: [ type: "password" ]
		],
		submitLabel: "Update",
		cancelLabel: "Cancel",
		onSuccessCallback: "ui.reloadPage();"
]) }