;/*!widget/public/pageCtrl/1.0.0/js/pageCtrl.js*/
define("widget/public/pageCtrl/1.0.0/js/pageCtrl",function(t){var i=t("widget/public/pageCtrl/1.0.0.tpl"),e="defClk",n=(navigator.userAgent.match(/\bMSIE\s+(\d+)\b/)||[0,10])[1]<9,a=function(t,i){return t=parseInt(t)||parseInt(i)||0,0>t?0:t},o=function(t,i,e){this.$container=t instanceof $?t:$(t),this.pageFn=i,this.inited=!1,e&&"object"==typeof e&&this.init(e)};return o.prototype.init=function(t,e){var n=this;return n.inited?void n.reset(t):(t=t||{},n.theme=e||"shuyin",n.begin=t.begin||0,n.page=a(void 0!==t.page?t.page:n.page,0),n.max=a(void 0!==t.max?t.max:n.max,3),n.idx=a(void 0!==t.idx?t.idx:n.idx,1),n.idx=a(t.idx-n.begin||n.idx,0),n.total=a(void 0!==t.total?t.total:0,0),n.idx>n.page&&(n.idx=Math.max(0,n.page-1)),n.delCurPage=function(){return n.reset({page:n.page-1,max:n.max,idx:n.idx}),n.idx},n.$container.html(i({page:n.page,max:n.max,idx:n.idx,total:n.total,theme:n.theme})),void(this.inited||(this.bindEvent(),this.inited=!0)))},o.prototype.reset=function(t){t=t||{};var e=a(t.idx,0),n=a(t.max,3),o=a(t.page,0),r=a(t.total,0),p=this;e>o&&(e=Math.max(0,o)),(e!=this.idx||o!=this.page)&&(this.$container,this.$container.html(i({page:o,max:n,idx:e,total:r,theme:p.theme})),this.page=o,this.idx=e,this.max=n,this.total=r,this.inited||(this.bindEvent(),this.inited=!0))},o.prototype.select=function(t,n){n=n||{};var a=this,o=a.page,r=a.max;a.$container.html(i({page:o,max:r,idx:t,total:a.total,theme:a.theme}));var p=a.idx;a.idx=t,n.silent||a.pageFn(t,p,e)},o.prototype.bindEvent=function(){var t=this;t.$container.on("click",".page-btn",function(){var i=$(this),n=a(i.attr("data-index"));t.select(n),e="defClk"}),n&&t.$container.on("dblclick",".page-next-btn,page-prev-btn",function(){$(this).trigger("click")}),t.$container.on("click",".page-prev-btn",function(){var i=$(this);i.hasClass("page-btn-dis")||(e="preClk",t.$container.find(".page-btn.page-btn-cur").prev().trigger("click"))}),t.$container.on("click",".page-next-btn",function(){var i=$(this);i.hasClass("page-btn-dis")||(e="nextClk",t.$container.find(".page-btn.page-btn-cur").next().trigger("click"))})},o.prototype.getIdx=function(){return this.idx},o.prototype.getpage=function(){return this.page},o.prototype.destroy=function(){this.$container.unbind("click dblclick"),this.$container.html("")},o});
;/*!widget/public/departSelect/1.0.0/js/departSelect.js*/
define('widget/public/departSelect/1.0.0/js/departSelect', function(require, exports, module) {

    /**
     * Created by zy on 2017/4/25.
     */
    /**
     * @release true

     * @author zy
     * @date 2017-04-25

     * @description 禁毒一体化平台产品推养而生的顶部。

     * @example 实例化：depart = new Depart(dom);
     * @param dom {Dom} 为顶部组件父级节点，将根据情况append模版，
     *
     * @description 部门选择层级组件，依赖模版 departSelect.tpl

     * @example
     html:
     <div class="depart"></div>

     js:
     var Depart = require('widget/public/departSelect/1.0.0/js/departSelect');
     var depart = new Depart($('.depart'));
     $.getJSON('json/data.json',function(data){
          var type = 0;
           depart.init(data.RECORDS,type,fnConfirm,fnCancel);
      });
     function fnConfirm(){//确定方法

   }
     function fnCancel(){//取消方法，默认为关闭窗口

   }
     @example end
     */

    /*
     * @require 'widget/public/departSelect/1.0.0/css/departSelect.less';
     */


    var tpl_depart = require('widget/public/departSelect/1.0.0.tpl');

    var ctrl = function(container){

        this.$container = (container instanceof $) ? container : $(container);

    }

    ctrl.prototype.init = function(data,type,fnConfirm,fnCancel){
        var _this = this;
        this.data = data;
        this.type = type;
        this.fnConfirm = fnConfirm || 'close' ;
        this.fnCancel = fnCancel || 'close' ;
        var array = new Array();
        for(var i in data){
            if(data[i].parentId == 0){
                var obj = new Object();
                obj.name = data[i].name;
                obj.id = data[i].id;
                obj.num = this.getLength(data,obj.id);
                array.push(obj);
            }
        }

        _this.$container.html(tpl_depart({
            data :array,
            title:'',
            str:'',
            flag :false,
            type : type
        }));

        _this.bindEvent();
    };

    ctrl.prototype.getLength = function(data,id){
        var index = 0;
        for(var i in data){
            if(data[i].parentId == id) index++;
        }
        return index;
    }

    ctrl.prototype.bindEvent = function(){
        var _this = this;
        //确定
        _this.$container.on('click','.confirm',function(){
            if(_this.fnConfirm!='close'){
                fnConfirm();
            }
        });

        //取消
        _this.$container.on('click','.cancel',function(){
            if(_this.fnCancel!='close'){
                fnCancel();
            }
        });

        //选项内容
        _this.$container.on('click','.item',function(){
            var parentId = $(this).attr('data-id');
            var name = $(this).attr('data-name');
            var num = $(this).attr('data-num');
            /*if(num>0 &&(!$(this).parent().find('input').is(':checked')))_this.reset(_this.data,parentId,name,true);
            else*/ $(this).prev().click();
        });

        _this.$container.on('click','.item span',function(e){
            var $this =$(this).parents('.item');
            var parentId = $this.attr('data-id');
            var name = $this.attr('data-name');
            var num = $this.attr('data-num');
            _this.reset(_this.data,parentId,name,true);
            e.stopPropagation();
        });

        //标题
        _this.$container.on('click','.title-item',function(){
            var parentId = $(this).attr('data-id');
            var name = $(this).text();
            _this.reset(_this.data,parentId,name,false);
        });

        //全选
        _this.$container.on('click','.select-all label input',function(){
            var $this = $(this);
            var $ul = $('.select-text').find('ul');
            if($this.is(':checked')){
                _this.$container.find('.select-content-ul input').each(function(){
                    $(this).prop('checked',true);
                    if(!$('.select-text ul li[data-id="'+$(this).next().attr("data-id")+'"]').length>0)
                        $ul.append('<li title="'+$(this).next().attr("data-name")+'" data-id="'+$(this).next().attr("data-id")+'">'+$(this).next().attr("data-name")+'<em>×</em></li>')
                });
            }else{
                _this.$container.find('.select-content-ul input').each(function(){
                    $(this).prop('checked',false);
                    $ul.find('li[data-id="'+$(this).next().attr("data-id")+'"]').remove();
                });
            }
        });

        //选项
        _this.$container.on('click','.select-content-ul li input',function(){
            var $this = $(this);
            var $ul = $('.select-text').find('ul');
            if($this.is(':checked')){
                if(_this.type != '1'&&$('.select-text>ul>li').length>0){
                    $this.prop('checked',false);
                    return;
                }
                $ul.append('<li title="'+$(this).next().attr("data-name")+'" data-id="'+$(this).next().attr("data-id")+'">'+$(this).next().attr("data-name")+'<em>×</em></li>')
            }else{
                $ul.find('li[data-id="'+$(this).next().attr("data-id")+'"]').remove();
            }
            _this.checkedAll();
        });

        //em
        _this.$container.on('click','.select-text ul li em',function(){
            var id = $(this).parent().attr('data-id');
            $('.select-content-ul').each(function(){
                $(this).find('li a[data-id="'+id+'"]').prev().prop('checked',false);
            });
            $(this).parent().remove();
            _this.checkedAll();
        });
    }

    //判断是否全选
    ctrl.prototype.checkedAll = function(){
        var flag = true;//默认是全选
        $('.select-content-ul li input').each(function(){
            if(!$(this).is(':checked')){
                flag = false;
            }
        });
        if(flag) $('.select-all').find('input').prop('checked',true);
        else $('.select-all').find('input').prop('checked',false);
    }

    ctrl.prototype.reset = function(data,parentId,name,flag){//flag true:点击列表 flase：点击title
        var html = '';
        var str = $('.select-text ul').html();
        var checkFlag = true;
        var array = new Array();
        for(var i in data){
            if(data[i].parentId == parentId){
                var obj = new Object();
                obj.name = data[i].name;
                obj.id = data[i].id;
                obj.num = this.getLength(data,obj.id);
                if($('.select-text ul').find('li[data-id="'+obj.id+'"]').length>0)
                    obj.checked = true;
                else{
                    checkFlag = false;
                    obj.checked = false;
                }
                array.push(obj);
            }
        }

        var children = this.$container.find('.select-title').children()
        if(flag){
            for(var i=0;i<children.length;i++){
                var text = children.eq(i).text();
                html +='<a href="javascript:void(0)" class="title-item" data-id="'+children.eq(i).attr("data-id")+'">'+text+'</a> &gt; ';
            }
            html += '<span data-id="'+parentId+'">'+name+'</span>';
        }else{
            for(var i=0;i<children.length;i++){
                var text = children.eq(i).text();
                if(children.eq(i).attr("data-id") == parentId){
                    html += '<span data-id="'+parentId+'">'+name+'</span>';
                    break;
                }
                else
                    html +='<a href="javascript:void(0)" class="title-item" data-id="'+children.eq(i).attr("data-id")+'">'+text+'</a> &gt; ';
            }
        }

        this.$container.html(tpl_depart({
            data :array,
            title:html,
            str:str || '',
            flag :checkFlag,
            type:this.type
        }));
    }

    return ctrl;


});