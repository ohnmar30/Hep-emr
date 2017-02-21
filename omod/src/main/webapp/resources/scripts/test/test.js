function validationSecondPhase() {
	return true;
}

function extraValidation() {}

function extraValidation() {}

function Common() {}

Common.format = function (number){
	return number ? number.toFixed(2) : "";
}

function loadingUserInteraction() {
	// alertify.alert("loading");
	var originalSubmit = jq("#tb-screening-submit");
	var alphaSubmit = jq("#tb-screening-submit-alpha");
	var submitButton = jq("input", originalSubmit);

	var alphaSubmit = jq("#tb-screening-save");
	var alphaCancel = jq("#tb-screeninging-cancel");
	alphaSubmit.attr("value", submitButton.attr("value"));
	alphaCancel.on("click", function() {
		var cancelButton = jq("button", originalSubmit).click();
	});
	// jq("#tbSufferedPatientNo tr[data-cal]").removeClass("hide");
	if (jq("#tb-screening-submit").text().trim() != "") {
		jq("#tb-screening-submit-alpha").removeClass("hide");
	}
	alphaSubmit.on("click",submitFun);

	function send() {
		console.log(jq("#tbSufferedPatientNo tr[data-cal]"));
		submitButton.click();
	}

	function confirmFun() {
		jq("#tbSufferedPatientNo tr[name=not-done] input").prop("checked", true);
		send();
	}

	function cancelFun() {
		return;
	}

	function submitFun() {
		var tbSuffer = getValue("tbSufferedPatient.value");
		$("#tbSufferedPatientNo tr[name=presumptive-tb] input").prop("checked",
				false);
		$("#tbSufferedPatientNo tr[name=not-done] input")
				.prop("checked", false);
		console.log($("#tbSufferedPatient"));
		if (tbSuffer == 1066) {
			if (jq("#tbSufferedPatientNo tr[data-cal=yes] input:checked").length <= 0) {
				alertify.confirm("Confirm", "Are you sure you want to .... ?",
						confirmFun, cancelFun);
			} else {
				$("#tbSufferedPatientNo tr[name=presumptive-tb] input").prop(
						"checked", true);
				send();
			}
		} else {
			send();
		}

	}
}

function noSignTbChecked(selectedElement) {
	if (!selectedElement.checked) {
		jq("#tbSufferedPatientNo tr[data-cal=yes] input").removeAttr(
				"disabled", "disabled");
		return;
	}
	var tbScreening = jq("#tbSufferedPatientNo tr");
	jq("#tbSufferedPatientNo tr[name=no-sign-of-tb] input").prop("checked",
			false);
	for (var i = 0; i < tbScreening.length; i++) {
		element = jq(tbScreening[i]);
		if (element.attr("data-cal") != "yes")
			continue;
		element = jq("input:checked", element);
		if (element.length > 0) {
			alertify.alert("Please unselected above .");
			jq("#tbSufferedPatientNo tr[name=no-sign-of-tb] input").prop(
					"checked", false);
			return;
		}
	}
	jq("#tbSufferedPatientNo tr[data-cal=yes] input").attr("disabled",
			"disabled");
	jq(selectedElement).removeAttr("disabled");
	jq("#tbSufferedPatientNo tr[name=no-sign-of-tb] input").prop("checked",
			true);
	// });

	function getTr(tElement) {
		var temp = tElement.parent().parent();
		console.log("Parent tr element", temp);
		return tElement.parent().parent();
	}

}

function LabOrder() {
}

LabOrder.checkAll = function(element) {
	var defaultCheckBox = $("#labOrderPrintArea [name=laborder-default-test] input[type=checkbox]");
	defaultCheckBox.prop("checked", $(element).prop("checked"));
}

LabOrder.validate = function() {
	fieldIndex = 10;
	j = 1;
	/*
	 * while(document.getElementById("w"+fieldIndex).value!="" && j < 26){
	 * console.log(document.getElementById("w"+fieldIndex)); var id =
	 * document.getElementById("w"+fieldIndex+"_hid").value if(id=="ERROR"){
	 * alert("Kindly enter data in correct format"); return false; } j++;
	 * fieldIndex=fieldIndex+2; }
	 */
	return true;
}

function EnterLabResult() {
}

EnterLabResult.validate = function(id) {
	var labResult = $("#enterLabResultForm");
	getLiverFunctionTest();
	console.log("Success");
	var table = $("#enterLabResultForm");
	$("[name=record]", table).each(function(index, element) {
		convertToJson(index, element)
	});

	var unitCollection = EnterLabResult.initUnit();
	$("[name=unit]", labResult).each(
			function(index, element) {
				element = $(element);
				if ($("select", element).length == 0)
					return;
				var select = $("select", element);
				var originUnit = select.val().trim();
				var targetUnit = element.attr("data-info").split(":")[0];
				var tr = getParent("tr", element);
				var target = $("[name=" + tr.attr("name") + "] input", tr);
				target.val(unitCollection[originUnit](target.val(), targetUnit,
						$("select", tr).attr("name")));
				//target.val(unitCollection[originUnit][targetUnit].convertor(target.val()));
			});
	$("#" + id).click();

	function getLiverFunctionTest(labResult) {
		var liver = {};
		var liverFunction = jq("[name=liver-function-test]", labResult);
		if (liverFunction.length <= 0)
			return;
		liver.serumBilirubin = jq("[name=liver-function-serum-bilirubin]",
				liverFunction).val();
		liver.alp = jq("[name=liver-function-alp]", liverFunction).val();
		liver.alt = jq("[name=liver-function-alt]", liverFunction).val();
		liver.ast = jq("[name=liver-function-ast]", liverFunction).val();
		jq("input[type=hidden]", liverFunction).val(JSON.stringify(liver));
	}

	function convertToJson(index, element) {
		var data = {};
		element = $(element);
		var key = element.attr("value");
		$("[name=" + key + "]", table).each(function(innerIndex, innerElement) {
					innerElement = $(innerElement);
					data[innerElement.attr("value")] = $("[name=value]",innerElement).val();
		});
		$("input[name=" + key + "_value]", element).val(JSON.stringify(data));
	}
}

EnterLabResult.confirmResult = function() {
	alertify.confirm("Confirm",	"You can't edit the Lab Result once it is confirmed. Are you sure?",confirmFun, cancelFun);
	function confirmFun() {
		$("#confirm").val("true");
		$("#enterLabResultForm").submit();
	}
	function cancelFun() {
		return;
	}
}

EnterLabResult.updateDropDown = function(element, id) {
	element = $(element);
	$("#" + id + "_value").val(element.val());
}

EnterLabResult.initDropDown = function(id) {
	var input = $("#" + id + "_value");
	var parent = input.parent();
	var select = $("select", parent);
	select.val(input.val());
}

EnterLabResult.preValue = function(element) {
	element = $(element);
	element.attr("data-pre", element.val());
}

EnterLabResult.convertUnit = function(element) {
	element = $(element);
	var unitCollection = EnterLabResult.initUnit();
	var td = getParent("td", element);
	var data = td.attr("data-info").split(":");
	var originUnit = data[0];
	var originRange = data[1];
	var tr = getParent("tr", td);
	var reference = $("[name=range]", tr);
	if (originUnit == element.val()) {
		reference.text(originRange);
	} else {
		reference.text(convert(originUnit, element.val().trim(), tr.attr("name")), element.attr("name"));
	}
	var target = $("[name=" + tr.attr("name") + "] input", tr);
	target.val(unitCollection[element.attr("data-pre").trim()](target.val(), element.val().trim(), tr.attr("name")));
	target.blur();

	function convert(originUnit, targetUnit, targetTest) {
		var range = reference.text().split("-");
		var parentUnit = unitCollection[originUnit];
		var convertor = unitCollection[originUnit];
		return convertor(range[0], targetUnit, targetTest) + "-"+ convertor(range[1], targetUnit, targetTest);
	}
}

EnterLabResult.initUnit = function() {
	var unitCollection = {};
	unitCollection["umol/L"] = function(amount, unit, targetTest) {
		var multiplier = {};
		switch (unit) {
		case "mg/dl":
			multiplier = {
				"159825" : 1 / 59.48,
				"790" : 1 / 88.4,
				"655" : 1 / 17.1
			};
			break;
		case "mg/dL":
			multiplier = {
				"159825" : 1 / 59.48,
				"790" : 1 / 88.4
			}
			break;
		default:
			return format(parseFloat(amount));
		}
		return format(parseFloat(amount) *   multiplier[targetTest]);
	}

	unitCollection["mg/dl"] = function(amount, unit, targetTest) {
		switch (unit) {
		case "umol/L":
			var multiplier = {
				"875" : 0.357,
				"1006" : 0.0259,
				"1009" : 0.0259,
				"1007" : 0.0259,
				"1008" : 0.0259,
				"887" : 0.0555,
				"160912" : 0.0555,
				"1012" : 0.111,
				"790" : 88.4,
				"655" : 17.1,
				"159825" : 59.48
			};
			return format(parseFloat(amount) * multiplier[targetTest]);
		default:
			return format(amount);
		}
	}

	unitCollection["10^12/L"] = function(amount, unit, targetTest) {
		switch (unit) {
		case "10^6/uL":
			var multiplier = {
				"679" : 1
			}
			return format(parseFloat(amount) * multiplier[targetTest]);
		}
	}

	unitCollection["10^6/uL"] = function(amount, unit, targetTest) {
		switch (unit) {
		case "10^12/L":
			var multiplier = {
				"679" : 1
			}
			return format(parseFloat(amount) * multiplier[targetTest]);
		}
	}

	unitCollection["10^3/uL"] = function(amount, unit, targetTest) {
		switch (unit) {
		case "10^9/L":
			var multiplier = {
				"679" : 1,
				"729" : 1,
				"678" : 1
			}
			return format(parseFloat(amount) * multiplier[targetTest]);
		default:
			return format(parseFloat(amount));
		}
	}

	unitCollection["10^9/L"] = function(amount, unit) {
		switch (unit) {
		case "10^3/uL":
			var multiplier = {
				"679" : 1,
				"729" : 1,
				"678" : 1
			}
			return format(parseFloat(amount));
		default:
			return amount;
		}
	}

	unitCollection["g/dL"] = function(amount, unit, targetTest) {
		switch (unit) {
		case "mmol/L":
			var multiplier = {
				"1017" : 0.15135
			};
			return format(parseFloat(amount) * multiplier[targetTest]);
		default:
			return amount;
		}
	}

	unitCollection["mg/dL"] = function(amount, unit, targetTest) {
		switch (unit) {
		case "mmol/L":
			var multipler = {
				"159825" : 59.48,
				"1006" : 0.0259,
				"1009" : 0.0259,
				"1007" : 0.0259,
				"1008" : 0.0259,
				"887" : 0.0555,
				"160912" : 0.0555,
				"1012" : 0.111,
				"875" : 0.357
			};
			return format(parseFloat(amount) * multipler[targetTest]);
		case "mEq/L":
			return format(amount);
		case "umol/L":
			var multipler = {
				"159825" : 59.48,
				"790" : 88.4
			};
			return format(parseFloat(amount) * multipler[targetTest]);
		default:
			return format(parseFloat(amount));
		}
	}

	unitCollection["mmol/L"] = function(amount, unit, targetTest) {
		switch (unit) {
		case "mg/dL":
			var multiplier = {
				"1006" : 1 / 0.0259,
				"1009" : 1 / 0.0259,
				"1007" : 1 / 0.0259,
				"1008" : 1 / 0.0259,
				"887" : 1 / 0.0555,
				"160912" : 1 / 0.0555,
				"1012" : 1 / 0.111,
				"875" : 0.357
			};
			return format(parseFloat(amount) / multiplier[targetTest]);
		case "g/dL":
			var multiplier = {
				"1017" : 1 / 0.15135
			};
			return format(parseFloat(amount) / multiplier[targetTest]);
		case "mEq/L":
			var multiplier = {
				"1132" : 1,
				"1133" : 1,
				"1134" : 1,
				"1135" : 1
			};
			return format(parseFloat(amount) / multiplier[targetTest]);
		default:
			return format(parseFloat(amount));
		}
	}

	unitCollection["mEq/L"] = function(amount, unit, targetTest) {
		switch (unit) {
		case "mmol/L":
			var multiplier = {
				"1132" : 1,
				"1133" : 1,
				"1134" : 1,
				"1135" : 1
			};
			return format(parseFloat(amount) * multiplier[targetTest]);
		default:
			return format(parseFloat(amount));
		}
	}

	unitCollection["U/L"] = function(amount, unit, targetTest) {
		switch (unit) {
		case "ukat/L":
			var multiplier = {
				"785" : 0.0167,
				"654" : 0.0167,
				"653" : 0.0167
			};
			return format(parseFloat(amount) * multiplier[targetTest]);
		default:
			return format(parseFloat(amount));
		}

	}

	unitCollection["ukat/L"] = function(amount, unit, targetTest) {
		switch (unit) {
		case "U/L":
			var multiplier = {
				"785" : 1 / 0.0167,
				"654" : 1 / 0.0167,
				"653" : 1 / 0.0167
			};
			return format(parseFloat(amount) * multiplier[targetTest]);
		default:
			return format(parseFloat(amount));
		}
	}

	unitCollection["ng/mL"] = function(amount, unit, targetTest) {
		switch (unit) {
		case "nmol/L":
			var multiplier = {
				"161503" : 1.54
			};
			return format(parseFloat(amount) * multiplier[targetTest]);
		default:
			return format(parseFloat(amount));
		}
	}

	unitCollection["nmol/L"] = function(amount, unit, targetTest) {
		switch (unit) {
		case "ng/mL":
			var multiplier = {
				"161503" : 1 / 1.54
			};
			return format(parseFloat(amount) * multiplier[targetTest]);
		case "ug/dl":
			var multiplier = {
				"161504" : 1 / 12.87
			};
			return format(parseFloat(amount) * multiplier[targetTest]);
		default:
			return format(parseFloat(amount));
		}
	}

	unitCollection["ug/dl"] = function(amount, unit, targetTest) {
		switch (unit) {
		case "nmol/L":
			var multiplier = {
				"161504" : 12.87
			};
			return format(parseFloat(amount) * multiplier[targetTest]);
		default:
			return format(parseFloat(amount));
		}
	}

	unitCollection["gl/dL"] = function(amount, unit, targetTest) {
		switch (unit) {
		case "g/L":
			var multiplier = {
				"717" : 10
			};
			return format(parseFloat(amount) * multiplier[targetTest]);
		default:
			return format(parseFloat(amount));
		}
	}

	unitCollection["g/L"] = function(amount, unit, targetTest) {
		switch (unit) {
		case "gl/dL":
			var multiplier = {
				"717" : 1 / 10
			};
			return format(parseFloat(amount) * multiplier[targetTest]);
		default:
			return format(parseFloat(amount));
		}
	}

	return unitCollection;

	function format(number) {
		return Common.format(number);
	}

}



EnterLabResult.redirectFocus = function(element) {
	element = $(element);
	var targetInput = $("td[value=" + element.attr("name") + "] input", element);
	targetInput.focus();
}

EnterLabResult.updateComment = function(gender, age, conceptId) {
	if ("true" == jq("#" + conceptId + "_isRadiology").val()) {
		console.log("Radiology");
		return;
	}
	var tr = getParent("tr", $("#" + conceptId + "_value"));
	var value = parseFloat($("#" + conceptId + "_value").val());
	var range = $("td[name=range]", tr).text();
	var low = parseFloat(range.split("-")[0]);
	var high = parseFloat(range.split("-")[1]);
	var comment = "";
	var commentColor = "";
	if (value == "")
		return;
	if (!isNaN(value) && !isNaN(low) && !isNaN(high)) {
		if (value < low) {
			comment = "Low";
			commentColor = "red";
		} else if (value > high) {
			comment = "High";
			commentColor = "red";
		} else {
			comment = "Normal";
			commentColor = "green";
		}
	}
	$("td[name=comment]", tr).text(comment);
	$("td[name=comment]", tr).css("color", commentColor);

	creatinineClearanceRate();

	function creatinineClearanceRate() {
		var total;
		var originUnit = $("[name=unit] select option",tr).first().text();
		var targetUnit = $("[name=unit] select",tr).val();
		var conversionFunction = EnterLabResult.initUnit()[targetUnit];
		value = conversionFunction(value, originUnit, 790);		
		gender = gender.toLowerCase() == "female"|| gender.toLowerCase() == "f" ? "female" : "male";
		if (conceptId == 790 && gender == 'female') {
			total = 175 * Math.pow(value, -1.154) * Math.pow(age, -0.203)* 0.742;
		} else if (conceptId == 790 && gender == 'male') {
			total = 175 * Math.pow(value, -1.154) * Math.pow(age, -0.203);
		}
		$("#" + conceptId + "_rate").val(
				Math.round(total * 100) / 100 + ' ml/min');
		if (conceptId == "1017") {
			var wbcValue = parseFloat($("#enterLabResultForm [name=678] [value=678] input").val());
		}
		if (total)
			$("[name=other-update]", tr).text(Common.format(total)+' ml/min');
		else{
			$("[name=other-update]", tr).text("");
		}
	}
}

EnterLabResult.initComment = function() {
	$("#enterLabResultForm input[type=text]").each(function(index, element) {
		if ($(element).val() == "")
			return;
		if (element.onblur) {
			element.onblur();
		}
	});
}

function getParent(tagName, element) {
	tagName = tagName.toLowerCase();
	while (element.prop("tagName").toLowerCase() != tagName) {
		element = element.parent();
	}
	return element;
}