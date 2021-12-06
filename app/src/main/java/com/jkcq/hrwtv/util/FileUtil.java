package com.jkcq.hrwtv.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Environment;
import android.text.TextUtils;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.RandomAccessFile;
import java.io.Reader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FileUtil {

    private static final String TAG = FileUtil.class.getSimpleName();


    /**
     * 方案目录
     */
    private static final String FILE = "file";

    /**
     * 根目录
     */
    private static String APP_ROOT_DIR = "coaptest";

    /**
     * 日志目录
     */
    private static final String LOG_FILE = "log";

    /**
     * 图片目录
     */
    private static final String IMAGE_FILE = "image";

    /**
     * 图片缓存目录
     */
    private static final String IMAGE_CACHE_FILE = "imageCache";

    /**
     * 网络请求缓存目录
     */
    private static final String NET_CACHE_FILE = "netCache";

    /**
     * 字体文件下载目录
     */
    private static final String FONTS_FILE = "fonts";

    /**
     * 语音缓存目录
     */
    private static final String VOICE_CACHE_FILE = "voice";

    private static String appPath;

    public static void initFile(Context context) {
        //APP_ROOT_DIR = context.getPackageName();
        appPath = getRootDir(context);

//        Logger.d("getCurrentLogPath appPath = " + appPath);
        if (!isFileExists(appPath)) {
            createDir(appPath);
        }
    }

    /**
     * 获取手机根目录存储地址
     *
     * @param context
     * @return
     */
    public static String getRootDir(Context context) {
        if (isSDExists()) {
            return getSDPath();
        }
        return getAppPath(context);
    }

    /**
     * 获取手机SD卡根目录存储地址
     *
     * @return
     */
    public static String getSDPath() {
        File sdDir = null;
        boolean sdCardExist = Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED);

        if (sdCardExist) {
            sdDir = Environment.getExternalStorageDirectory();
        }
        return (null == sdDir) ? null : sdDir.toString();
    }

    /**
     * 获取手机内存根目录存储地址
     *
     * @param context 上下文对象
     * @return 目录
     */
    public static String getAppPath(Context context) {
        String fileDir = context.getFilesDir().getAbsolutePath();
        if (fileDir.lastIndexOf("/") > 0) {
            fileDir = fileDir.substring(0, fileDir.lastIndexOf("/"));
        }
        return fileDir;
    }

    /**
     * 获取图片路径
     *
     * @return 缓存路径File
     */
    public static String getFile() {
        String path;
        if (isSDExists()) {
            path = getSDPath();
        } else {
            path = appPath;
        }
        if (null == path) {
            return null;
        }
        String logPath = path + File.separator + APP_ROOT_DIR + File.separator + FILE + File.separator;
        if (!isFileExists(logPath)) {
            createDir(logPath);
        }


        return logPath;
    }


    /**
     * 获取缓存的总目录
     *
     * @return
     */
    public static File getCacheFile() {
        String path;
        if (isSDExists()) {
            path = getSDPath();
        } else {
            path = appPath;
        }
        if (null == path) {
            return null;
        }
        String logPath = path + File.separator + APP_ROOT_DIR;
        File file = new File(logPath);
        return file;
    }

    /**
     * 获取图片缓存路径
     *
     * @return 缓存路径File
     */
    public static File getImageCacheFile() {
        String path;
        if (isSDExists()) {
            path = getSDPath();
        } else {
            path = appPath;
        }
        if (null == path) {
            return null;
        }
        String logPath = path + File.separator + APP_ROOT_DIR + File.separator
                + IMAGE_CACHE_FILE;
        if (!isFileExists(logPath)) {
            createDir(logPath);
        }

        File file = new File(logPath);

        return file;
    }

    /**
     * 获取网络请求缓存路径
     *
     * @return 缓存路径File
     */
    public static File getNetCacheFile() {
        String path;
        if (isSDExists()) {
            path = getSDPath();
        } else {
            path = appPath;
        }
        if (null == path) {
            return null;
        }
        String logPath = path + File.separator + APP_ROOT_DIR + File.separator
                + NET_CACHE_FILE;
        if (!isFileExists(logPath)) {
            createDir(logPath);
        }

        File file = new File(logPath);
        return file;
    }

    /**
     * 获取图片路径
     *
     * @return 缓存路径File
     */
    public static File getImageFile() {
        String path;
        if (isSDExists()) {
            path = getSDPath();
        } else {
            path = appPath;
        }
        if (null == path) {
            return null;
        }
        String logPath = path + File.separator + APP_ROOT_DIR + File.separator
                + IMAGE_FILE + File.separator;
        if (!isFileExists(logPath)) {
            createDir(logPath);
        }

        File file = new File(logPath);
        return file;
    }

    public static String getPhotoFileName() {
       /* Date date = new Date(System.currentTimeMillis());
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "yyyyMMddHHmmss");*/
        return "share" + ".jpg";
    }

    public static File getImageFile(String name) {
        String path;
        if (isSDExists()) {
            path = getSDPath();
        } else {
            path = appPath;
        }
        if (null == path) {
            return null;
        }
        String logPath = path + File.separator + APP_ROOT_DIR + File.separator
                + IMAGE_FILE + File.separator;
        if (!isFileExists(logPath)) {
            createDir(logPath);
        }

        File file = new File(logPath, name);
        return file;
    }


    /**
     * 获取用户头像固定路径，目的是调用摄像头拍摄时将大图保存在指定位置
     *
     * @return
     */
    public static File getLogoImageFile() {
        File file = getImageFile();

        String logoPath = file.getAbsolutePath() + File.separator + "logo.png";
        File logoFile = new File(logoPath);
        if (!logoFile.exists()) {
            createFile(logoPath);
        }
        return logoFile;
    }

    /**
     * 获取字体文件路径
     *
     * @return 缓存路径File
     */
    public static File getFontsFile() {
        String path;
        if (isSDExists()) {
            path = getSDPath();
        } else {
            path = appPath;
        }
        if (null == path) {
            return null;
        }
        String logPath = path + File.separator + APP_ROOT_DIR + File.separator
                + FONTS_FILE;
        if (!isFileExists(logPath)) {
            createDir(logPath);
        }

        File file = new File(logPath);
        return file;
    }

    /**
     * 获取语音缓存目录
     *
     * @return
     */
    public static File getVoiceFile() {
        String path;
        if (isSDExists()) {
            path = getSDPath();
        } else {
            path = appPath;
        }
        if (null == path) {
            return null;
        }
        String logPath = path + File.separator + APP_ROOT_DIR + File.separator
                + VOICE_CACHE_FILE;
        if (!isFileExists(logPath)) {
            createDir(logPath);
        }

        File file = new File(logPath);
        return file;
    }

    /**
     * 获取文件夹下所有的文件大小 MB
     *
     * @param file
     * @return
     */
    public static double getDirSize(File file) {
        //判断文件是否存在
        if (file.exists()) {
            //如果是目录则递归计算其内容的总大小
            if (file.isDirectory()) {
                File[] children = file.listFiles();
                double size = 0;
                for (File f : children)
                    size += getDirSize(f);
                return size;
            } else {//如果是文件则直接返回其大小,以“兆”为单位
                double size = (double) file.length() / 1024 / 1024;
                return size;
            }
        } else {
//            Logger.e("文件或者文件夹不存在，请检查路径是否正确！");
            return 0.0;
        }
    }

    public static boolean isSDExists() {
        return Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED);
    }

    public static String getCurrentLogPath() {
        String path;
        if (isSDExists()) {
            path = getSDPath();
        } else {
            path = appPath;
        }
        if (null == path) {
            return null;
        }
        String logPath = path + File.separator + APP_ROOT_DIR + File.separator
                + LOG_FILE;
        if (isFileExists(logPath)) {
            createDir(logPath);
        }
        return logPath;
    }

    /**
     * 判断文件是否存在
     *
     * @param filePath 文件地址
     * @return true 文件已经存在 false 文件不存在
     */
    public static boolean isFileExists(String filePath) {
        if (TextUtils.isEmpty(filePath)) {
            return false;
        }
        File file = new File(filePath);
        if (null == file || !file.exists()) {
            return false;
        }
        return true;
    }

    /**
     * 创建文件/文件夹
     *
     * @param fullDir 完整地址
     * @return 创建好的文件
     */
    public static File createDir(String fullDir) {
        File file = new File(fullDir);

        boolean isSucceed;
        if (!file.exists()) {
            File parentDir = new File(file.getParent());
            if (!parentDir.exists()) {
                isSucceed = parentDir.mkdirs();
                if (!isSucceed) {
//                    Logger.e("createDir mkdirs failed");
                }
            }
            try {
                isSucceed = file.mkdir();
                if (!isSucceed) {
//                    Logger.e("createDir createNewFile failed");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return file;
    }

    /**
     * 创建文件
     *
     * @param filePath
     */
    public static boolean createFile(String filePath) {
        try {
            File file = new File(filePath);
            if (!file.exists()) {
                if (!file.getParentFile().exists()) {
                    file.getParentFile().mkdirs();
                }
                return file.createNewFile();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return true;
    }

    /**
     * 删除文件
     *
     * @param filePath
     */
    public static void deleteFile(String filePath) {
        if (TextUtils.isEmpty(filePath)) {
            return;
        }
        File file = new File(filePath);
        if (null == file || !file.exists()) {
            return;
        }
        file.delete();
    }

    public static void deleteFile(File file) {
        try {
            if (null == file || !file.exists()) {
                return;
            }
            file.delete();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 递归删除目录下的所有文件及子目录下所有文件
     *
     * @param dir 将要删除的文件目录
     * @return boolean Returns "true" if all deletions were successful.
     * If a deletion fails, the method stops attempting to
     * delete and returns "false".
     */
    public static boolean deleteDir(File dir) {
        if (dir.isDirectory()) {
            String[] children = dir.list();
            //递归删除目录中的子目录下
            for (int i = 0; i < children.length; i++) {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
        }
        // 目录此时为空，可以删除
        return dir.delete();
    }

    /**
     * 判断文件名是否是yyyyMMdd的日期格式
     *
     * @param filename
     */
    public static boolean isFileNamedWithDate(String filename) {
        Pattern pattern = Pattern.compile("\\d{4}\\-{1}\\d{2}\\-{1}\\d{2}");
        Matcher matcher = pattern.matcher(filename);
        if (matcher.find()) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 日志文件超过24小时删除
     *
     * @param filePath
     */
    public static void deleteFileByDate(String filePath) {
        File file = new File(filePath);
        if (!file.exists() || !file.isDirectory()) {
            return;
        }
//        String currentTime = TimeUtil.getTime();
//        File[] files = file.listFiles();
//        if (null == files) {
//            return;
//        }
//        for (File f : files) {
//            if (null == f) {
//                continue;
//            }
//            if (isFileNamedWithDate(f.getName())) {
//                String fileName = f.getName();
//                fileName = fileName.substring(0, fileName.lastIndexOf("."));
//                if (TimeUtil.compareDate(fileName, currentTime)) {
//                    deleteFile(f);
//                }
//            }
//        }
    }

    private static void makeRootDirectory(String filePath) {
        File file;
        try {
            file = new File(filePath);
            if (!file.exists()) {
                file.mkdir();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 将字符保存在指定文件中
     *
     * @param filePath
     * @param buffer
     */
    public static void saveToFile(String filePath, StringBuffer buffer) {
        if (null == filePath || null == buffer || buffer.length() == 0) {
            return;
        }
        File crashFile = new File(filePath);
        if (!isFileExists(filePath)) {
            crashFile = createDir(filePath);
        }

        OutputStreamWriter out = null;
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(filePath);
            out = new OutputStreamWriter(fos, "UTF-8");
            out.write(buffer.toString());
            out.flush();
        } catch (IOException e) {
            if (crashFile.exists()) {
                deleteFile(crashFile);
            }
        } finally {
            try {
                if (null != fos) {
                    fos.close();
                }
                if (out != null) {
                    out.close();
                }
            } catch (Exception e) {
            }
        }
    }

    public static void writeImage(Bitmap bitmap, String destPath, int quality) {
        try {
            FileUtil.deleteFile(destPath);
            if (FileUtil.createFile(destPath)) {
                FileOutputStream out = new FileOutputStream(destPath);
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
                try {
                    out.flush();
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static File writeImage(Bitmap bitmap, File f, int quality) {
        try {

            if (bitmap != null) {
                FileOutputStream fos;
                try {
                    fos = new FileOutputStream(f);
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 80, fos);
                    fos.flush();
                    fos.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return f;
    }


//    /**
//     * 将String数据存为文件
//     */
//    public static File wtiteFileFromBytes(String name, String path) {
//        byte[] b = name.getBytes();
//        BufferedOutputStream stream = null;
//        File file = null;
//        try {
//            file = new File(path);
//            FileOutputStream fstream = null;
//            if(!file.exists()){
//                file.createNewFile();//如果文件不存在，就创建该文件
//                fstream = new FileOutputStream(file);//首次写入获取
//            }else{
//                //如果文件已存在，那么就在文件末尾追加写入
//                fstream = new FileOutputStream(file,true);//这里构造方法多了一个参数true,表示在文件末尾追加写入
//            }
//            stream = new BufferedOutputStream(fstream);
//            stream.write(b);
//        } catch (Exception e) {
//            e.printStackTrace();
//        } finally {
//            if (stream != null) {
//                try {
//                    stream.close();
//                } catch (IOException e1) {
//                    e1.printStackTrace();
//                }
//            }
//        }
//        return file;
//    }

    /**
     * 将String数据存为文件
     */
    public static File wtiteFileFromBytes(String name, String path) {
        byte[] b = name.getBytes();
        BufferedOutputStream stream = null;
        File file = null;
        try {
            file = new File(path);
            if(!file.exists()){
                file.createNewFile();//如果文件不存在，就创建该文件
            }
            RandomAccessFile raf = null;
            raf = new RandomAccessFile(file, "rw");
            raf.seek(file.length());
            raf.write(b);
            // FileOutputStream fstream = new FileOutputStream(file, true);
            //  stream = new BufferedOutputStream(fstream);
            // stream.write(b);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (stream != null) {
                try {
                    stream.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        }
        return file;
    }


    public static String getFileData(String path) {
        StringBuffer buffer = new StringBuffer();
        try {
            FileInputStream fis = new FileInputStream(path);
            InputStreamReader isr = new InputStreamReader(fis, "UTF-8");//文件编码Unicode,UTF-8,ASCII,GB2312,Big5
            Reader in = new BufferedReader(isr);
            int ch;
            while ((ch = in.read()) > -1) {
                buffer.append((char) ch);
            }
            in.close();
        } catch (IOException e) {
//            Logger.e("yichang", "文件不存在!");

        }
        return buffer.toString();  //buffer.toString())就是读出的内容字符
    }


}
