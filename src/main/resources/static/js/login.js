function fromGet(url, formEle){
//    var form = $('form')[0];
//    var formData = new FormData(form);

	$.ajax({
		type: "GET",
		url: url,
		data: formEle.serialize(),
		headers:{"Authorization" : bearer},
		dataType: false,
        beforeSend: function(xhr) {
            if (localStorage.token) {
                xhr.setRequestHeader('Authorization', localStorage.token);
            }
        },
		success: function (res, status) {
            $("#login-console").html(res);
			console.log("Status:" + status + ",res:" + JSON.stringify(res));
		},
		error: function (xhr, status) {
			console.log("Status:" + status + ",xhr:" + JSON.stringify(xhr));
		}
	});
}


function formPost(url, formEle, afterFunc, afterError){
	$.ajax({
		type: "POST",
		url: url,
		data: formEle.serialize(),
		dataType: false,
		success: function (res, status) {
			if(afterFunc != null)
			    afterFunc(res);

			console.log("Status:" + status + ",res:" + JSON.stringify(res));
		},
		error: function (xhr, status) {
			if(afterError != null)
		        afterError(xhr);

			console.log("Status:" + status + ",xhr:" + JSON.stringify(xhr));
		}
	});
}


function consoleShowError(result){
    $("#login-console").text(result);
}

function toManager(json){
    localStorage.token = "Bearer " + json.access_token;
    var formEle = $("#login-form");
    fromGet("/admin/manager", formEle);
}

function tryLogin() {

    var formEle = $("#login-form");
    formPost("/login", formEle, toManager, consoleShowError);



}
