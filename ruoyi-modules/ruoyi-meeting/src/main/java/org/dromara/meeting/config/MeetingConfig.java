package org.dromara.meeting.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;

/**
 * Meeting模块配置
 *
 * @author Lion Li
 */
@Configuration
@MapperScan("org.dromara.meeting.mapper")
public class MeetingConfig {
}