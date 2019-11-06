package cn.appsys.service.datadictionary;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import cn.appsys.dao.datadictionary.DataDictionaryMapper;
import cn.appsys.pojo.DataDictionary;
/**
 * 字典服务实现咧
 * @author Administrator
 *
 */
@Service
public class DatadictionaryServiceImpl implements DataDictionaryService {
	@Autowired
	DataDictionaryMapper mapper;//dao接口
	@Override
	public List<DataDictionary> getDataDictionaryByTypeCode(String typeCode) {
		return mapper.getDataDictionaryByTypeCode(typeCode);
	}

}
