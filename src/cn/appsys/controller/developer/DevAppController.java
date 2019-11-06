package cn.appsys.controller.developer;
import java.io.File;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.io.FilenameUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import cn.appsys.pojo.AppCategory;
import cn.appsys.pojo.AppInfo;
import cn.appsys.pojo.AppVersion;
import cn.appsys.pojo.DataDictionary;
import cn.appsys.pojo.DevUser;
import cn.appsys.service.appcategory.AppcategoryService;
import cn.appsys.service.appinfo.AppInfoService;
import cn.appsys.service.appversion.AppVersionService;
import cn.appsys.service.datadictionary.DataDictionaryService;
import cn.appsys.tools.ManageUtil;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

@Controller
@RequestMapping("/dev/flatform/app")
public class DevAppController {
	private Logger logger = Logger.getLogger(DevLoginController.class);// 日志
	@Autowired
	private AppInfoService appInfoService;// app信息服务实现类
	@Autowired
	private DataDictionaryService dataDictionaryService;//字典服务实现类
	@Autowired
	private AppcategoryService appCategoryService;// app等级服务实现类
	@Autowired
	private AppVersionService appVersionService;//app版本服务

	/**
	 * 	分页查询列表
	 * @param model
	 * @param currentPageNo 页码
	 * @param querySoftwareName 模糊查询名称
	 * @param _queryStatus 状态
	 * @param _queryFlatformId 平台
	 * @param _queryCategoryLevel1 一级分类
	 * @param _queryCategoryLevel2 二级分类
	 * @param _queryCategoryLevel3 三级分类
	 * @param session
	 * @return
	 */
	@RequestMapping("/list")
	public String list( Model model,
			@RequestParam(value = "pageIndex", required = false , defaultValue="1")Integer currentPageNo,
			@RequestParam(value="querySoftwareName",required=false) String querySoftwareName,
			@RequestParam(value="queryStatus",required=false) String _queryStatus,
			@RequestParam(value="queryFlatformId",required=false) String _queryFlatformId,
			@RequestParam(value="queryCategoryLevel1",required=false) String _queryCategoryLevel1,
			@RequestParam(value="queryCategoryLevel2",required=false) String _queryCategoryLevel2,
			@RequestParam(value="queryCategoryLevel3",required=false) String _queryCategoryLevel3,
			HttpSession session) {
		logger.info("[DevAppController]执行查询AppInfo列表[list()]====================================");
		logger.info("-查询条件[pageIndex]:"+currentPageNo);
		logger.info("------[querySoftwareName]:"+querySoftwareName);
		logger.info("------[queryStatus]:"+_queryStatus);
		logger.info("------[queryFlatformId]:"+_queryFlatformId);
		logger.info("------[queryCategoryLevel1]:"+_queryCategoryLevel1+"-[queryCategoryLevel2]:"+_queryCategoryLevel2+"-[queryCategoryLevel3]:"+_queryCategoryLevel3);
		
		Integer queryStatus = ManageUtil.strToInteger(_queryStatus);//查询的app状态
		Integer queryFlatformId = ManageUtil.strToInteger(_queryFlatformId);//查询的平台
		Integer queryCategoryLevel1 = ManageUtil.strToInteger(_queryCategoryLevel1);//查询一级列表
		Integer queryCategoryLevel2 = ManageUtil.strToInteger(_queryCategoryLevel2);//查询二级列表
		Integer queryCategoryLevel3 = ManageUtil.strToInteger(_queryCategoryLevel3);//查询二级列表
		
		DevUser devUser = (DevUser)session.getAttribute("devUserSession");//获取当前登录开发人员
		logger.info("------[当前用户]:"+devUser.getDevCode());
		//分页处理
		PageHelper.startPage(currentPageNo, 4);//pagehelper实现分页
		List<AppInfo> allAppInfo = appInfoService.getAppInfoList(querySoftwareName,queryStatus,
				queryFlatformId,queryCategoryLevel1,queryCategoryLevel2,
				queryCategoryLevel3,devUser.getId());// 查询appinfo
		PageInfo appInfoList = new PageInfo(allAppInfo);// 使用pageinfo
		logger.info("--查询获得条目数量["+appInfoList.getTotal()+"],当前返回第[" + currentPageNo + "]页,共[" + allAppInfo.size() + "]条记录");
		//下拉列表-使用本类中方法getDataDictionaryList()查询状态列表和平台列表
		List<DataDictionary> statusList = this.getDataDictionaryList("APP_STATUS");//本类中方法
		List<DataDictionary> flatFormList = this.getDataDictionaryList("APP_FLATFORM");
		//下拉列表-等级第一级列表
		List<AppCategory> categoryLevel1List = appCategoryService.getAppCategoryListByParentId(null);
		
		model.addAttribute("categoryLevel1List", categoryLevel1List);
		model.addAttribute("appInfoList", appInfoList);
		model.addAttribute("statusList", statusList);
		model.addAttribute("flatFormList", flatFormList);
		model.addAttribute("queryStatus", queryStatus);
		model.addAttribute("queryFlatformId", queryFlatformId);
		model.addAttribute("queryCategoryLevel1", queryCategoryLevel1);
		model.addAttribute("queryCategoryLevel2", queryCategoryLevel2);
		model.addAttribute("queryCategoryLevel3", queryCategoryLevel3);
		return "/developer/appinfolist";
	}
	
	/**
	 * ajax根据父级id编号获取列表
	 * @return
	 */
	@RequestMapping("/categorylevellist.json")
	@ResponseBody
	public Object getCategoryLevelList(String pid){
		//转换格式
		Integer queryPid = ManageUtil.strToInteger(pid);
		//调用等级服务根据父级id查询等级列表
		List<AppCategory> categoryLevelList = appCategoryService.getAppCategoryListByParentId(queryPid);
		logger.info("[DevAppController]执行ajax查询等级列表[getCategoryLevelList()],查询父级id["+pid+"],获得["+categoryLevelList.size()+"]条记录");
		return JSONArray.toJSONString(categoryLevelList);//转换成json格式并返回
	}
	
	/**
	 * 添加appinfo页面中调用,加载平台信息
	 * @param typeCode
	 * @return
	 */
	@RequestMapping("datadictionarylist.json")
	@ResponseBody
	public List<DataDictionary> datadictionarylist(String tcode){
		return  this.getDataDictionaryList(tcode);
	}
	
	/**
	 * 根据编码获取状态列表
	 * @param pid
	 * @return
	 */
	public List<DataDictionary> getDataDictionaryList(String typeCode){
		return dataDictionaryService.getDataDictionaryByTypeCode(typeCode);
	}

	/**
	 * 跳转至添加
	 * @return
	 */
	@RequestMapping("/appinfoadd")
	public String toAppInfoAdd(){
		return "/developer/appinfoadd";
	}

	/**
	 * 验证apk名称
	 * @return
	 */
	@RequestMapping("/apkexist.json")
	@ResponseBody
	public String apkExist(String APKName){
		logger.info("[DevAppController]执行判断appName是否可用[apkExist()],查询APPName["+APKName+"]");
		Map<String, String> resultMap = new HashMap<String, String>();
		if (APKName != null && ! APKName.equals("")) {
			if (appInfoService.getAppInfo(null,APKName)!=null) {//没有找到
				resultMap.put("APKName", "exist");
			}else{
				resultMap.put("APKName", "noexist");
			}
		}else{
			resultMap.put("APKName", "empty");
		}
		return JSONArray.toJSONString(resultMap);
	}

	/**
	 * 添加保存app信息
	 * @param attach 上传的logo图标
	 * @param appInfo 
	 * @param session
	 * @param request
	 * @return
	 */
	@RequestMapping("/appinfoaddsave")
	public String appInfoAddSave(@RequestParam(value="a_logoPicPath",required= false)MultipartFile attach,
			AppInfo appInfo,HttpSession session,HttpServletRequest request){
		logger.info("[DevAppController]执行存储app信息[appInfoAddSave()]====================================");
		String logoPicPath=null;//图片URL路径
		String logoLocPath=null;//LOGO图片的服务器存储路径
		if(!attach.isEmpty()){//如果存在上传文件
			String path = request.getSession().getServletContext()
					.getRealPath("statics"+java.io.File.separator+"uploadfiles");//存储文件地址
			logger.info("-文件存储路径:"+path);
			String oldFileName = attach.getOriginalFilename();//获取原文件名
			logger.info("-原文件名:"+oldFileName);
			String prefix = FilenameUtils.getExtension(oldFileName);//原文件后缀
			logger.info("-原文件后缀:"+prefix);
			int filesize = 500000;//限制大小500k
			if (attach.getSize()>filesize) {//超过限制大小
				request.setAttribute("fileUploadError", " * 上传文件过大！");
				return "developer/appinfoadd";
			}else if(prefix.equalsIgnoreCase("jpg")||prefix.equalsIgnoreCase("png") 	
				||prefix.equalsIgnoreCase("jepg")||prefix.equalsIgnoreCase("pneg")){//判断后缀,即文件格式
				 	String fileName = appInfo.getAPKName() + ".jpg";//上传LOGO图片命名:apk名称.jpg
					File targetFile = new File(path,fileName);//根据存储地址和文件名创建对象
					if(!targetFile.exists()){//判断该文件是否存在
						 targetFile.mkdirs();//创建文件
					 }
					try {
						attach.transferTo(targetFile);//保存文件
						logger.info("-上传文件...");
					} catch (Exception e) {
						e.printStackTrace();
						request.setAttribute("fileUploadError", " * 上传失败！");
						return "developer/appinfoadd";
					}
					 //保存url地址和存储地址
					 logoPicPath = request.getContextPath()+"/statics/uploadfiles/"+fileName;
					 logoLocPath = path+File.separator+fileName;
			}else{//文件格式错误
				request.setAttribute("fileUploadError", " * 上传文件格式不正确！");
				return "developer/appinfoadd";
			}//end of size and prefix
		}//end of isEmpty
		DevUser devUser = (DevUser)session.getAttribute("devUserSession");//获取当前登录开发人员
		appInfo.setDevId(devUser.getId());//开发者id
		appInfo.setCreatedBy(devUser.getId());//创建人
		appInfo.setCreationDate(new Date());//创建日期
		appInfo.setLogoPicPath(logoPicPath);//LOGO图片URL路径
		appInfo.setLogoLocPath(logoLocPath);//LOGO图片的服务器存储路径
		appInfo.setStatus(1);//状态默认为1
		int result = appInfoService.addAppInfo(appInfo);//执行添加
		logger.info("--执行添加appinfo结果["+result+"]");
		if(result>0){
			return "redirect:/dev/flatform/app/list";
		}
		return "developer/appinfoadd";
	}

	/**
	 * 跳转修改页面,在此查询要修改的appinfo
	 * @param id
	 * @param model
	 * @return
	 */
	@RequestMapping("/appinfomodify")
	public String toAppInfoModify(@RequestParam("id") String id,
			@RequestParam(value="err",required= false)String err,
			Model model){
		String fileUploadError=ManageUtil.ErrJudge(err);//根据错误信息选择错误提示
		if (fileUploadError!=null) {
			logger.info("--修改appinfo出现异常[" + fileUploadError+"]");
		}	
		logger.info("[DevAppController]跳转修改页面[toAppInfoModify()]====================================");
		AppInfo appInfo  = appInfoService.getAppInfo(Integer.parseInt(id),null);//查询
		logger.info("--要修改id为["+id+"]的app,名称["+appInfo.getSoftwareName()+"]");
		model.addAttribute("appInfo",appInfo);
		model.addAttribute("fileUploadError",fileUploadError);//保存异常至前台显示
		return "developer/appinfomodify";
	}
	
	/**
	 * 修改appinfo
	 * @param attach 图标文件
	 * @param appInfo appinfo信息
	 * @param session 
	 * @param request
	 * @return
	 */
	@RequestMapping("/appinfomodifysave")
	public String appInfoModifySave(@RequestParam(value="attach",required= false)MultipartFile attach,
			AppInfo appInfo,HttpSession session,HttpServletRequest request){
		logger.info("[DevAppController]执行修改app信息[appInfoModifySave()]====================================");
		String logoPicPath=null;//图片URL路径
		String logoLocPath=null;//LOGO图片的服务器存储路径
		if(!attach.isEmpty()){//如果删除的图标并重新上传了,修改图标
			String path = request.getSession().getServletContext()
					.getRealPath("statics"+java.io.File.separator+"uploadfiles");//存储文件地址
			logger.info("-文件存储路径:"+path);
			String oldFileName = attach.getOriginalFilename();//获取原文件名
			logger.info("-原文件名:"+oldFileName);
			String prefix = FilenameUtils.getExtension(oldFileName);//原文件后缀
			logger.info("-原文件后缀:"+prefix);
			int filesize = 500000;//限制大小500k
			if (attach.getSize()>filesize) {//超过限制大小
				 return "redirect:/dev/flatform/app/appinfomodify?id="+appInfo.getId()+"&err=sizeErr";
			}else if(prefix.equalsIgnoreCase("jpg")||prefix.equalsIgnoreCase("png") 	
				||prefix.equalsIgnoreCase("jepg")||prefix.equalsIgnoreCase("pneg")){//判断后缀,即文件格式
				 	String fileName = appInfo.getAPKName() + ".jpg";//上传LOGO图片命名:apk名称.jpg
					File targetFile = new File(path,fileName);//根据存储地址和文件名创建对象
					if(!targetFile.exists()){//判断该文件是否存在
						 targetFile.mkdirs();//创建文件
					 }
					try {
						attach.transferTo(targetFile);//保存文件
						logger.info("-上传文件...");
					} catch (Exception e) {
						e.printStackTrace();
						return "redirect:/dev/flatform/app/appinfomodify?id="+appInfo.getId()+"&err=uploadErr";
					}
					 //保存url地址和存储地址
					 logoPicPath = request.getContextPath()+"/statics/uploadfiles/"+fileName;
					 logoLocPath = path+File.separator+fileName;
			}else{//文件格式错误
				return "redirect:/dev/flatform/app/appinfomodify?id="+appInfo.getId()+"&err=prefixErr";
			}
		}
		DevUser devUser = (DevUser)session.getAttribute("devUserSession");//获取当前登录开发人员
		appInfo.setModifyBy(devUser.getId());//修改人id
		appInfo.setModifyDate(new Date());//修改日期
		appInfo.setLogoLocPath(logoLocPath);//设null在sql语句中忽略,或者修改后图片url
		appInfo.setLogoPicPath(logoPicPath);//设null在sql语句中忽略,修改后图片路径
		int result = appInfoService.modifyAppInfo(appInfo);//执行修改
		logger.info("--执行修改appinfo结果["+result+"]");
			if(result > 0){//成功
				return "redirect:/dev/flatform/app/list";
			}
		return "developer/appinfomodify";
	}
	
	/**
	 * ajax删除logo或者apk文件
	 * @param flag 标识是logo还是apk文件
	 * @param id
	 * @return
	 */
	@RequestMapping("/delfile.json")
	@ResponseBody
	public Object delfile(@RequestParam(value="flag",required=false) String flag,
			 @RequestParam(value="id",required=false) String id){
		logger.info("[DevAppController]执行清除logo图标或者apk文件[delfile()]====================================");
		Map<String, String> resultMap = new HashMap<String, String>();
		String fileLocPath = null;//文件地址
		int result = -1;
		Integer oprId = ManageUtil.strToInteger(id);//操作id
		if(flag == null || flag.equals("") || id == null || id.equals("")){//
			resultMap.put("result", "failed");
		}else if(flag.equals("logo")){//appinfo里删除logo
			fileLocPath = appInfoService.getAppInfo(oprId,null).getLogoLocPath();//获取图片地址
			File file = new File(fileLocPath);
		    if(file.exists()){//存在图片
			     if(file.delete()){//删除图片
			    	 result = appInfoService.deleteAppLogo(oprId);//执行删除
			    	 if(result>0)
						resultMap.put("result", "success");
			     }	
		    }
		    logger.info("-appid["+id+"]删除logo结果["+result+"]");
		}else if(flag.equals("apk")){//version里删除apk文件
			fileLocPath = appVersionService.getAppVersionList(oprId, null).get(0).getApkLocPath();//获取apk文件地址
			File file = new File(fileLocPath);
		    if(file.exists()){//存在图片
			     if(file.delete()){//删除图片
			    	 result = appVersionService.deleteApkFile(oprId);//执行删除
			    	 if(result>0)
						resultMap.put("result", "success");
			     }	
		    }
		    logger.info("-appid["+id+"]删除apk文件结果["+result+"]");
		}
		return JSON.toJSONString(resultMap);
	}
	
	/**
	 * 查询该app所有版本,并跳转添加版本页面
	 * @param id appid
	 * @param model 保存查询app的版本信息
	 * @return
	 */
	@RequestMapping("/appversionadd")
	public String toVersionAdd(@RequestParam("id") String appId,
			@RequestParam(value="err",required= false)String err, AppVersion appVersion,Model model){
		String fileUploadError=ManageUtil.ErrJudge(err);//根据错误信息选择错误提示
		if (fileUploadError!=null) {
			logger.info("--版本添加出现异常[" + fileUploadError+"]");
		}	
		logger.info("[DevAppController]跳转添加版本[toVersionAdd()]====================================");
		logger.info("--要为id["+appId+"]的app添加版本");
		//查询该app的版本列表
		List<AppVersion> appVersionList  = appVersionService.getAppVersionList(null,Integer.parseInt(appId));
		//保存app版本信息
		appVersion.setAppId(Integer.parseInt(appId));
		appVersion.setAppName((appInfoService.getAppInfo(Integer.parseInt(appId),null)).getSoftwareName());
		model.addAttribute("appVersionList", appVersionList);
		model.addAttribute(appVersion);
		model.addAttribute("fileUploadError",fileUploadError);
		return "developer/appversionadd";
	}
	
	/**
	 * 添加app版本,上传apk文件
	 * @param attach apk文件
	 * @param appVersion 版本信息
	 * @param session
	 * @param request
	 * @return
	 */
	@RequestMapping("/addversionsave")
	public String addSersionSave(@RequestParam(value="a_downloadLink",required= false) MultipartFile attach,
			AppVersion appVersion,HttpSession session,HttpServletRequest request){
		logger.info("[DevAppController]执行新增版本[addSersionSave()]====================================");
		Integer appId=appVersion.getAppId();//appId
		logger.info("-更新版本的appid["+appId+"]");
		String downloadLink =  null;//下载地址
		String apkLocPath = null;//本地保存地址
		String apkFileName = null;//apk文件名
		
		if(!attach.isEmpty()){//上传文件不为空
			String path = request.getSession().getServletContext()
					.getRealPath("statics"+java.io.File.separator+"uploadfiles");//存储文件地址
			logger.info("-文件存储路径:"+path);
			String oldFileName = attach.getOriginalFilename();//获取原文件名
			logger.info("-原文件名:"+oldFileName);
			String prefix = FilenameUtils.getExtension(oldFileName);//原文件后缀
			logger.info("-原文件后缀:"+prefix);
			int filesize = 500;//500m
			if (attach.getSize()/1024/1024>filesize) {//attach.getSize()字节,限制大小为500m
				return "redirect:/dev/flatform/app/appversionadd?id="+appId+"&err=sizeErr";
			}else{//大小合格	
				if(prefix.equalsIgnoreCase("apk")){//如果是apk文件
					 //通过appinfo服务获取app的apkName
					 String apkName = appInfoService.getAppInfo(appId,null).getAPKName();
					 if(apkName == null || apkName.equals("")){ //apk名为空,则重新查询跳转
						 return "redirect:/dev/flatform/app/appversionadd?id="+appId+"&err=nameErr";
					 }
					 //设置apk文件名:apknamne + _ + 版本号 +.akp
					 apkFileName = apkName + "-" +appVersion.getVersionNo() + ".apk";
					 File targetFile = new File(path,apkFileName);
					 if(!targetFile.exists()){
						 targetFile.mkdirs();//创建文件
					 }
					 try {
						attach.transferTo(targetFile);
					 } catch (Exception e) {
						e.printStackTrace();
						//异常重新查询跳转
						return "redirect:/dev/flatform/app/appversionadd?id="+appId+"&err=uploadErr";
					 }
					 downloadLink = request.getContextPath()+"/statics/uploadfiles/"+apkFileName;//下载地址
					 apkLocPath = path+File.separator+apkFileName;//本地地址
				}else{
					//格式不对重新查询跳转
					return "redirect:/dev/flatform/app/appversionadd?id="+appId+"&err=prefixErr";
				}//end of prefix
			}//end of size
		}//end of isEmpty
		DevUser devUser = (DevUser)session.getAttribute("devUserSession");//获取当前登录开发人员
		//修改版本信息
		appVersion.setCreatedBy(devUser.getId());
		appVersion.setCreationDate(new Date());
		appVersion.setDownloadLink(downloadLink);
		appVersion.setApkLocPath(apkLocPath);
		appVersion.setApkFileName(apkFileName);
		int result = appVersionService.addVersion(appVersion);
		if(result>0)//执行添加成功后
			return "redirect:/dev/flatform/app/list";//跳转页面
		logger.info("--添加新版本执行结果["+result+"]");
		return "developer/appinfomodify";
		//return "redirect:/dev/flatform/app/appversionadd?id="+appId;
	}
	
	/**
	 * 跳转至修改版本页面
	 * @param vid 版本id
	 * @param aid appid
	 * @param err 错误
	 * @param model 
	 * @return
	 */
	@RequestMapping("/appversionmodify")
	public String toAppInfoModify(@RequestParam(value="vid")String vid,@RequestParam(value="aid")String aid,
			@RequestParam(value="error",required= false)String err,Model model){
		String fileUploadError= ManageUtil.ErrJudge(err);
		if (fileUploadError!=null) {
			logger.info("--版本修改出现异常[" + fileUploadError+"]");
		}
		logger.info("[DevAppController]跳转修改版本[toAppInfoModify()]====================================");
		//根据条件获取最新appversion,以及该app的所有版本
		AppVersion appVersion = appVersionService.getAppVersionList(Integer.parseInt(vid),null).get(0);
		List<AppVersion> appVersionList = appVersionService.getAppVersionList(null,Integer.parseInt(aid));
		logger.info("--appid["+aid+"],有版本["+appVersionList.size()+"]个,最新版本id["+appVersion.getId()+"],版本号["+appVersion.getVersionNo()+"]");
		
		model.addAttribute(appVersion);
		model.addAttribute("appVersionList",appVersionList);
		model.addAttribute("fileUploadError",fileUploadError);
		return "developer/appversionmodify";
	}

	 /**
	  * 保存修改最新版本
	  * @param attach 上传文件
	  * @param appVersion 表单信息
	  * @param request
	  * @param session
	  * @return
	  */
	@RequestMapping("/appversionmodifysave")
	public String appVersionModifySave(@RequestParam(value="attach",required= false) MultipartFile attach,
			AppVersion appVersion,HttpServletRequest request,HttpSession session){
		logger.info("[DevAppController]执行修改版本[appVersionModifySave()]====================================");
		String downloadLink =  null;//下载地址
		String apkLocPath = null;//本地保存地址
		String apkFileName = null;//apk文件名
		Integer appId=appVersion.getAppId();//appId
		Integer versionId=appVersion.getId();//versionId
		logger.info("--appid["+appId+"],最新版本id["+appVersion.getId()+"],版本号["+appVersion.getVersionNo()+"]");
		
		if(!attach.isEmpty()){//上传文件不为空
			String path = request.getSession().getServletContext()
					.getRealPath("statics"+java.io.File.separator+"uploadfiles");//存储文件地址
			logger.info("-文件存储路径:"+path);
			String oldFileName = attach.getOriginalFilename();//获取原文件名
			logger.info("-原文件名:"+oldFileName);
			String prefix = FilenameUtils.getExtension(oldFileName);//原文件后缀
			logger.info("-原文件后缀:"+prefix);
			int filesize = 500;//500m
			if (attach.getSize()/1024/1024>filesize) {//attach.getSize()字节,限制大小为500m
				return "redirect:/dev/flatform/app/appversionmodify?vid="+versionId+"&aid="+appId+"&err=sizeErr";
			}else{
				if(prefix.equalsIgnoreCase("apk")){//如果是apk文件
					 //通过appinfo服务获取app的apkName
					 String apkName = appInfoService.getAppInfo(appId,null).getAPKName();
					//apk名为空,则重新查询跳转
					 if(apkName == null || apkName.equals("")){ 
						 return "redirect:/dev/flatform/app/appversionmodify?vid="+versionId+"&aid="+appId+"&err=nameErr";
					 }
					 //设置apk文件名:apknamne + _ + 版本号 +.akp
					 apkFileName = apkName + "-" +appVersion.getVersionNo() + ".apk";
					 File targetFile = new File(path,apkFileName);
					 if(!targetFile.exists()){
						 targetFile.mkdirs();//创建文件
					 }
					 try {
						attach.transferTo(targetFile);
					 } catch (Exception e) {
						e.printStackTrace();
						//异常重新查询跳转
						return "redirect:/dev/flatform/app/appversionmodify?vid="+versionId+"&aid="+appId+"&err=uploadErr";
					 }
					 downloadLink = request.getContextPath()+"/statics/uploadfiles/"+apkFileName;//下载地址
					 apkLocPath = path+File.separator+apkFileName;//本地地址
				}else{
					//格式不对重新查询跳转
					return "redirect:/dev/flatform/app/appversionmodify?vid="+versionId+"&aid="+appId+"&err=prefixErr";
				}//end of prefix
			}//end of size
		}//end of isEmpty
		DevUser devUser = (DevUser)session.getAttribute("devUserSession");//获取当前登录开发人员
		//修改版本信息
		appVersion.setModifyBy(devUser.getId());
		appVersion.setModifyDate(new Date());
		appVersion.setDownloadLink(downloadLink);
		appVersion.setApkLocPath(apkLocPath);
		appVersion.setApkFileName(apkFileName);
		
		int result = appVersionService.updateVersion(appVersion);
		if(result>0)//执行修改后成功后
			return "redirect:/dev/flatform/app/list";//跳转页面
		logger.info("--修改新版本执行结果["+result+"]");
		return "developer/appversionmodify";//文件为空重新跳转修改页面
	}
	
	/**
	 * 查看app信息
	 * @param id 显示的app的id
	 * @param model
	 * @return
	 */
	@RequestMapping("/appview/{id}")
	public String appView(@PathVariable String id,Model model){
		logger.info("[DevAppController]执行查看信息[appView()]====================================");
		Integer appId = ManageUtil.strToInteger(id);
		//查询app信息和版本列表
		AppInfo appInfo = appInfoService.getAppInfo(appId,null);
		List<AppVersion> appVersionList = appVersionService.getAppVersionList(null, appId);
		logger.info("--appid["+appInfo.getId()+"],appName["+appInfo.getSoftwareName()+"],有版本["+appVersionList.size()+"]条");
		
		model.addAttribute("appVersionList", appVersionList);
		model.addAttribute(appInfo);
		return "developer/appinfoview";
	}

	/**
	 * ajax实现删除appinfo
	 * @param flag
	 * @param id appid
	 * @return
	 */
	@RequestMapping(value="/delapp.json")
	@ResponseBody
	public Object delApp(@RequestParam String id){
		logger.info("[DevAppController]执行删除信息[delApp()]====================================");
		HashMap<String, String> resultMap = new HashMap<String, String>();//保存结果
		Integer appId = ManageUtil.strToInteger(id);//转换格式
		logger.info("-删除app的id["+appId+"]");
		int result = -1;
		if (appId==null) {//id为空了
			resultMap.put("delResult", "notexist");
		}else{
			result = appInfoService.deleteAppInfo(appId);//执行删除
			if (result>0) {
				resultMap.put("delResult", "true");//删除成功
			}else{
				resultMap.put("delResult", "false");//删除失败
			}
			logger.info("--执行了删除,结果["+result+"]");
		}	
		return JSONArray.toJSONString(resultMap);
	}	

	/**
	 * 上下架
	 * @param appid
	 * @param session
	 * @return
	 */
	@RequestMapping("/{appid}/sale")
	@ResponseBody
	public Object sale(@PathVariable String appid,HttpSession session){
		logger.info("[DevAppController]执行上架功能[sale()]====================================");
		HashMap<String, Object> resultMap = new HashMap<String, Object>();
		Integer id = ManageUtil.strToInteger(appid);
		resultMap.put("errorCode", "0");//errorCode:0为正常
		resultMap.put("appId", id);
		if (id != null) {
			DevUser devUser = (DevUser)session.getAttribute("devUserSession");//操作人员
			AppInfo appInfo = appInfoService.getAppInfo(id, null);//要修改的appInfo
			appInfo.setModifyBy(devUser.getId());
			try {
				if (appInfoService.updateSatus(appInfo) > 0) {
					resultMap.put("resultMsg", "success");
				}
			} catch (Exception e) {
				resultMap.put("errorCode", "exception000001");
			}	
		}else{
			resultMap.put("errorCode", "param000001");
		}
		return resultMap;
	}
}
