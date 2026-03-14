package com.cl.controller;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.text.ParseException;
import java.util.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

import com.cl.utils.ValidatorUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.cl.annotation.IgnoreAuth;
import com.cl.annotation.SysLog;

import com.cl.entity.TongzhiRecordEntity;
import com.cl.entity.view.TongzhiRecordView;

import com.cl.service.TongzhiRecordService;
import com.cl.service.TokenService;
import com.cl.utils.PageUtils;
import com.cl.utils.R;
import com.cl.utils.MPUtil;
import com.cl.utils.MapUtils;
import com.cl.utils.CommonUtil;

@RestController
@RequestMapping("/tongzhirecord")
public class TongzhiRecordController {
    @Autowired
    private TongzhiRecordService tongzhiRecordService;


    @RequestMapping("/page")
    public R page(@RequestParam Map<String, Object> params,TongzhiRecordEntity tongzhirecord,
                                                                                                                                                    HttpServletRequest request){
                    String tableName = request.getSession().getAttribute("tableName").toString();
                                                                                                                                                EntityWrapper<TongzhiRecordEntity> ew = new EntityWrapper<TongzhiRecordEntity>();
                                                                                                                                                                                                                                
        PageUtils page = tongzhiRecordService.queryPage(params, MPUtil.sort(MPUtil.between(MPUtil.likeOrEq(ew, tongzhirecord), params), params));
        return R.ok().put("data", page);
    }


    @IgnoreAuth
    @RequestMapping("/list")
    public R list(@RequestParam Map<String, Object> params,TongzhiRecordEntity tongzhirecord,
		HttpServletRequest request){
        EntityWrapper<TongzhiRecordEntity> ew = new EntityWrapper<TongzhiRecordEntity>();

		PageUtils page = tongzhiRecordService.queryPage(params, MPUtil.sort(MPUtil.between(MPUtil.likeOrEq(ew, tongzhirecord), params), params));
        return R.ok().put("data", page);
    }

	@RequestMapping("/lists")
    public R list( TongzhiRecordEntity tongzhirecord){
       	EntityWrapper<TongzhiRecordEntity> ew = new EntityWrapper<TongzhiRecordEntity>();
      	ew.allEq(MPUtil.allEQMapPre( tongzhirecord, "tongzhirecord")); 
        return R.ok().put("data", tongzhiRecordService.selectListView(ew));
    }

	 @RequestMapping("/query")
    public R query(TongzhiRecordEntity tongzhirecord){
        EntityWrapper< TongzhiRecordEntity> ew = new EntityWrapper< TongzhiRecordEntity>();
 		ew.allEq(MPUtil.allEQMapPre( tongzhirecord, "tongzhirecord")); 
		TongzhiRecordView tongzhirecordView =  tongzhiRecordService.selectView(ew);
		return R.ok("查询通知记录成功").put("data", tongzhirecordView);
    }
	
    @RequestMapping("/info/{id}")
    public R info(@PathVariable("id") Long id){
        TongzhiRecordEntity tongzhirecord = tongzhiRecordService.selectById(id);
		tongzhirecord = tongzhiRecordService.selectView(new EntityWrapper<TongzhiRecordEntity>().eq("id", id));
        return R.ok().put("data", tongzhirecord);
    }

    @IgnoreAuth
    @RequestMapping("/detail/{id}")
    public R detail(@PathVariable("id") Long id){
        TongzhiRecordEntity tongzhirecord = tongzhiRecordService.selectById(id);
		tongzhirecord = tongzhiRecordService.selectView(new EntityWrapper<TongzhiRecordEntity>().eq("id", id));
        return R.ok().put("data", tongzhirecord);
    }
    

    @RequestMapping("/save")
    @SysLog("新增通知记录")
    public R save(@RequestBody TongzhiRecordEntity tongzhirecord, HttpServletRequest request){
    	ValidatorUtils.validateEntity(tongzhirecord);
        tongzhiRecordService.insert(tongzhirecord);
        return R.ok();
    }
    
    @IgnoreAuth
    @SysLog("新增通知记录")
    @RequestMapping("/add")
    public R add(@RequestBody TongzhiRecordEntity tongzhirecord, HttpServletRequest request){
    	ValidatorUtils.validateEntity(tongzhirecord);
        tongzhiRecordService.insert(tongzhirecord);
        return R.ok();
    }


    @RequestMapping("/update")
    @Transactional
    @SysLog("修改通知记录")
    public R update(@RequestBody TongzhiRecordEntity tongzhirecord, HttpServletRequest request){
        ValidatorUtils.validateEntity(tongzhirecord);
        tongzhiRecordService.updateById(tongzhirecord);
        return R.ok();
    }

    @RequestMapping("/retry/{id}")
    @Transactional
    @SysLog("重试发送通知")
    public R retry(@PathVariable("id") Long id, HttpServletRequest request){
        TongzhiRecordEntity tongzhirecord = tongzhiRecordService.selectById(id);
        if(tongzhirecord != null && "失败".equals(tongzhirecord.getStatus())) {
            tongzhirecord.setRetryCount(tongzhirecord.getRetryCount() + 1);
            tongzhirecord.setStatus("待重试");
            tongzhiRecordService.updateById(tongzhirecord);
            return R.ok("已标记为重试状态");
        }
        return R.error("该记录无法重试");
    }


    @RequestMapping("/delete")
    @SysLog("删除通知记录")
    public R delete(@RequestBody Long[] ids){
        tongzhiRecordService.deleteBatchIds(Arrays.asList(ids));
        return R.ok();
    }
    

    @RequestMapping("/value/{xColumnName}/{yColumnName}")
    public R value(@PathVariable("yColumnName") String yColumnName, @PathVariable("xColumnName") String xColumnName,HttpServletRequest request) {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("xColumn", MPUtil.camelToSnake(xColumnName));
        params.put("yColumn", MPUtil.camelToSnake(yColumnName));
        EntityWrapper<TongzhiRecordEntity> ew = new EntityWrapper<TongzhiRecordEntity>();
        List<Map<String, Object>> result = MPUtil.snakeListToCamel(tongzhiRecordService.selectValue(params, ew));
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        for(Map<String, Object> m : result) {
            for(String k : m.keySet()) {
                if(m.get(k) instanceof Date) {
                    m.put(k, sdf.format((Date)m.get(k)));
                }
            }
        }

        Collections.sort(result, (map1, map2) -> {
            Number total1 = (Number) map1.get("total");
            Number total2 = (Number) map2.get("total");
            if(total1==null)
            {
                total1 = 0;
            }
            if(total2==null)
            {
                total2 = 0;
            }
            String order = request.getParameter("order");
            if(StringUtils.isNotBlank(order)&&order.equals("desc")){
                return Double.compare(total2.doubleValue(), total1.doubleValue());
            }
            return Double.compare(total1.doubleValue(), total2.doubleValue());
        });

        return R.ok().put("data", result);
    }

    @RequestMapping("/valueMul/{xColumnName}")
    public R valueMul(@PathVariable("xColumnName") String xColumnName,@RequestParam String yColumnNameMul, HttpServletRequest request) {
        String[] yColumnNames = MPUtil.camelToSnake(yColumnNameMul).split(",");
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("xColumn", MPUtil.camelToSnake(xColumnName));
        List<List<Map<String, Object>>> result2 = new ArrayList<List<Map<String,Object>>>();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        EntityWrapper<TongzhiRecordEntity> ew = new EntityWrapper<TongzhiRecordEntity>();
        for(int i=0;i<yColumnNames.length;i++) {
            params.put("yColumn", yColumnNames[i]);
            List<Map<String, Object>> result = MPUtil.snakeListToCamel(tongzhiRecordService.selectValue(params, ew));
            for(Map<String, Object> m : result) {
                for(String k : m.keySet()) {
                    if(m.get(k) instanceof Date) {
                        m.put(k, sdf.format((Date)m.get(k)));
                    }
                }
            }
            result2.add(result);
        }
        return R.ok().put("data", result2);
    }

    @RequestMapping("/value/{xColumnName}/{yColumnName}/{timeStatType}")
    public R valueDay(@PathVariable("yColumnName") String yColumnName, @PathVariable("xColumnName") String xColumnName, @PathVariable("timeStatType") String timeStatType,HttpServletRequest request) {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("xColumn", MPUtil.camelToSnake(xColumnName));
        params.put("yColumn", MPUtil.camelToSnake(yColumnName));
        params.put("timeStatType", timeStatType);
        EntityWrapper<TongzhiRecordEntity> ew = new EntityWrapper<TongzhiRecordEntity>();
        List<Map<String, Object>> result = MPUtil.snakeListToCamel(tongzhiRecordService.selectTimeStatValue(params, ew));
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        for(Map<String, Object> m : result) {
            for(String k : m.keySet()) {
                if(m.get(k) instanceof Date) {
                    m.put(k, sdf.format((Date)m.get(k)));
                }
            }
        }
        return R.ok().put("data", result);
    }

    @RequestMapping("/valueMul/{xColumnName}/{timeStatType}")
    public R valueMulDay(@PathVariable("xColumnName") String xColumnName, @PathVariable("timeStatType") String timeStatType,@RequestParam String yColumnNameMul,HttpServletRequest request) {
        String[] yColumnNames = MPUtil.camelToSnake(yColumnNameMul).split(",");
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("xColumn", xColumnName);
        params.put("timeStatType", timeStatType);
        List<List<Map<String, Object>>> result2 = new ArrayList<List<Map<String,Object>>>();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        EntityWrapper<TongzhiRecordEntity> ew = new EntityWrapper<TongzhiRecordEntity>();
        for(int i=0;i<yColumnNames.length;i++) {
            params.put("yColumn", yColumnNames[i]);
            List<Map<String, Object>> result = MPUtil.snakeListToCamel(tongzhiRecordService.selectTimeStatValue(params, ew));
            for(Map<String, Object> m : result) {
                for(String k : m.keySet()) {
                    if(m.get(k) instanceof Date) {
                        m.put(k, sdf.format((Date)m.get(k)));
                    }
                }
            }
            result2.add(result);
        }
        return R.ok().put("data", result2);
    }

    @RequestMapping("/group/{columnName}")
    public R group(@PathVariable("columnName") String columnName,HttpServletRequest request) {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("column", MPUtil.camelToSnake(columnName));
        EntityWrapper<TongzhiRecordEntity> ew = new EntityWrapper<TongzhiRecordEntity>();
        List<Map<String, Object>> result = MPUtil.snakeListToCamel(tongzhiRecordService.selectGroup(params, ew));
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        for(Map<String, Object> m : result) {
            for(String k : m.keySet()) {
                if(m.get(k) instanceof Date) {
                    m.put(k, sdf.format((Date)m.get(k)));
                }
            }
        }
        return R.ok().put("data", result);
    }


    @RequestMapping("/count")
    public R count(@RequestParam Map<String, Object> params,TongzhiRecordEntity tongzhirecord, HttpServletRequest request){
        String tableName = request.getSession().getAttribute("tableName").toString();
        EntityWrapper<TongzhiRecordEntity> ew = new EntityWrapper<TongzhiRecordEntity>();
        int count = tongzhiRecordService.selectCount(MPUtil.sort(MPUtil.between(MPUtil.likeOrEq(ew, tongzhirecord), params), params));
        return R.ok().put("data", count);
    }


}
