function post(url, data, afterFunc, afterError){
	$.ajax({
		type: "POST",
		url: url,
		data: JSON.stringify(data),
		headers:{'Content-Type':'application/json;charset=utf8'},
		dataType: "json",
		success: function (res, status) {
			if(afterFunc != null)
			    afterFunc(res.result);

//			console.log("Status:" + status + ",res:" + JSON.stringify(res));
		},
		error: function (xhr, status) {
			if(afterError != null)
		        afterError(xhr);

			console.log("Status:" + status + ",xhr:" + JSON.stringify(xhr));
		}
	});
}


