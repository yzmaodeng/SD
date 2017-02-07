package com.sd.action;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
@Controller
@RequestMapping("/pic")
public class PicUploadAction {
	//该接口已经不用了
	@RequestMapping("/upload.do")
	public void execute(@RequestParam MultipartFile upload,HttpServletRequest request,HttpServletResponse response) throws Exception {  
        response.setCharacterEncoding("UTF-8");  
        PrintWriter out = response.getWriter();  
        // CKEditor提交的很重要的一个参数  
        String callback = request.getParameter("CKEditorFuncNum");  
        String uploadContentType=upload.getContentType();
        String expandedName = ""; // 文件扩展名  
        if (uploadContentType.equals("image/pjpeg") || uploadContentType.equals("image/jpeg")) {  
            // IE6上传jpg图片的headimageContentType是image/pjpeg，而IE9以及火狐上传的jpg图片是image/jpeg  
            expandedName = ".jpg";  
        } else if (uploadContentType.equals("image/png") || uploadContentType.equals("image/x-png")) {  
            // IE6上传的png图片的headimageContentType是"image/x-png"  
            expandedName = ".png";  
        } else if (uploadContentType.equals("image/gif")) {  
            expandedName = ".gif";  
        } else if (uploadContentType.equals("image/bmp")) {  
            expandedName = ".bmp";  
        } else {  
            out.println("<script type=\"text/javascript\">");  
            out.println("window.parent.CKEDITOR.tools.callFunction(" + callback  
                    + ",'','文件格式不正确（必须为.jpg/.gif/.bmp/.png文件）');");  
            out.println("</script>");  
            return ;  
        }  
        if (upload.getSize() > 600 * 1024) {  
            out.println("<script type=\"text/javascript\">");  
            out.println("window.parent.CKEDITOR.tools.callFunction(" + callback  
                    + ",''," + "'文件大小不得大于600k');");  
            out.println("</script>");  
            return ;  
        }  
  
        InputStream is =upload.getInputStream();  
        String str=new File(request.getSession().getServletContext().getRealPath("")).getParent();
        String uploadPath = str+"\\img\\";
        File filepath = new File(uploadPath);
		if (!filepath.exists()) {    
			filepath.mkdirs();    
		} 
        String fileName = java.util.UUID.randomUUID().toString(); // 采用UUID的方式命名保证不会重复  
        fileName += expandedName;  
        File toFile = new File(uploadPath, fileName);  
        OutputStream os = new FileOutputStream(toFile);  
          
        // 文件复制到指定位置  
        byte[] buffer = new byte[1024];  
        int length = 0;  
        while ((length = is.read(buffer)) > 0) {  
            os.write(buffer, 0, length);  
        }  
        is.close();  
        os.close();  
        // 返回“图像”选项卡并显示图片  
        out.println("<script type=\"text/javascript\">");  
        out.println("window.parent.CKEDITOR.tools.callFunction(" + callback  
                + ",'/img/" + fileName + "','')"); // 相对路径用于显示图片  
        out.println("</script>");  
        return ;  
    } 
}
