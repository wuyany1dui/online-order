package com.design.onlineorder.config;

import com.alibaba.excel.converters.Converter;
import com.alibaba.excel.enums.CellDataTypeEnum;
import com.alibaba.excel.metadata.CellData;
import com.alibaba.excel.metadata.GlobalConfiguration;
import com.alibaba.excel.metadata.property.ExcelContentProperty;
import com.design.onlineorder.enums.BaseEnum;

/**
 * @author DrEAmSs
 */
public class BaseEnumConverter implements Converter<BaseEnum<?>> {

    @Override
    public Class<?> supportJavaTypeKey() {
        return BaseEnum.class;
    }

    @Override
    public CellDataTypeEnum supportExcelTypeKey() {
        return null;
    }

    @Override
    public BaseEnum<?> convertToJavaData(CellData cellData, ExcelContentProperty excelContentProperty,
                                         GlobalConfiguration globalConfiguration) {
        return null;
    }

    @Override
    public CellData<?> convertToExcelData(BaseEnum baseEnum, ExcelContentProperty excelContentProperty,
                                          GlobalConfiguration globalConfiguration) {
        return new CellData<>(baseEnum.getLabel());
    }
}
