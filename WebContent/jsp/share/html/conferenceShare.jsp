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
<title>${conf.name }</title>
<jsp:include page="/jsp/base/base.jsp"></jsp:include>
<link rel="stylesheet" href="${ctx }/jsp/share/css/mobile.css">
<link rel="stylesheet" type="text/css" href="${ctx}/jsp/share/css/api.css"/>
<link rel="stylesheet" type="text/css" href="${ctx}/jsp/share/css/common.css"/>
<link rel="stylesheet" type="text/css" href="${ctx}/jsp/share/css/conferencewindow.css"/>
<style type="text/css">
#shareImg{
	overflow:hidden;
	position:fixed;
	bottom:100%;
}
</style>
<script type="text/javascript" >
		function openDetail(index) {
			$(".mainUl").find('li').removeClass('liactive').eq(index).addClass('liactive');
			$("#main #wrap").each(function(){
				$(this).css("display","none");
			});
			$("#main #wrap").eq(index).css("display","block");
		}
		function myUpload(){
			var ua = navigator.userAgent.toLowerCase();	
			var is_weixin = (function(){
				return navigator.userAgent.toLowerCase().indexOf('micromessenger') !== -1
			})();
			var hint = document.getElementById('weixin-tip');
			if (is_weixin) {
				location.href ="http://a.app.qq.com/o/simple.jsp?pkgname=com.naton.bonedict";
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
				var desc="${conf.name }";
				var link= 'http://orthoday.com/SD/conference/shareQuery.out?gid=${conf.gid }';// 分享链接
			    var imgUrl= 'http://orthoday.com/SD/jsp/share/image/1024.jpg'; // 分享图标
				wx.onMenuShareAppMessage({
				    title: title, // 分享标题
				    desc:desc, // 分享描述
				    link: link, // 分享链接
				    imgUrl: imgUrl, // 分享图标
				    type: '', // 分享类型,music、video或link，不填默认为link
				    dataUrl: '', // 如果type是music或video，则要提供数据链接，默认为空
				    success: function () { 
//		 		        alert("嘿嘿");
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
<div id="wrap" class="force">
	<div id="main">
		<div class="des_wrap">
			<div class="detail">
				<h3>${conf.name }</h3>
<%-- 				<img src="${ctx}/jsp/share/image/moments/favorite1.png" width="30px" height="30px" class="right_img" onclick="ToFavorite()" /> --%>
			</div>
			<div class="detail">
				<h5 class="text">时间：<fmt:formatDate value="${conf.dateBegin }" pattern="yyyy-MM-dd HH:mm" />——<fmt:formatDate value="${conf.dateEnd }" pattern="yyyy-MM-dd HH:mm" /></h5>
				<img src="${ctx}/jsp/share/image/conference/img_meeting_time.png" width="20px" height="20px" class="left_img"/>
			</div>
			<div class="detail">
				<h5 class="text">地址：${conf.local }</h5>
				<img src="${ctx}/jsp/share/image/conference/img_meeting_area.png" width="20px" height="20px" class="left_img"/>
			</div>
			<div class="detail">
				<h5 class="text">发起：${conf.sponsor}</h5> 
				<img src="${ctx}/jsp/share/image/conference/img_meeting_time.png" width="20px" height="20px" class="left_img"/>
			</div>
		</div>
		<ul class="mainUl">
			<li class="liactive rightLi" onclick="openDetail(0)">
				大会信息
			</li>
			<li class="rightLi" onclick="openDetail(1)">
				学术日程
			</li>
			<li onclick="openDetail(2)">
				位置信息
			</li>
		</ul>
		<div id="wrap" class="left" style="">
			<div id="main">
				<div class="des_txt">
					<h5>
						${conf.detail }
<!-- 						<img width="100%" src="http://img0.bdstatic.com/img/image/imglogo-r.png"/> -->
<!-- 						抱哈我刚发额为无法 -->
					</h5>
				</div>
			</div>
		</div>
		<div id="wrap" class="mid" style="display:none;">
			<div id="main">
			<c:forEach items="${topicList}" var="mm">
<!-- 				<ul class="mainList"> -->
					<div class="mainli">
					<div class="maindiv">
					<h4><fmt:formatDate value="${mm.datetimeBegin }" pattern="yyyy-MM-dd HH:mm" />——<fmt:formatDate value="${mm.datetimeEnd }" pattern="yyyy-MM-dd HH:mm" /></h4>
					<h2>${mm.title }</h2>
					<h3>主持人：${mm.moderator }</h3>
					<h3>点评人：${mm.reviewer }</h3>
					<c:forEach items="${map[mm.gid]}" var="nn">
						<div class="lili">
						<div>
						<div class="Round"></div>
						<div class="content">
						<h4><fmt:formatDate value="${nn.datetimeBegin }" pattern="yyyy-MM-dd HH:mm" />——<fmt:formatDate value="${nn.datetimeEnd }" pattern="yyyy-MM-dd HH:mm" /></h4>
						<h2>${nn.speaker}</h2> 
						<h3>${nn.title}</h3>
						</div>
						</div>
						</div>
					</c:forEach>
					</div>
					</div>
			</c:forEach>
<!-- 				</ul> -->
			</div>
		</div>
		
		<div id="wrap" class="right" style="display:none;">
			<div id="main">
				<div class="des_txt">
					<h5>${conf.localDetail} </h5>
				</div>
			</div>
		</div>
	</div>
</div>

</body>
</html>