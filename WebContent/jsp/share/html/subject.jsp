<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>  
<c:set var="ctx" value="${pageContext.request.contextPath}" ></c:set>
<!DOCTYPE html >
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<meta content="utf-8" http-equiv="Content-Language">
	<meta content="width=device-width,initial-scale=1.0,user-scalable=no" name="viewport">
	<title>专题</title>
	<link rel="stylesheet" href="${ctx }/jsp/share/css/mobile.css">
	<jsp:include page="/jsp/base/base.jsp"></jsp:include>
	<script type="text/javascript" src="http://res.wx.qq.com/open/js/jweixin-1.0.0.js"></script> 

<script type="text/javascript">
var timestamp1;
var nonceStr1;
var signature1;
function share(){
	var value={};
	value.url=location.href.split('#')[0];
	$.ajax({
		async:false,
        type: "POST",
        dataType: "json",
        url: "${ctx}/weixin/query.do",
        data:value,
        success: function (result) {
        	timestamp1=result.timestamp;
        	nonceStr1=result.noncestr;
        	signature1=result.signature;
        },
        error: function(result) {
            alert("error");
         }
    });
	wx.config({
	    debug: false, // 开启调试模式,调用的所有api的返回值会在客户端alert出来，若要查看传入的参数，可以在pc端打开，参数信息会通过log打出，仅在pc端时才会打印。
	    appId: 'wxa44b5c800ddaf616', // 必填，公众号的唯一标识
	    timestamp: timestamp1, // 必填，生成签名的时间戳
	    nonceStr: nonceStr1, // 必填，生成签名的随机串
	    signature: signature1,// 必填，签名，见附录1
	    jsApiList: ['onMenuShareAppMessage','onMenuShareTimeline','onMenuShareQQ','onMenuShareWeibo','onMenuShareQZone'] // 必填，需要使用的JS接口列表，所有JS接口列表见附录2
	});
	wx.ready(function(){
		var title="${channel.name }";
		var desc="${post.detail }";
		var link= 'http://orthoday.com/SD/channel/shareQuery.out?gid=${post.gid}';// 分享链接
	    var imgUrl= 'http://orthoday.com/SD/jsp/share/image/1024.jpg'; // 分享图标
		wx.onMenuShareAppMessage({
		    title: title, // 分享标题
		    desc:desc, // 分享描述
		    link: link, // 分享链接
		    imgUrl: imgUrl, // 分享图标
		    type: '', // 分享类型,music、video或link，不填默认为link
		    dataUrl: '', // 如果type是music或video，则要提供数据链接，默认为空
		    success: function () { 
// 		        alert("嘿嘿");
		    },
		    cancel: function () { 
		        // 用户取消分享后执行的回调函数
		    }
		});
		wx.onMenuShareTimeline({
		    title:title, // 分享标题
		    link: link, // 分享链接
		    imgUrl:imgUrl, // 分享图标
		    success: function () { 
		        // 用户确认分享后执行的回调函数
		    },
		    cancel: function () { 
		        // 用户取消分享后执行的回调函数
		    }
		});
		wx.onMenuShareQQ({
		    title: title, // 分享标题
		    desc:desc, // 分享描述
		    link:link, // 分享链接
		    imgUrl:imgUrl, // 分享图标
		    success: function () { 
		       // 用户确认分享后执行的回调函数
		    },
		    cancel: function () { 
		       // 用户取消分享后执行的回调函数
		    }
		});
		wx.onMenuShareWeibo({
		    title: title, // 分享标题
		    desc:desc, // 分享描述
		    link: link, // 分享链接
		    imgUrl: imgUrl, // 分享图标
		    success: function () { 
		       // 用户确认分享后执行的回调函数
		    },
		    cancel: function () { 
		        // 用户取消分享后执行的回调函数
		    }
		});
		wx.onMenuShareQZone({
		    title: title, // 分享标题
		    desc: desc, // 分享描述
		    link: link, // 分享链接
		    imgUrl:imgUrl, // 分享图标
		    success: function () { 
		       // 用户确认分享后执行的回调函数
		    },
		    cancel: function () { 
		        // 用户取消分享后执行的回调函数
		    }
		});
	    // config信息验证后会执行ready方法，所有接口调用都必须在config接口获得结果之后，config是一个客户端的异步操作，所以如果需要在页面加载时就调用相关接口，则须把相关接口放在ready函数中调用来确保正确执行。对于用户触发时才调用的接口，则可以直接调用，不需要放在ready函数中。
	});
}
var is_weixin = (function(){
	return navigator.userAgent.toLowerCase().indexOf('micromessenger') !== -1
})();
function gotoUrl(){
	var type = browserRedirect();
	if (is_weixin) {
		location.href ="http://a.app.qq.com/o/simple.jsp?pkgname=com.naton.bonedict";
// 		var state = {
// 				title: "123",
// 				url: "/SD/channel/shareQuery.out?down=1&gid=${post.gid }",
// 				otherkey: ""
// 			};
// 		window.history.pushState(state, document.title, "/SD/channel/shareQuery.out?down=1&gid=${post.gid }");
	}else if (type == 'ios'){
		location.href ='Orthopedia://?${post.gid }';
        setTimeout(function() {
        	location.href = 'https://appsto.re/cn/C-2T7.i'
        }, 30);
	}
	else if (type == 'android'){
		location.href ='orthopedia://splash';
		setTimeout(function() {
            location.href = 'https://dn-orthopedia.qbox.me/orthopedia.apk'
        }, 30);
	}
}

function toApp(){
	var ua = navigator.userAgent.toLowerCase();	
	var is_weixin = (function(){
		return navigator.userAgent.toLowerCase().indexOf('micromessenger') !== -1
	})();
	var hint = document.getElementById('weixin-tip');
	var close = document.getElementById('close');
	if (is_weixin) {
// 		window.history.replaceState(null, document.title, "/SD/channel/shareQuery.out?go=1&gid=${post.gid }");
		if("${param.go}"=="1"){
	 		var winHeight = typeof window.innerHeight != 'undefined' ? window.innerHeight : document.documentElement.clientHeight; //兼容IOS，不需要的可以去掉
	 		hint.style.height = winHeight + 'px'; //兼容IOS弹窗整屏
	 		hint.style.display = 'block';
	 		close.onclick = function() {
	 			hint.style.display = 'none';
			}
		}else{
			location.href ='${ctx}/channel/shareQuery.out?go=1&gid=${post.gid }';
		}
	}else if (/iphone|ipad|ipod/.test(ua)) {
        location.href ='Orthopedia://?${post.gid }';
        setTimeout(function() {
        	location.href = 'https://appsto.re/cn/C-2T7.i'
        }, 30);
		    
	} else if(/android/.test(ua)) {
		window.location ='orthopedia://splash';
		setTimeout(function() {
//             location.href = 'https://dn-orthopedia.qbox.me/orthopedia.apk'
            alert("如果未跳转，请先下载");
        }, 30);
		
	}
}
$(function (){
	share();
	if (!is_weixin) {
		if("${param.down}"=="1"){
			myUpload();
			return;
		}
	}
	$.ajax({
        type: "POST",
        dataType: "json",
        url: "${ctx}/textbook/getSubjectForWebPage?gid=${param.gid}",
        success: function (result) {
        	console.info(result);
        	$("#pic").append("<img width='100%' src='http://123.59.41.16/SDpic/common/picSelect?gid="+result.result_data.pic+"'/>");
        	$("#title").append(result.result_data.description);
        	for(var i=0;i<result.result_data.markList.length;i++){
        		var myObject=result.result_data.markList[i];
        		$("#mark").append("<span >"+myObject.mark+"</span>");
        		$("#wrap").append("<div class='marks'>"+myObject.mark+"</div>");
        		for(var j=0;j<myObject.textbookList.length;j++){
        			var textbook=myObject.textbookList[j];
        			var str='<div class="content"><div class="contentPic">';
        			str+='<img width="100px" height="80px" src="http://123.59.41.16/SDpic/common/picSelect?gid='+textbook.pic+'"/>';
        			str+='</div><div class="textbook"><div class="title">'+textbook.title+'</div>';
        			str+='<div class="remark"><div class="author">'+textbook.author+'</div><div class="num">'+textbook.readNumber+'浏览</div></div></div>';
        			str+='<div style="clear:both"></div></div>';
        			$("#wrap").append(str);
        		}
        	}
        },
        error: function(result) {
            alert("error");
         }
    });
	
});
</script>
<style type="text/css">
#shareImg{
	overflow:hidden;
	position:fixed;
	bottom:100%;
}
.down{
	margin-bottom:0px;
}
#weixin-tip{display:none;position:fixed;left:0;top:0;background:rgba(0,0,0,0.8);filter:alpha(opacity=80);width:100%;height:100%;z-index:100;}
#weixin-tip p{text-align:center;margin-top:10%;padding:0 5%;position:relative;}
#weixin-tip .close{color:#fff;padding:5px;font:bold 20px/24px simsun;text-shadow:0 1px 0 #ddd;position:absolute;top:0;left:5%;}
#detail span{border:1px solid black;border-radius:25px;margin-right:5px;padding:3px;}
.marks{height:20px;line-height:20px;background-color:rgb(235,235,235);padding:5px 5px 5px 10px;font-size: 14px;}
.content{margin:10px;}
.contentPic{float:left;}
.textbook{float:left;margin:0 0 0 5px;height:80px;position:relative;}
.remark{position:absolute;bottom:0px;width:220px}
.author{float:left;width:150px;position:relative;display:inline}
.num{float:right;position:relative;display:inline}
.title{width:230px}
</style>
</head>
<body>
<img src="${ctx }/jsp/share/image/1024.jpg" id="shareImg" />
<div class="down">
	<a href="###" onclick="myUpload();return false;">
	<dl class="downLeave">
		<dt><img class="other" id="logo" src="${ctx }/images/icon1.png" /></dt>
		<dd class="downSlogan"><img src="${ctx }/images/5.png"></dd>
		<dd><img src="${ctx }/images/star.png"><img src="${ctx }/images/star.png"><img src="${ctx }/images/star.png"><img src="${ctx }/images/star.png"><img src="${ctx }/images/star.png">
		</dd>
	</dl>
	</a>
	<a id="myUpload" href="###" onclick="myUpload();return false;" class="downBtn">立即下载</a>
</div>
<div id="wrap">
	<div id="pic" style="width:100%">
<!-- 		<img width="100%" src="http://123.59.41.16/SDpic/common/picSelect?gid=098b736a-d952-4341-8436-4f2d2b2112d3"/> -->
	</div>
	<div id="detail" style="margin:5px 5px 15px 5px">
		<div id="title">
<!-- 			专题介绍专题介绍专题介绍专题介绍专题介绍专题介绍专题介绍专题介 -->
		</div>
		<div style="margin-top:10px" id="mark">
<!-- 			<span >标签一</span><span>标签一</span><span>标签一</span><span>标签一</span> -->
		</div>
	</div>
<!-- 	<div class="marks" > -->
<!-- 		第一个 -->
<!-- 	</div> -->
<!-- 	<div class="content"> -->
<!-- 		<div class="contentPic"> -->
<!-- 			<img width="100px" height="80px" src="http://123.59.41.16/SDpic/common/picSelect?gid=d297865c-aabb-48d7-89fb-fedb7b59a41b"/> -->
<!-- 		</div> -->
<!-- 		<div class="textbook"> -->
<!-- 			<div class="title">这是一个文章这是一个文章这是一个文章</div> -->
<!-- 			<div class="remark"> -->
<!-- 				<div class="author">用户名</div><div class="num">100浏览</div> -->
<!-- 			</div> -->
<!-- 		</div> -->
<!-- 		<div style="clear:both"></div> -->
<!-- 	</div> -->
</div>
</body>
</html>