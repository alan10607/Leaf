function post(url, data, afterFunc){
	$.ajax({
		type: "POST",
		url: url,
		data: JSON.stringify(data),
		headers:{'Content-Type':'application/json;charset=utf8'},
		dataType: "json",
		success: function (res, status) {
			afterFunc(res.result);
			console.log("Status:" + status + ",res:" + JSON.stringify(res));
		},
		error: function (xhr, status) {
			console.log("Status:" + status + ",xhr:" + JSON.stringify(xhr));
		}
	});
}

function vote(num){
	var data = {
		"leafName" : LEAF_NAME,
	    "voteFor"  : num
	};
	post(BASE_URL + "/vote", data, voteAfter);
	updateCount(num);
}

function voteAfter(num){
	//!!!還是把DTO的 choice1 改成good吧
	//假裝有更新
	var id = "#count" + num;
	$(id).text(parseInt($(id).text()) + 1);
};


//自動更新頁面
setInterval(updateCount, 1000);
function updateCount() {
	//每秒發一次, 測試高併發
	var data = {
		"leafName" : LEAF_NAME
	};
    post(BASE_URL + "/getCount", data, updateCountAfter);
}

//更新頁面後處理
function updateCountAfter(leafDTO){
    $("#good").text(leafDTO.good);
    $("#bad").text(leafDTO.bad);
}
