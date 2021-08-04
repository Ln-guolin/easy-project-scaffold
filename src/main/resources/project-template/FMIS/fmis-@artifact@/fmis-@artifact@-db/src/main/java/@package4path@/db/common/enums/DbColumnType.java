package @packageName@.db.common.enums;

import com.baomidou.mybatisplus.generator.config.rules.IColumnType;

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
