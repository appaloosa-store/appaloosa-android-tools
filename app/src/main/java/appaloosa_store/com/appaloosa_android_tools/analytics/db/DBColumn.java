package appaloosa_store.com.appaloosa_android_tools.analytics.db;

public enum DBColumn {
    ID(0),
    TIME(1),
    CATEGORY(2),
    NAME(3),
    CONNECTION(4);

    public static final String[] COLUMNS = new String[] {ID.toString(), TIME.toString(), CATEGORY.toString(), NAME.toString(), CONNECTION.toString()};

    private Integer index;

    DBColumn(int index) {
        this.index = index;
    }

    public Integer getIndex() {
        return index;
    }
}