package cn.appsys.tools;
/**
 * 工具类
 * 功能1:字符串转换为integer
 * 功能2:选择错误提示
 * @author Administrator
 *
 */
public class ManageUtil {
	/**
	 * 字符串转换为integer
	 * @param str
	 * @return
	 */
	public static Integer strToInteger(String str){
		//不为空则转换.为空则返回空
		return (str != null && !str.equals(""))? Integer.parseInt(str): null;
//		if (str != null && !str.equals("")) return Integer.parseInt(str);
//		return null;
	}
	
	/**
	 * 根据错误返回提示信息
	 * @param err
	 * @return
	 */
	public static String ErrJudge(String err){
		if (err!=null) { //判断错误信息
			if (err.equals("sizeErr")) {
				return "上传文件尺寸过大!";
			}else if(err.equals("nameErr")){
				return "APK信息不完整！";
			}else if(err.equals("uploadErr")){
				return "上传文件失败！";
			}else if(err.equals("prefixErr")){
				return "文件格式不正确！";
			}
		}	
		return null;
	}
	
}
