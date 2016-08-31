package com.bonc.epm.paas.controller;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.stereotype.Controller;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.alibaba.fastjson.JSON;
import com.bonc.epm.paas.constant.StorageConstant;
import com.bonc.epm.paas.dao.StorageDao;
import com.bonc.epm.paas.entity.FileInfo;
import com.bonc.epm.paas.entity.Storage;
import com.bonc.epm.paas.entity.User;
import com.bonc.epm.paas.kubernetes.util.KubernetesClientService;
import com.bonc.epm.paas.util.CurrentUserUtils;
import com.bonc.epm.paas.util.SFTPUtil;
import com.bonc.epm.paas.util.SshConnect;
import com.bonc.epm.paas.util.ZipCompressing;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.SftpException;

@Controller
public class StorageController {

	private static final Logger log = LoggerFactory.getLogger(StorageController.class);
	
	@Autowired
	private StorageDao storageDao;
	
	@Autowired
  private CephController cephController;

    /**
	 * 进入存储卷组页面
	 * 
	 * @param model
	 * @return
	 */

	@RequestMapping(value = { "service/storage" }, method = RequestMethod.GET)
	public String findStorages(Model model) {
		User cUser = CurrentUserUtils.getInstance().getUser();
		int leftstorage = 0;
		List<Storage> list = storageDao.findByCreateBy(cUser.getId());
		for (Storage storage : list) {
			leftstorage = leftstorage + (int) storage.getStorageSize();
		}
		model.addAttribute("leftstorage", (int) cUser.getVol_size() - leftstorage / 1024);
		// long createBy = CurrentUserUtils.getInstance().getUser().getId();
		// List<Storage> storages = storageDao.findByCreateBy(createBy);
		// model.addAttribute("storages",storages);
		model.addAttribute("menu_flag", "service");

		return "storage/storage.jsp";
	}

	/**
	 * 根据分页配置查询当前存储卷组
	 * 
	 * @param pageable
	 * @return
	 */
	@RequestMapping(value = { "service/storageList" }, method = RequestMethod.POST)
	@ResponseBody
	public String findStorageList(Pageable pageable, Model model) {
		Map<String, Object> map = new HashMap<String, Object>();
		long createBy = CurrentUserUtils.getInstance().getUser().getId();
		List<Storage> storages = storageDao.findAllByCreateByAndUseTypeOrderByCreateDateDesc(createBy, pageable,StorageConstant.NOT_USER);
		map.put("storages", storages);
		map.put("status", "200");
		map.put("count", storageDao.countByCreateBy(createBy));
		model.addAttribute("menu_flag", "service");
		return JSON.toJSONString(map);

	}

	/**
	 * 根据存储卷id跳转进入存储卷组详细页面
	 * 
	 * @param model
	 * @param id
	 * @return
	 */
	@RequestMapping(value = { "service/storage/detail/{id}" }, method = RequestMethod.GET)
	public String detail(Model model, @PathVariable long id) {
		Storage storage = storageDao.findOne(id);
		model.addAttribute("id", id);
		model.addAttribute("storage", storage);
		model.addAttribute("menu_flag", "service");
		return "storage/storage_detail.jsp";
	}

	/**
	 * 跳转进入新建卷组页面
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping(value = { "service/storage/add" }, method = RequestMethod.GET)
	public String storageAdd(Model model) {
		User cUser = CurrentUserUtils.getInstance().getUser();
		int leftstorage = 0;
		List<Storage> list = storageDao.findByCreateBy(cUser.getId());
		for (Storage storage : list) {
			leftstorage = leftstorage + (int) storage.getStorageSize();
		}
		model.addAttribute("leftstorage", (int) cUser.getVol_size() - leftstorage / 1024);
		model.addAttribute("menu_flag", "service");
		return "storage/storage_add.jsp";
	}

	/**
	 * 新建存储
	 * 
	 * @param storage
	 * @param model
	 * @return
	 */
	@RequestMapping(value = { "service/storage/build" }, method = RequestMethod.POST)
	@ResponseBody
	public String buildStorage(Storage storage, Model model) {
		Map<String, Object> map = new HashMap<String, Object>();
		long createBy = CurrentUserUtils.getInstance().getUser().getId();
		storage.setCreateDate(new Date());
		storage.setUseType(StorageConstant.NOT_USER);
		storage.setCreateBy(createBy);
		Storage storageValidate = storageDao.findByCreateByAndStorageName(createBy, storage.getStorageName());
		if (null == storageValidate) {
			// ceph中创建存储卷目录 TODO 3
			CephController ceph = new CephController();
			ceph.connectCephFS();
			ceph.createStorageCephFS(storage.getStorageName(), storage.isVolReadOnly());

			storageDao.save(storage);
			map.put("status", "200");
		} else {
			map.put("status", "500");
		}
		return JSON.toJSONString(map);
	}

	/**
	 * 新建存储时，对存储名进行查重；
	 * 
	 * @param storageName
	 * @return
	 */
	@RequestMapping(value = { "service/storage/build/validate" }, method = RequestMethod.POST)
	@ResponseBody
	public String validate(String storageName) {
		Map<String, Object> map = new HashMap<String, Object>();
		long createBy = CurrentUserUtils.getInstance().getUser().getId();
		Storage StorageValidate = storageDao.findByCreateByAndStorageName(createBy, storageName);
		if (StorageValidate == null) {
			map.put("status", "200");
		} else {
			map.put("status", "500");
		}

		return JSON.toJSONString(map);
	}

	/**
	 * 根据存储卷组Id修改存储卷组的存储大小；
	 * 
	 * @param storageId
	 * @param storageUpdateSize
	 * @return
	 */
	@RequestMapping(value = { "service/storage/dilatation" }, method = RequestMethod.POST)
	@ResponseBody
	public String dilatationStorage(long storageId, Integer storageUpdateSize) {
		Map<String, Object> map = new HashMap<String, Object>();
		Storage storage = storageDao.findOne(storageId);
		storage.setStorageSize(storageUpdateSize);
		storageDao.save(storage);
		map.put("status", "200");
		return JSON.toJSONString(map);
	}

	/**
	 * 删除选定的存储卷组；
	 * 
	 * @param storageId
	 * @return
	 */
	@RequestMapping(value = { "service/storage/delete" })
	@ResponseBody
	public String deleteStorage(@RequestParam long storageId) {
		Map<String, Object> map = new HashMap<String, Object>();
		Storage storage = storageDao.findOne(storageId);
		if (StorageConstant.IS_USER == storage.getUseType()) {
			map.put("status", "500");
		} else {
			storageDao.delete(storageId);
			CephController cephCon = new CephController();
			cephCon.connectCephFS();
			cephCon.deleteStorageCephFS(storage.getStorageName());
			map.put("status", "200");
		}
		return JSON.toJSONString(map);
	}
	
	@RequestMapping(value={"hasUsed.do"},method=RequestMethod.GET)
  @ResponseBody
  public String hasUsed(String storageName,long totalSize){
    Map map = new HashMap();
    String namespace = CurrentUserUtils.getInstance().getUser().getNamespace();
    File file = new File(cephController.getMountpoint()+namespace+storageName);
    mountLocalCeph(file);
  
    long hasUsed = file.length()/1024/1024;
    map.put("length", String.valueOf(hasUsed));
    return JSON.toJSONString(map); 
    }
	/**
	 * 
	 * Description:展示文件列表
	 * 
	 * @param path
	 * @param dirName
	 * @param storageName
	 * @return 
	 * @see
	 */

	  @RequestMapping(value={"listFile"},method=RequestMethod.GET)
	  @ResponseBody
	  public String fileList(String path,String dirName,String storageName){
	      Map map = new HashMap();
	      String directory=path;
	      String namespace = CurrentUserUtils.getInstance().getUser().getNamespace();
	      if("".equals(dirName)){
	        File file = new File(cephController.getMountpoint()+namespace+storageName);
	        mountLocalCeph(file);

	        directory=cephController.getMountpoint()+namespace+storageName;
	             }
	      if ("../".equals(dirName)){
	          if((path).equals(cephController.getMountpoint()+namespace+storageName)){
	              map.put("status", "500");
	              return JSON.toJSONString(map); 
	             }
	        directory=directory.substring(0,directory.lastIndexOf('/'));
	        directory=directory.substring(0,directory.lastIndexOf('/')+1);
	      }else{
	        directory+=dirName;
	             }
        List<FileInfo> list = SFTPUtil.listFileInfo(directory);
        map.put("fileList", list);
        return JSON.toJSONString(map); 
	       }
	  /**
	   * 
	   * Description: 如果没有本地cephfs目录没有挂载，则挂载。
	   * 
	   * @param file 
	   * @see
	   */
	  public void mountLocalCeph(File file){
	      if(!file.exists()){
              CephController cephCon = new CephController();
              cephCon.connectCephFS();
            try {
                    SshConnect.connect(cephController.getUsername(), cephController.getPassword(), cephController.getUrl(), 22);
                    SshConnect.exec("cd "+cephController.getMountpoint(), 1000);
                    SshConnect.exec(cephController.getMountexec(), 1000);
                }catch (JSchException | IOException | InterruptedException e) {
                    e.printStackTrace();
                                   }
                      }
	    }
	
/**
 * 
 * Description: 上传文件到卷组
 * 
 * @param file
 * @param path
 * @return 
 * @see
 */

	@RequestMapping(value={"upload"},method=RequestMethod.POST)
  @ResponseBody
    public String handleFileUpload(@RequestParam("file")MultipartFile file,String path
                                   ,String storageName,long id){
       if(!file.isEmpty()){
           try {
              Map map = new HashMap();
              String namespace = CurrentUserUtils.getInstance().getUser().getNamespace();
              File dir = new File(cephController.getMountpoint()+namespace+"/"+storageName);
             // File dir = new File(path);
              storageDao.findOne(id).getStorageSize();
              long used = (file.getSize() + dir.length())/1024/1024;
              long result =  storageDao.findOne(id).getStorageSize() - used;
              if (0 >= result){
                                      //上传文件大小超过可用大小，返回失败
                 map.put("status", "500");
                 return JSON.toJSONString(map); 
                              }
              BufferedOutputStream out = new BufferedOutputStream(
                      new FileOutputStream(
                              new File(path+file.getOriginalFilename())));
              out.write(file.getBytes());
              out.flush();
              out.close();
              map.put("used",used);
              map.put("status","200");
              return JSON.toJSONString(map); 
           } catch (FileNotFoundException e) {
              e.printStackTrace();
              return"上传失败,"+e.getMessage();
           } catch (IOException e) {
              e.printStackTrace();
              return"上传失败,"+e.getMessage();
                        } 
       }else{
           return"上传失败，因为文件是空的.";
                }

    }
    /**
     * 
     * Description: 下载卷组中的文件
     * 
     * @param downfiles
     * @param directory
     * @param request
     * @param response
     * @throws IOException
     * @throws JSchException
     * @throws InterruptedException 
     * @see
     */

   @RequestMapping(value={"media"}, method = RequestMethod.GET)  
   public void downloadFile(String downfiles,String directory,HttpServletRequest request, HttpServletResponse response)  
           throws IOException, JSchException, InterruptedException {  

       SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
       String newdownfile =  df.format(new Date())+".zip";
       String[] downfile = downfiles.split(",");
       File[] files = new File [downfile.length] ;
       for(int i =0;i<downfile.length;i++){
           files[i]=new File(directory+downfile[i]);
               }    
       ZipCompressing.zip(directory+newdownfile, files); //测试压缩目录
           //下载
       String fullPath = directory+newdownfile;
       response.setContentType(request.getServletContext().getMimeType(fullPath));  
       response.setHeader("Content-Disposition", "attachment;filename="+newdownfile);  
       try {  
           InputStream myStream =new FileInputStream(fullPath);
           IOUtils.copy(myStream, response.getOutputStream());  
           response.flushBuffer();  
           System.out.println("下载成功");
       } catch (IOException e) {  
       }  finally{
           File rm = new File(fullPath);
           rm.delete();
                }
   } 
}
