const UPDATE_COUNT_INTERVAL = 1000;//每秒發一次, 測試高併發
const clickAudio = null;

document.addEventListener("touchstart", function(){}, true);//手機版點擊觸發css :active動畫

function init(){
    updateCount();
    getYouPop();
    setClickAudio();
}

function setClickAudio(){
    audio = document.createElement("audio");
    audio.src = CLICK_SOUND_URL;
}

function playClickAudio() {
    audio.play();
}

//自動更新頁面
//setInterval(updateCount, UPDATE_COUNT_INTERVAL);
function updateCount() {
	var data = {"leafName" : LEAF_NAME};
    post("/view/findCount", data, updateCountAfter, null);
}

function updateCountAfter(leafDTO){
    $("#good").text(leafDTO.good);
    $("#bad").text(leafDTO.bad);
}

//按下投票
function vote(voteFor){
    playClickAudio();

	var data = {
		"leafName" : LEAF_NAME,
	    "voteFor"  : voteFor
	};
	post("/view/vote", data, voteAfter, null);
}

function voteAfter(leafDTO){
//    if(leafDTO.good != null) $("#good").text(leafDTO.good);
//    if(leafDTO.bad != null) $("#bad").text(leafDTO.bad);
    updateCount();
    getYouPop();
}

function getYouPop(){
    localStorage.leafYouCount = localStorage.leafYouCount == null ? 1 : (parseInt(localStorage.leafYouCount) + 1);
    $("#you-pop").text(localStorage.leafYouCount);
}