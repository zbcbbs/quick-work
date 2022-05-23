package com.dongzz.quick.tools.service.impl;

import com.dongzz.quick.tools.service.SmsService;
import org.springframework.stereotype.Service;

/**
 * 阿里云 短信服务接口
 */
@Service
public class SmsServiceAlibabaImpl implements SmsService {

    @Override
    public String sendSmsCode(String no, int length) throws Exception {
        return null;
    }

    @Override
    public boolean validateSmsCode(String no, String input) throws Exception {
        return false;
    }
}
