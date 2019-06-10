;/*!widget/public/pageCtrl/1.0.0.tpl*/
define("widget/public/pageCtrl/1.0.0.tpl",function(require,exports,module){return function(it,opt){with(it=it||{}){var _$out_=[];if(page>1){_$out_.push(" ");var index="undefined"!=typeof idx?idx:1,isFirst=1==index?!0:!1,isLast=index==page?!0:!1,from=parseInt(index-max/2),to=parseInt(index+max/2);(2==from||to==page-2)&&(max+=2);{"pager_id_"+Math.random().toString().replace(/\D/g,"")}if(_$out_.push(' <div class="page-box ',theme,'"> <!-- <span class="page-recored">第',index,"页/共",page,"页（共",total,"条记录）</ themespan> --> "),max>=page-2){_$out_.push(" "),"shuyin"==theme&&(_$out_.push(' <a href="javascript:void(0);" class="page-prev-btn '),isFirst&&_$out_.push("page-btn-dis"),_$out_.push('"><i class=""></i></a>')),_$out_.push(" ");for(var i=1,len=page;len>=i;i++)_$out_.push(' <a href="javascript:void(0);" data-index="',i,'" class="page-btn'),1==i&&_$out_.push(" page-first"),i==len&&_$out_.push(" page-last"),i==index&&_$out_.push(" page-btn-cur"),_$out_.push('">',i,"</a> ");_$out_.push(" "),"shuyin"==theme&&(_$out_.push('<a href="javascript:void(0);" class="page-next-btn '),isLast&&_$out_.push("page-btn-dis"),_$out_.push('"><i class=""></i></a> ')),_$out_.push(" ")}else{if(_$out_.push(" "),from>1&&page-1>to){_$out_.push(" "),"shuyin"==theme&&(_$out_.push(' <a href="javascript:void(0);" class="page-prev-btn '),isFirst&&_$out_.push("page-btn-dis"),_$out_.push('"><i class=""></i></a>')),_$out_.push(' <a href="javascript:void(0);" data-index="1" class="page-btn page-first">1</a> <span class="page-dot">'),"shuyin"==theme?_$out_.push("<i></i><i></i><i></i>"):"jidu"==theme&&_$out_.push("…"),_$out_.push("</span> ");for(var i=from,len=page;to>i;i++)_$out_.push(' <a href="javascript:void(0);" data-index="',i+1,'" class="page-btn'),i+1==index&&_$out_.push(" page-btn-cur"),_$out_.push('">',i+1,"</a> ");_$out_.push(' <span class="page-dot">'),"shuyin"==theme?_$out_.push("<i></i><i></i><i></i>"):"jidu"==theme&&_$out_.push("…"),_$out_.push('</span> <a href="javascript:void(0);" data-index="',len,'" class="page-btn page-last">',len,"</a> "),"shuyin"==theme&&(_$out_.push('<a href="javascript:void(0);" class="page-next-btn '),isLast&&_$out_.push("page-btn-dis"),_$out_.push('"><i class=""></i></a> ')),_$out_.push(" ")}else{if(_$out_.push(" "),2>=from){_$out_.push(" "),"shuyin"==theme&&(_$out_.push(' <a href="javascript:void(0);" class="page-prev-btn '),isFirst&&_$out_.push("page-btn-dis"),_$out_.push('"><i class=""></i></a>')),_$out_.push(" ");for(var i=1,len=page;i<=Math.max(max,to);i++)_$out_.push(' <a href="javascript:void(0);" data-index="',i,'" class="page-btn'),1==i&&_$out_.push(" page-first"),i==len&&_$out_.push(" page-last"),i==index&&_$out_.push(" page-btn-cur"),_$out_.push('">',i,"</a> ");_$out_.push(' <span class="page-dot">'),"shuyin"==theme?_$out_.push("<i></i><i></i><i></i>"):"jidu"==theme&&_$out_.push("…"),_$out_.push('</span> <a href="javascript:void(0);" data-index="',len,'" class="page-btn page-last">',len,"</a> "),"shuyin"==theme&&(_$out_.push('<a href="javascript:void(0);" class="page-next-btn '),isLast&&_$out_.push("page-btn-dis"),_$out_.push('"><i class=""></i></a> ')),_$out_.push(" ")}if(_$out_.push(" "),to>=page-1){_$out_.push(" "),"shuyin"==theme&&(_$out_.push(' <a href="javascript:void(0);" class="page-prev-btn '),isFirst&&_$out_.push("page-btn-dis"),_$out_.push('"><i class=""></i></a>')),_$out_.push(' <a href="javascript:void(0);" data-index="1" class="page-btn page-first">1</a> <span class="page-dot">'),"shuyin"==theme?_$out_.push("<i></i><i></i><i></i>"):"jidu"==theme&&_$out_.push("…"),_$out_.push("</span> ");for(var i=Math.min(from,page-max),len=page;page>i;i++)_$out_.push(' <a href="javascript:void(0);" data-index="',i+1,'" class="page-btn'),i==index-1&&_$out_.push(" page-btn-cur"),i==page-1&&_$out_.push(" page-last"),_$out_.push('">',i+1,"</a> ");_$out_.push(" "),"shuyin"==theme&&(_$out_.push('<a href="javascript:void(0);" class="page-next-btn '),isLast&&_$out_.push("page-btn-dis"),_$out_.push('"><i class=""></i></a> ')),_$out_.push(" ")}_$out_.push(" ")}_$out_.push(" ")}_$out_.push(" </div>")}return _$out_.join("")}}});
;/*!widget/public/departSelect/1.0.0.tpl*/
define('widget/public/departSelect/1.0.0.tpl', function(require, exports, module) {

    return  function (it, opt) {
        it = it || {};
        with(it) {
            var _$out_= [];
            _$out_.push('<h2>请选择部门</h2><div class="select-text"> <ul> ');
            if(str!=''){
                _$out_.push('', str, '');
            }
            _$out_.push(' </ul></div><div class="select-title"> ');
            if(!title)
                _$out_.push('<span data-id="0">HOME</span>');
            else{
                _$out_.push('', title, '');
            }
            _$out_.push('</div><div class="select-content"> ');
            if(type =='1'){
                _$out_.push(' <div class="select-all"> <label><input type="checkbox" ');
                if(flag){
                    _$out_.push('checked="', flag, '"');
                }
                _$out_.push('/>全选</label> </div> ');
            }
            _$out_.push(' <ul class="select-content-ul"> ');
            for(var i = 0;i<data.length;i++){
                _$out_.push(' <li><input type="checkbox" ');
                if(data[i].checked){
                    _$out_.push('checked="', data[i].checked, '"');
                }
                _$out_.push('/> <a class="item" href="javascript:void(0);" data-name="', data[i].name, '" data-id="', data[i].id, '" data-num="', data[i].num, '">', data[i].name, '');
                if(data[i].num>0){
                    _$out_.push('<span class="right">', data[i].num, ' &gt;</span>');
                }
                _$out_.push('</a></li> ');
            }
            _$out_.push(' </ul></div><div class="select-foot"> <button class="confirm">确定</button> <button class="cancel">取消</button></div>');
            return _$out_.join('');
        }
    }

});