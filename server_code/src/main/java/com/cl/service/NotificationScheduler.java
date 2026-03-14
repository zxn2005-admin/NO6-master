package com.cl.service;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.cl.entity.JiuzhentongzhiEntity;
import com.cl.entity.TongzhiRecordEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

@Component
public class NotificationScheduler {

    private static final Logger logger = LoggerFactory.getLogger(NotificationScheduler.class);

    @Autowired
    private JiuzhentongzhiService jiuzhentongzhiService;

    @Autowired
    private TongzhiRecordService tongzhiRecordService;

    @Scheduled(fixedRate = 30000)
    public void sendNotifications() {
        logger.info("开始执行通知发送任务...");
        
        EntityWrapper<TongzhiRecordEntity> wrapper = new EntityWrapper<>();
        wrapper.in("status", new String[]{"待发送", "待重试"});
        List<TongzhiRecordEntity> records = tongzhiRecordService.selectList(wrapper);
        
        for (TongzhiRecordEntity record : records) {
            try {
                sendNotification(record);
            } catch (Exception e) {
                logger.error("发送通知失败，记录ID: " + record.getId(), e);
                handleSendFailure(record, e.getMessage());
            }
        }
        
        logger.info("通知发送任务执行完成");
    }

    private void sendNotification(TongzhiRecordEntity record) {
        logger.info("正在发送通知，通知编号: " + record.getTongzhibianhao());
        
        record.setStatus("发送中");
        tongzhiRecordService.updateById(record);
        
        boolean sendSuccess = simulateSendNotification(record);
        
        if (sendSuccess) {
            record.setStatus("成功");
            record.setSendTime(new Date());
            tongzhiRecordService.updateById(record);
            
            JiuzhentongzhiEntity notification = jiuzhentongzhiService.selectById(record.getJiuzhentongzhiId());
            if (notification != null) {
                notification.setStatus("成功");
                jiuzhentongzhiService.updateById(notification);
            }
            
            logger.info("通知发送成功，通知编号: " + record.getTongzhibianhao());
        } else {
            throw new RuntimeException("模拟发送失败");
        }
    }

    private boolean simulateSendNotification(TongzhiRecordEntity record) {
        return true;
    }

    private void handleSendFailure(TongzhiRecordEntity record, String errorMessage) {
        record.setRetryCount(record.getRetryCount() + 1);
        record.setErrorMessage(errorMessage);
        
        if (record.getRetryCount() >= 3) {
            record.setStatus("失败");
            logger.error("通知发送失败超过3次，标记为失败，通知编号: " + record.getTongzhibianhao());
        } else {
            record.setStatus("待重试");
            logger.warn("通知发送失败，将进行重试，通知编号: " + record.getTongzhibianhao() + ", 重试次数: " + record.getRetryCount());
        }
        
        tongzhiRecordService.updateById(record);
        
        JiuzhentongzhiEntity notification = jiuzhentongzhiService.selectById(record.getJiuzhentongzhiId());
        if (notification != null) {
            notification.setStatus(record.getStatus());
            jiuzhentongzhiService.updateById(notification);
        }
    }
}
