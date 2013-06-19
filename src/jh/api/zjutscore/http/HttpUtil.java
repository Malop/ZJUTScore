package jh.api.zjutscore.http;

import org.apache.commons.httpclient.*;
import org.apache.commons.httpclient.methods.*;
import org.apache.commons.httpclient.HttpClient;

public class HttpUtil {

	private String userId;
	private String password;
	private String term;
	private Cookie[] user_cookie;
	private HttpClient loginClient, queryClient, viewStateClient;
	private PostMethod loginPost, queryPost;
	private GetMethod viewStateGet;

	public HttpUtil(String userId, String password, String term) {
		this.userId = userId;
		this.password = password;
		this.term = term;
	}

	/*
	 * ���빦�ܵ�post
	 */
	public boolean login() throws Exception {
		// ���빦�ܵ�post
		loginClient = new HttpClient();
		loginPost = new PostMethod(NetConstant.URL_LOGIN);

		loginPost.setRequestHeader("Content-Type","application/x-www-form-urlencoded;charset=gb2312");
		loginPost.addParameter("__EVENTTARGET", "");
		loginPost.addParameter("__EVENTARGUMENT", "");
		loginPost.addParameter("__VIEWSTATE", NetConstant.LOGIN_ConstantParam);
		loginPost.addParameter("Cbo_LX", "ѧ��");
		loginPost.addParameter("Txt_UserName", userId);
		loginPost.addParameter("Txt_Password", password);
		loginPost.addParameter("Img_DL.x", "40");
		loginPost.addParameter("Img_DL.y", "13");

		int loginStatusCode = loginClient.executeMethod(loginPost);
		//System.out.println("aaa=" + loginStatusCode);

		// �õ��û�cookie��Ϣ
		user_cookie = loginClient.getState().getCookies();
		if (user_cookie.length != 0 && loginStatusCode == 302) {
			/*for (int i = 0; i < user_cookie.length; i++) {
				System.out.println("cookies[" + i + "]="+ user_cookie[i].toString());
			}*/
			loginPost.releaseConnection();
			return true;
		} else {
			// System.out.println("dddd","����ʧ�ܣ�δ��ȡcookies");
			return false;
		}
	}

	public String getViewStateHtml() throws Exception {

		viewStateClient = new HttpClient();
		viewStateGet = new GetMethod(NetConstant.URL_GetViewState);

		viewStateGet.setRequestHeader("Content-Type",
				"application/x-www-form-urlencoded;charset=gb2312");
		viewStateGet.setRequestHeader("Cookie", user_cookie[0].toString());

		viewStateClient.executeMethod(viewStateGet);

		String viewStateResponse = new String(viewStateGet
				.getResponseBodyAsString().getBytes("gb2312"));
		// System.out.println("viewStateResponse="+viewStateResponse);
		viewStateGet.releaseConnection();

		return viewStateResponse;

	}

	/*
	 * ��ѯ���ܵ�post
	 */
	public String query(String viewState) throws Exception {
		// ��ѯ���ܵ�post
		queryClient = new HttpClient();
		queryPost = new PostMethod(NetConstant.URL_CJCX);

		queryPost.setRequestHeader("Content-Type",
				"application/x-www-form-urlencoded;charset=gb2312");
		queryPost.addParameter("__EVENTTARGET", "");
		queryPost.addParameter("__EVENTARGUMENT", "");
		queryPost.addParameter("__VIEWSTATE", viewState);
		queryPost.addParameter("1", "rbtnXq");
		queryPost.addParameter("ddlXq", term);
		queryPost.addParameter("ddlKc", "�����пγ̣�");
		queryPost.addParameter("Button1", "��ͨ���Գɼ���ѯ");
		queryPost.setRequestHeader("Cookie", user_cookie[0].toString());

		queryClient.executeMethod(queryPost);

		// �õ�����ҳ�沢��ӡ
		String queryResponse = new String(queryPost.getResponseBodyAsString().getBytes("gb2312"));
		// System.out.println("queryResponse="+queryResponse);
		queryPost.releaseConnection();
		return queryResponse;
	}

}
