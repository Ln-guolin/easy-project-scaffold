package @packageName@.db.common.enums;

import com.baomidou.mybatisplus.generator.config.rules.IColumnType;

/**
 * Project:               afrbtc2-db
 * Author:                zhxd
 * Company:               Big Player Group
 * Created Date:          2019-06-05
 * Description:
 * Copyright @ 2017-2019 JUWAN.GROUP – Confidential and Proprietary
 * <p>
 * History:
 * ------------------------------------------------------------------------------
 * Date            |time        |Author    |Change Description
 */
public enum DbColumnType implements IColumnType {

    DATE("Date", "java.util.Date"),
    STRING("String", "java.lang.String"),
    ;

    private final String type;
    private final String pkg;

    private DbColumnType(final String type, final String pkg) {
        this.type = type;
        this.pkg = pkg;
    }

    @Override
    public String getType() {
        return this.type;
    }

    @Override
    public String getPkg() {
        return this.pkg;
    }
}
