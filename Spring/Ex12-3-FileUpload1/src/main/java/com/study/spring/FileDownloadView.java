package com.study.spring;

import java.io.File;
import java.io.FileInputStream;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.util.FileCopyUtils;
import org.springframework.web.servlet.view.AbstractView;

public class FileDownloadView extends AbstractView {

	public FileDownloadView() {
		setContentType("application/download; charset=utf-8");
	}

	@Override
	protected void renderMergedOutputModel(Map<String, Object> model, HttpServletRequest req, HttpServletResponse res)
			throws Exception {

		File file = (File) model.get("downloadFile");
		String oriFileName = (String) model.get("oriFileName");

		res.setContentType(getContentType());
		res.setContentLength((int) file.length());
		res.setHeader("Content-Disposition",
				"attachment; filename=\"" + URLEncoder.encode(oriFileName, "utf-8") + "\";");
		res.setHeader("Content-Transfer-Encoding", "binary");

		OutputStream out = res.getOutputStream();
		FileInputStream fis = null;

		try {
			fis = new FileInputStream(file);
			FileCopyUtils.copy(fis, out);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (fis != null) {
				try {
					fis.close();
				} catch (Exception e2) {
					e2.printStackTrace();
				}
			}
		}
		out.flush();
	}

}
