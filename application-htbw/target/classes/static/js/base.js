/**
 * Created by hzcominfo on 2016/11/8.
 */
var VERSION = "20160607";
var ProjectPath = (window.location.origin || (window.location.protocol + "//" + window.location.host)) + '/' + window.location.pathname.split("/")[1] + '/';
var Base = function () {
    function get(url, success, error, contentType, dataType, async) {
        ajax("GET", url, "", success, error, contentType, dataType, async);
    }

    function post(url, param, success, error, contentType, dataType, async) {
        ajax("POST", url, param, success, error, contentType, dataType, async);
    }

    function ajax(type, url, param, success, error, contentType, dataType, async) {
        if ("undefined" == typeof (contentType) || contentType == null) {
            contentType = "application/x-www-form-urlencoded; charset=UTF-8";
        } else if ('json' == contentType.toLocaleLowerCase()) {
            contentType = 'application/json;charset=UTF-8';
            if ("undefined" != typeof (param) && param != null) {
                param = JSON.stringify(param);
            }
        }
        if ("undefined" == typeof (dataType) || dataType == null) {
            dataType = "JSON";
        }
        if ("undefined" == typeof (async) || async == null) {
            async = true;
        }
        $.ajax({
            type:type,
            url:url,
            data:param,
            contentType:contentType,
            dataType:dataType,
            cache:false,
            async:async,
            success:function (data, textStatus) {
                if ("undefined" != typeof (success) && success != null) {
                    success(data);
                }

            },
            error:function (data, textStatus, info) {
                if ("undefined" == typeof (error) || error == null) {
                    console.error("base ajax error:" + data);
                }else{
                    error(data);
                }
            }
        });
    }

    /**
     * 动态加载css，js文件
     * @param filename
     * @param filetype
     */
    function loadHeadFile(filename, filetype, version) {
        var fileref = "";
        if(undefined == version) version = VERSION;
        if (filetype == "js") {
            fileref = document.createElement('script');
            fileref.setAttribute("type", "text/javascript");
            fileref.setAttribute("src", filename + '?' + version);
        } else if (filetype == "css") {
            fileref = document.createElement('link');
            fileref.setAttribute("rel", "stylesheet");
            fileref.setAttribute("type", "text/css");
            fileref.setAttribute("href", filename + '?' + version);
        }
        if (typeof fileref != "undefined") {
            $("head").append(fileref);
        }
    }

    /**
     *
     */
    function cn(string){
        if(string == undefined || string == "undefined" || string == "null"){
            return '';
        }
        return string;
    }

    /**
     * get url query value
     * @param name
     */
    function getURLQuery(name){
        var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)");
        var query = window.location.search.substr(1).match(reg);
        if(query != null) return decodeURI(query[2]);
        return null;
    }
    return {
        get:function (url, success, error, contentType, dataType) {
            get(url, success, error, contentType, dataType);
        },
        post:function (url, param, success, error, contentType, dataType) {
            post(url, param, success, error, contentType, dataType);
        },
        ajax:function (type, url, param, success, error, contentType, dataType) {
            ajax(type, url, param, success, error, contentType, dataType);
        },
        loadHeadFile: function(filename, filetype, version){
            loadHeadFile(filename, filetype, version);
        },
        cn: function (string) {
            return cn(string);
        },
        getURLQuery: function (name) {
            return getURLQuery(name);
        }
    };

}();

