package cn.appsys.service.datadictionary;
import java.util.List;
import cn.appsys.pojo.DataDictionary;
/**
 * 字典服务接口
 * @author Administrator
 */
public interface DataDictionaryService {
	/**
	 * 根据类型查询数据字典列表,不传递参数为查询所有
	 * @param typeCode
	 * @return
	 * @throws Exception
	 */
	public List<DataDictionary> getDataDictionaryByTypeCode(String typeCode);
}
