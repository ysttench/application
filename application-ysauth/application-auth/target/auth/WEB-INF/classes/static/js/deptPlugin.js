function  getAllDepartment(dom,style){
    $.ajax({
        url : rootPath + "/sysdepartment/getAllDepartmentTreeData",
        type : "post",
        data : {},
        traditional : true,
        dataType : "json",
        async : false,
        success : function(data) {
            if(data == null){
                layer.msg("暂无部门数据");
            }
            data=JSON.parse(data);
            var $this = dom;
            $this.empty();
            ReactDOM.render(React.createElement(antd.Cascader, {
                options: data[0].children,
                expandTrigger:'hover',
                popupClassName: '',
                placeholder: $this.prev().attr('data_name')||'',
                onChange: function (value) {
                    if(value.length == 0) {
                        $this.prev().val('');
                    } else {
                        $this.prev().val(value[value.length - 1]);
                    }
                },
                style:  style || {width:'100%',marginLeft:'-4px'},
                changeOnSelect: true
            }), $this[0]);
        
        },
        error : function() {
        }
    });
}