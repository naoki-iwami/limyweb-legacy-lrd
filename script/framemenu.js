function get(id) {
    return document.getElementById(id);
}
function minimizeNav() {
    get("leftColumn").style.left = (gSize - 18) + "%";
    get("bodyColumn").style.marginLeft = (gSize + 2) + "%";
    if (--gSize <= 0) {
        clearInterval(gMinimizeTimerId);
        get("navcolumn").style.display = "none";
        get("frameSizeBtn").src = "/images/restore.png";
        get("frameSizeBtn").alt = "restore";
        get("frameSizeBtn").title = "restore";
        get("leftColumn").style.position = "static";
        get("leftColumn").style.width = "0%";
        get("bodyColumn").style.marginLeft = "2%";
    }
}

function restoreNav() {
    get("leftColumn").style.left = (gSize - 18) + "%";
    get("bodyColumn").style.marginLeft = (gSize + 2) + "%";
    if (++gSize >= 18) {
        clearInterval(gMinimizeTimerId);
        get("navcolumn").style.display = "block";
        get("frameSizeBtn").src = "/images/minimize.png";
        get("frameSizeBtn").alt = "minimize";
        get("frameSizeBtn").title = "minimize";
        get("leftColumn").style.position = "static";
        get("leftColumn").style.width = "18%";
        get("bodyColumn").style.marginLeft = "20%";
    }
}

function minimize() {
    if (get("navcolumn").style.display != "none") {
        gSize = 17;
        get("navcolumn").style.overflow = "hidden";
        get("leftColumn").style.position = "absolute";
        if (gShowFull) {
            // 最大化したままアニメーションすると遅いので、通常メニュー表示に戻す
            showFullMenu();
        }
        gMinimizeTimerId = setInterval('minimizeNav()', 30);
    } else {
        gSize = 1;
        get("navcolumn").style.display = "block";
        get("navcolumn").style.overflow = "hidden";
        get("leftColumn").style.position = "absolute";
        get("leftColumn").style.width = "18%";
        gMinimizeTimerId = setInterval('restoreNav()', 30);
    }

}

var gShowFull = false;
var gDefaultMenuHtml = null;
var gFullMenuHtml = null;

function showFullMenu(repositoryId) {
    if (!gShowFull) {
        if (gFullMenuHtml) {
            get("menuOverview").innerHTML = gFullMenuHtml;
            get("showFullBtn").src = g_resourceUrlBase + "/images/collapse.png";
            get("showFullBtn").title = "show default menu";
        } else {
            gDefaultMenuHtml = get("menuOverview").innerHTML;
			DWRActionUtil.execute(
					{ namespace:'/', action:'dwr', executeResult:'false' },
					'dwrForm', function(result) {
				
				get("menuOverview").innerHTML = result.mainString;
				get("showFullBtn").src = g_resourceUrlBase + "/images/collapse.png";
				get("showFullBtn").title = "show default menu";
			});
        }
    } else {
        gFullMenuHtml = get("menuOverview").innerHTML;
        get("menuOverview").innerHTML = gDefaultMenuHtml;
        get("showFullBtn").src = g_resourceUrlBase + "/images/expand.png";
        get("showFullBtn").title = "show full menu";
    }
    gShowFull = !gShowFull;
}
