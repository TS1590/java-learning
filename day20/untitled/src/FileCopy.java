// FileCopy.java —— Day 7 IO 流：文件复制工具（字节流 + 缓冲流 + 进度条）
// 用法：java FileCopy <源文件> <目标文件>

import java.io.*;

public class FileCopy {
    public static void main(String[] args) {
        if (args.length < 2) {
            System.out.println("用法：java FileCopy <源文件> <目标文件>");
            return;
        }
        String src = args[0];
        String dest = args[1];
        File from = new File(src);          // File 类：描述文件路径/大小/是否存在

        if (!from.exists()) {
            System.out.println("源文件不存在：" + src);
            return;
        }

        long total = from.length();          // 文件总字节数，算进度用
        long copied = 0;
        InputStream in = null;
        OutputStream out = null;

        try {
            // 缓冲流包字节流：一次读一大块，减少磁盘访问次数（性能关键）
            in = new BufferedInputStream(new FileInputStream(from));
            out = new BufferedOutputStream(new FileOutputStream(dest));

            byte[] buffer = new byte[1024];  // 1KB 缓冲数组
            int len;
            while ((len = in.read(buffer)) != -1) {   // -1 = 读完了
                out.write(buffer, 0, len);            // 写实际读到的 len 个字节
                copied += len;
                System.out.print("\r复制进度：" + (copied * 100 / total) + "%");
            }
            out.flush();                      // 缓冲流最后必须 flush（把剩余数据推出去）
            System.out.println("\n复制完成 ✅ → " + dest);
        } catch (IOException e) {
            System.out.println("复制失败：" + e.getMessage());
        } finally {
            // 关资源：复习 Day 6 的 finally —— 无论成功失败都要关
            if (in != null)  { try { in.close();  } catch (IOException e) { } }
            if (out != null) { try { out.close(); } catch (IOException e) { } }
        }
    }
}
