package com.cl.entity.view;

import com.cl.entity.TongzhiRecordEntity;

import com.baomidou.mybatisplus.annotations.TableName;
import org.apache.commons.beanutils.BeanUtils;
import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;

import java.io.Serializable;
import com.cl.utils.EncryptUtil;
 

@TableName("tongzhi_record")
public class TongzhiRecordView  extends TongzhiRecordEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	public TongzhiRecordView(){
	}
 
 	public TongzhiRecordView(TongzhiRecordEntity tongzhiRecordEntity){
 	try {
			BeanUtils.copyProperties(this, tongzhiRecordEntity);
		} catch (IllegalAccessException | InvocationTargetException e) {
			e.printStackTrace();
		}
 		
	}
	


}
