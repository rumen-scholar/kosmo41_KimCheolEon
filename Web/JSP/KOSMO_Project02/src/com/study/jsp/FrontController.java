package com.study.jsp;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Timestamp;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.study.jsp.command.*;
import com.study.jsp.websocket.command.*;

//@WebServlet("/FrontController")
@WebServlet("*.do")

public class FrontController extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public FrontController() {
		super();
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
//		System.out.println("doGet");
		actionDo(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
//		System.out.println("doPost");
		actionDo(request, response);
	}

	private void actionDo(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		request.setCharacterEncoding("UTF-8");

		String viewPage = null;
		BCommand command = null;
		CCommand command2 = null;

		String uri = request.getRequestURI();
		String conPath = request.getContextPath();
		String com = uri.substring(conPath.length());

//		System.out.println("uri : " + uri);
//		System.out.println("conPath : " + conPath);
//		System.out.println("command : " + com);

		HttpSession session = null;
		session = request.getSession();
		int curPage = 1;

		if (session.getAttribute("cpage") != null) {
			curPage = (int) session.getAttribute("cpage");
		}

		if (com.equals("/write_view.do")) {
			viewPage = "write_view.jsp";
			DispatcherPage(request, response, viewPage);

		} else if (com.equals("/write.do")) {
			command = new BWriteCommamd();
			command.execute(request, response);
			viewPage = "list.do";
			DispatcherPage(request, response, viewPage);

		} else if (com.equals("/list.do")) {
			command = new BListCommand();
			command.execute(request, response);
			viewPage = "list.jsp";
			DispatcherPage(request, response, viewPage);

		} else if (com.equals("/content_view.do")) {
			command = new BContentCommand();
			command.execute(request, response);
			viewPage = "content_view.jsp";
			DispatcherPage(request, response, viewPage);

		} else if (com.equals("/modify_view.do")) {
			command = new BContentCommand();
			command.execute(request, response);
			viewPage = "modify_view.jsp";
			DispatcherPage(request, response, viewPage);

		} else if (com.equals("/modify.do")) {
			command = new BModifyCommand();
			command.execute(request, response);

//			문서를 수정한다음에 자기 페이지로 돌아가야하는데, 파라미터를 받으면서 가야함
			command = new BContentCommand();
			command.execute(request, response);
//			"content_view.do" 로 쏴도 비슷하긴 한데, 조금 다름
//			viewPage = "content_view.do";
			viewPage = "content_view.jsp";
			DispatcherPage(request, response, viewPage);

		} else if (com.equals("/delete.do")) {
			System.out.println("curPage : " + curPage);
			command = new BDeleteCommand();
			command.execute(request, response);
			viewPage = "list.do?page=" + curPage;
			DispatcherPage(request, response, viewPage);

		} else if (com.equals("/reply_view.do")) {
			command = new BReplyViewCommand();
			command.execute(request, response);
			viewPage = "reply_view.jsp";
			DispatcherPage(request, response, viewPage);

		} else if (com.equals("/reply.do")) {
			System.out.println("curPage : " + curPage);
			command = new BReplyCommand();
			command.execute(request, response);
			viewPage = "list.do?page=" + curPage;
			DispatcherPage(request, response, viewPage);
		}
		/////////////////////////////////////////////////////

		else if (com.equals("/search.do")) {
			command = new BSearchCommand();
			command.execute(request, response);
			viewPage = "list2_search.jsp";
			DispatcherPage(request, response, viewPage);

		} else if (com.equals("/list2_search.do")) {
			command = new BSearchCommand();
			command.execute(request, response);
			viewPage = "list2_search.jsp";
			DispatcherPage(request, response, viewPage);

		} else if (com.equals("/acclogin.do")) {
			command = new BAccuntInfoSet();
			command.execute(request, response);

		} else if (com.equals("/acclogout.do")) {
			command = new BAccuntInfoDel();
			command.execute(request, response);

		} else if (com.equals("/download.do")) {
			command = new BDownloadCommand();
			command.execute(request, response);
		}

		////////////////////////////////////////////////////////////////////

		if (com.equals("/chatUsercall.do")) {
			command2 = new CChatUserCall();
			command2.execute(request, response);
		} else if (com.equals("/roomListcall.do")) {
			command2 = new CRoomListCall();
			command2.execute(request, response);
		} else if (com.equals("/roomCreate.do")) {
			command2 = new CRoomCreateCall();
			command2.execute(request, response);
		} else if (com.equals("/roomOut.do")) {
			command2 = new CRoomOutCall();
			command2.execute(request, response);
		}

////		여기가 로그인때는 필요없는데 null 교체되서 죽는 부분
//		RequestDispatcher dispatcher = request.getRequestDispatcher(viewPage);
//		dispatcher.forward(request, response);

	}

	private void DispatcherPage(HttpServletRequest request, HttpServletResponse response, String viewPage)
			throws ServletException, IOException {
		RequestDispatcher dispatcher = request.getRequestDispatcher(viewPage);
		dispatcher.forward(request, response);
	}
}
