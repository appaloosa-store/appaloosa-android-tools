package appaloosa_store.com.appaloosa_android_tools.analytics.db;

public enum DBColumn {
    ID(0),
    EVENT(1);

    public static final String[] COLUMNS = new String[] {ID.toString(), EVENT.toString()};

    private Integer index;

    DBColumn(int index) {
        this.index = index;
    }

    public Integer getIndex() {
        return index;
    }
}