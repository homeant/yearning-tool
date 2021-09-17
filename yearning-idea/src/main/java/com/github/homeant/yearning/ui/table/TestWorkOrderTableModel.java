package com.github.homeant.yearning.ui.table;

import com.github.homeant.yearning.api.domain.TestWorkOrderResp;
import com.intellij.util.ui.ColumnInfo;
import com.intellij.util.ui.ListTableModel;

import static com.github.homeant.yearning.utils.YearningUtils.newColumnInfo;

public class TestWorkOrderTableModel extends ListTableModel<TestWorkOrderResp> {

    public TestWorkOrderTableModel(){
        super();
        setColumnInfos(new ColumnInfo[]{
                newColumnInfo("sql语句", TestWorkOrderResp::getSql),
                newColumnInfo("错误等级", TestWorkOrderResp::getLevel),
                newColumnInfo("错误信息", TestWorkOrderResp::getError)
        });
    }
}
