var pager = {
		pagerid 			: 'pagerNav', //分页容器ID
		mode				: 'link', //模式(link 或者 click)
		pno					: 1, //当前页码
		total				: 1, //总页码
		totalRecords		: 0, //总数据条数
		isShowFirstPageBtn	: true, //是否显示首页按钮
		isShowLastPageBtn	: true, //是否显示尾页按钮
		isShowPrePageBtn	: true, //是否显示上一页按钮
		isShowNextPageBtn	: true, //是否显示下一页按钮
		isShowTotalPage 	: false, //是否显示总页数
		isShowTotalRecords 	: false, //是否显示总记录数
		isGoPage 			: false,//是否显示页码跳转输入框,默认不显示
		hrefFormer			: '', //链接前部
		hrefLatter			: '', //链接尾部
		gopageWrapId		: 'pager_gopage_wrap',
		gopageButtonId		: 'pager_btn_go',
		gopageTextboxId		: 'pager_btn_go_input',
		i18n                : 'zh',  // 语言：zh和en分别对应中文和英文，默认中文
		zhLang				: {
			firstPageText			: '«',
			firstPageTipText		: '首页',
			lastPageText			: '»',
			lastPageTipText			: '尾页',
			prePageText				: '‹',
			prePageTipText			: '上一页',
			nextPageText			: '›',
			nextPageTipText			: '下一页',
			totalPageBeforeText		: '共',
			totalPageAfterText		: '页',
			totalRecordsAfterText	: '条数据',
			gopageBeforeText		: '转到',
			gopageButtonOkText		: '确定',
			gopageAfterText			: '页',
			buttonTipBeforeText		: '第',
			buttonTipAfterText		: '页'				
		},
		enLang				: {
			firstPageText			: '«',
			firstPageTipText		: 'First',
			lastPageText			: '»',
			lastPageTipText			: 'Last',
			prePageText				: '‹',
			prePageTipText			: 'Prev',
			nextPageText			: '›',
			nextPageTipText			: 'Next',
			totalPageBeforeText		: 'Total',
			totalPageAfterText		: 'Pages',
			totalRecordsAfterText	: 'Records',
			gopageBeforeText		: 'To',
			gopageButtonOkText		: 'Go',
			gopageAfterText			: 'Page',
			buttonTipBeforeText		: '',
			buttonTipAfterText		: ''
		},
		//链接算法（当处于link模式）,参数n为页码
		getLink	: function(n){
			//这里的算法适用于比如：
			//hrefFormer=http://www.xx.com/news/20131212
			//hrefLatter=.html
			//那么首页（第1页）就是http://www.xx.com/news/20131212.html
			//第2页就是http://www.xx.com/news/20131212_2.html
			//第n页就是http://www.xx.com/news/20131212_n.html
			if(n == 1){
				return this.hrefFormer + this.hrefLatter;
			}
			return this.hrefFormer + '_' + n + this.hrefLatter;
		},
		//页码单击事件处理函数（当处于mode模式）,参数n为页码
		click	: function(n){
			// TODO 这里自己实现分页点击事件，这里可以用this或者pager访问pager对象
			return false;
		},
		//获取href的值（当处于mode模式）,参数n为页码
		getHref	: function(n){
			// TODO 这里自己实现取分页元素的链接
			return 'javascript:void(0);';
		},
		//跳转框得到输入焦点时
		focus_gopage : function (){
			var btnGo = $('#'+this.gopageButtonId);
			$('#'+this.gopageTextboxId).attr('hideFocus',true);
			btnGo.show();
			btnGo.css('left','0px');
			$('#'+this.gopageWrapId).css('border-color','#0099cc');
			btnGo.animate({left: '+=44'}, 50,function(){
				//$('#'+this.gopageWrapId).css('width','88px');
			});
		},
		//跳转框失去输入焦点时
		blur_gopage : function(){
			var _this = this;
			setTimeout(function(){
				var btnGo = $('#'+_this.gopageButtonId);
				btnGo.animate({
				    left: '-=44'
				  }, 100, function(){
					  btnGo.css('left','0px');
					  btnGo.hide();
					  $('#'+_this.gopageWrapId).css('border-color','#DFDFDF');
				  });
			},400);
		},
		//跳转输入框按键操作
		keypress_gopage : function(){
			var event = arguments[0] || window.event;
			var code = event.keyCode || event.charCode;
			//delete key
			if(code == 8) return true;
			//enter key
			if(code == 13){
				pager.gopage();
				return false;
			}
			//copy and paste
			if(event.ctrlKey && (code == 99 || code == 118)) return true;
			//only number key
			if(code<48 || code>57)return false;
			return true;
		},
		//跳转框页面跳转
		gopage : function(){
			var str_page = $('#'+this.gopageTextboxId).val();
			if(isNaN(str_page)){
				$('#'+this.gopageTextboxId).val(this.next);
				return;
			}
			var n = parseInt(str_page);
			if(n < 1) n = 1;
			if(n > this.total) n = this.total;
			if(this.mode == 'click'){
				this._clickHandler(n);
			}else{
				window.location = this.getLink(n);
			}
		},
		//不刷新页面直接手动调用选中某一页码
		selectPage : function(n){
			this._config['pno'] = n;
			this.generPageHtml(this._config,true);
		},
		//生成控件代码
		generPageHtml : function(config,enforceInit){
			if(enforceInit || !this.inited){
				this.init(config);
			}
			
			var lang = this.zhLang;
			if (this.i18n == "en") {
				lang = this.enLang;
			}
			
			var str_first='',str_prv='',str_next='',str_last='';
			if(this.isShowFirstPageBtn){
				if(this.hasPrv){
					str_first = '<a '+this._getHandlerStr(1)+' class="previous" title="'
						+(lang.firstPageTipText || lang.firstPageText)+'">'+lang.firstPageText+'</a>';
				}else{
					str_first = '<span class="disabled" title="'
						+(lang.firstPageTipText || lang.firstPageText)+'">'+lang.firstPageText+'</span>';
				}
			}
			if(this.isShowPrePageBtn){
				if(this.hasPrv){
					str_prv = '<a '+this._getHandlerStr(this.prv)+' class="previous" title="'
						+(lang.prePageTipText || lang.prePageText)+'">'+lang.prePageText+'</a>';
				}else{
					str_prv = '<span class="disabled" title="'
						+(lang.prePageTipText || lang.prePageText)+'">'+lang.prePageText+'</span>';
				}
			}
			if(this.isShowNextPageBtn){
				if(this.hasNext){
					str_next = '<a '+this._getHandlerStr(this.next)+' class="next" title="'
						+(lang.nextPageTipText || lang.nextPageText)+'">'+lang.nextPageText+'</a>';
				}else{
					str_next = '<span class="disabled" title="'
						+(lang.nextPageTipText || lang.nextPageText)+'">'+lang.nextPageText+'</span>';
				}
			}
			if(this.isShowLastPageBtn){
				if(this.hasNext){
					str_last = '<a '+this._getHandlerStr(this.total)+' class="next" title="'
						+(lang.lastPageTipText || lang.lastPageText)+'">'+lang.lastPageText+'</a>';
				}else{
					str_last = '<span class="disabled" title="'
						+(lang.lastPageTipText || lang.lastPageText)+'">'+lang.lastPageText+'</span>';
				}
			}
			var str = '';
			var dot = '<span>...</span>';
			var total_info='';
			if(this.isShowTotalPage || this.isShowTotalRecords){
				total_info = '<span>'+lang.totalPageBeforeText;
				if(this.isShowTotalPage){
					total_info += this.total + lang.totalPageAfterText;
					if(this.isShowTotalRecords){
						total_info += '/';
					}
				}
				if(this.isShowTotalRecords){
					total_info += this.totalRecords + lang.totalRecordsAfterText;
				}
				
				total_info += '</span>';
			}
			
			var gopage_info = '';
			if(this.isGoPage){
				gopage_info = '&nbsp;'+lang.gopageBeforeText+'<span id="'+this.gopageWrapId+'" class="gopage_wrap">'+
					'<input type="button" id="'+this.gopageButtonId+'" class="btn_go" onclick="pager.gopage()" value="'+lang.gopageButtonOkText+'" />'+
					'<input type="text" id="'+this.gopageTextboxId+'" class="btn_go_input" onfocus="pager.focus_gopage()"  onkeypress="return pager.keypress_gopage(event);"   onblur="pager.blur_gopage()" value="'+this.next+'" /></span>'+lang.gopageAfterText;
			}
			
			//分页处理
			if(this.total <= 8){
				for(var i=1;i<=this.total;i++){
					if(this.pno == i){
						str += '<a '+this._getHandlerStr(i)+' class="active" title="'
							+lang.buttonTipBeforeText + i + lang.buttonTipAfterText+'">'+i+'</a>';
					}else{
						str += '<a '+this._getHandlerStr(i)+' title="'
							+lang.buttonTipBeforeText + i + lang.buttonTipAfterText+'">'+i+'</a>';
					}
				}
			}else{
				if(this.pno <= 5){
					for(var i=1;i<=7;i++){
						if(this.pno == i){
							str += '<a '+this._getHandlerStr(i)+' class="active" title="'
								+lang.buttonTipBeforeText + i + lang.buttonTipAfterText+'">'+i+'</a>';
						}else{
							str += '<a '+this._getHandlerStr(i)+' title="'+
								lang.buttonTipBeforeText + i + lang.buttonTipAfterText+'">'+i+'</a>';
						}
					}
					str += dot;
				}else{
					str += '<a '+this._getHandlerStr(1)+' title="'
						+lang.buttonTipBeforeText + '1' + lang.buttonTipAfterText+'">1</a>';
					str += '<a '+this._getHandlerStr(2)+' title="'
						+lang.buttonTipBeforeText + '2' + lang.buttonTipAfterText +'">2</a>';
					str += dot;
					
					var begin = this.pno - 2;
					var end = this.pno + 2;
					if(end > this.total){
						end = this.total;
						begin = end - 4;
						if(this.pno - begin < 2){
							begin = begin-1;
						}
					}else if(end + 1 == this.total){
						end = this.total;
					}
					for(var i=begin;i<=end;i++){
						if(this.pno == i){
							str += '<a '+this._getHandlerStr(i)+' class="active" title="'
								+lang.buttonTipBeforeText + i + lang.buttonTipAfterText+'">'+i+'</a>';
						}else{
							str += '<a '+this._getHandlerStr(i)+' title="'
								+lang.buttonTipBeforeText + i + lang.buttonTipAfterText+'">'+i+'</a>';
						}
					}
					if(end != this.total){
						str += dot;
					}
				}
			}
			
			str = str_first + str_prv + str + str_next + str_last + total_info + gopage_info;
			$("#"+this.pagerid).html(str);
		},
		//分页按钮控件初始化
		init : function(config){
			this.pno = isNaN(config.pno) ? 1 : parseInt(config.pno);
			this.total = isNaN(config.total) ? 1 : parseInt(config.total);
			this.totalRecords = isNaN(config.totalRecords) ? 0 : parseInt(config.totalRecords);
			if(config.pagerid){this.pagerid = config.pagerid;}
			if(config.mode){this.mode = config.mode;}
			if(config.gopageWrapId){this.gopageWrapId = config.gopageWrapId;}
			if(config.gopageButtonId){this.gopageButtonId = config.gopageButtonId;}
			if(config.gopageTextboxId){this.gopageTextboxId = config.gopageTextboxId;}
			if(config.isShowFirstPageBtn != undefined){this.isShowFirstPageBtn=config.isShowFirstPageBtn;}
			if(config.isShowLastPageBtn != undefined){this.isShowLastPageBtn=config.isShowLastPageBtn;}
			if(config.isShowPrePageBtn != undefined){this.isShowPrePageBtn=config.isShowPrePageBtn;}
			if(config.isShowNextPageBtn != undefined){this.isShowNextPageBtn=config.isShowNextPageBtn;}
			if(config.isShowTotalPage != undefined){this.isShowTotalPage=config.isShowTotalPage;}
			if(config.isShowTotalRecords != undefined){this.isShowTotalRecords=config.isShowTotalRecords;}
			if(config.isGoPage != undefined){this.isGoPage=config.isGoPage;}
			if(config.i18n){this.i18n=config.i18n;}
			if(config.zhLang){
				for(var key in config.zhLang){
					this.zhLang[key] = config.zhLang[key];
				}
			}
			if(config.enLang){
				for(var key in config.enLang){
					this.enLang[key] = config.enLang[key];
				}
			}
			this.hrefFormer = config.hrefFormer || '';
			this.hrefLatter = config.hrefLatter || '';
			if(config.getLink && typeof(config.getLink) == 'function'){this.getLink = config.getLink;}
			if(config.click && typeof(config.click) == 'function'){this.click = config.click;}
			if(config.getHref && typeof(config.getHref) == 'function'){this.getHref = config.getHref;}
			if(!this._config){
				this._config = config;
			}
			//validate
			if(this.pno < 1) this.pno = 1;
			this.total = (this.total <= 1) ? 1: this.total;
			if(this.pno > this.total) this.pno = this.total;
			this.prv = (this.pno<=2) ? 1 : (this.pno-1);
			this.next = (this.pno >= this.total-1) ? this.total : (this.pno + 1);
			this.hasPrv = (this.pno > 1);
			this.hasNext = (this.pno < this.total);
			
			this.inited = true;
		},
		_getHandlerStr : function(n){
			if(this.mode == 'click'){
				return 'href="'+this.getHref(n)+'" onclick="return pager._clickHandler('+n+')"';
			}
			//link模式，也是默认的
			return 'href="'+this.getLink(n)+'"';
		},
		_clickHandler	: function(n){
			var res = false;
			if(this.click && typeof this.click == 'function'){
				res = this.click.call(this,n) || false;
			}
			return res;
		}
};