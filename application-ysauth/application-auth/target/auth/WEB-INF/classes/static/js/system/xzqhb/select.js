/**
 * 获取国际列表
 */
function loadCountry(countryControllId) {
    loadCountry(countryControllId, null);
}
/**
 * 获取国际列表
 */
function loadCountry(countryControllId, countryVal) {
    var $country = $('#' + countryControllId);
    var param = {};
    Base.post(rootPath + '/xzqhb/getCountry', param, function(data) {
        $country.empty();
        if (data != null && data.length > 0) {
            var html = "<option value=''></option>";
            $.each(data, function(i, n) {
                html += displayOption(countryVal, n);
            });
            $country.append(html);
        }
    }, function(data) {
    }, null, null);
}

/**
 * 获取省份列表
 */
function loadProvince(provinceControllId) {
    loadProvince(provinceControllId, provinceVal);
}

/**
 * 获取省市二级联动下拉框
 */
function loadProvince(provinceControllId, provinceVal, cityControllId) {
    loadProvince(provinceControllId, provinceVal, cityControllId, null, null, null);
}

/**
 * 获取省市二级联动下拉框
 */
function loadProvince(provinceControllId, provinceVal, cityControllId, cityVal) {
    loadProvince(provinceControllId, provinceVal, cityControllId, cityVal, null, null);
}

/**
 * 获取省市县三级联动下拉框
 */
function loadProvince(provinceControllId, provinceVal, cityControllId, cityVal, districtControllId) {
    loadProvince(provinceControllId, provinceVal, cityControllId, cityVal, districtControllId, null);
}
/**
 * 获取省市县三级联动下拉框
 */
function loadProvince(provinceControllId, provinceVal, cityControllId, cityVal, districtControllId, districtVal) {
    var province = $('#' + provinceControllId);
    var param = {};
    Base.post(rootPath + '/xzqhb/getProvince', param, function(data) {
        province.empty();
        if (data != null && data.length > 0) {
            var html = "<option value=''></option>";
            $.each(data, function(i, n) {
                html += displayOption(provinceVal, n);
            });
            province.append(html);
            if (cityControllId != null) {
//                if (provinceVal != null) {
                    loadCity(provinceVal, cityControllId, cityVal, districtControllId, districtVal);
//                } else {
//                    loadCity(data[0].dm, cityControllId, cityVal, districtControllId, districtVal);
//                }
            }
        }
    }, function(data) {
    }, null, null);
}

/**
 * 获取市份列表
 */
function loadCity(provinceId, cityControllId) {
    loadCity(provinceId, cityControllId, null, null, null);
}

/**
 * 获取市份列表
 */
function loadCity(provinceId, cityControllId, cityVal) {
    loadCity(provinceId, cityControllId, cityVal, null, null);
}

/**
 * 获取市及县列表
 */
function loadCity(provinceId, cityControllId, cityVal, districtControllId) {
    debugger;
    loadCity(provinceId, cityControllId, cityVal, districtControllId, null);
}
/**
 * 获取市及县列表
 */
function loadCity(provinceId, cityControllId, cityVal, districtControllId, districtVal) {
    var city = $('#' + cityControllId);
    var param = {"provinceId":provinceId};
    Base.post(rootPath + '/xzqhb/getCity', param, function(data) {
        city.empty();
        if (data != null && data.length > 0) {
            var html = "<option value=''></option>";
            $.each(data, function(i, n) {
                html += displayOption(cityVal, n);
            });
            city.append(html);
            if (districtControllId != null) {
//                if (cityVal == null) {
//                    loadDistrict(data[0].dm, districtControllId);
//                } else {
                    loadDistrict(cityVal, districtControllId, districtVal);
//                }
            }
        }
    }, function(data) {
        city.empty();
    }, null, null);
}

/**
 * 获取县列表
 */
function loadDistrict(cityId, districtControllId) {
    loadDistrict(cityId, districtControllId, null);
}
/**
 * 获取县列表
 */
function loadDistrict(cityId, districtControllId, districtVal) {
    var district = $('#' + districtControllId);
    var param = {"cityId":cityId};
    Base.post(rootPath + '/xzqhb/getDistrict', param, function(data) {
        district.empty();
        if (data != null && data.length > 0) {
            if (district.attr("data-parent") != undefined && district.attr("data-parent") == "true") {
                district.append("<option value=\"false\">市本级</option>");
            }
            var html = "";
            $.each(data, function(i, n) {
                html += displayOption(districtVal, n);
            });
            district.append(html);
        }
    }, function(data) {
        district.empty();
    }, null, null);
}


    
function displayOption(val, n) {
    var html = "";
    if (val == n.dm) {
        html += "<option value=" + n.dm + " selected>" + n.mc + "</option>";
    } else {
        html += "<option value=" + n.dm + ">" + n.mc + "</option>";
    }
    return html;
}