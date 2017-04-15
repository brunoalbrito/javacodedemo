
//--------------------
//------usage---------
//--------------------

//jQuery.confirmDialog({
//		"title" : "对话框",
//		"beforeInitHtmlFunc":function(){
//			//alert("....");
//		},
//		"content" : '<div class="layer_editor">'+
//					 ' <div> <span>网址：</span>'+
//						'<input type="text">'+
//					 ' </div>'+
//					 ' <div> <span>网址名称：</span>'+
//					'	<input type="text">'+
//					'  </div>'+
//					 ' <div class="layer_ed_img"> <span>网址名称：</span>'+
//						'<input type="text">'+
//						'<label for="layerFriendImg" class="bde disib cm_browse"> 浏 览'+
//						'<input type="file" dir="rtl" id="layerFriendImg">'+
//						'</label>'+
//						'<p class="fz14">（限制大小和格式）</p>'+
//						'<img src="" alt="图片预览" class="layer_ed_logo fz12"> </div>'+
//					 ' <div class="layer_position"> <span>排列位置：</span>'+
//						'<input type="text">'+
//						'&nbsp; <strong class="fz12">从数字1开始排列</strong> </div>'+
//					'</div>',
//		"afterInitHtmlFunc":function(){
//			//alert("....");
//		},
//		"rightBtnFunc":function(){
//			
//		}
//});

(function(jQuery){
	var ConfirmDialog = function(){
		this.htmlTempJQ = null;
		this.options = {
			"ctxSelect" : 'body',
			"title" : "对话框",
			"leftBtnName" : "取 &nbsp; 消",
			"rightBtnName" : "确 &nbsp; 认",
			"beforeInitHtmlFunc":function(dialogObj){},
			"afterInitHtmlFunc":function(dialogObj){},
			"leftBtnFunc":function(dialogObj){},
			"rightBtnFunc":function(dialogObj){},
			"content" : "内容...",
		};
		
		this.empty = function(data){
			if(typeof data=='undefined'||data==null||data=="")
			{
				return true;
			}
			return false;
		};
		
		this.close = function(){
			this.htmlTempJQ.remove();
		};
		
		this.leftBtnFuncAdapter = function(){
			if(this.options.leftBtnFunc(this.htmlTempJQ)!==false)
			{
				this.close();
			}
		};
		
		this.rightBtnFuncAdapter = function(){
			if(this.options.rightBtnFunc(this.htmlTempJQ)!==false)
			{
				this.close();
			}
		};
		
		this.initHtml = function()
		{
			var htmlTemp = '<div class="layer_thickness">'+
			  '<div class="layer_window clearfix">'+
				'<h1 class="clearfix"><i></i><span>'+this.options.title+'</span>'+
					'<a href="javascript:void(0);" title="关闭" class="layer_shut bg_a floatr head_close"></a>'+
				'</h1>'+
				'<div class="confirmBody">'+
					this.options.content+
				'</div>'+
				'<div class="layer_btn">';
				if(!this.empty(this.options.leftBtnName))
				{
					htmlTemp += ' <a class="leftBtn">'+this.options.leftBtnName+'</a>';
				}
				if(!this.empty(this.options.rightBtnName))
				{
					htmlTemp += ' <a  class="rightBtn">'+this.options.rightBtnName+'<a>';
				}
				htmlTemp += '</div>';
			 ' </div>'+
			'</div>';
			return htmlTemp;
		};
		
		this.start = function(optionsParam)
		{
			var optionsTemp = jQuery.extend(this.options,optionsParam);
			var thisTemp = this;
			
			//
			this.options.beforeInitHtmlFunc(this.htmlTempJQ);
			var htmlTemp = this.initHtml();
			this.htmlTempJQ = $(htmlTemp);
			this.htmlTempJQ.appendTo(this.options.ctxSelect);//
			this.options.afterInitHtmlFunc(this.htmlTempJQ);
			//编辑弹层
			$(window).scroll(function(){
				if($('.layer_thickness') != null){
					$('.layer_thickness .layer_window').css({
						'top': ($(window).scrollTop() + 100) + 'px'
					})
				}
			});
			
			//左侧按钮
			var leftBtnTemp = this.htmlTempJQ.find(".leftBtn");
			if(!this.empty(leftBtnTemp))
			{
				leftBtnTemp.click( function () {
					thisTemp.leftBtnFuncAdapter();
				});
			}
			
			//右侧按钮
			var rightBtnTemp = this.htmlTempJQ.find(".rightBtn");
			if(!this.empty(rightBtnTemp))
			{
				rightBtnTemp.click( function () {
					thisTemp.rightBtnFuncAdapter();
				});
			}
			
			//顶部关闭按钮
			var headCloseBtnTemp = this.htmlTempJQ.find(".head_close");
			if(!this.empty(headCloseBtnTemp))
			{
				headCloseBtnTemp.click( function () {
					thisTemp.close();
				});
			}
		};
	};
	
	jQuery.extend({
		confirmDialog: function(optionsParam) {
			var confirmDialog = new ConfirmDialog();
			optionsParam.ctxSelect = ".wrapper";
			confirmDialog.start(optionsParam);
		},
		confirmDelDialog: function(optionsParam) {
			var confirmDialog = new ConfirmDialog();
			optionsParam.ctxSelect = ".wrapper";
			optionsParam.content = "<p class='list_1'>"+optionsParam.content+"</p>";
			confirmDialog.start(optionsParam);
		},
		
	});  
})(jQuery);
