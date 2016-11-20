package browser;

/**
 * Seleniumでのブラウザパフォーマンス
 *
 */
public class BrowserPerformance {


    /**
     * リクエスト時間
     */
    private long requestTime;
    
    /**
     * レスポンス時間
     */
    private long responseTime;

    /**
     * DNS取得時間
     */
    private long domainLookupTime;
    
    /**
     * DOM構築時間
     */
    private long domLoadingTime;

    /**
     * onLoadイベントの処理時間
     */
    private long loadEventTime;

    /**
     * レンダリングの処理時間
     * <pre>
     * ページ読み込みからolLoadイベント完了までの時間
     * </pre>
     */
    private long renderingTime;


    @Override
    public String toString() {
        return "BrowserPerformance [requestTime=" + requestTime + ", responseTime=" + responseTime
                + ", domainLookupTime=" + domainLookupTime + ", domLoadingTime=" + domLoadingTime + ", loadEventTime="
                + loadEventTime + ", renderingTime=" + renderingTime + "]";
    }

    public long getRequestTime() {
        return requestTime;
    }

    public void setRequestTime(long requestTime) {
        this.requestTime = requestTime;
    }

    public long getResponseTime() {
        return responseTime;
    }

    public void setResponseTime(long responseTime) {
        this.responseTime = responseTime;
    }

    public long getDomainLookupTime() {
        return domainLookupTime;
    }

    public void setDomainLookupTime(long domainLookupTime) {
        this.domainLookupTime = domainLookupTime;
    }

    public long getDomLoadingTime() {
        return domLoadingTime;
    }

    public void setDomLoadingTime(long domLoadingTime) {
        this.domLoadingTime = domLoadingTime;
    }

    public long getLoadEventTime() {
        return loadEventTime;
    }

    public void setLoadEventTime(long loadEventTime) {
        this.loadEventTime = loadEventTime;
    }

    public long getRenderingTime() {
        return renderingTime;
    }

    public void setRenderingTime(long renderingTime) {
        this.renderingTime = renderingTime;
    }
    
    
}
