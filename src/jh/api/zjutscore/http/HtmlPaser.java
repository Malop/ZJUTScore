package jh.api.zjutscore.http;

import java.util.ArrayList;
import java.util.List;

import jh.api.zjutscore.bean.ScoreBean;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;


public class HtmlPaser {

	private Document doc;
	
	public HtmlPaser(String htmlStr){
		doc = Jsoup.parse(htmlStr);
	}
	
	public String getQueryViewState(){
		String viewState=null;
		Elements eles_input = doc.getElementsByTag("input");
		for(int i=0;i<eles_input.size();i++){
			Element ele_viewState = eles_input.get(i);
			if(ele_viewState.attr("name").equals("__VIEWSTATE")){
				viewState = ele_viewState.val(); 
				break;
			}
		}
		return viewState;
	}
	
	public List<ScoreBean> getScore(){
		
		List<ScoreBean> scoreList = new ArrayList<ScoreBean>();
		
		Element	 table = doc.select("table#DataGrid1").first();
		Elements trs = table.select("tr");
		for(int i=1;i<trs.size();i++){
			ScoreBean scorebean = new ScoreBean();
			Elements font = trs.get(i).select("font");
			Element elecourse = font.get(1);
			Element eleScore = font.get(3);
			Element eleCredit = font.get(5);
			scorebean.setCourse(elecourse.text().toString());
			scorebean.setCredit(eleCredit.text().toString());
			scorebean.setScore(eleScore.text().toString());
			scoreList.add(scorebean);
		}
		return scoreList;
	}
}
