const UPDATE_COUNT_INTERVAL = 1000;//每秒發一次, 測試高併發

function init(){
    updateCount();
}

function playClickSound() {
    const audio = document.createElement("audio");
    audio.src = CLICK_SOUND_URL;
    audio.play();
}

//自動更新頁面
setInterval(updateCount, UPDATE_COUNT_INTERVAL);
function updateCount() {
	var data = {"leafName" : LEAF_NAME};
    post("/view/findCount", data, updateCountAfter, null);
}

function updateCountAfter(leafDTO){
    $("#good").text(leafDTO.good);
    $("#bad").text(leafDTO.bad);
}

function vote(voteFor){
    playClickSound();

	var data = {
		"leafName" : LEAF_NAME,
	    "voteFor"  : voteFor
	};
	post("/view/vote", data, voteAfter, null);
}

function voteAfter(leafDTO){
    if(leafDTO.good != null) $("#good").text(leafDTO.good);
    if(leafDTO.bad != null) $("#bad").text(leafDTO.bad);
};