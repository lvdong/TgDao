package com.tg.dao.generator.sql.where;

import com.tg.dao.annotation.ModelCondition;
import com.tg.dao.annotation.ModelConditions;
import com.tg.dao.constant.SqlMode;
import com.tg.dao.generator.model.TableMapping;
import com.tg.dao.generator.sql.where.param.Param;
import org.dom4j.Element;

import javax.lang.model.element.ExecutableElement;

/**
 * Created by twogoods on 2017/8/2.
 */
public class ModelWhereSqlGen extends AbstractWhereSqlGen {
    private ModelConditions modelConditions;

    public ModelWhereSqlGen(ExecutableElement executableElement, TableMapping tableInfo, SqlMode sqlMode, ModelConditions modelConditions) {
        super(executableElement, tableInfo, sqlMode);
        this.modelConditions = modelConditions;
    }

    @Override
    public void generateWhereSql(Element sqlElement) {
        ModelCondition[] conditions = modelConditions.value();
        if (conditions.length == 0) {
            return;
        }
        Element whereElement = sqlElement.addElement("where");
        for (ModelCondition modelCondition : conditions) {
            generateModelWhereParam(whereElement, modelCondition);
        }
    }

    private void generateModelWhereParam(Element whereElement, ModelCondition modelCondition) {
        if (modelCondition.criterion().inCriterion()) {
            generateINSuffix(modelCondition, whereElement);
            return;
        }
        Param param = new Param.Builder().whereElement(whereElement)
                .criterion(modelCondition.criterion())
                .attach(modelCondition.attach())
                .column(getColumn(modelCondition.column(), modelCondition.field()))
                .field(modelCondition.field())
                .test(modelCondition.test())
                .build();
        whereParamSqlGen.generateWhereParamSql(param);
    }
}
