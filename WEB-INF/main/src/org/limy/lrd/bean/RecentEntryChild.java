package org.limy.lrd.bean;

public class RecentEntryChild {
    
    /** Lrdファイルパス */
    private String path;
    
    /** Lrdページ名 */
    private String title;

    public String getHref() {
        return path.substring(0, path.length() - 4) + ".html";
    }
    
    /**
     * Lrdファイルパスを取得します。
     * @return Lrdファイルパス
     */
    public String getPath() {
        return path;
    }

    /**
     * Lrdファイルパスを設定します。
     * @param path Lrdファイルパス
     */
    public void setPath(String path) {
        this.path = path;
    }

    /**
     * Lrdページ名を取得します。
     * @return Lrdページ名
     */
    public String getTitle() {
        return title;
    }

    /**
     * Lrdページ名を設定します。
     * @param title Lrdページ名
     */
    public void setTitle(String title) {
        this.title = title;
    }

}
