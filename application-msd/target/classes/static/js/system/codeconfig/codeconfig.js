// 通过参数设置编码给一栏列表返回参数设置名称
function getListConfigName(columnId, configCode, configValue) {
    getConfigName(columnId, configCode, configValue);
    return "<div id='" + columnId + "'>";
}

function getConfigName(columnId, configCode, configValue) {
    var param = {
        "configCode" : configCode,
        "configValue" : configValue
    };
    Base.post(rootPath + '/codeConfig/findConfigName', param, function(data) {
        if (data != null && data.length > 0 && data != "error") {
            $('#' + columnId).append(data);
        }
    }, 
    function(data) {}, null, null);
}