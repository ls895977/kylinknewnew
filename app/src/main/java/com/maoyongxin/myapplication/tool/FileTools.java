package com.maoyongxin.myapplication.tool;

import android.content.Context;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.DecimalFormat;

import Decoder.BASE64Encoder;

public class FileTools {

	/**
	 * 导入Raw下的文件
	 */
	private void imporDatabase(Context context, String home, String filename, int inRaw) {
		File dir = new File(home);
		if (!dir.exists()) {
			dir.mkdirs();
		}
		File file = new File(dir, filename);
		try {
			if (!file.exists()) {
				file.createNewFile();
				InputStream is = context.getApplicationContext().getResources()
						.openRawResource(inRaw);
				FileOutputStream fos = new FileOutputStream(file);
				byte[] buffere = new byte[is.available()];
				is.read(buffere);
				fos.write(buffere);
				is.close();
				fos.close();
			}

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	/**
	 * 将文件导入指定的位置
	 * @param home
	 * @param filename
	 */
	private void imporDatabase(String home, String filename) {
		File dir = new File(home);
		if (!dir.exists()) {
			dir.mkdirs();
		}
		File file = new File(dir, filename);
		File srcFile = new File(home);
		try {
			if (!file.exists()) {
				file.createNewFile();
				InputStream is = new FileInputStream(srcFile);
				FileOutputStream fos = new FileOutputStream(file);
				byte[] buffere = new byte[is.available()];
				is.read(buffere);
				fos.write(buffere);
				is.close();
				fos.close();
			}

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	/**
	 * 创建目录，全路径（包含文件名）
	 * @param paths
	 * @return
	 */
	public static boolean mkdirLocalPath(String paths) {
		File dirFile = new File(paths).getParentFile();
		if(dirFile != null && dirFile.exists())
			return true;
		if (!dirFile.mkdirs()) {
			return false;
		}
		return true;
	}
	/**
	 * 创建目录，不是全路径（不包含文件名）
	 * @param DirectoryPath
	 * @return
	 */
	public static boolean mkdirLocalDirectory(String DirectoryPath) {
		File dirFile = new File(DirectoryPath);
		if(dirFile != null && dirFile.exists())
			return true;
		if (!dirFile.mkdirs()) {
			return false;
		}
		return true;
	}
	/**
	 * 检测文件夹是否存在
	 * @param folderPath
	 * @return
	 */
	public static boolean isFolderExist(String folderPath) {
		boolean result = false;
		File f = new File(folderPath);
		if (!f.exists()) {
			if (f.mkdirs()) {
				result = true;
			} else
				result = false;
		} else
			result = true;
		return result;
	}
	
	/**
	 * 获取文件大小
	 * @param f 文件
	 * @return
	 */
	public static String getFileSizes(File f)  {
		long s = 0;
		try {
			if (f.exists()) {
				FileInputStream fis = null;
				fis = new FileInputStream(f);
				s = fis.available();
			} else {
//				f.createNewFile();
//				System.out.println("文件不存在");
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return FormetFileSize(s);
	}
	/**
	 * 获取文件大小
	 * @param filePath 文件路径
	 * @return
	 */
	public static String getFileSizes(String filePath)  {
		long s = 0;
		try {
			File f = new File(filePath);
			if (f.exists()) {
				FileInputStream fis = null;
				fis = new FileInputStream(f);
				s = fis.available();
			} else {
//				f.createNewFile();
//				System.out.println("文件不存在");
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return FormetFileSize(s);
	}
	private static String FormetFileSize(long fileS) {// 转换文件大小
		DecimalFormat df = new DecimalFormat("#.00");
		String fileSizeString = "";
		if (fileS < 1024) {
			fileSizeString = df.format((double) fileS) + "B";
		} else if (fileS < 1048576) {
			fileSizeString = df.format((double) fileS / 1024) + "K";
		} else if (fileS < 1073741824) {
			fileSizeString = df.format((double) fileS / 1048576) + "M";
		} else {
			fileSizeString = df.format((double) fileS / 1073741824) + "G";
		}
		return fileSizeString;
	}
	/**
	 * 拷贝，移动文件
	 * @param oldFileName 旧文件
	 * @param newFileName 新文件
	 * @param append 是否追加
	 * @param isDelete 是否删除旧文件
	 */
	public static void MoveFile(String oldFileName, String newFileName, boolean append, boolean isDelete){
		if(mkdirLocalPath(newFileName)){
			File oldFile = new File(oldFileName);
			if(oldFile != null && oldFile.exists()){
				if(oldFile.isDirectory()){
					for(File old : oldFile.listFiles()){
						try {
							FileInputStream fis = new FileInputStream(old);
							int length = fis.available();   
							byte [] buffer = new byte[length];   
							fis.read(buffer);       
							fis.close();
							FileOutputStream fout = new FileOutputStream(newFileName+"/"+old.getName(), append); //true 为追加
							fout.write(buffer);   
							fout.close();   
						} catch (IOException e) {
							e.printStackTrace();
						}       
					}
				} else {
					try {
						FileInputStream fis = new FileInputStream(oldFile);
						int length = fis.available();   
						byte [] buffer = new byte[length];   
						fis.read(buffer);       
						fis.close();
						FileOutputStream fout = new FileOutputStream(newFileName, append); //true 为追加
						fout.write(buffer);   
						fout.close();   
					} catch (IOException e) {
						e.printStackTrace();
					} 
				}
				if(isDelete)
					delete(oldFile);
			}
		}
	}
	public static void delete(File file) {
	       if (file.isFile()) {  
	            file.delete();  
	            return;  
	        }  
	  
	        if(file.isDirectory()){  
	            File[] childFiles = file.listFiles();
	            if (childFiles == null || childFiles.length == 0) {  
	               file.delete();  
	                return;  
	            }  
	  
	            for (int i = 0; i < childFiles.length; i++) {  
	                delete(childFiles[i]);  
	            }  
	            file.delete();  
	        }  
	    } 
	/**
	 * 删除目录（文件夹）以及目录下的文件
	 * @param   sPath 被删除目录的文件路径
	 * @return  目录删除成功返回true，否则返回false
	 */
	public static boolean deleteDirectory(String sPath) {
	    //如果sPath不以文件分隔符结尾，自动添加文件分隔符
	    if (!sPath.endsWith(File.separator)) {
	        sPath = sPath + File.separator;
	    }
	    File dirFile = new File(sPath);
	    //如果dir对应的文件不存在，或者不是一个目录，则退出
	    if (!dirFile.exists() || !dirFile.isDirectory()) {
	        return false;
	    }
	    boolean flag = true;
	    //删除文件夹下的所有文件(包括子目录)
	    File[] files = dirFile.listFiles();
	    for (int i = 0; i < files.length; i++) {
	        //删除子文件
	        if (files[i].isFile()) {
	            flag = FileTools.deleteFile(files[i].getAbsolutePath());
	            if (!flag) break;
	        } //删除子目录
	        else {
	            flag = deleteDirectory(files[i].getAbsolutePath());
	            if (!flag) break;
	        }
	    }
	    if (!flag) return false;
	    //删除当前目录
	    if (dirFile.delete()) {
	        return true;
	    } else {
	        return false;
	    }
	}

	/**
	 * 删除单个文件
	 * @param   sPath    被删除文件的文件名
	 * @return 单个文件删除成功返回true，否则返回false
	 */
	public static boolean deleteFile(String sPath) {
	    boolean flag = false;
	    File file = new File(sPath);
	    // 路径为文件且不为空则进行删除
	    if (file.isFile() && file.exists()) {
	        file.delete();
	        flag = true;
	    }
	    return flag;
	}
	/**
	 * 复制单个文件
	 * @param oldPath String 原文件路径 如：c:/fqf.txt
	 * @param newPath String 复制后路径 如：f:/fqf.txt
	 * @return boolean
	 */
	public static void copyFile(String oldPath, String newPath) {
		try {
			int bytesum = 0;
			int byteread = 0;
			File oldfile = new File(oldPath);
			if (oldfile.exists()) { //文件存在时
				InputStream inStream = new FileInputStream(oldPath); //读入原文件
				FileOutputStream fs = new FileOutputStream(newPath);
				byte[] buffer = new byte[1444];
				int length;
				while ( (byteread = inStream.read(buffer)) != -1) {
					bytesum += byteread; //字节数 文件大小
					System.out.println(bytesum);
					fs.write(buffer, 0, byteread);
				}
				inStream.close();
			}
		}
		catch (Exception e) {
			System.out.println("复制单个文件操作出错");
			e.printStackTrace();

		}

	}

	/**
	 * 复制整个文件夹内容
	 * @param oldPath String 原文件路径 如：c:/fqf
	 * @param newPath String 复制后路径 如：f:/fqf/ff
	 * @return boolean
	 */
	public static void copyFolder(String oldPath, String newPath) {

		try {
			(new File(newPath)).mkdirs(); //如果文件夹不存在 则建立新文件夹
			File a=new File(oldPath);
			String[] file=a.list();
			File temp=null;
			for (int i = 0; i < file.length; i++) {
				if(oldPath.endsWith(File.separator)){
					temp=new File(oldPath+file[i]);
				}
				else{
					temp=new File(oldPath+ File.separator+file[i]);
				}

				if(temp.isFile()){
					FileInputStream input = new FileInputStream(temp);
					FileOutputStream output = new FileOutputStream(newPath + "/" +
							(temp.getName()).toString());
					byte[] b = new byte[1024 * 5];
					int len;
					while ( (len = input.read(b)) != -1) {
						output.write(b, 0, len);
					}
					output.flush();
					output.close();
					input.close();
				}
				if(temp.isDirectory()){//如果是子文件夹
					copyFolder(oldPath+"/"+file[i],newPath+"/"+file[i]);
				}
			}
		}
		catch (Exception e) {
			System.out.println("复制整个文件夹内容操作出错");
			e.printStackTrace();

		}

	}
	/**
	 * 获取文件的base64字节码
	 * @param imgFile
	 * @return
	 */
	public static String getImageStr(String imgFile) {
		InputStream inputStream = null;
		byte[] data = null;
		try {
			inputStream = new FileInputStream(imgFile);
			data = new byte[inputStream.available()];
			inputStream.read(data);
			inputStream.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		BASE64Encoder encoder = new BASE64Encoder();
		return encoder.encode(data);
	}
}