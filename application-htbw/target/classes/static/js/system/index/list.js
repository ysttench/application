$(function() {
    // 统计
    jkNum();
    ycNum();
    jbNum();
    gzNum();
    jbjlNum();
    glNum();
})
function jkNum() {
    $.ajax({
        type : "post",
        datatype : "json",
        url : rootPath + "/equipment/jkNum",
        success : function(msg) {
            $("#jk").empty().append(msg);
        }
    })
}
function ycNum() {
    $.ajax({
        type : "post",
        datatype : "json",
        url : rootPath + "/equipment/ycNum",
        success : function(msg) {
            $("#yc").empty().append(msg);
        }
    })
}

function jbNum() {
    $.ajax({
        type : "post",
        datatype : "json",
        url : rootPath + "/warnmsg/jbNum",
        success : function(msg) {
            $("#jb").empty().append(msg);
        }
    })
}

function gzNum() {
    $.ajax({
        type : "post",
        datatype : "json",
        url : rootPath + "/warnmsg/gzNum",
        success : function(msg) {
            $("#gz").empty().append(msg);
        }
    })
}

function jbjlNum() {
    $.ajax({
        type : "post",
        datatype : "json",
        url : rootPath + "/logrecord/jbjlNum",
        success : function(msg) {
            $("#jbjl").empty().append(msg);
        }
    })
}

function glNum() {
    $.ajax({
        type : "post",
        datatype : "json",
        url : rootPath + "/annount/glNum",
        success : function(msg) {
            $("#gl").empty().append(msg);
        }
    })
}

