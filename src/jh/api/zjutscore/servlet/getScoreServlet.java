package jh.api.zjutscore.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import jh.api.zjutscore.bean.ScoreBean;
import jh.api.zjutscore.http.HtmlPaser;
import jh.api.zjutscore.http.HttpUtil;

public class getScoreServlet extends HttpServlet{
	private static final long serialVersionUID = 1L;
	//private static String newsURL = "http://www.zjut.edu.cn/zjutnews/BigClass.jsp?showItemNumber=20&pageNumber=1&bigclassid=";
	//private HttpClient httpclient = new HttpClient();
	private HttpUtil myHttpUtil ;
	private HtmlPaser queryHtmlPaser, viewStateHtmlPaser;
	//private StudentInfoBean studentBean;
	private List<ScoreBean> scoreList;

	
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		
		request.setCharacterEncoding("utf-8");
		response.setContentType("text/html;charset=utf-8");
		response.setCharacterEncoding("utf-8");
		PrintWriter out = response.getWriter();
		out.println("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
		
		String userId = request.getParameter("userId");
		String password = request.getParameter("password");
		String term = request.getParameter("term");
		
		myHttpUtil = new HttpUtil(userId,password, term);
		try {
			if(!myHttpUtil.login()){
				out.print("<script language='javascript'>alert('�ʺŻ�����������������룡�����ȷ��û���Ǿ���ԭ���ķ�����̱���ˣ��Ȼ�ɣ���Ҳľ�а취...');window.location.href='index.jsp';</script>");
				
			}else{
				viewStateHtmlPaser = new HtmlPaser(myHttpUtil.getViewStateHtml());
				String queryViewState = viewStateHtmlPaser.getQueryViewState();
				
				queryHtmlPaser = new HtmlPaser(myHttpUtil.query(queryViewState));
				//studentBean = queryHtmlPaser.getStudentInfo();
				scoreList = queryHtmlPaser.getScore();
				if(scoreList.size() == 0){
					out.print("<script language='javascript'>alert('��ѧ��û����ĳɼ������������룡');window.location.href='index.jsp';</script>");
				}else{
					
					out.println("<courses>");
					for(int i=0;i<scoreList.size();i++){
						out.println("<course>");
						
						out.println("<courseName>");
						out.println(scoreList.get(i).getCourse());
						out.println("</courseName>");
						out.println("<courseCredit>");
						out.println(scoreList.get(i).getCredit());
						out.println("</courseCredit>");
						out.println("<courseScore>");
						out.println(scoreList.get(i).getScore());
						out.println("</courseScore>");
						
						out.println("</course>");
					}
					out.println("</courses>");
				}
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request,response);
	}
}
