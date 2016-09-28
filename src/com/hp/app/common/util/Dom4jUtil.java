package com.hp.app.common.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;

public class Dom4jUtil {
	  public Document parse(String sfile) throws DocumentException {
          SAXReader reader = new SAXReader();
          Document document = reader.read(new File(sfile));
          return document;
      }

      public void writerDoc(Document document,String fileName) throws IOException {

         OutputFormat format = OutputFormat.createPrettyPrint();
          // 输出全部原始数据，并用它生成新的我们需要的XML文件
          XMLWriter writer2 = new XMLWriter(new FileOutputStream(new File(fileName)), format);
          writer2.write(document); //输出到文件
          writer2.close();

      }
}