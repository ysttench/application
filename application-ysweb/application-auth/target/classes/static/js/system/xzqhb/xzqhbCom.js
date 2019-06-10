
// 通过地址编码给一栏列表返回地址名称
function getListAddress(columnId, value) {
    getAddressName(columnId, value);
    return "<div id='" + columnId + "'>";
}

function getAddressName(columnId, code) {
    var param = {"code" : code};
    Base.post(rootPath + '/xzqhb/getXzqhName', param, function(data) {
        if (data != null && data.length > 0) {
            $('#' + columnId).append(data);
        } else {
            $('#' + columnId).append(code);
        }
    }, function(data) {
    }, null, null);
}

function getAddressNameByObj(spanObj, code, insertFlag) {
    var param = {"code" : code};
    Base.post(rootPath + '/xzqhb/getXzqhName', param, function(data) {
        if (data != null && data.length > 0) {
            if (insertFlag != undefined && insertFlag =="before") {
                var text = spanObj.text();
                spanObj.text("");
                spanObj.append(data);
                spanObj.append(text);
            } else {
                spanObj.append(data);
            }
        }
    }, function(data) {
    }, null, null);
}