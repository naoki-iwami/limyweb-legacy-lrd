var gTargetId;
var gTimer = null;
function get(targetId) {
  return document.getElementById(targetId);
}
function showTree() {
  w = window.open("showTree?" + get("nowUrl").innerHTML,
    "Tree", "width=400,height=800,scrollbars=yes");
  w.focus();
}
function showAlert(result) {
	if (result.alertString) {
		alert(result.alertString);
	}
}
function preview() {
	startTimer();
	
	get("dwrForm").methodName.value = "compile";
	get("dwrForm").contents.value = createMainText();
	DWRActionUtil.execute(
			{ namespace:'/', action:'dwr', executeResult:'false' },
			'dwrForm', function(result) {
		
		endTimer();
		get("preview").innerHTML = result.mainString;
		showAlert(result);
	});
}
function moveImage() {
	gImagePos += 1;
	get("timer").style.left = gImagePos;
}

function startTimer() {
	get("timer").style.visibility = "visible";
	if (gTimer) {
		window.clearInterval(gTimer);
	}
	gImagePos = 4;
	get("timer").style.left = 4;
	get("timer").style.top = 4;
	gTimer = window.setInterval("moveImage()", 20);
}
function endTimer() {
	get("timer").style.visibility = "hidden";
	if (gTimer) {
		window.clearInterval(gTimer);
	}
}
function createFile() {
	startTimer();
	
	get("dwrForm").methodName.value = "createFile";
	get("dwrForm").contents.value = get("lrdPath").value;
	DWRActionUtil.execute(
			{ namespace:'/', action:'dwr', executeResult:'false' },
			'dwrForm', function(result) {
		
		endTimer();
		get("preview").innerHTML = result.mainString;
		showAlert(result);
		if (!result.error) {
			changeLocation(get("lrdPath").value);
		}
	});
}
function commit() {
	startTimer();

	get("dwrForm").methodName.value = "commit";
	get("dwrForm").contents.value = createMainText();
	DWRActionUtil.execute(
			{ namespace:'/', action:'dwr', executeResult:'false' },
			'dwrForm', function(result) {
		
		endTimer();
		get("preview").innerHTML = result.mainString;
		showAlert(result);
	});
}
function deployFrame(deployId) {
	startTimer();

	get("dwrForm").methodName.value = "deployFrame";
	get("dwrForm").contents.value = deployId;
	DWRActionUtil.execute(
			{ namespace:'/', action:'dwr', executeResult:'false' },
			'dwrForm', function(result) {
		
		endTimer();
		showAlert(result);
	});
}
function deployFull(deployId) {
	g_deployFinish = false;
	get("bar0").style.display = "block";
	get("nowCount").innerHTML = "?";
	get("allCount").innerHTML = "?";
	
	get("dwrForm").methodName.value = "deployFull";
	get("dwrForm").contents.value = deployId;
	DWRActionUtil.execute(
			{ namespace:'/', action:'dwr', executeResult:'false' },
			'dwrForm', function(result) {
		
		comet_work(100);
		get("bar0").style.display = "none";
		g_deployFinish = true;
		showAlert(result);
	});
}
function deployRecent(deployId) {
	startTimer();

	get("dwrForm").methodName.value = "deployRecentPage";
	get("dwrForm").contents.value = deployId;
	DWRActionUtil.execute(
			{ namespace:'/', action:'dwr', executeResult:'false' },
			'dwrForm', function(result) {
		
		endTimer();
		showAlert(result);
	});
}
function addRss() {
	startTimer();

	get("dwrForm").methodName.value = "addRss";
	DWRActionUtil.execute(
			{ namespace:'/', action:'dwr', executeResult:'false' },
			'dwrForm', function(result) {
		
		endTimer();
		showAlert(result);
	});

}
function getAllMenu() {
	startTimer();

	get("dwrForm").methodName.value = "getAllMenu";
	DWRActionUtil.execute(
			{ namespace:'/', action:'dwr', executeResult:'false' },
			'dwrForm', function(result) {
		
		endTimer();
	});
}
function changeLocation(targetId) {
	// 直でDwrを呼び出すと、外部ウィンドウから操作したときにうまく動作しない
	window.location.href = "?id=" + gRepositoryId + "&lrdPath=" + targetId;
	return;
}
function toggleMenu() {
  var targetComp = get("mainButton");
  var el = get("mainText");
  if (el.style.display == "none") {
    el.style.display = "";
    get("buttonBox").style.display = "";
    get("adminBox").style.display = "";
    targetComp.value = "-";
  } else {
    el.style.display = "none";
    get("buttonBox").style.display = "none";
    get("adminBox").style.display = "none";
    targetComp.value = "+";
  }
}
function init() {
	get("dwrForm").repositoryId.value = gRepositoryId;
	get("dwrForm").lrdPath.value = gTargetId;
	get("nowUrl").innerHTML = gTargetId;
	dwr.engine.setActiveReverseAjax(true);
	/*
	setTimeout(function() {
		DwrExt.changeLocation(gRepositoryId, gTargetId, function(result) {
			initTextFields(result.mainString);
			preview();
		});
	}, 0);
	*/
	
	get("dwrForm").methodName.value = "changeLocation";
	DWRActionUtil.execute(
			{ namespace:'/', action:'dwr', executeResult:'false' },
			'dwrForm', function(result) {
		
		initTextFields(result.mainString);
		preview();
	});
}
function initTextFields(contents) {
	var lines = contents.split("\n");
	var bodyFlg = false;
	var bodyText = "";
	var lastEndPos = -1;
	for (idx in lines) {
		var line = lines[idx].replace("\r", "");
		if (!bodyFlg) {
			if (line.match(/^title *= *(.*)/i)) {
				get("titleText").value = RegExp.$1;
			}
			if (line.match(/^print-time *= *(.*)/i)) {
				if (RegExp.$1.match(/true/i)) {
					get("chkPrintTime").checked = true;
				} else {
					get("chkPrintTime").checked = false;
				}
			}
			if (line.match(/^date *= *(.*)/i)) {
				get("dateText").value = RegExp.$1;
			}
			if (line.match(/^depend *= *(.*)/i)) {
				get("dependText").value = RegExp.$1;
			}
			if (line.match(/^ref *= *(.*)/i)) {
				get("refText").value = RegExp.$1;
			}
			if (line.match(/^extcode *= *(.*)/i)) {
				get("chkExtCode").checked = true;
			}
			if (line == "=begin") {
				bodyFlg = true;
			}
		} else {
			if (line == "=end") {
				lastEndPos = bodyText.length;
			}
			bodyText += line + "\n";
		}
	}
	if (lastEndPos >= 0) {
		get("bodyText").value = bodyText.substr(0, lastEndPos);
	} else {
		get("bodyText").value = bodyText;
	}
}
function createMainText() {
	var mainText = "";
	if (get("titleText").value.length > 0) {
		mainText += "title=" + get("titleText").value + "\n";
	}
	if (get("dependText").value.length > 0) {
		mainText += "depend=" + get("dependText").value + "\n";
	}
	if (get("refText").value.length > 0) {
		mainText += "ref=" + get("refText").value + "\n";
	}
	if (!get("chkPrintTime").checked) {
		mainText += "print-time=false\n";
	}
	if (get("chkExtCode").checked) {
		mainText += "extcode=on\n";
	}
	mainText += "=begin\n";
	mainText += get("bodyText").value;
	if (mainText.charCodeAt(mainText.length - 1) != 10) {
		mainText += "\n";
	}
	mainText += "=end\n";
	return mainText;
}
function comet_work(nowCount, allCount) {
	if (!g_deployFinish) {
		get("nowCount").innerHTML = "" + nowCount;
		get("allCount").innerHTML = "" + allCount;
//		get("bar1").style.width = percentage + "%";
		get("bar1").style.width = (100 * nowCount / allCount) + "%";
	}
}

function showAttachBlock() {
	get('attachBlock').style.display = 'block';
}
