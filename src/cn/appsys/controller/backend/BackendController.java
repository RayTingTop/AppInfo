package cn.appsys.controller.backend;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

import cn.appsys.controller.developer.DevLoginController;
import cn.appsys.pojo.AppCategory;
import cn.appsys.pojo.AppInfo;
import cn.appsys.pojo.AppVersion;
import cn.appsys.pojo.DataDictionary;
import cn.appsys.service.appcategory.AppcategoryService;
import cn.appsys.service.appinfo.AppInfoService;
import cn.appsys.service.appversion.AppVersionService;
import cn.appsys.service.backend.BackendUserService;
import cn.appsys.service.datadictionary.DataDictionaryService;
import cn.appsys.tools.ManageUtil;

/**
 * 
 * @author LinWen 
 */
@RequestMapping("manager")
@Controller
public class BackendController {
	
	private Logger logger = Logger.getLogger(DevLoginController.class);// 日志
	@Autowired
	private AppInfoService appInfoService;// app信息服务实现类
	@Autowired
	private DataDictionaryService dataDictionaryService;//字典服务实现类
	@Autowired
	private AppcategoryService appCategoryService;// app等级服务实现类
	@Autowired
	private AppVersionService appVersionService; // app版本
	
	/**
	 * 后台APP列表查询
	 * @param session
	 * @return
	 */
	@RequestMapping("/backend/app/list")
	public String appList(Model model,
			@RequestParam(value = "pageIndex", required = false , defaultValue="1")Integer currentPageNo,
			@RequestParam(value="querySoftwareName",required=false) String querySoftwareName,
			@RequestParam(value="queryStatus",required=false) String _queryStatus,
			@RequestParam(value="queryFlatformId",required=false) String _queryFlatformId,
			@RequestParam(value="queryCategoryLevel1",required=false) String _queryCategoryLevel1,
			@RequestParam(value="queryCategoryLevel2",required=false) String _queryCategoryLevel2,
			@RequestParam(value="queryCategoryLevel3",required=false) String _queryCategoryLevel3,
			HttpSession session){
		
		logger.info("[DevAppController]执行查询AppInfo列表[list()]====================================");
		logger.info("-查询条件[pageIndex]:"+currentPageNo);
		logger.info("------[querySoftwareName]:"+querySoftwareName);
		logger.info("------[queryStatus]:"+_queryStatus);
		logger.info("------[queryFlatformId]:"+_queryFlatformId);
		logger.info("------[queryCategoryLevel1]:"+_queryCategoryLevel1);
		logger.info("------[queryCategoryLevel2]:"+_queryCategoryLevel2);
		logger.info("------[queryCategoryLevel3]:"+_queryCategoryLevel3);
		
		session.setAttribute("pageIndex", currentPageNo);
		session.setAttribute("querySoftwareName", querySoftwareName);
		session.setAttribute("queryStatus", _queryStatus);
		session.setAttribute("queryFlatformId", _queryFlatformId);
		session.setAttribute("queryCategoryLevel1", _queryCategoryLevel1);
		session.setAttribute("queryCategoryLevel2", _queryCategoryLevel2);
		session.setAttribute("queryCategoryLevel3", _queryCategoryLevel3);
		
		Integer queryStatus = ManageUtil.strToInteger(_queryStatus);//查询的app状态
		Integer queryFlatformId = ManageUtil.strToInteger(_queryFlatformId);//查询的平台
		Integer queryCategoryLevel1 = ManageUtil.strToInteger(_queryCategoryLevel1);//查询一级列表
		Integer queryCategoryLevel2 = ManageUtil.strToInteger(_queryCategoryLevel2);//查询二级列表
		Integer queryCategoryLevel3 = ManageUtil.strToInteger(_queryCategoryLevel3);//查询二级列表
		
		//分页处理
		PageHelper.startPage(currentPageNo, 4);//pagehelper实现分页
		List<AppInfo> allAppInfo = appInfoService.getAppInfoList(querySoftwareName,queryStatus,queryFlatformId,queryCategoryLevel1,queryCategoryLevel2,queryCategoryLevel3,null);// 查询appinfo
		PageInfo appInfoList = new PageInfo(allAppInfo);// 使用pageinfo
		logger.info("--查询获得条目数量["+appInfoList.getTotal()+"],当前返回第[" + currentPageNo + "]页,共[" + allAppInfo.size() + "]条记录");
		
		//下拉列表-使用本类中方法getDataDictionaryList()查询状态列表和平台列表
		List<DataDictionary> flatFormList = this.getDataDictionaryList("APP_FLATFORM");
		//下拉列表-等级第一级列表
		List<AppCategory> categoryLevel1List = appCategoryService.getAppCategoryListByParentId(null);
		
		model.addAttribute("appInfoList", appInfoList);
		model.addAttribute("categoryLevel1List", categoryLevel1List);
		model.addAttribute("flatFormList", flatFormList);
		
		
		return "backend/applist";
	}
	
	@ResponseBody
	@RequestMapping("/backend/app/categorylevellist.json")
	public List<AppCategory> getCateGorylevelList(Integer pid){
		Integer queryPid= null;
		if (pid!=null&&!pid.equals("")) queryPid= Integer.valueOf(pid);
		//调用等级服务根据父级id查询等级列表
		List<AppCategory> categoryLevelList = appCategoryService.getAppCategoryListByParentId(queryPid);
		logger.info("[DevAppController]执行ajax查询等级列表[getCategoryLevelList()],查询父级id["+pid+"],获得["+categoryLevelList.size()+"]条记录");
		return categoryLevelList;
	}
	
	@RequestMapping("/backend/app/check")
	public String check(Model model,HttpSession session,Integer aid,Integer vid){
		model.addAttribute("appInfo",appInfoService.getAppInfoPlus(aid));
		model.addAttribute("appVersion",appVersionService.getAppVersion(vid));
		return "backend/appcheck";
	}
	
	@RequestMapping("/backend/app/checksave")
	public String checkSave(HttpSession session,Integer id,Integer status,Model model){
		if(appInfoService.updateAppInfoStatus(id, status)>0){
			if(status == 2){
				model.addAttribute("UpdateStatus","[审核通过]操作成功！");
			}else if(status == 3){
				model.addAttribute("UpdateStatus","[审核不通过]操作成功！");
			}
		}else{
			if(status == 2){
				model.addAttribute("UpdateStatus","[审核通过]操作失败！");
			}else if(status == 3){
				model.addAttribute("UpdateStatus","[审核不通过]操作失败！");
			}
		}
		return "backend/save";
	}
	
	/**
	 * 根据编码获取状态列表
	 * @param pid
	 * @return
	 */
	public List<DataDictionary> getDataDictionaryList(String typeCode){
		return dataDictionaryService.getDataDictionaryByTypeCode(typeCode);
	}
}
