(function(jQuery){
	var Toast = function(optionsParam){  
		this.options = {
			'context':	$('body'),//上下文  
			'message':	'消息内容...',//显示内容  
			'durationTime':4000,//持续时间  
			'left':null,//距容器左边的距离  
			'top':null,//距容器上方的距离  
		};
		this.msgEntity = null;
		
		//初始化显示的位置内容等  
		this.init = function(optionsParam){  
			var optionsTemp = jQuery.extend(this.options,optionsParam);
			
			//设置消息体  
			var msgDIV = new Array();  
			msgDIV.push('<div id="toastMessage">');  
			msgDIV.push('<span>'+this.options.message+'</span>');  
			msgDIV.push('</div>');  
			this.msgEntity = $(msgDIV.join('')).appendTo(this.options.context);  
			
			//设置消息样式  
			var left = this.options.left == null ? this.options.context.width()/2 - this.msgEntity.find('span').width()/2 : this.options.left;  
			var top = this.options.top == null ? (screen.availHeight/4)*1 : this.options.top;  
			this.msgEntity.css({position:'absolute',top:top,'z-index':'99',left:left,'background-color':'black',color:'white','font-size':'18px',padding:'10px',margin:'10px','border-radius':'5px'});  
			this.msgEntity.hide();  
		}, 
		this.init(optionsParam);
		
		//显示动画  
		this.show = function(){  
			var thisTemp = this;
			this.msgEntity.fadeIn(this.options.durationTime/4);  
			this.msgEntity.fadeOut(this.options.durationTime*3/4,function(){
				thisTemp.msgEntity.remove();
			 });  
		}  
	} 
	
	//--------------
	//----jQuery.toast({'message':'错误消息'});----------
	//--------------
	jQuery.extend({
		toast: function(optionsParam) {
			return new Toast(optionsParam);
		},
	});
})(jQuery);

