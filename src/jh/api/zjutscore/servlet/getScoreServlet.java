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
		
		
		String userId = request.getParameter("userId");
		String password = request.getParameter("password");
		String term = request.getParameter("term");
		String method = request.getParameter("method");
		
		myHttpUtil = new HttpUtil(userId,password, term);
		try {
			if(!myHttpUtil.login()){
				out.print("<script language='javascript'>alert('帐号或密码错误！请重新输入！如果你确定没错，那就是原创的服务器瘫掉了，等会吧！我也木有办法...');window.location.href='index.jsp';</script>");
				
			}else{
				viewStateHtmlPaser = new HtmlPaser(myHttpUtil.getViewStateHtml());
				String queryViewState = viewStateHtmlPaser.getQueryViewState();
				
				queryHtmlPaser = new HtmlPaser(myHttpUtil.query(queryViewState));
				//studentBean = queryHtmlPaser.getStudentInfo();
				scoreList = queryHtmlPaser.getScore();
				if(scoreList.size() == 0){
					out.print("<script language='javascript'>alert('该学期没有你的成绩！请重新输入！');window.location.href='index.jsp';</script>");
				}else if(method.equals("xml")){
					out.println("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
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
				}else if(method.equals("json")){
					out.print("{\"courses\":[");
					for(int j=0;j<scoreList.size();j++){
						out.print("{\"courseName\":\""+scoreList.get(j).getCourse()+"\",");
						out.print("\"courseCredit\":\""+scoreList.get(j).getCredit()+"\",");
						out.print("\"courseScore\":\""+scoreList.get(j).getScore()+"\"}");
						if(j != scoreList.size()-1){
							out.print(",");
						}else{
							out.print("]}");
						}
					}
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
