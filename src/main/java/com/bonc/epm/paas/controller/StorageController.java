package com.bonc.epm.paas.controller;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
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
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.alibaba.fastjson.JSON;
import com.bonc.epm.paas.constant.StorageConstant;
import com.bonc.epm.paas.constant.UserConstant;
import com.bonc.epm.paas.dao.StorageDao;
import com.bonc.epm.paas.entity.FileInfo;
import com.bonc.epm.paas.entity.Storage;
import com.bonc.epm.paas.entity.User;
import com.bonc.epm.paas.util.CmdUtil;
import com.bonc.epm.paas.util.CurrentUserUtils;
import com.bonc.epm.paas.util.FileUtils;
import com.bonc.epm.paas.util.SFTPUtil;
import com.bonc.epm.paas.util.ZipCompressing;
import com.jcraft.jsch.JSchException;

/**
 * 
 * 〈一句话功能简述〉 〈功能详细描述〉
 * 
 * @author YuanPeng
 * @version 2016年9月5日
 * @see StorageController
 * @since
 */
@Controller
public class StorageController {

    /**
     * 
     */
    private static final Logger LOG = LoggerFactory.getLogger(StorageController.class);
    /**
     * 
     */
    @Autowired
    private StorageDao storageDao;
    /**
     * 
     */
    @Autowired
    private CephController cephController;

    /**
     * 进入存储卷组页面
     * 
     * @param model
     *            Model
     * @return String
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
        model.addAttribute("li_flag", "storage");
        return "storage/storage.jsp";
    }

    /**
     * 根据分页配置查询当前存储卷组
     * 
     * @param pageable
     *            Pageable
     * @param model
     *            Model
     * @return String
     */
    @RequestMapping(value = { "service/storageList" }, method = RequestMethod.POST)
    @ResponseBody
    public String findStorageList(Pageable pageable, Model model) {
        Map<String, Object> map = new HashMap<String, Object>();
        String userType =CurrentUserUtils.getInstance().getUser().getUser_autority();
        long createBy=0;
        if(UserConstant.AUTORITY_USER.equals(userType)){
            createBy = CurrentUserUtils.getInstance().getUser().getParent_id();
        }else{
            createBy = CurrentUserUtils.getInstance().getUser().getId();
                }
        List<Storage> storages = storageDao.findAllByCreateByOrderByCreateDateDesc(createBy, pageable);
        map.put("storages", storages);
        map.put("status", "200");
        map.put("count", storageDao.countByCreateBy(createBy));
        model.addAttribute("menu_flag", "service");
        return JSON.toJSONString(map);

    }

    /**
     * 根据存储卷id跳转进入存储卷组详细页面
     * 
     * @param model Model
     * @param id  
     * @return storage_detail.jsp
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
     * @param model Model
     * @return storager_add.jsp
     */
    @RequestMapping(value = { "service/storage/add" }, method = RequestMethod.GET)
    public String storageAdd(Model model) {
        User cUser = CurrentUserUtils.getInstance().getUser();
        float leftstorage = 0;
        List<Storage> list = storageDao.findByCreateBy(cUser.getId());
        for (Storage storage : list) {
            leftstorage = leftstorage + (float) storage.getStorageSize();
        }
        model.addAttribute("leftstorage", (float) cUser.getVol_size() - leftstorage / 1024);
        model.addAttribute("menu_flag", "service");
        return "storage/storage_add.jsp";
    }

    /**
     * 新建存储
     * 
     * @param storage 新建卷组名
     * @param model Model
     * @return JSON
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
            try {
                ceph.connectCephFS();
            }
            catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            ceph.createStorageCephFS(storage.getStorageName(), storage.isVolReadOnly());

            storageDao.save(storage);
            map.put("status", "200");
        } 
        else {
            map.put("status", "500");
        }
        return JSON.toJSONString(map);
    }

    /**
     * 新建存储时，对存储名进行查重；
     * 
     * @param storageName 卷组名
     * @return JSON
     */
    @RequestMapping(value = { "service/storage/build/validate" }, method = RequestMethod.POST)
    @ResponseBody
    public String validate(String storageName) {
        Map<String, Object> map = new HashMap<String, Object>();
        long createBy = CurrentUserUtils.getInstance().getUser().getId();
        Storage StorageValidate = storageDao.findByCreateByAndStorageName(createBy, storageName);
        if (StorageValidate == null) {
            map.put("status", "200");
        } 
        else {
            map.put("status", "500");
        }

        return JSON.toJSONString(map);
    }

    /**
     * 根据存储卷组Id修改存储卷组的存储大小；
     * 
     * @param storageId 卷组id
     * @param storageUpdateSize 卷组修改大小
     * @return JSON
     */
    @RequestMapping(value = { "service/storage/dilatation" }, method = RequestMethod.POST)
    @ResponseBody
    public String dilatationStorage(long storageId, Integer storageUpdateSize) {
        Map<String, Object> map = new HashMap<String, Object>();
        User cUser = CurrentUserUtils.getInstance().getUser();
        Storage storage = storageDao.findOne(storageId);
        int usedStorage=0;
        List<Storage> list = storageDao.findByCreateBy(cUser.getId());
        for (Storage stor : list) {
            usedStorage = usedStorage + (int) stor.getStorageSize();
        }
        if (storageUpdateSize/1024 - storage.getStorageSize()/1024>((int) cUser.getVol_size() - usedStorage / 1024)){
            map.put("status", "500");
            return JSON.toJSONString(map);
        }
        
        storage.setStorageSize(storageUpdateSize);
        storageDao.save(storage);
        map.put("status", "200");
        return JSON.toJSONString(map);
    }

    /**
     * 
     * Description: <br>
     * 1、…<br>
     * 2、…<br>
     * Implement: <br>
     * 1、…<br>
     * 2、…<br>
     * 
     * @param storageId 卷组id
     * @return JSON
     * @see
     */
    @RequestMapping(value = { "service/storage/delete" })
    @ResponseBody
    public String deleteStorage(@RequestParam String ids) {
        Map<String, Object> map = new HashMap<String, Object>();
        String[] id = ids.split(",");
        Storage storage= new Storage();
        for(int i=0 ;i<id.length;i++){
        storage = storageDao.findOne(Long.parseLong(id[i]));
        if (StorageConstant.IS_USER == storage.getUseType()) {
            map.put("status", "500");
            return JSON.toJSONString(map);
                } 
        }
        for(int i=0 ;i<id.length;i++){
        
            storageDao.delete(Long.parseLong(id[i]));
            CephController cephCon = new CephController();
            try {
                cephCon.connectCephFS();
            }
            catch (Exception e) {
                e.printStackTrace();
            }
            cephCon.deleteStorageCephFS(storage.getStorageName());
            map.put("status", "200");
        }
        return JSON.toJSONString(map);
    }
    /**
     * 卷组已使用的大小
     * @param storageName 卷组名
     * @param totalSize 卷组总大小
     * @return JSON
     * @see
     */
    @RequestMapping(value = { "hasUsed.do" }, method = RequestMethod.GET)
    @ResponseBody
    public String hasUsed(String storageName, long totalSize) {
        Map map = new HashMap();
        String namespace = CurrentUserUtils.getInstance().getUser().getNamespace();
        File file = new File(cephController.getMountpoint() + namespace + storageName);
        String directory =cephController.getMountpoint() + namespace + storageName;
        long hasUse=0;
        if(new File(directory).exists()){
            hasUse = SFTPUtil.getHasUsed(directory);
        }else{
            map.put("status","400");
        //mountLocalCeph(file);
        }
        map.put("length", String.valueOf(hasUse /1024/1024 ));
        return JSON.toJSONString(map);
    }

    /**
     * 
     * Description:展示文件列表
     * 
     * @param path 文件夹路径
     * @param dirName 目录名
     * @param storageName 卷组名
     * @return JSON
     * @see
     */

    @RequestMapping(value = { "listFile" }, method = RequestMethod.GET)
    @ResponseBody
    public String fileList(String path, String dirName, String storageName) {
        Map map = new HashMap();
        String directory = path;
        String namespace = CurrentUserUtils.getInstance().getUser().getNamespace();
        if ("".equals(dirName)) {
            File file = new File(cephController.getMountpoint() + namespace + storageName);
            //mountLocalCeph(file);

            directory = cephController.getMountpoint() + namespace + storageName;
        }
        if ("../".equals(dirName)) {
            if ((path).equals(cephController.getMountpoint() + namespace + storageName)) {
                map.put("status", "500");
                return JSON.toJSONString(map);
            }
            directory = directory.substring(0, directory.lastIndexOf('/'));
            directory = directory.substring(0, directory.lastIndexOf('/') + 1);
        } 
        else {
            directory += dirName;
        }
        if(new File(directory).exists()){
            List<FileInfo> list = SFTPUtil.listFileInfo(directory);
            map.put("fileList", list);
        }else{
            map.put("status","400");
        }
        
        return JSON.toJSONString(map);
    }

    /**
     * 
     * Description: 如果没有本地cephfs目录没有挂载，则挂载。
     * 
     * @param file 文件名
     * @see
     */
    @RequestMapping(value = { "storage/mount.do" }, method = RequestMethod.GET)
    @ResponseBody
    public String mountLocalCeph() {
        Map map = new HashMap();
        String namespace = CurrentUserUtils.getInstance().getUser().getNamespace();
        File file = new File(cephController.getMountpoint() + namespace);
        if (!file.exists()) {
            CephController cephCon = new CephController();
            try {
                cephCon.connectCephFS();
            }
            catch (Exception e1) {
                e1.printStackTrace();
            }
            try {
                CmdUtil.exeCmd(cephController.getMountexec(), cephController.getCephDir());
                map.put("status", "200");
            } 
            catch (IOException  e) {
                map.put("status", "500");
                e.printStackTrace();
            }
        }
        return JSON.toJSONString(map);
    }

    /**
     * 
     * Description: 上传文件到卷组
     * 
     * @param file 上传文件名
     * @param path 路径
     * @param storageName 卷组名
     * @param id 卷组的id
     * @return JSON
     * @see
     */

    @RequestMapping(value = { "upload" }, method = RequestMethod.POST)
    @ResponseBody
    public String handleFileUpload(@RequestParam("file") MultipartFile[] files, String path, String storageName, long id) {
            try {
                Map map = new HashMap();
                for(int i =0;i<files.length;i++){
                long fileSize =files[i].getSize() /1024/1024;
                String namespace = CurrentUserUtils.getInstance().getUser().getNamespace();
                File dir = new File(cephController.getMountpoint() + namespace + "/" + storageName);
                storageDao.findOne(id).getStorageSize();
                long used = (fileSize + dir.length()) / 1024 / 1024;
                long result = storageDao.findOne(id).getStorageSize() - used;
                if (0 >= result) {
                    map.put("status", "500");
                    return JSON.toJSONString(map);
                }
                    BufferedOutputStream out = new BufferedOutputStream(
                            new FileOutputStream(new File(path + files[i].getOriginalFilename())));
                    out.write(files[i].getBytes());
                    out.flush();
                    out.close();
                map.put("used", used);
                map.put("status", "200");
                }
                return JSON.toJSONString(map);
            } 
            catch (FileNotFoundException e) {
                e.printStackTrace();
                return "上传失败," + e.getMessage();
            } 
            catch (IOException e) {
                e.printStackTrace();
                return "上传失败," + e.getMessage();
            }
        } 

    /**
     * 
     * Description: 下载卷组中的文件
     * 
     * @param downfiles 需要下载的文件和文件夹名们
     * @param directory 所在目录
     * @param request 
     * @param response 
     * @throws IOException 
     * @throws JSchException 
     * @throws InterruptedException 
     * @see
     */

    @RequestMapping(value = { "media" }, method = RequestMethod.GET)
    public void downloadFile(String downfiles, String directory, HttpServletRequest request,
            HttpServletResponse response) throws IOException, JSchException, InterruptedException {

        SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
        String newdownfile = df.format(new Date()) + ".zip";
        String[] downfile = downfiles.split(",");
        File[] files = new File[downfile.length];
        for (int i = 0; i < downfile.length; i++) {
            files[i] = new File(directory + downfile[i]);
        }
        ZipCompressing.zip(directory + newdownfile, files); // 测试压缩目录
        // 下载
        String fullPath = directory + newdownfile;
        response.setContentType(request.getServletContext().getMimeType(fullPath));
        response.setHeader("Content-Disposition", "attachment;filename=" + newdownfile);
        try {
            InputStream myStream = new FileInputStream(fullPath);
            IOUtils.copy(myStream, response.getOutputStream());
            response.flushBuffer();
            System.out.println("下载成功");
        } 
        catch (IOException e) {
        } 
        finally {
            File rm = new File(fullPath);
            rm.delete();
        }
    }
    @RequestMapping(value = { "storage/format" }, method = RequestMethod.POST)
    @ResponseBody
    public String formatStorage(String storageName,boolean isVolReadOnly){
        Map map = new HashMap();
        try {
            cephController.formatStorageCephFS(cephController.getMountpoint(),storageName,isVolReadOnly);
            map.put("status", 200);
                  }
        catch (FileNotFoundException e) {
            map.put("status", 500);
            e.printStackTrace();
        }finally{
            return JSON.toJSONString(map);
        }

    } 
    /**
     * 手动创建文件夹
     * 
     * @param storageName
     * @param isVolReadOnly
     * @return 
     * @see
     */
    @RequestMapping(value = { "storage/createFile.do" }, method = RequestMethod.POST)
    @ResponseBody
    public String createFile(String storageName,String dirName,String path){
        Map map = new HashMap();
        String namespace = CurrentUserUtils.getInstance().getUser().getNamespace();
//        String directory = cephController.getMountpoint() + namespace + storageName;
        File file = new File(path+"/"+dirName);
        if(true==file.mkdir()){
            map.put("status", "200");
        }else{
            map.put("status", "500");
                }
        return JSON.toJSONString(map);

    } 
    /**
     * 手动删除文件或文件夹
     * 
     * @param storageName
     * @param isVolReadOnly
     * @return 
     * @see
     */
    @RequestMapping(value = { "storage/delFile.do" }, method = RequestMethod.GET)
    @ResponseBody
    public String delFile(String storageName,String fileNames,String path){
        Map map = new HashMap();
        String namespace = CurrentUserUtils.getInstance().getUser().getNamespace();
//        String directory = cephController.getMountpoint() + namespace + storageName;
        String[] fileName = fileNames.split(",");
        for(int i=0 ;i<fileName.length;i++){
           File file = new File(path+"/"+fileName[i]);
           if(file.isDirectory()){
               //删除文件夹下文件和此文件夹
               FileUtils.delAllFile(path+"/"+fileName[i]);
               file.delete();
           }else{
               file.delete();
                       }
                  }
        map.put("status", "200");
        return JSON.toJSONString(map);


    } 
    /**
     * 
     * Description: 解压压缩文件
     * @param storageName
     * @param fileName
     * @param path
     * @return string
     * @see
     */
    @RequestMapping(value = { "storage/unZipFile.do" }, method = RequestMethod.GET)
    @ResponseBody
    public String unZipFile(String storageName,String fileName,String path){
        Map map = new HashMap();
        try {
            CmdUtil.exeCmd( "unzip -o "+fileName, path);
        } catch (IOException e) {
            e.printStackTrace();
        }
        map.put("status", "200");
        return JSON.toJSONString(map);
    }
    
    @RequestMapping(value = { "service/storage/getVols.do" }, method = RequestMethod.GET)
    @ResponseBody
    public String getVol(){
        Map map = new HashMap();
        String userType =CurrentUserUtils.getInstance().getUser().getUser_autority();
        long createBy=0;
        if(UserConstant.AUTORITY_USER.equals(userType)){
            createBy = CurrentUserUtils.getInstance().getUser().getParent_id();
        }else{
            createBy = CurrentUserUtils.getInstance().getUser().getId();
                }
        List<Storage> storages = storageDao.findByCreateBy(createBy);
        map.put("storages", storages);
        return JSON.toJSONString(map);
    }
//    public static void main(String[] args) {
//        try {
//            CephController ccl = new CephController();
//            CmdUtil.exeCmd(ccl.getMountexec(), ccl.getMountpoint());
//        } catch (IOException e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        }
//    }
}
