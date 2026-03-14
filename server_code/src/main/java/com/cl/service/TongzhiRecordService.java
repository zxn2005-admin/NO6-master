package com.cl.service;

import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.service.IService;
import com.cl.utils.PageUtils;
import com.cl.entity.TongzhiRecordEntity;
import java.util.List;
import java.util.Map;
import org.apache.ibatis.annotations.Param;
import com.cl.entity.view.TongzhiRecordView;


public interface TongzhiRecordService extends IService<TongzhiRecordEntity> {

    PageUtils queryPage(Map<String, Object> params);
    
   	List<TongzhiRecordView> selectListView(Wrapper<TongzhiRecordEntity> wrapper);
   	
   	TongzhiRecordView selectView(@Param("ew") Wrapper<TongzhiRecordEntity> wrapper);
   	
   	PageUtils queryPage(Map<String, Object> params,Wrapper<TongzhiRecordEntity> wrapper);
   	
   	List<Map<String, Object>> selectValue(Map<String, Object> params, Wrapper<TongzhiRecordEntity> wrapper);
	
	List<Map<String, Object>> selectTimeStatValue(Map<String, Object> params, Wrapper<TongzhiRecordEntity> wrapper);
	
	List<Map<String, Object>> selectGroup(Map<String, Object> params, Wrapper<TongzhiRecordEntity> wrapper);
   
}
