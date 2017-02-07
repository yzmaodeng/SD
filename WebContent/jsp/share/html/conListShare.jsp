<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>  
<c:set var="ctx" value="${pageContext.request.contextPath}" ></c:set>
<!DOCTYPE html >
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta name="viewport" content="maximum-scale=1.0,minimum-scale=1.0,user-scalable=0,width=device-width,initial-scale=1.0"/>
<title>${param.month}月骨科会议列表</title>
<jsp:include page="/jsp/base/base.jsp"></jsp:include>
<link rel="stylesheet" href="${ctx }/jsp/share/css/mobile.css">
<link rel="stylesheet" type="text/css" href="${ctx}/jsp/share/css/api.css"/>
<link rel="stylesheet" type="text/css" href="${ctx}/jsp/share/css/conferenceframe.css"/>
<style type="text/css">
#shareImg{
	overflow:hidden;
	position:fixed;
	bottom:100%;
}
</style>
<script>
var _hmt = _hmt || [];
(function() {
var hm = document.createElement("script");
hm.src = "//hm.baidu.com/hm.js?aa63e0651dac35a3475129201f34c3fe";
var s = document.getElementsByTagName("script")[0]; 
s.parentNode.insertBefore(hm, s);
})();

function myUpload(){
			var ua = navigator.userAgent.toLowerCase();	
			var is_weixin = (function(){
				return navigator.userAgent.toLowerCase().indexOf('micromessenger') !== -1
			})();
			var hint = document.getElementById('weixin-tip');
			if (is_weixin) {
				location.href ="http://a.app.qq.com/o/simple.jsp?pkgname=com.naton.bonedict";
// 				var state = {
// 						title: "123",
// 						url: "/SD/conference/listShare.out?year=${param.year}&month=${param.month}&down=1",
// 						otherkey: ""
// 					};
// 				window.history.pushState(state, document.title, "/SD/conference/listShare.out?year=${param.year}&month=${param.month}&down=1");
// 				share();
// 				var winHeight = typeof window.innerHeight != 'undefined' ? window.innerHeight : document.documentElement.clientHeight; //兼容IOS，不需要的可以去掉
// 				hint.style.height = winHeight + 'px'; //兼容IOS弹窗整屏
// 				hint.style.display = 'block';
// 				return false;
			}else 
				if (/iphone|ipad|ipod/.test(ua)) {
				        location.href ='Orthopedia://?${post.gid }';
				        setTimeout(function() {
				            location.href = 'https://appsto.re/cn/C-2T7.i'
				        }, 30);
				    
			} else if(/android/.test(ua)) {
				location.href = 'https://dn-orthopedia.qbox.me/orthopedia.apk'
			}
		}
var timestamp1;
var nonceStr1;
var signature1;
function share(){
	var value={};
	value.url=location.href.split('#')[0];
	$.ajax({
		async : false,
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
		var title="骨今中外";
		var desc="${param.month}月骨科会议列表";
		var link= 'http://orthoday.com/SD/conference/listShare.out?year=${param.year}&month=${param.month}';// 分享链接
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
$(function (){
	share();
	if (!is_weixin) {
		if("${param.down}"=="1"){
			myUpload();
			return;
		}
	}
});
</script>
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
        <ul class="listView">
            <!--第一个月标题-->
            <!--普通空白-->
<!--             <li> -->
<!--                 <div class="h10"></div> -->
<!--             </li> -->
            <!--月份标题-->
            <li>
                <div class="bighead">
                    <div class="bigRound">
                        ${param.month}月
                    </div>
                </div>
            </li>
         <c:forEach items="${confList}" var="mm" varStatus="cc">
            <!--带蓝线的空白-->
            <li>
                <div class="h10header"></div>
            </li>
            <!--内容行-->
            <a href="${ctx}/conference/shareQuery.out?gid=${mm.gid}">
            <li>
                <div class="contenthead">
                    <div class="smallRound">
                       	<fmt:formatDate value="${mm.dateBegin }" pattern="dd" />
                    </div>
                </div>
                <div class="content" onclick="" tapmode="btnactive">
                    <p id="meng" class="title">
                        ${mm.name} 
                    </p>
                    <p class="con">
                       	 地址：${mm.local}
                    </p>
                    <p class="conname">
                       	 发起：${mm.sponsor}
                    </p>
                </div>
            </li>
            </a>
        </c:forEach>
        </ul>
    </div>
</body>
</html>