//构建全国三级联动下拉框
function constructAllDataSelect(dom1,option){
    if(BrowserStorage.getLocalStorage('province')){
        screenBox(dom1, JSON.parse(BrowserStorage.getLocalStorage('province')),option );
    }else{
        var _province=new Array();
        $.ajax({
            type : "GET",
            url : rootPath + "/xzqhb/getAllData",
            dataType : "json",
            traditional : true,
            async : false,
            success : function(data) {
                var shengArr = new Array();
                var shiArr = new Array();
                var xianArr = new Array();
                $.each(data, function(i, n) {
                    if (n.dm.substr(2, 4) == "0000") {
                        shengArr.push(n);
                    } else if (n.dm.substr(4, 2) == "00") {
                        shiArr.push(n);
                    } else if (n.dm.substr(4, 2) != "00") {
                        xianArr.push(n);
                    }
                });
                $.each(shengArr, function(i1, n1) {
                    var shenObj = new Object();
                    var shitempArr = new Array();
                    $.each(shiArr, function(i2, n2) {
                        var shiObj = new Object();
                        if (n1.dm.substr(0, 2) == n2.dm.substr(0, 2)) {
                            var xiantempArr = new Array();
                            $.each(xianArr, function(i3, n3) {
                                var xianObj = new Object();
                                if (n3.dm.substr(0, 4) == (n1.dm.substr(0, 2) + n2.dm.substr(2, 2))) {
                                    xianObj.value = n3.dm;
                                    xianObj.label = n3.mc;
                                    xiantempArr.push(xianObj);
                                }
                            });
                            shiObj.children = xiantempArr;
                            shiObj.value = n2.dm;
                            shiObj.label = n2.mc;
                            shitempArr.push(shiObj);
                        }
                    });
                    shenObj.children = shitempArr;
                    shenObj.value = n1.dm;
                    shenObj.label = n1.mc;
                    _province.push(shenObj);
                });
                
                BrowserStorage.addLocalStorage('province',JSON.stringify(_province));
                screenBox(dom1, _province,option );
            }
        });
    }
}
//构建当前区域下拉框
function constructDistrictSelect(dom1,option,firstdata){
    if(BrowserStorage.getLocalStorage('district')){
        screenBox(dom1, JSON.parse(BrowserStorage.getLocalStorage('district')),option);
    }else{
        var _district=new Array();
        $.ajax({
            url : rootPath + "/xzqhb/getXzqh",
            type : "post",
            data : {},
            traditional : true,
            dataType : "json",
            async : false,
            success : function(data) {
                var s = data.substr(0, 4);
                var value = s + "00";
                var param = {"cityId":value};
                $.ajax({
                    url: rootPath + '/xzqhb/getDistrict',
                    type : "post",
                    data: param,
                    dataType:"json",
                    async : false,
                    success:function(data){
                        if(firstdata){
                            _district.push(firstdata);
                        }
                        $.each(data,function(i,n){
                            var policeobj=new Object();
                            policeobj.value=n.dm;
                            policeobj.label=n.mc;
                            _district.push(policeobj);
                        });
                        BrowserStorage.addLocalStorage('district',JSON.stringify(_district));
                        screenBox(dom1, _district,option);
                    }
                });
            },
            error : function() {
                //alert("获取行政区划编码异常！！");
            }
        });
    }
    
}
