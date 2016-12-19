/*
 * 文件名：PoiUtils.java
 * 版权：Copyright by www.huawei.com
 * 描述：
 * 修改人：root
 * 修改时间：2016年11月21日
 * 跟踪单号：
 * 修改单号：
 * 修改内容：
 */

package com.bonc.epm.paas.util;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.bonc.epm.paas.entity.Service;
import javax.swing.JOptionPane;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.DataFormat;

/**
 * 〈一句话功能简述〉
 * 〈功能详细描述〉
 * @author root
 * @version 2016年11月21日
 * @see PoiUtils
 * @since
 */

public class PoiUtils {
        
    /**
     * 
     * Description: <br>
         *   全两导出
     */
    public HSSFWorkbook exportTest(List<Service> list,String[] header,List<String[]> context){
            HSSFWorkbook wb = new HSSFWorkbook();                       // 声明一个工作薄
            HSSFSheet sheet = wb.createSheet("服务信息导出表");           //声明一个单子并命名
            sheet.setDefaultColumnWidth(15);                        //给单子名称一个长度
            HSSFCellStyle style = wb.createCellStyle();            // 生成一个样式  
            //设置这些样式  
            style.setFillForegroundColor(HSSFColor.LIME.index);  
            style.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);  
            style.setBorderBottom(HSSFCellStyle.BORDER_THIN);  
            style.setBorderLeft(HSSFCellStyle.BORDER_THIN);  
            style.setBorderRight(HSSFCellStyle.BORDER_THIN);  
            style.setBorderTop(HSSFCellStyle.BORDER_THIN);  
            style.setAlignment(HSSFCellStyle.ALIGN_CENTER);  
            HSSFFont font = wb.createFont();             //生成一个字体  
            font.setColor(HSSFColor.WHITE.index);  
            font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);  
            style.setFont(font);                        //把字体应用到当前的样式  
            HSSFRow row = sheet.createRow(0);           //创建第一行（也可以称为表头）
            style.setAlignment(HSSFCellStyle.ALIGN_CENTER);           //样式字体居中
            //给表头第一行一次创建单元格
           
            for(int i=0;i<header.length;i++){
                HSSFCell cell = row.createCell(i);
                cell = row.createCell(i);  
                cell.setCellValue(header[i]); 
                cell.setCellStyle(style);
                
                          }
//            cell.setCellValue("名称"); 
//            cell.setCellStyle(style);
//            cell = row.createCell(1);  
//            cell.setCellValue("运行状态");  
//            cell.setCellStyle(style);  
//            cell = row.createCell(2);  
//            cell.setCellValue("镜像");  
//            cell.setCellStyle(style); 
//            sheet.setColumnHidden(1,true); //隐藏某一列
            
            //body的样式
            HSSFCellStyle contentstyle = wb.createCellStyle();    //生成单元格的风格对象
            HSSFFont contentFont = wb.createFont();               //生成字体对象
            contentFont.setFontName("ARIAL");                       //设置字体对象font的字体名为“ARIAL”
            contentFont.setBoldweight((short) (10));                //设置字体对象font的字体大小为12
            contentFont.setBold(false);                             //设置字体对象font的字体加粗
            contentFont.setColor(HSSFColor.BLACK.index);            //设置字体对象font的字体颜色为白色
            contentstyle.setFont(contentFont);                      //设置style的字体属性为font
            contentstyle.setBorderTop(HSSFCellStyle.BORDER_THIN);  //设置上下左右的边框
            contentstyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);
            contentstyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);
            contentstyle.setBorderRight(HSSFCellStyle.BORDER_THIN);
            contentstyle.setAlignment(HSSFCellStyle.ALIGN_LEFT);  // 设置headstyle的内容居中现实
            contentstyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);//headstyle的内容竖直方向居中对齐
            contentstyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);   
            contentstyle.setFillForegroundColor(HSSFColor.LIGHT_TURQUOISE.index);  //设置背景颜色，
             DataFormat  format = wb.createDataFormat();
             contentstyle.setDataFormat(format.getFormat("@"));
                   //向单元格里填充数据
                   for (short i = 0; i < context.size(); i++) {
                    row = sheet.createRow(i + 1);
                    for(int n=0;n<context.get(i).length;n++){
                        HSSFCell conCell = row.createCell(n);
                        sheet.autoSizeColumn(n, true);
                        conCell.setCellStyle(contentstyle);
                        conCell.setCellValue(context.get(i)[n]);
                                            }
                    
                }
                return wb;
        }
}
