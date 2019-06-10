package com.ysttench.application.hdy.controller;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.ysttench.application.hdy.mapper.FileMapper;
import com.ysttench.application.hdy.mapper.SysmsgMapper;
import com.ysttench.application.hdy.rdto.FileMsg;
import com.ysttench.application.hdy.rdto.SysMsg;

@Controller
@RequestMapping("/down/")
public class DownController {
    @Autowired
    FileMapper fileMapper;
    @Autowired
    SysmsgMapper sysmsgMapper;

    @RequestMapping("updateoldpath")
    public String updateoldpath(HttpServletRequest request, Model model) {
	String oldpath = request.getParameter("oldpath");
	sysmsgMapper.updateold(oldpath);
	SysMsg s = sysmsgMapper.findMsg();
	model.addAttribute("oldpath", s.getOLD_PATH());
	model.addAttribute("newpath", s.getNEW_PATH());
	return ForwardConstants.LOGIN;
    }

    @RequestMapping("updatenewpath")
    public String updatenewpath(HttpServletRequest request, Model model) {
	String newpath = request.getParameter("newpath");
	sysmsgMapper.updatenew(newpath);
	SysMsg s = sysmsgMapper.findMsg();
	model.addAttribute("oldpath", s.getOLD_PATH());
	model.addAttribute("newpath", s.getNEW_PATH());
	return ForwardConstants.LOGIN;
    }

    @RequestMapping("addimgsg")
    public String addimgsg(HttpServletRequest request, Model model) {
	SysMsg s = sysmsgMapper.findMsg();
	model.addAttribute("oldpath", s.getOLD_PATH());
	model.addAttribute("newpath", s.getNEW_PATH());
	File file = new File(s.getOLD_PATH()+"\\多余文件");
	File[] fs = file.listFiles();
	for (File file2 : fs) {
	    String fileName = file2.getName();
	    fileMapper.add(fileName);
	}
	delAllFile(s.getOLD_PATH() + "\\多余文件");
	return ForwardConstants.LOGIN;
    }

    @RequestMapping("deleteimg")
    public String deleteimg(Model model) {
	SysMsg s = sysmsgMapper.findMsg();
	String path = s.getOLD_PATH() + "\\";
	File path1 = new File(path + "\\material\\"+s.getNEW_PATH());

	File[] fs1 = path1.listFiles();
	List<String> fs = new ArrayList<String>();
	for (File file : fs1) {
	    if (file.toString().contains(".html")) {
		fs.add(file.toString());
	    }
	}
	int n = fs.size();
	for (int i = 0; i < n; i++) {
	    String s1 = fs.get(i);
	    int mn = Integer.parseInt(s1.substring(29+s.getNEW_PATH().length(),s1.indexOf(".html")));
	    List<FileMsg> list = fileMapper.getmsg();
	    for (FileMsg fileMsg : list) {
		String str = fileMsg.getFILE_NAME();
		File fstr = new File(path + "\\material\\"+s.getNEW_PATH()+"\\" + mn + "_files\\" + str);
		if (fstr.exists()) {
		    fstr.delete();
		}
	    }
	    System.out.println("第" + mn + "话数据处理完成");
	}
	SysMsg m = sysmsgMapper.findMsg();
	model.addAttribute("oldpath", m.getOLD_PATH());
	model.addAttribute("newpath", m.getNEW_PATH());
	return ForwardConstants.LOGIN;
    }

    @RequestMapping("updatemsg")
    public String updatemsg(Model model) {
	SysMsg s = sysmsgMapper.findMsg();
	String path = s.getOLD_PATH() + "\\";
	String newpath = s.getNEW_PATH() + "\\";
	File ff = new File(s.getOLD_PATH() + "\\"+s.getNEW_PATH());
	if (ff.exists()) {
	    model.addAttribute("msg", "已存在该文件");
	    SysMsg m = sysmsgMapper.findMsg();
	    model.addAttribute("oldpath", m.getOLD_PATH());
	    model.addAttribute("newpath", m.getNEW_PATH());
	    return ForwardConstants.LOGIN;
	}
	File path1 = new File(path + "\\material\\"+newpath);
	File[] fs1 = path1.listFiles();
	List<String> fs2 = new ArrayList<String>();
	for (File file : fs1) {
	    if (file.toString().contains(".html")) {
		fs2.add(file.toString());
	    }
	}
	int n = fs2.size();
	for (int i = 0; i < n; i++) {
	    String s1 = fs2.get(i);
	    int mn = Integer.parseInt(s1.substring(29+s.getNEW_PATH().length(),s1.indexOf(".html")));
	    File f = new File(path + newpath + "第" + mn + "话");
	    if (f.exists()) {
		f.delete();
	    } else {
		f.mkdirs();
	    }
	    File oldpath = new File(path + "\\material\\"+newpath+ mn + "_files");
	    File[] fs = oldpath.listFiles();
	    Arrays.sort(fs, new CompratorByLastModified());
	    for (int j = (fs.length - 1); j > -1; j--) {
		File toBeRenamed = new File(oldpath + "\\" + fs[j].getName());
		File newFile = new File(path + newpath + "第" + mn + "话\\" + (fs.length - j) + ".jpg");
		toBeRenamed.renameTo(newFile);
	    }
	    System.out.println("第" + mn + "话转换完成");
	}
	delAllFile(path + "\\material\\"+newpath);
	SysMsg m = sysmsgMapper.findMsg();
	model.addAttribute("oldpath", m.getOLD_PATH());
	model.addAttribute("newpath", m.getNEW_PATH());
	return ForwardConstants.LOGIN;

    }
    @RequestMapping("updatemsg1")
    public String updatemsg1(Model model) {
	SysMsg s = sysmsgMapper.findMsg();
	String path = s.getOLD_PATH() + "\\";
	String newpath = s.getNEW_PATH() + "\\";
	File path1 = new File(path + "\\material\\"+newpath);
	File[] fs1 = path1.listFiles();
	List<String> fs2 = new ArrayList<String>();
	for (File file : fs1) {
	    if (file.toString().contains(".html")) {
		fs2.add(file.toString());
	    }
	}
	int n = fs2.size();
	for (int i = 0; i < n; i++) {
	    String s1 = fs2.get(i);
	    int mn = Integer.parseInt(s1.substring(29+s.getNEW_PATH().length(),s1.indexOf(".html")));
	    File f = new File(path + newpath + "第" + mn + "话");
	    if (f.exists()) {
		f.delete();
	    } else {
		f.mkdirs();
	    }
	    File oldpath = new File(path + "\\material\\"+newpath+ mn + "_files");
	    File[] fs = oldpath.listFiles();
	    Arrays.sort(fs, new CompratorByLastModified());
	    for (int j = (fs.length - 1); j > -1; j--) {
		File toBeRenamed = new File(oldpath + "\\" + fs[j].getName());
		File newFile = new File(path + newpath + "第" + mn + "话\\" + (fs.length - j) + ".jpg");
		toBeRenamed.renameTo(newFile);
	    }
	    System.out.println("第" + mn + "话转换完成");
	}
	delAllFile(path + "\\material\\"+newpath);
	SysMsg m = sysmsgMapper.findMsg();
	model.addAttribute("oldpath", m.getOLD_PATH());
	model.addAttribute("newpath", m.getNEW_PATH());
	return ForwardConstants.LOGIN;

    }
    // 删除指定文件夹下的所有文件

    public static boolean delAllFile(String path) {
	boolean flag = false;
	File file = new File(path);
	if (!file.exists()) {
	    return flag;
	}
	if (!file.isDirectory()) {
	    return flag;
	}
	String[] tempList = file.list();
	File temp = null;
	for (int i = 0; i < tempList.length; i++) {
	    if (path.endsWith(File.separator)) {
		temp = new File(path + tempList[i]);
	    } else {
		temp = new File(path + File.separator + tempList[i]);
	    }
	    if (temp.isFile()) {
		temp.delete();
	    }
	    if (temp.isDirectory()) {
		delAllFile(path + "/" + tempList[i]);// 先删除文件夹里面的文件
		delFolder(path + "/" + tempList[i]);// 再删除空文件夹
		flag = true;
	    }
	}
	return flag;
    }

    // 删除文件夹
    public static void delFolder(String folderPath) {
	try {
	    delAllFile(folderPath); // 删除完里面所有内容
	    String filePath = folderPath;
	    filePath = filePath.toString();
	    java.io.File myFilePath = new java.io.File(filePath);
	    myFilePath.delete(); // 删除空文件夹
	} catch (Exception e) {
	    e.printStackTrace();
	}
    }

    static class CompratorByLastModified implements Comparator<File> {
	public int compare(File f1, File f2) {
	    long diff = f1.lastModified() - f2.lastModified();
	    if (diff > 0)
		return -1;// 倒序正序控制
	    else if (diff == 0)
		return 0;
	    else
		return 1;// 倒序正序控制
	}

    }
}
