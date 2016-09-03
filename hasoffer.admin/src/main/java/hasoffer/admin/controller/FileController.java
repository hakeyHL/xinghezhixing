package hasoffer.admin.controller;

import hasoffer.core.exception.UnknownException;
import org.apache.commons.io.FileUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping(value = "/uploadfile")
public class FileController {

	/**
	 * 上传问题件
	 * /file
	 * POST
	 *
	 * @param file,文件
	 * @return 文件路径
	 */
	@RequestMapping(value = "", method = RequestMethod.POST)
	@ResponseBody
	public ModelAndView upload(@RequestParam(value = "file", required = true) MultipartFile file) {
		ModelAndView modelAndView = new ModelAndView();
		Map<String, Object> models = new HashMap<String, Object>();
		File tempFile = null;
		try {
			tempFile = File.createTempFile("allbuy-ptm", file.getOriginalFilename());
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			FileOutputStream outputStream = new FileOutputStream(tempFile);
			byte[] buffer = new byte[1024];
			int count = 0;
			InputStream inputStream =  file.getInputStream();
			while ((count = inputStream.read(buffer)) > 0){
				outputStream.write(buffer, 0, count);
			}
			outputStream.close();
			String fileUrl = "";// FileServer.getInstance().getUrl(FileServer.getInstance().save(tempFile));

			models.put("fileUrl", fileUrl);
		} catch (IOException e) {
			throw new UnknownException(e);
		} finally {
			FileUtils.deleteQuietly(tempFile);
		}
		modelAndView.addAllObjects(models);
        modelAndView.setViewName("uploadfile");
		return modelAndView;
	}
}
