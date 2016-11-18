package browser;

/**
 * Selenium�ł̃u���E�U�p�t�H�[�}���X
 *
 */
public class BrowserPerformance {


	/**
	 * ���N�G�X�g����
	 */
	private long requestTime;
	
	/**
	 * ���X�|���X����
	 */
	private long responseTime;

	/**
	 * DNS�擾����
	 */
	private long domainLookupTime;
	
	/**
	 * DOM�\�z����
	 */
	private long domLoadingTime;

	/**
	 * onLoad�C�x���g�̏�������
	 */
	private long loadEventTime;

	/**
	 * �����_�����O�̏�������
	 * <pre>
	 * �y�[�W�ǂݍ��݂���olLoad�C�x���g�����܂ł̎���
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
