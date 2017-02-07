<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>  
<c:set var="ctx" value="${pageContext.request.contextPath}" ></c:set>
<!DOCTYPE html >
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<meta content="utf-8" http-equiv="Content-Language">
	<meta content="width=device-width,initial-scale=1.0,user-scalable=no" name="viewport">
	<title>${post.detail }</title>
	<link rel="stylesheet" href="${ctx }/jsp/share/css/mobile.css">
	<jsp:include page="/jsp/base/base.jsp"></jsp:include>
	<script type="text/javascript" src="http://res.wx.qq.com/open/js/jweixin-1.0.0.js"></script> 
<style type="text/css">
#shareImg{
	overflow:hidden;
	position:fixed;
	bottom:100%;
}
#weixin-tip{display:none;position:fixed;left:0;top:0;background:rgba(0,0,0,0.8);filter:alpha(opacity=80);width:100%;height:100%;z-index:100;}
#weixin-tip p{text-align:center;margin-top:10%;padding:0 5%;position:relative;}
#weixin-tip .close{color:#fff;padding:5px;font:bold 20px/24px simsun;text-shadow:0 1px 0 #ddd;position:absolute;top:0;left:5%;}
</style>
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
        	console.info(result.ss);
        	console.info(result.signature);
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
function browserRedirect() {
    var sUserAgent = navigator.userAgent.toLowerCase();
    var bIsIpad = sUserAgent.match(/ipad/i) == "ipad";
    var bIsIphoneOs = sUserAgent.match(/iphone os/i) == "iphone os";
    var bIsMidp = sUserAgent.match(/midp/i) == "midp";
    var bIsUc7 = sUserAgent.match(/rv:1.2.3.4/i) == "rv:1.2.3.4";
    var bIsUc = sUserAgent.match(/ucweb/i) == "ucweb";
    var bIsAndroid = sUserAgent.match(/android/i) == "android";
    var bIsCE = sUserAgent.match(/windows ce/i) == "windows ce";
    var bIsWM = sUserAgent.match(/windows mobile/i) == "windows mobile";
    if (bIsIpad || bIsIphoneOs) {
        return "ios";
    } else if (bIsAndroid){
    	return "android";
    } 
}
$(function (){
	share();
	var mark="${record.mark}".split(",");
	for(var i=0;i<mark.length;i++){
		$("#mark").append("<div class='mark'>"+mark[i]+"</div>");
	}
	var sick="${record.sick}".split(",");
	for(var i=0;i<sick.length;i++){
		$("#sick").append("<div class='sick'>"+sick[i]+"</div>");
	}
	$.ajax({
        type: "POST",
        dataType: "json",
        url: "${ctx}/record/queryCourse.do",
        data:"gid=${record.gid}",
        success: function (result) {
        	for(var i=0;i<result.length;i++){
        		$("body").append('<div class="topic"><img src="${ctx }/jsp/share/image/record/type.png"/>'+result[i].type+'</div>');
        		$("body").append('<div class="course" id="course'+i+'"></div>');
        		if(result[i].pic!=""){
        			var pic=result[i].pic.split(",");
        			for(var j=0;j<pic.length;j++){
        				$("#course"+i).append('<img src="http://123.59.41.16/SDpic/common/picSelect?gid='+pic[j]+'"/>');
        			}
        		}
        		if(result[i].vid!=""){
        			var vid=result[i].vid.split(",");
        			for(var j=0;j<vid.length;j++){
        				$("#course"+i).append('<video src="http://123.59.41.16/'+vid[j]+'" controls="controls"></video>');
        			}
        		}
        		if(result[i].audio!=""){
        			var audio=result[i].audio.split(",");
        			for(var j=0;j<audio.length;j++){
        				$("#course"+i).append('<audio src="http://123.59.41.16/'+audio[j]+'" controls="controls"></audio>');
        			}
        		}
        		if(result[i].detail!=""){
        			$("#course"+i).append('<div><span>备注：</span>&nbsp;'+result[i].detail+'</div>');
        		}
        	}
        },
        error: function(result) {
            alert("error");
         }
    });
	
});
</script>
<style>
.mark{
border:1px solid #038CE6;color:#038CE6;margin:5px;padding:0 5px;border-radius:10px;position:relative;float:left;
}
.sick{
background:#edf1f7;color:grey;margin:0 10px 10px 0px;padding:10px;position:relative;float:left;height:30px;line-height:30px;
}
.topic{
height:60px;line-height:60px;font-size:20px;color:grey;padding-left:20px;border-bottom:2px solid rgb(240,239,245);
}
.topic img{
vertical-align:middle;padding-right:10px;width:30px;height:30px;
}
.course{
padding:5px 0;margin:0 20px;
}
.course img,.course video{
width:100px;height:100px;background:rgb(240,239,245);
}
.course div span{
color:grey;
}
</style>
</head>
<body class="padBox">
<c:if test="${param.local!='app'}">
<img src="${ctx }/jsp/share/image/1024.jpg" id="shareImg" />
<div class="down" style="margin-bottom: 0px;">
	<a href="###" onclick="myUpload();return false;">
	<dl class="downLeave">
		<dt><img class="other" id="logo" src="${ctx }/images/icon1.png" /></dt>
		<dd class="downSlogan"><img src="${ctx }/images/5.png"></dd>
		<dd>
			<img src="${ctx }/images/star.png"><img src="${ctx }/images/star.png"><img src="${ctx }/images/star.png"><img src="${ctx }/images/star.png"><img src="${ctx }/images/star.png">
		</dd>
	</dl>	
	</a>
	<a id="myUpload" href="###" onclick="myUpload();return false;" class="downBtn">立即下载</a>
</div>
</c:if> 
<div id="mark" style="font-size:20px;color:grey;padding-left:20px">
	<div style="margin:5px;padding:0 5px;position:relative;float:left;">标签</div>
</div>
<div style="clear:both"></div>
<div style="height:15px;background:rgb(240,239,245)">
</div>
<div style="height:60px;line-height:60px;font-size:20px;color:grey;padding-left:20px;border-bottom:2px solid rgb(240,239,245);">
	<img style="vertical-align:middle;padding-right:10px;" width="30px" height="30px" src="${ctx }/jsp/share/image/record/information.png"/>患者信息
</div>
<div style="height:80px;line-height:80px;font-size:20px;color:grey;margin:0 20px;border-bottom:1px dashed black;">
	<span>${record.name }&nbsp;|&nbsp;
	<c:if test="${record.gender!=null&&record.gender==1}">男</c:if>
	<c:if test="${record.gender!=null&&record.gender==2}">女</c:if>
	&nbsp;${record.age}岁
	</span>
	<span style="float:right;margin:10px;">
		<c:if test="${record.head==null||record.head==''}">
			<img width="50px" height="50px" src="${ctx }/jsp/share/image/record/head.png"/>
		</c:if>
		<c:if test="${record.head!=null&&record.head!=''}">
			<img width="50px" height="50px" src="http://123.59.41.16/SDpic/common/picSelect?gid=${record.head}"/>
		</c:if>
	</span>
</div>
<div style="height:60px;line-height:60px;font-size:20px;color:grey;margin:0 20px;border-bottom:1px dashed black;">
	•&nbsp;就诊时间<span style="float:right"><fmt:formatDate value="${record.visitDate}" type="date"/></span>
</div>
<div style="height:60px;line-height:60px;font-size:20px;color:grey;margin:0 20px;border-bottom:1px dashed black;">
	•&nbsp;手术时间<span style="float:right"><fmt:formatDate value="${record.operationTime}" type="date"/></span>
</div>
<div style="height:60px;line-height:60px;font-size:20px;color:grey;margin:0 20px;border-bottom:1px dashed black;">
	•&nbsp;手术病案号<span style="float:right">${record.caseNo}</span>
</div>
<div style="height:60px;line-height:60px;font-size:20px;color:grey;margin:0 20px;">
	•&nbsp;身份证号<span style="float:right">${record.IDCardNo}</span>
</div>
<div style="height:15px;background:rgb(240,239,245)">
</div>
<div style="height:60px;line-height:60px;font-size:20px;color:grey;padding-left:20px;border-bottom:2px solid rgb(240,239,245);">
	<img style="vertical-align:middle;padding-right:10px;" width="30px" height="30px" src="${ctx }/jsp/share/image/record/diagnosis.png"/>诊断
</div>
<div id="sick" style="height:60px;line-height:60px;padding:10px 0 0 0;font-size:20px;color:grey;margin:0 20px;">
</div>
<div style="clear:both"></div>
<div style="height:15px;background:rgb(240,239,245)">
</div>
<div style="height:60px;line-height:60px;font-size:20px;color:grey;padding-left:20px;border-bottom:2px solid rgb(240,239,245);">
	<img style="vertical-align:middle;padding-right:10px;" width="30px" height="30px" src="${ctx }/jsp/share/image/record/condition.png"/>基本病情
</div>
<div style="height:60px;line-height:60px;font-size:20px;color:grey;margin:0 20px;">
	${record.state}
</div>
<div style="height:30px;position:relative;text-align:center;background:rgb(240,239,245);padding:10px 30px 0 30px">
	<div style="position:absolute;color:grey;margin:0 auto;left:0;right:0;">患者病程</div>
	<img width="100%" src="${ctx }/jsp/share/image/record/course.png"/>
</div>
</body>
</html>