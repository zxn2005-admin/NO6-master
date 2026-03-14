package com.cl.dao;

import com.cl.entity.TongzhiRecordEntity;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import java.util.List;
import java.util.Map;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.pagination.Pagination;

import org.apache.ibatis.annotations.Param;
import com.cl.entity.view.TongzhiRecordView;


public interface TongzhiRecordDao extends BaseMapper<TongzhiRecordEntity> {
	
	List<TongzhiRecordView> selectListView(@Param("ew") Wrapper<TongzhiRecordEntity> wrapper);

	List<TongzhiRecordView> selectListView(Pagination page,@Param("ew") Wrapper<TongzhiRecordEntity> wrapper);
	
	TongzhiRecordView selectView(@Param("ew") Wrapper<TongzhiRecordEntity> wrapper);
	
	List<Map<String, Object>> selectValue(@Param("params") Map<String, Object> params, @Param("ew") Wrapper<TongzhiRecordEntity> wrapper);
	
	List<Map<String, Object>> selectTimeStatValue(@Param("params") Map<String, Object> params, @Param("ew") Wrapper<TongzhiRecordEntity> wrapper);
	
	List<Map<String, Object>> selectGroup(@Param("params") Map<String, Object> params, @Param("ew") Wrapper<TongzhiRecordEntity> wrapper);

}
