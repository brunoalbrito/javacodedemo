
/**
 * 左侧菜单
 */
function init_left_menu_bar()
{
	// 左侧菜单，一级菜单 
	$('.slide_nav li').click(function(){
		$(this).siblings().attr('data-id','');
		$('.set_up').hide();
		$('.slide_nav li').attr('id','');
		$('.slide_nav i').attr('id','');

		if ($(this).attr('data-id') == 'ckSet') {
			$(this).children('i').attr('id','');
			$(this).attr('data-id','');
			$(this).children('.set_up').hide();
			$(this).attr('id','');

		} else {
			$(this).attr('data-id','ckSet');
			$(this).children('.set_up').show();
			$(this).children('i').attr('id',$(this).children('i').attr('class'));
			$(this).attr('id','bgcolor');
		}
	});
	
	// 左侧菜单，二级菜单 
	$('.set_up ol li').hover(function(){
		$(this).addClass('ck_selected');
	},function(){
		$(this).removeClass('ck_selected');
	})
}

/**
 * 列表“头部右侧”按钮
 */
function table_head_right_btn()
{
	$('.cd_icon a').hover(function(){
		$('.cd_icon i').eq($(this).index()).addClass('disib');
	},function(){
		$('.cd_icon i').eq($(this).index()).removeClass('disib');
	});
}

$(document).ready(function(){
	init_left_menu_bar();//左侧菜单
	table_head_right_btn();//列表“头部右侧”按钮

	
	//批量选中
	$(".ck_all").click( function () {
		var checkedCount = $(".cd_table tbody tr input:checked").size();
		if(checkedCount>0)
		{
			$(".cd_table tbody tr input:checkbox").prop('checked',false);
		}
		else
		{
			$(".cd_table tbody tr input:checkbox").prop('checked',true);
		}
	});

});

var KindEditorHelper = {
	/**
	 	var editor = KindEditorHelper.createEditor('textarea[name="description"]',{
			uploadJson : '<if condition="I('get.id')"> {:U('Products/edit')}<else />{:U('Products/add')} </if>',
		});
	 */
	createEditor : function(selector,options){
		 //富文本框插件
		 var defaultConfig = {
			autoHeightMode : true,
			width: '100%',
			height: '300px',
			resizeType: 1,
			pasteType : 2,
			urlType : 'absolute',
			//文件上传
			allowFileManager : false,
			allowImageRemote : false,
			filePostName:'file',
			formatUploadUrl:true,
			uploadJson : '',
			extraFileUploadParams:{'is_upload': '1','ajax': '1','is_kind': '1'},
			afterCreate : function() {
				this.loadPlugin('autoheight');
			},
			items : [ 
			   		  /*'undo', 'redo', '|', */'cut', 'copy', 'paste',
			   		/* 'plainpaste',  */'wordpaste', '|', /* 'justifyleft', 'justifycenter', 'justifyright',
			   		'justifyfull',  */'insertorderedlist', 'insertunorderedlist', 'indent', 'outdent', 'subscript',
			   		'superscript', 'clearhtml'/* , 'quickformat', 'selectall'*/,  '|', 'formatblock', 'fontname', 'fontsize', '|', /* '/', */
			   		'forecolor', 'hilitecolor', 'bold','italic', 'underline', 'strikethrough', /* 'lineheight', */ 'removeformat', '|', 
			   		'image', /*'multiimage','flash', 'media', */
			   		'table', 'hr',/* 'baidumap', */ 'pagebreak',
			   		/* 'anchor', */ 'link', 'unlink'/* , '|', 'about' */,'|','source'
			   	],
		};
		
		var settings = jQuery.extend({}, defaultConfig, options);
		var uploadJson = settings.uploadJson;
		if(uploadJson==null||uploadJson==''||typeof uploadJson=='undefined')
		{
			settings.allowImageUpload = false;	
			settings.allowFileManager = false;	
		}
		return KindEditor.create(selector,settings);
	},
	
	/**
	   KindEditorHelper.uploadButton(".testUploadBtn",{fieldName : 'file',afterUploadFunc : function(data) {	console.log(data);	}});
	 */
	uploadButton:function(selector,options){
		var afterUploadFunc = options.afterUploadFunc;
		delete options.afterUploadFunc;
		var defaultConfig = {
			button : KindEditor(selector)[0],
			fieldName : 'imgFile',
			form : null,
			target : null,
			extraParams:{'is_upload': '1','ajax': '1'},
			width: 60,
			afterUpload : function(data) {
				if (afterUploadFunc) {
					if(Object.prototype.toString.call(afterUploadFunc) === '[object Function]')	{
						afterUploadFunc(data);
						return;
					}
				}
				if (data.status === 0) {
					var dataTemp = data.data;
					//console.log(dataTemp);
				} else {
					alert(data.info);
				}
			},
			afterError : function(html) {
			}
		};
		var settings = jQuery.extend({}, defaultConfig, options);
		var uploadbutton = KindEditor.uploadbutton(settings);
		uploadbutton.fileBox.change(function(e) {
			uploadbutton.submit();
		});
	}
}