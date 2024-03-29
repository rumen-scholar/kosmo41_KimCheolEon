package com.project01.server;

import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.ProtocolException;
import java.net.Socket;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Set;
import java.util.StringTokenizer;

import com.project01.DB.*;
//import com.project01.DB.B01chat_usersDO;
//import com.project01.DB.B02chat_usersDAO;
//import com.project01.DB.C01roomlistDO;
//import com.project01.DB.C02roomlistDAO;

public class A02ServerFunction {

	B02chat_usersDAO chuDAO = new B02chat_usersDAO();
	C02roomlistDAO roomDAO = new C02roomlistDAO();

	// 접속된 모든 클라이언트들에게 메시지 전달
	public void sendAllMsg(String msg) {
		// 출력 스트림을 순차적으로 얻어와서 해당 메시지를 출력한다.
		Iterator<?> it = Server.clientMap.keySet().iterator();

		while (it.hasNext()) {
			try {
				PrintWriter it_out = (PrintWriter) Server.clientMap.get(it.next());

				it_out.println(URLEncoder.encode(msg, "UTF-8"));
			} catch (Exception e) {
				System.out.println("예외[Server/sendAllMsg] : " + e);
			}
		}
	}

	// Location Msg
	public void sendPrivateMsg(String msg, String name) {

		// 보내는 사람 정보 추출
		ArrayList<B01chat_usersDO> user = chuDAO.checkUSERS("NAME", name);
		String location = user.get(0).getLOCATION();
		String holdWhisper = user.get(0).getHOLDWHISPER();
		String holdName = user.get(0).getWHISPERNAME();

		// 테이블 확인해서 유저가 HOLD 상태일때의 플래그
		if (holdWhisper.equals("HOLD")) {
			try {
				PrintWriter it_out = (PrintWriter) Server.clientMap.get(name);
				it_out.println(URLEncoder.encode("[SYSTEM] 고정귓속말상태 \n" + "\t\t [내용] " + msg, "UTF-8"));

				System.out.println(holdName);

				PrintWriter hold = (PrintWriter) Server.clientMap.get(holdName.trim());
				hold.println(URLEncoder.encode("[귓속말] " + msg, "UTF-8"));
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return;
		}

		// 같은 로케이션 유저들 추출
		ArrayList<B01chat_usersDO> list = chuDAO.checkUSERS("LOGIN = 'IN' and LOCATION", location);
		int size = list.size();
		for (int index = 0; index < size; index++) {
			PrintWriter it_out = (PrintWriter) Server.clientMap.get(list.get(index).getNAME());
			try {
				it_out.println(URLEncoder.encode(msg, "UTF-8"));
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	// 커맨드 처리 부분 (switch ~ case로 분류 및 메소드 호출)
	public void CommandProcess(String command, String name, String body) {

		PrintWriter it_out = (PrintWriter) Server.clientMap.get(name);

		try {
			System.out.println("command : [" + command + "]");
			System.out.println("name : [" + name + "]");
			System.out.println("body : [" + body + "]");

			switch (command) {

			case "/list":
				CmdList(name);
				break;

			case "/to":
				CmdWhisper(name, body);
				break;

			// ---------------------------------- //

			case "/roomlist":
				CmdRoomList(name);
				break;

			case "/mkroom":
				CmdMKRoom(name, body);
				break;

			case "/dstroom":
				CmdDSTRoom(name);
				break;

			case "/in":
				CmdRoomIn(name, body);
				break;

			case "/exit":
				CmdExitRoom(name);
				break;

			case "/kick":
				CmdRoomKick(name, body);
				break;

			case "/adminto":
				CmdAdminTo(name, body);
				break;

			case "/myroomlist":
				CmdMyRoomList(name);
				break;

			case "/info":
				CmdUserInfo(name, body);
				break;

			case "/all":
				CmdAllMsg(name, body);
				break;

			case "/toh":
				CmdHoldWhisper(name, body);
				break;

			default:
				CmdDefault(name);

			}
		} catch (Exception e) {
			System.out.println("예외[Server/CommandProcess] : " + e);
			try {
				it_out.println(URLEncoder.encode("[SYSTEM] 올바르지 않은 명령입니다.", "UTF-8"));
			} catch (UnsupportedEncodingException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}

	}

	// 명령어) /list
	// ClientMap 을 조사해서 전체 접속자 리스트를 불러옴
	public void CmdList(String name) {
		String sResult = "";

		try {
			// Key만 담고있는 컬렉션 인스턴스 생성
			Set<String> keys = Server.clientMap.keySet();

			// 전체 Key 출력
			for (String n : keys) {
				if (sResult == "") {
					sResult = n;
				} else {
					sResult = sResult + ", " + n;
				}
			}
			PrintWriter it_out = (PrintWriter) Server.clientMap.get(name);
			it_out.println(URLEncoder.encode(sResult.toString(), "UTF-8"));

		} catch (Exception e) {
			System.out.println("예외[Server/CmdList] : " + e);
		}
	}

	// 명령어) /to 상대방 내용
	// 토크나이징을 통해 상대방에게 메시지 전달
	public void CmdWhisper(String name, String body) {
		StringTokenizer to = new StringTokenizer(body, " ");
		String wantName = to.nextToken();
		String toBody = to.nextToken("").trim();

		try {
			PrintWriter it_out = (PrintWriter) Server.clientMap.get(wantName);
			it_out.println(URLEncoder.encode("From [" + name + "] : " + toBody, "UTF-8"));

			PrintWriter it_out2 = (PrintWriter) Server.clientMap.get(name);
			it_out2.println(URLEncoder.encode("[정상발송] : " + toBody, "UTF-8"));
		} catch (Exception e) {
			System.out.println("예외[Server/CmdWhisper] : " + e);
		}
	}

	// 명령어) /mkroom 방제목 # 인원수 # 공개/비공개 # 비밀번호
	// 토크나이징을 통해 토큰을 분류해서 정보들을 ROOMLIST 테이블에 insert
	// 보낼 메시지는 전문 작성, 최종전송 방식
	// 패스 합을 통해서 Check 되어야 최종 insert
	public void CmdMKRoom(String name, String body) {
		try {
			String sResult = "";

			String roomName = "";
			String roomMax = "";
			String roomHidden = "";
			String roomPass = "";
			int PassCheck = 0;

			System.out.println(PassCheck);

			PrintWriter it_out = (PrintWriter) Server.clientMap.get(name);

			// 확인1) ADMIN 권한을 갖고있는가?
			if (chuDAO.checkUSERS("NAME", name).get(0).getROOMADMIN().equals("ADMIN")) {
				PassCheck -= 99;
				it_out.println(URLEncoder.encode("ADMIN 상태로 새 방을 생성할 수 없습니다.", "UTF-8"));
			} else {
				if (body.equals("")) {
					sResult = "\n[MKRoom] 방을 생성합니다.명령어를 작성해주세요.\n" + "/mkroom [방이름]#[인원제한]#[공개/비공개]#[비공개시, 패스워드]\n\n"
							+ "\t예시)\n" + "\t\t /mkroom 모두모여랑!#3#공개\n" + "\t\t /mkroom 방이름입니다 # 3 # 비공개 # 1324";
				} else {

					// 토크나이징 시작
					StringTokenizer mkroom = new StringTokenizer(body, "#");

					int tokenCount = mkroom.countTokens();

					// 이 부분은 CASE 로 고치는 부분이 좋다/////////////////////////////////
					// 확인2) 토큰 3개 - 공개방의 토크나이징
					if (tokenCount == 3) {
						roomName = mkroom.nextToken().trim();
						roomMax = mkroom.nextToken().trim();
						roomHidden = mkroom.nextToken().trim();

						PassCheck += 1;
					}
					// 확인2) 토큰 4개 - 비공개방의 토크나이징
					if (tokenCount == 4) {
						roomName = mkroom.nextToken().trim();
						roomMax = mkroom.nextToken().trim();
						roomHidden = mkroom.nextToken().trim();
						roomPass = mkroom.nextToken().trim();

						PassCheck += 1;
					}
					//////////////////////////////////////////////////////////////////////



					// 확인3) 최대인원수가 숫자로 설정된게 맞는가? / Integer.parseInt로 TRUE, FALSE 반환
					if (isDigit(roomMax) == true) {

						PassCheck += 2;
					}

					// 확인4) 최대인원수가 0으로 되어있지 않은가?
					if (!roomMax.equals("0")) {
						PassCheck += 4;
					}

					// 확인5) 방구별이 공개/비공개 로만 설정되어있는가?
					if (roomHidden.equals("공개") || roomHidden.equals("비공개")) {
						PassCheck += 8;
					}

					System.out.println(PassCheck);

					// 확인6) 상기의 패스사항을 모두 만족해야 insert 가능
					if (PassCheck == 15) {
						C01roomlistDO roomDO = new C01roomlistDO();
						roomDO.setRNAME(roomName);
						roomDO.setRMAX(Integer.toString(Integer.parseInt(roomMax)));
						roomDO.setRHIDDEN(roomHidden);
						roomDO.setRPASS(roomPass);

						// 방생성
						if (roomDAO.insertRoom(roomDO)) {
							sResult = "방이 생성되었습니다.";
						}

						// 방장 권한 업데이트
						chuDAO.updateCHAT_USERS("NAME", name, "ROOMADMIN", "ADMIN");
						// 로케이션 업데이트 (룸리스트 조회한 후 생성된 방을 이름으로 검색하여 룸번호 확보 - sequence 로 준비)
						C01roomlistDO selectRoom = roomDAO.selectRoomList("RNAME", roomName);
						chuDAO.updateCHAT_USERS("NAME", name, "LOCATION", selectRoom.getRNUMBER());

					} else {
						sResult = "방 생성에 실패하였습니다";
						it_out.println(URLEncoder.encode("작성한 옵션 : [" + body.toString() + "]", "UTF-8"));
					}
				}
				// 작성된 전문 최종 전송 (오류)
				it_out.println(URLEncoder.encode(sResult.toString(), "UTF-8"));
			}
		} catch (Exception e) {
			System.out.println("예외[Server/CmdList] : " + e);
		}
	}

	// 명령어) /dstroom
	// 명령어를 보낸 사람이 ADMIN 인지 확인후에, 가차없이 방을 delete.
	// 후에 ADMIN 권한을 NOADMIN 으로 UPDATE 한 후에, LOCATION이 같은 전부를 대기실 (1번) 으로 UPDATE
	public void CmdDSTRoom(String name) {

		PrintWriter it_out = (PrintWriter) Server.clientMap.get(name);

		// B01chat_usersDO USER = chuDAO.checkUSERS("NAME", name);
		ArrayList<B01chat_usersDO> USER = chuDAO.checkUSERS("NAME", name);

		String isName = USER.get(0).getNAME();
		String isAdmin = USER.get(0).getROOMADMIN();
		String isLocation = USER.get(0).getLOCATION();

		try {
			if (!isAdmin.equals("ADMIN")) {
				it_out.println(URLEncoder.encode("[SYSTEM] ADMIN 권한이 없습니다.", "UTF-8"));
			} else {

				it_out.println(URLEncoder.encode("[SYSTEM] ADMIN 권한이 확인되었습니다. 방을 삭제합니다.", "UTF-8"));

				sendPrivateMsg("[SYSTEM] ADMIN 이 방을 삭제하여, 대기실로 이동합니다.", isName);

				// 로케이션업데이트 하기전에, 파괴하려는 방 어드민 권환 회수
				chuDAO.updateCHAT_USERS("NAME", name, "ROOMADMIN", "NOADMIN");

				// 방 삭제
				if (roomDAO.deleteRoom(isLocation)) {
					// 로케이션 업데이트
					chuDAO.updateCHAT_USERS("LOCATION", isLocation, "LOCATION", "1");
				}
			}
		} catch (Exception e) {
			System.out.println("예외[Server/CmdDefault] : " + e);
		}
	}

	// 명령어) /roomlist
	// select * from roomlist 를 통해 획득한 정보를 ArrayList 에서 반복해서 꺼내서 전문작성
	// 최종으로 커맨드를 시행한 사람에게 전문 전송
	public void CmdRoomList(String name) {
		String sResult = "";

		ArrayList<C01roomlistDO> list = roomDAO.getRoomList();

		int totalElements = list.size();// arrayList의 요소의 갯수를 구한다.

		for (int index = 0; index < totalElements; index++) {
			String rnumber = list.get(index).getRNUMBER();
			String rmax = list.get(index).getRMAX();
			String rusercount = list.get(index).getRUSERCOUNT();
			String rname = list.get(index).getRNAME();
			String rhidden = list.get(index).getRHIDDEN();

			sResult += "\n\t[No." + rnumber + "]\t" + "[총원:" + rmax + "]\t" + "[현재원:" + rusercount + "]\t" + "[상태:"
					+ rhidden + "]\n\t[" + rname + "]\n";
		}
		System.out.println("sResult 전문 : " + sResult);
		PrintWriter it_out = (PrintWriter) Server.clientMap.get(name);
		try {
			it_out.println(URLEncoder.encode(sResult.toString(), "UTF-8"));
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	// 명령어) /in [방번호] [비밀번호]
	// 토큰의 갯수를 통해 공개방/비공개방의 경우 판단, 조건 확인후 UPDATE 함
	public void CmdRoomIn(String name, String body) {

		PrintWriter it_out = (PrintWriter) Server.clientMap.get(name);

		if (body.equals("")) {
			try {
				it_out.println(URLEncoder.encode("\n\t예시)\n" + "\t\t/in [방번호] [비공개방일시, 비밀번호]", "UTF-8"));
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return;
		}

		ArrayList<B01chat_usersDO> USER = chuDAO.checkUSERS("NAME", name);
		// 명령어 날린사람 계급 확보 ADMIN / NOADMIN
		String isAdmin = USER.get(0).getROOMADMIN();
		// 명령어 날린사람의 LOCATION 확보
		String isLocation = USER.get(0).getLOCATION();

		int PassCheck = 0;

		// body 분리 및 토큰개수 확보
		StringTokenizer inToken = new StringTokenizer(body, " ");
		int tokenCount = inToken.countTokens();

		String tokenBody = "";
		String tokenPass = "";

		// 토큰의 갯수에 따른 토큰데이터 확보
		if (tokenCount == 1) {
			tokenBody = inToken.nextToken();
		}

		if (tokenCount == 2) {
			tokenBody = inToken.nextToken();
			tokenPass = inToken.nextToken();
		}

		// 방이 존재하는가?
		// 방이 존재하지 않으면 NULLpoint error, CommandProcess 에서 에러처리
		C01roomlistDO selectRoom = roomDAO.selectRoomList("RNUMBER", tokenBody);

		// 대상된 방의 정보 확보
		String doRnumber = selectRoom.getRNUMBER();
		int doMax = Integer.parseInt(selectRoom.getRMAX());
		int doUserCount = Integer.parseInt(selectRoom.getRUSERCOUNT());
		String doHidden = selectRoom.getRHIDDEN();
		String doPass = selectRoom.getRPASS();

		// 이동할 수 있는 권한인가? - ADMIN 이면 방을 삭제하거나 계승후에 이동할 수 있도록 제한
		if (isAdmin.equals("ADMIN")) {
			try {
				it_out.println(
						URLEncoder.encode("[SYSTEM] ADMIN 이 방을 이동하려면 방파괴(dstroom) / 권한승계(admin) 한 후에 가능합니다", "UTF-8"));
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			// PassCheck -= 1;
			return;
		}

		// 숫자를 입력한 것이 맞는가?
		if (!isDigit(tokenBody)) {
			try {
				it_out.println(URLEncoder.encode("[SYSTEM] 숫자가 아닙니다.", "UTF-8"));
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			// PassCheck -= 1;
			return;
		}

		// 토큰의 갯수가 최대 2개를 넘지 않는가?
		if (tokenCount > 2) {
			try {
				it_out.println(URLEncoder.encode("[SYSTEM] 입력 인자 갯수가 다릅니다.", "UTF-8"));
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			// PassCheck -= 1;
			return;
		}

		// 방에 들어갈수 있는 인원수인가?
		if (doMax == doUserCount) {
			try {
				it_out.println(URLEncoder.encode("[SYSTEM] 방의 인원이 꽉찼습니다.", "UTF-8"));
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			// PassCheck -= 1;
			return;
		}

		System.out.println("PassCheck : " + PassCheck);
		if (PassCheck != 0) {
			try {
				it_out.println(URLEncoder.encode("[SYSTEM] 조건이 맞지 않습니다. (번호 : " + tokenBody + ")", "UTF-8"));
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return;
		}

		// 에러 검출 안됬을 시 시작
		System.out.println("IN 처리 시작");

		// 토큰 1개짜리, 공개방 입장
		if (doHidden.equals("공개")) {
			chuDAO.updateCHAT_USERS("NAME", name, "LOCATION", tokenBody);

			// 새로 만들어진 방번호 선택, 추출된 방번호를 +1 해서 UPDATE
			C01roomlistDO selectCreateRoom = roomDAO.selectRoomList("RNUMBER", tokenBody);
			int AddCount = Integer.parseInt(selectCreateRoom.getRUSERCOUNT()) + 1;

			roomDAO.updateRoom(Integer.toString(AddCount), selectCreateRoom.getRNUMBER());

			try {
				it_out.println(URLEncoder.encode("[SYSTEM] 방이 이동되었습니다. (번호 : " + tokenBody + ")", "UTF-8"));
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		// 토큰 2개짜리, 비공개방 입장
		if (doHidden.equals("비공개")) {
			if (tokenPass.equals(doPass)) {
				chuDAO.updateCHAT_USERS("NAME", name, "LOCATION", tokenBody);

				C01roomlistDO selectCreateRoom = roomDAO.selectRoomList("RNUMBER", tokenBody);

				try {
					it_out.println(URLEncoder.encode("[SYSTEM] 방이 이동되었습니다. (번호 : " + tokenBody + ")", "UTF-8"));
				} catch (UnsupportedEncodingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} else {
				try {
					it_out.println(URLEncoder.encode("[SYSTEM] 비밀번호가 틀렸습니다.", "UTF-8"));
				} catch (UnsupportedEncodingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}

	}

	// 명령어) /exit
	// ADMIN 권한이 없다면 누구나 대기실로 UPDATE
	public void CmdExitRoom(String name) {

		PrintWriter it_out = (PrintWriter) Server.clientMap.get(name);

		ArrayList<B01chat_usersDO> USER = chuDAO.checkUSERS("NAME", name);

		String isName = USER.get(0).getNAME();
		String isAdmin = USER.get(0).getROOMADMIN();
		String isLocation = USER.get(0).getLOCATION();

		try {
			if (isAdmin.equals("ADMIN")) {
				it_out.println(
						URLEncoder.encode("[SYSTEM] ADMIN 권한을 가지고 Exit 할 수 없습니다.(/admin 혹은 /dstroom 가능)", "UTF-8"));
			} else {

				it_out.println(URLEncoder.encode("[SYSTEM] 대기실로 이동합니다.", "UTF-8"));

				// 로케이션 업데이트
				chuDAO.updateCHAT_USERS("NAME", isName, "LOCATION", "1");
			}
		} catch (Exception e) {
			System.out.println("예외[Server/CmdDefault] : " + e);
		}
	}

	// 명령어) /kick
	// ADMIN 권한이 없다면 누구나 대기실로 UPDATE
	// !! 오류  !!
	// 상대방이 같은 방에 있는지 검사를 안함
	public void CmdRoomKick(String name, String body) {

		PrintWriter it_out = (PrintWriter) Server.clientMap.get(name);

		if (body.equals("")) {
			try {
				it_out.println(URLEncoder.encode("\n\t예시)\n" + "\t\t/kick [상대방닉네임]", "UTF-8"));
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return;
		}

		ArrayList<B01chat_usersDO> USER = chuDAO.checkUSERS("NAME", name);

		String isName = USER.get(0).getNAME();
		String isAdmin = USER.get(0).getROOMADMIN();
		String isLocation = USER.get(0).getLOCATION();

		try {
			if (!isAdmin.equals("ADMIN")) {
				it_out.println(URLEncoder.encode("[SYSTEM] ADMIN 권한이 있어야 KiCK 할 수 있습니다.", "UTF-8"));
			} else {

				chuDAO.updateCHAT_USERS("NAME", body.trim(), "LOCATION", "1");

				PrintWriter kick = (PrintWriter) Server.clientMap.get(body.trim());
				kick.println(URLEncoder.encode("[SYSTEM] ADMIN에 의해, ROOM에서 KICK 당했습니다.", "UTF-8"));
			}

		} catch (Exception e) {
			System.out.println("예외[Server/CmdDefault] : " + e);
		}
	}

	// 명령어) /adminto
	// ADMIN 권한이 상대에게 강제 승계
	// !! 오류  !!
	// 1. 상대방이 같은 방에 있는지 검사를 안함
	// 2. 상대가 ADMIN 인지를 확인하지 않음
	public void CmdAdminTo(String name, String body) {

		PrintWriter it_out = (PrintWriter) Server.clientMap.get(name);

		if (body.equals("")) {
			try {
				it_out.println(URLEncoder.encode("\n\t예시)\n" + "\t\t/adminto [상대방닉네임]", "UTF-8"));
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return;
		}

		ArrayList<B01chat_usersDO> USER = chuDAO.checkUSERS("NAME", name);

		String isName = USER.get(0).getNAME();
		String isAdmin = USER.get(0).getROOMADMIN();
		String isLocation = USER.get(0).getLOCATION();

		try {
			if (!isAdmin.equals("ADMIN")) {
				it_out.println(URLEncoder.encode("[SYSTEM] ADMIN 권한이 있어야 ADMIN을 물려줄 수 있습니다.", "UTF-8"));
			} else {

				// ADMIN UPDATE
				chuDAO.updateCHAT_USERS("NAME", body.trim(), "ROOMADMIN", "ADMIN");

				// 방장 권환 회수
				chuDAO.updateCHAT_USERS("NAME", name, "ROOMADMIN", "NOADMIN");

				PrintWriter AdminTo = (PrintWriter) Server.clientMap.get(body.trim());
				AdminTo.println(URLEncoder.encode("[SYSTEM] 당신은 ADMIN에 의해 ADMIN 권한을 물려받았습니다.", "UTF-8"));
			}

		} catch (Exception e) {
			System.out.println("예외[Server/CmdDefault] : " + e);
		}
	}

	// 명령어) /roomlist
	public void CmdMyRoomList(String name) {

		PrintWriter it_out = (PrintWriter) Server.clientMap.get(name);
		String Result = "";

		// NAME 정보 따옴
		ArrayList<B01chat_usersDO> user = chuDAO.checkUSERS("NAME", name);
		// NAME의 로케이션 따옴
		String location = user.get(0).getLOCATION();

		// LOCATION 에서, IN 상태인 놈들 찾음
		ArrayList<B01chat_usersDO> list = chuDAO.checkUSERS("LOGIN = 'IN' and LOCATION", location);

		// 사이즈 구해서 전문구성
		int size = list.size();
		for (int index = 0; index < size; index++) {
			Result += list.get(index).getNAME() + " ";
		}

		try {
			it_out.println(URLEncoder.encode(Result, "UTF-8"));
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	// 명령어) /info [닉네임]
	public void CmdUserInfo(String name, String body) {
		PrintWriter it_out = (PrintWriter) Server.clientMap.get(name);
		String Result = "";

		// BODY 정보 따옴 (상대방)
		ArrayList<B01chat_usersDO> user = chuDAO.checkUSERS("NAME", body.trim());
		// BODY의 정보들
		String bodyName = user.get(0).getNAME();
		String bodyLogin = user.get(0).getLOGIN();
		String bodyBlock = user.get(0).getBLOCK();
		String bodyLoc = user.get(0).getLOCATION();
		String bodyAdmin = user.get(0).getROOMADMIN();

		Result = "\n\n닉네임 : [" + bodyName + "]\n상태 : [" + bodyLogin + "]\n계정상태 : [" + bodyBlock + "]\n마지막위치 : [룸번호-"
				+ bodyLoc + "]\n방장여부 : [" + bodyAdmin + "]";

		try {
			it_out.println(URLEncoder.encode(Result, "UTF-8"));
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}


	// 명령어) /all [메시지]
	public void CmdAllMsg(String name, String body) {

		Iterator<?> it = Server.clientMap.keySet().iterator();

		while (it.hasNext()) {
			try {
				PrintWriter it_out = (PrintWriter) Server.clientMap.get(it.next());

				it_out.println(URLEncoder.encode("[(발송자) " + name + "] " + body, "UTF-8"));
			} catch (Exception e) {
				System.out.println("예외[Server/sendAllMsg] : " + e);
			}
		}
	}

	// 명령어) /toh 상대방
	public void CmdHoldWhisper(String name, String body) {
		System.out.println("고정귓 내부진입");
		System.out.println("[" + name + "]");
		System.out.println("[" + body + "]");

		PrintWriter it_out = (PrintWriter) Server.clientMap.get(name);

		ArrayList<B01chat_usersDO> USER = chuDAO.checkUSERS("NAME", name);

		// String isName = USER.get(0).getNAME();
		String isHold = USER.get(0).getHOLDWHISPER();
		String isHoldName = USER.get(0).getWHISPERNAME();

		if (isHold.equals("HOLD") || body.equals("")) {
			try {
				it_out.println(URLEncoder.encode("\n\t[고정귓속말을 해제합니다]", "UTF-8"));
				chuDAO.updateCHAT_USERS("NAME", name, "HOLDWHISPER = 'NOHOLD', WHISPERNAME", "");
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return;
		}

		ArrayList<B01chat_usersDO> user = chuDAO.checkUSERS("NAME", body.trim());

		if (user.get(0).getLOGIN().equals("NOTIN")) {
			try {
				it_out.println(URLEncoder.encode("[SYSTEM] 대상이 접속중이지 않습니다.", "UTF-8"));
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return;
		}

		if (isHold.equals("NOHOLD")) {
			try {
				it_out.println(URLEncoder.encode("\n\t[고정귓속말로 전환합니다]", "UTF-8"));
				chuDAO.updateCHAT_USERS("NAME", name, "HOLDWHISPER = 'HOLD', WHISPERNAME", body.trim());
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return;
		}

	}

	///////////////////////////////////////////////////////////////////////////////////////////////

	public void CmdDefault(String name) {

		try {
			PrintWriter it_out = (PrintWriter) Server.clientMap.get(name);

			it_out.println(URLEncoder.encode("알수없는 명령어 입니다.", "UTF-8"));
		} catch (Exception e) {
			System.out.println("예외[Server/CmdDefault] : " + e);
		}

	}

	// -------------------------------------------------------------------------------------------//

	// public boolean isDigit(String s) {
	// try {
	// // Double.parseDouble(s);
	// Integer.parseInt(s);
	// return true;
	// } catch (NumberFormatException e) {
	// return false;
	// }
	// }

	public boolean isDigit(String s) {
		int MaxLength = s.length() - 1;

		for (int i = 0; i < MaxLength; i++) {
			switch (s.charAt(i)) {
			case '0':case '1':case '2':case '3':case '4':case '5':case '6':case '7':case '8':case '9':
				return true;
			default:
				break;
			}
		}
		return false;
	}
}
