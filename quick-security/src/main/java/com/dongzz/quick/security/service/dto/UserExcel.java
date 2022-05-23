package com.dongzz.quick.security.service.dto;

import com.dongzz.quick.common.annotation.excel.ExcelColumn;
import com.dongzz.quick.common.annotation.excel.ExcelDate;
import com.dongzz.quick.common.annotation.excel.ExcelGroup;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

@Setter
@Getter
@ToString
public class UserExcel {

    @ExcelColumn(value = "ID", col = 1)
    private Integer id;

    @ExcelColumn(value = "账号", col = 2)
    @ExcelGroup("\\|")
    private String username;

    @ExcelColumn(value = "密码", col = 3)
    private String password;

    @ExcelColumn(value = "账号", col = 4)
    @ExcelGroup("\\|")
    private String nickname;

    @ExcelColumn(value = "生日", col = 5)
    @ExcelDate("yyyy-MM-dd")
    private Date birthday;


}
