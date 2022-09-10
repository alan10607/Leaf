function consoleShowError(result){
    $("#manager-console").text(result);
}

//查詢所有leaf
function findLeaf(){
    var leafName = $("#leaf-textbox").val();
    if(leafName != null && leafName.trim() != ""){
        var data = {"leafName" : leafName};
        post("/leaf/find", data, findLeafAfter, findLeafError);
    }else{
        post("/leaf/findAll", {}, findAllLeafAfter, findLeafError);
    }
}

function findLeafAfter(leafDTO){
    var tBody = "<tr><td>id</td><td>leafName</td><td>good</td><td>bad</td><td>updatedDate</td></tr>";
    tBody += `<tr><td>${leafDTO.id}</td><td>${leafDTO.leafName}</td><td>${leafDTO.good}</td><td>${leafDTO.bad}</td><td>${leafDTO.updatedDate}</td></tr>`
    $("#leafTBody").html(tBody);
};

function findAllLeafAfter(leafDTOList){
    var tBody = "<tr><td>id</td><td>leafName</td><td>good</td><td>bad</td><td>updatedDate</td></tr>";
    for(let leafDTO of leafDTOList)
        tBody += `<tr><td>${leafDTO.id}</td><td>${leafDTO.leafName}</td><td>${leafDTO.good}</td><td>${leafDTO.bad}</td><td>${leafDTO.updatedDate}</td></tr>`

    $("#leafTBody").html(tBody);
};

function findLeafError(result){
    $("#leafTBody").html("");
    consoleShowError("Error: " + result);
};

//建立leaf
function createLeaf(){
    var leafName = $("#leaf-textbox").val();
    if(leafName != null && leafName.trim() != ""){
	    var data = {"leafName" : leafName};
    	post("/leaf/create", data, findLeaf, consoleShowError);
    }else{
        consoleShowError("No leaf name input");
    }
}

//刪除leaf
function deleteLeaf(){
    var leafName = $("#leaf-textbox").val();
    if(leafName != null && leafName.trim() != ""){
	    var data = {"leafName" : leafName};
    	post("/leaf/delete", data, findLeaf, consoleShowError);
    }else{
         consoleShowError("No leaf name input");
    }
 }

//查詢所有user
function findUser(){
    var email = $("#user-textbox").val();
    if(email != null && email.trim() != ""){
        var data = {"email" : email};
        post("/user/findUser", data, findUserAfter, findUserError);
    }else{
        post("/user/findAllUser", {}, findAllUserAfter, findUserError);
    }
}

function findUserAfter(dto){
    var tBody = "<tr><td>id</td><td>userName</td><td>email</td><td>userRole</td><td>updatedDate</td></tr>";
    tBody += `<tr><td>${dto.id}</td><td>${dto.userName}</td><td>${dto.email}</td><td>${getUserRole(dto.userRole)}</td><td>${dto.updatedDate}</td></tr>`
    $("#userTBody").html(tBody);
};

function findAllUserAfter(leafUserDTOList){
    var tBody = "<tr><td>id</td><td>userName</td><td>email</td><td>userRole</td><td>updatedDate</td></tr>";
    for(let dto of leafUserDTOList)
        tBody += `<tr><td>${dto.id}</td><td>${dto.userName}</td><td>${dto.email}</td><td>${getUserRole(dto.userRole)}</td><td>${dto.updatedDate}</td></tr>`

    $("#userTBody").html(tBody);
};

function getUserRole(userRole){
    return userRole.map(r => r.roleName).join(",");
}

function findUserError(leafDTOList){
    $("#userTBody").html("");
};

//建立user
function createUser(){
    var email = $("#user-textbox").val();
    var pw = $("#user-pw-textbox").val();
    var userName = $("#user-userName-textbox").val();
    if(email.trim() != "" && pw.trim() != "" && userName.trim() != ""){
        var data = {
            "email" : email,
            "pw" : pw,
            "userName" : userName
        };
    	post("/user/createUser", data, findUser, consoleShowError);
    }else{
        consoleShowError("No user name input");
    }
}

//刪除user
function deleteUser(){
    var email = $("#user-textbox").val();
    if(email.trim() != ""){
        var data = {"email" : email};
    	post("/user/deleteUser", data, findUser, consoleShowError);
    }else{
         consoleShowError("No user name input");
    }
 }
