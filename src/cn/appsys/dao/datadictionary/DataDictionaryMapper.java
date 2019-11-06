package cn.appsys.dao.datadictionary;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import cn.appsys.pojo.DataDictionary;
/**
 * 数据字典dao接口
 * @author Administrator
 *
 */
public interface DataDictionaryMapper {
	/**
	 * 根据类型查询数据字典列表,不传递参数为查询所有
	 * @param typeCode
	 * @return
	 * @throws Exception
	 */
	public List<DataDictionary> getDataDictionaryByTypeCode(@Param(value="typeCode")String typeCode);
}
