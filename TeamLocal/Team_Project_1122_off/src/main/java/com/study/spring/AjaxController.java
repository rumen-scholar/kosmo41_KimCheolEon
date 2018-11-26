package com.study.spring;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.ibatis.session.SqlSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.UserRecord;
import com.google.firebase.auth.UserRecord.UpdateRequest;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import com.study.spring.Joinapply.dao.JoinapplyDao;
import com.study.spring.dao.IDao;
import com.study.spring.dto.Joinapply;
import com.study.spring.dto.Joinapplycount;
import com.study.spring.signup.dao.signupDao;
import com.study.spring.signup.dto.content_ReplyDto;
import com.study.spring.signup.dto.signupDto;

@Controller
public class AjaxController {

	private static final Logger logger = LoggerFactory.getLogger(AjaxController.class);

	@Autowired
	private SqlSession sqlSession;

	@Autowired
	private ServletContext servletContext;

	private HttpSession session = null;

	int stateNumber = 1;

	public FirebaseMessaging message = null;
	public FirebaseAuth auth = null;

	public void FirebaseFunction() throws IOException {

		FirebaseMessaging message;
		FirebaseAuth auth;

		if (stateNumber == 1) {
			FileInputStream serviceAccount = new FileInputStream(
					servletContext.getRealPath("resources/kosmo-teamproject-key.json"));
			FirebaseOptions options = new FirebaseOptions.Builder()
					.setCredentials(GoogleCredentials.fromStream(serviceAccount))
					.setDatabaseUrl("https://kosmo-teamproject-aee81.firebaseio.com").build();
			FirebaseApp.initializeApp(options);

			this.auth = FirebaseAuth.getInstance();
			this.message = FirebaseMessaging.getInstance();

			stateNumber++;
		}
	}

	// 모임게시판 참가신청
	@RequestMapping("/join_join")
	public String Join_join(HttpServletRequest request, HttpServletResponse response) throws IOException {
		request.setCharacterEncoding("UTF-8");
		response.setContentType("text/html; charset=UTF-8");

		IDao dao = sqlSession.getMapper(IDao.class);
		PrintWriter writer = response.getWriter();

		// 참가인원 확인
		Joinapplycount joinCount = dao.joinapplycount(request.getParameter("numId"));
		int fixed = Integer.parseInt(request.getParameter("fixed")); // 정원
		int jcount = Integer.parseInt(joinCount.getCount()); // 현재 참가인원

		if (fixed > jcount) {
			try {
				// 중복참가 확인
				Joinapply joinapply = dao.joinapply(request.getParameter("numId"), request.getParameter("name"));
				if (joinapply.equals("null")) {
				}
			} catch (Exception e) {
				dao.Join_join(request.getParameter("numId"), request.getParameter("name"));
				writer.println("[{\"result\":\"OK\",\"desc\":\"" + "참가신청이 완료됐습니다." + "\"}]");
				writer.close();
			}
		}

		return "joinlist";
	}

	// 모임게시물 삭제하기
	@RequestMapping("/join_delete")
	public String join_delete(HttpServletRequest request, HttpServletResponse response, Model model)
			throws IOException {

		System.out.println("join_delete()");
		IDao dao = sqlSession.getMapper(IDao.class);
		PrintWriter writer = response.getWriter();

		session = request.getSession();

		String numId = request.getParameter("numId");
		// 작성자여부 확인
		String name = (String) session.getAttribute("userName");
		String maker = (String) session.getAttribute("makerName");

		if (maker.equals(name)) {
			dao.manegement_delete(numId);
			dao.join_delete(numId);
			writer.println("[{\"result\":\"OK\",\"desc\":\"" + "글삭제에 성공했습니다" + "\"}]");
		} else {
			writer.println("[{\"result\":\"OK\",\"desc\":\"" + "작성자가 아니라 글삭제에 실패했습니다" + "\"}]");
		}

		writer.close();
		return "joinlist";
	}

	// 모임게시물 취소하기
	@RequestMapping("/join_cancel")
	public String join_cancel(HttpServletRequest request, HttpServletResponse response, Model model)
			throws IOException {

		System.out.println("join_cancel()");
		IDao dao = sqlSession.getMapper(IDao.class);
		PrintWriter writer = response.getWriter();

		session = request.getSession();

		String numId = request.getParameter("numId");
		String name = (String) session.getAttribute("userName");

		try {
			dao.Join_cancel(numId, name);
			writer.println("[{\"result\":\"OK\",\"desc\":\"" + "신청하신 내용을 취소했습니다" + "\"}]");
		} catch (Exception e) {
			writer.println("[{\"result\":\"OK\",\"desc\":\"" + "신청하신 내용을 취소하지 못했습니다" + "\"}]");
		}

		writer.close();

		return "joinlist";
	}

	// 일반게시물 댓글달기
	@RequestMapping("/content_reply")
	public void content_reply(HttpServletRequest request, HttpServletResponse response, Model model)
			throws IOException {

		System.out.println("content_reply()");
		IDao dao = sqlSession.getMapper(IDao.class);
		PrintWriter writer = response.getWriter();

		String numId = request.getParameter("numId");
		String content = request.getParameter("content");
		String name = request.getParameter("name");

		try {
			dao.content_reply(numId, name, content);
			writer.println("[{\"result\":\"OK\",\"desc\":\"" + "댓글이 정상등록 되었습니다." + "\"}]");
		} catch (Exception e) {
			writer.println("[{\"result\":\"OK\",\"desc\":\"" + "댓글등록에 실패했습니다." + "\"}]");
		}

		writer.close();
	}

	// 일반게시물 댓글삭제하기
	@RequestMapping("/reply_delete")
	public void reply_delete(HttpServletRequest request, HttpServletResponse response, Model model) throws IOException {
		System.out.println("reply_delete()");

		IDao dao = sqlSession.getMapper(IDao.class);
		PrintWriter writer = response.getWriter();

		String numId = request.getParameter("numId");

		try {
			dao.reply_delete(numId);
			writer.println("[{\"result\":\"OK\",\"desc\":\"" + "댓글삭제에 성공 했습니다." + "\"}]");
		} catch (Exception e) {
			writer.println("[{\"result\":\"OK\",\"desc\":\"" + "댓글삭제에 실패 했습니다." + "\"}]");
		}

		writer.close();
	}

	@RequestMapping("/updateVerify")
	public void updateVerify(HttpServletRequest request, HttpServletResponse response) throws IOException {

		request.setCharacterEncoding("UTF-8");
		response.setContentType("text/html; charset=UTF-8");

		PrintWriter writer = response.getWriter();

		String Ajax_updateEmail = request.getParameter("Ajax_updateEmail");
		String userName = request.getParameter("userName");

		System.out.println("Ajax_updateEmail : " + Ajax_updateEmail);
		System.out.println("userName : " + userName);

		session = request.getSession();
		session.setAttribute("userName", userName);
		session.setAttribute("email_id", Ajax_updateEmail);
		session.setAttribute("email_test", "123456");

		signupDao signupdao = sqlSession.getMapper(signupDao.class);
		try {

			signupdao.updateVerified("PASS", Ajax_updateEmail);

			session.setAttribute("ValidMem", "yes");

			writer.println("[{\"result\":\"OK\",\"desc\":\"[" + userName + "] 님의 접속을 환영합니다!\"}]");

		} catch (NullPointerException e) {

			writer.println("[{\"result\":\"FAIL\",\"desc\":\"갱신 에러!!\"}]");

		} finally {
			writer.close();
		}

	}

	@RequestMapping("/memberListCall")
	public void memberListCall(HttpServletRequest request, HttpServletResponse response) throws IOException {

		request.setCharacterEncoding("UTF-8");
		response.setContentType("text/html; charset=UTF-8");

		PrintWriter writer = response.getWriter();

		int numId = Integer.parseInt(request.getParameter("numId"));

		System.out.println("numId 확인 : " + numId);

		String memberAppend = "";

		JoinapplyDao joinapplydao = sqlSession.getMapper(JoinapplyDao.class);

		try {

			ArrayList<Joinapply> memList = joinapplydao.memberList(numId);
			for (int i = 0; i < memList.size(); i++) {
				System.out.println(memList.get(i).getUserName() + " : " + memList.get(i).getState());
				if (memList.get(i).getState().equals("ACTIVE")) {
					memberAppend += "<p>" + memList.get(i).getUserName() + "</p>";
				}
			}

			writer.println(memberAppend);

		} catch (NullPointerException e) {

			writer.println("[{\"result\":\"FAIL\",\"desc\":\"Error!!!\"}]");

		} finally {
			writer.close();
		}

	}

	@RequestMapping("/firebaseAdmin")
	public void firebaseAdmin(HttpServletRequest req, HttpServletResponse res)
			throws IOException, FirebaseAuthException {

		System.out.println("firebaseAdmin()");

		FirebaseFunction();

		// ----------------------------------------------------------------------------------------

		String uid = req.getParameter("uid");
		String displayName = req.getParameter("displayName");

		System.out.println("uid : " + uid);
		System.out.println("displayName : " + displayName);

		UpdateRequest request = new UpdateRequest(uid).setEmailVerified(true).setDisplayName(displayName);

		UserRecord userRecord = FirebaseAuth.getInstance().updateUser(request);
		System.out.println("Successfully updated user: " + userRecord.getUid());

	}

	@RequestMapping("/FCMpushMessage")
	public void FCMpushMessage(HttpServletRequest request, HttpServletResponse respons)
			throws FirebaseMessagingException, IOException {

		System.out.println("FCMpushMessage()");

		FirebaseFunction();

		// ----------------------------------------------------------------------------------//

		int numId = Integer.parseInt(request.getParameter("numId"));
		String boardName = request.getParameter("boardName");
		String fcmTitle = request.getParameter("fcmTitle");
		String fcmBody = request.getParameter("fcmBody");

		// where NUMID_FK
		IDao dao = sqlSession.getMapper(IDao.class);
		ArrayList<Joinapply> mgmtSelect = dao.groupUser(numId);

		signupDao signdao = sqlSession.getMapper(signupDao.class);

		try {
			for (int i = 0; i < mgmtSelect.size(); i++) {
				signupDto signdto = signdao.tokenGet(mgmtSelect.get(i).getUserName());
				if (!signdto.getTOKEN().equals("NODATA")) {
//					  See documentation on defining a message payload.
					Message message = Message.builder().putData("title", fcmTitle).putData("message", fcmBody)
							.setToken(signdto.getTOKEN()).build();
					String response = FirebaseMessaging.getInstance().send(message);
					System.out.println("Successfully sent message: " + response);

				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

	}
}