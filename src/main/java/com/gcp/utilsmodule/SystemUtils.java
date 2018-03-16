package com.gcp.utilsmodule;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.text.DecimalFormat;
import java.util.List;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.ClipData;
import android.content.ContentValues;
import android.content.Context;
import android.content.IntentFilter;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.Resources.NotFoundException;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Environment;
import android.os.StatFs;
import android.text.ClipboardManager;
import android.text.InputFilter;
import android.text.TextUtils;
import android.text.method.DigitsKeyListener;
import android.util.Base64;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;

public class SystemUtils {
	public static String DES_KEY = "qM7c1gcDVeBqXKbzo7Lj3RHm";

	public static class FileUtils {

		public static boolean copy(InputStream inputStream,
				OutputStream outputStream) {
			try {
				byte[] buffer = new byte[1024];
				int length;
				while ((length = inputStream.read(buffer)) > 0) {
					outputStream.write(buffer, 0, length);
				}
				inputStream.close();
				outputStream.close();
			} catch (Exception e) {
				e.printStackTrace();
				try {
					if (inputStream != null)
						inputStream.close();
					if (outputStream != null)
						outputStream.close();
				} catch (Exception ee) {
					ee.printStackTrace();
				}
				return false;
			}
			return true;
		}

		public static boolean copy(InputStream inputStream, File outFile) {
			if (outFile != null && outFile.exists() && outFile.isFile()) {
				try {
					OutputStream outputStream = new FileOutputStream(outFile);
					return copy(inputStream, outputStream);
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				}
			}
			return false;
		}

		public static boolean isPresent(File file) {
			return file != null && file.exists() && file.isFile();
		}

		/**
		 * Gets the dir available size. 获取指定目录剩余空间
		 * 
		 * @param path
		 *            the path
		 * @return the dir available size
		 */
		@SuppressWarnings("deprecation")
		public static long getDirAvailableSize(String path) {
			StatFs stat = new StatFs(path);
			long totalBlocks = stat.getBlockCount();
			return totalBlocks;
		}

		private static File getAppDataDir(Context context) {
			final String appDir = "/Android/data/" + context.getPackageName();
			File file = new File(Environment.getExternalStorageDirectory()
					.getPath() + appDir);
			return file;
		}

		public static File getCacheDir(Context context) {
			File file = new File(getAppDataDir(context), "cache");
			return file;
		}

		public static String getCacheDirPath(Context context) {
			return getCacheDir(context).getAbsolutePath();
		}

		public static File getImageDir(Context context) {
			File file = new File(getAppDataDir(context), "images");
			return file;
		}

		public static String getImageDirPath(Context context) {
			return getImageDir(context).getAbsolutePath();
		}

		public static File getDownloadDir(Context context) {
			File file = new File(getAppDataDir(context), "download");
			return file;
		}

		public static String getDownloadDirPath(Context context) {
			return getDownloadDir(context).getAbsolutePath();
		}

		public static File getLogDir(Context context) {
			File file = new File(getAppDataDir(context), "log");
			return file;
		}

		public static String getLogDirPath(Context context) {
			return getLogDir(context).getAbsolutePath();
		}

		/**
		 * 转换文件大小
		 */
		public static String formetFileSize(long size) {
			DecimalFormat df = new DecimalFormat("#.00");
			String fileSizeString = "";
			if (size < 1024) {
				fileSizeString = df.format((double) size) + "B";
			} else if (size < 1048576) {
				fileSizeString = df.format((double) size / 1024) + "K";
			} else if (size < 1073741824) {
				fileSizeString = df.format((double) size / 1048576) + "M";
			} else {
				fileSizeString = df.format((double) size / 1073741824) + "G";
			}
			return fileSizeString;
		}

		public static File writeData(File file, byte[] data) {
			if (data == null || data.length <= 0) {
				return null;
			}
			try {
				if (!file.exists()) {
					file.createNewFile();
				}
				RandomAccessFile raf = new RandomAccessFile(file, "rw");
				raf.seek(file.length());
				raf.write(data);
				raf.close();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			return file;

		}
	}

	public static class ViewUtils {

		/** 还没有对Invisible进行处理 */
		public static void setVisibility(View view, boolean visible) {
			view.setVisibility(visible ? View.VISIBLE : View.GONE);
		}

		/**
		 * 
		 * @Title: setInputPolicy
		 * @Description: 为EditText设置输入策略
		 * @param editText
		 *            目标输入框
		 * @param inputType
		 *            输入类型
		 * @param accepted
		 *            接收输入的字符，设置了这个就默认了一个KeyListener
		 * @param maxLength
		 *            最大输入字符数，默认设置Filters
		 * @return: void
		 */
		public static void setInputPolicy(EditText editText, int inputType,
				String accepted, int maxLength) {
			if (editText == null)
				return;
			editText.setInputType(inputType);
			editText.setKeyListener(DigitsKeyListener.getInstance(accepted));
			InputFilter[] filters = { new InputFilter.LengthFilter(maxLength) };
			editText.setFilters(filters);
		}
		
		public static int getListViewHeightBasedOnAdapter(ListView listView) {
			ListAdapter listAdapter = listView.getAdapter();
			if (listAdapter == null) {
				// pre-condition
				return -1;
			}

			int totalHeight = 0;
			for (int i = 0; i < listAdapter.getCount(); i++) {
				View listItem = listAdapter.getView(i, null, listView);
				listItem.measure(0, 0);
				totalHeight += listItem.getMeasuredHeight();
			}
			return totalHeight;

		}
		/**
		 * Checks if is list view higher than screen. 判断ListView的高度是否高过屏幕的高度
		 * 
		 * @param listView
		 *            the list view
		 * @return true, if is list view higher than screen
		 */
		public static boolean isListViewHigherThanScreen(Context ctx,
				ListView listView) {

			return DeviceUtils.getScreenSize(ctx)[1] < getListViewHeightBasedOnAdapter(listView);
		}
	}

	public static class TerminalUtils {

		public static final String COMMAND_SU = "su";
		public static final String COMMAND_SH = "sh";
		public static final String COMMAND_EXIT = "exit\n";
		public static final String COMMAND_LINE_END = "\n";

		public static boolean rootCommand(String command) {
			Process process = null;
			boolean isSuc = false;
			try {
				process = exec(COMMAND_SU);
				isSuc = writeCommand(process.getOutputStream(), command);
				process.waitFor();
			} catch (IOException e) {
				e.printStackTrace();
				isSuc = false;
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} finally {
				if (process != null) {
					process.destroy();
				}
			}
			return isSuc;
		}

		public static boolean shellCommand(String command) {
			Process process = null;
			boolean isSuc = false;
			try {
				process = exec(COMMAND_SH);
				isSuc = writeCommand(process.getOutputStream(), command);
			} catch (IOException e) {
				e.printStackTrace();
				isSuc = false;
			} finally {
				if (process != null) {
					process.destroy();
				}
			}
			return isSuc;
		}

		/** 判断手机是否root，不弹出root请求框<br/> */
		public static boolean isDeviceRoot() {
			String binPath = "/system/bin/su";
			String xBinPath = "/system/xbin/su";
			if (new File(binPath).exists() && isExecutable(binPath))
				return true;
			if (new File(xBinPath).exists() && isExecutable(xBinPath))
				return true;
			return false;
		}

		/** 判断/system/bin/su和/system/xbin/su文件是否存在 */
		private static boolean isExecutable(String filePath) {
			Process p = null;
			try {
				p = Runtime.getRuntime().exec("ls -l " + filePath);
				BufferedReader in = new BufferedReader(new InputStreamReader(
						p.getInputStream()));
				String str = in.readLine();
				if (str != null && str.length() >= 4) {
					char flag = str.charAt(3);
					if (flag == 's' || flag == 'x')
						return true;
				}
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				if (p != null) {
					p.destroy();
				}
			}
			return false;
		}

		private static Process exec(String prog) throws IOException {
			return Runtime.getRuntime().exec(prog);
		}

		private static boolean writeCommand(OutputStream out, String command) {
			boolean isSuc = false;
			DataOutputStream os = new DataOutputStream(out);
			try {
				os.writeBytes(command + COMMAND_LINE_END);
				os.writeBytes(COMMAND_EXIT);
				os.flush();
				isSuc = true;
			} catch (IOException e) {
				e.printStackTrace();
				isSuc = false;
			} finally {
				try {
					if (os != null) {
						os.close();
					}
				} catch (Exception e) {
				}
			}
			return isSuc;

		}

		public static boolean silentInstall(String apkPath) throws Exception {
			boolean isPresent = true;
			if (TextUtils.isEmpty(apkPath)) {
				isPresent = false;
				throw new Exception("apk path is empty.");
			}
			File file = new File(apkPath);
			if (file == null || file.length() <= 0) {
				isPresent = false;
				throw new Exception("apk file is null.");
			}
			if (!file.exists()) {
				isPresent = false;
				throw new Exception("apk file is't exists.");
			}
			if (!file.isFile()) {
				isPresent = false;
				throw new Exception("maby apk path is dir not a file.");
			}
			if (isPresent) {// apk文件确实存在就执行root
				StringBuilder command = new StringBuilder();
				command.append("chmod 777 " + apkPath + COMMAND_LINE_END);// 给权限777
				command.append("LD_LIBRARY_PATH=/vendor/lib:/system/lib pm install -r "
						+ apkPath);// pm install -r
				return rootCommand(command.toString());
			}
			return isPresent;
		}

		public static boolean silentUninstall(String apkPackage,
				boolean keepData) throws Exception {
			boolean isPresent = true;
			if (TextUtils.isEmpty(apkPackage)) {
				isPresent = false;
				throw new Exception("apk package name is empty.");
			}
			if (isPresent) {// 包名存在
				StringBuilder command = new StringBuilder();
				command.append(
						"LD_LIBRARY_PATH=/vendor/lib:/system/lib pm uninstall")
						.append(keepData ? " -k " : " ")
						.append(apkPackage.replace(" ", "\\ "));
				return rootCommand(command.toString());
			}
			return isPresent;
		}
	}

	public static class MediaUtils {

		public final static String SYS_RECORD_COMMAND = "/system/bin/screenrecord";

		public static void startRecording(String fileName) {
			final String command = SYS_RECORD_COMMAND + " " + fileName + " ";
			new Thread() {
				@Override
				public void run() {
					TerminalUtils.rootCommand(command);
				};
			}.start();
		}

		public static void stopRecording() {
			/*
			 * CommandResult res = TerminalUtils.rootCommand("ps | grep " +
			 * SYS_RECORD_COMMAND, false); if
			 * (StringUtils.isNotEmpty(res.successMsg)) { List<String> list =
			 * blankSplitter.splitToList(res.successMsg); if (list != null &&
			 * list.size() >= 2) { String pidStr = list.get(1); Log.d("DEBUG",
			 * "pidStr=" + pidStr); res = ShellUtils.execCommand("kill -2 " +
			 * pidStr, true); Log.d("DEBUG", "res:" + res.errorMsg + "|" +
			 * res.successMsg + "|" + res.result); } }
			 */
		}
	}

	/**
	 * The Class ResouceFinder.
	 * 
	 * @ClassName ResouceFinder
	 * @Description 资源查找
	 * @version version
	 * @author Bvin
	 * @update 2013-9-10 下午02:17:41
	 */
	public static class ResourceFinder {

		/**
		 * Find string.
		 * 
		 * @param ctx
		 *            the ctx
		 * @param res
		 *            the res
		 * @return the string
		 */
		public static String findString(Context ctx, int res) {
			String str = null;
			try {
				str = ctx.getResources().getString(res);
			} catch (NotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return str;
		}

		/**
		 * Find color.
		 * 
		 * @param ctx
		 *            the ctx
		 * @param res
		 *            the res
		 * @return the int
		 */
		public static int findColor(Context ctx, int res) {
			int color = 0;
			try {
				color = ctx.getResources().getColor(res);
			} catch (NotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return color;
		}

		/**
		 * 
		 * @Title: findAttrColor
		 * @Description: 寻找attr实际值
		 * @param ctx
		 *            ||不能用getApplicationContext()
		 * @param attr
		 * @return: int
		 */
		public static int findAttrColor(Context ctx, int attr) {
			TypedArray array = ctx.getTheme().obtainStyledAttributes(
					new int[] { attr });
			int backgroundColor = array.getColor(0, 0x000000);
			array.recycle();
			return backgroundColor;
		}

		public static CharSequence findAttrText(Context ctx, int attr) {
			TypedArray array = ctx.getTheme().obtainStyledAttributes(
					new int[] { attr });
			CharSequence str = array.getText(attr);
			array.recycle();
			return str;
		}

		/**
		 * Find dimens.
		 * 
		 * @param ctx
		 *            the ctx
		 * @param res
		 *            the res
		 * @return the float
		 */
		public static float findDimens(Context ctx, int res) {
			float dimens = 0;
			try {
				dimens = ctx.getResources().getDimension(res);
			} catch (NotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return dimens;
		}

		public static Drawable findDrawable(Context ctx, int res) {
			Drawable drawable = null;
			try {
				drawable = ctx.getResources().getDrawable(res);
			} catch (NotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return drawable;
		}

		private static int reverseResourceId(Context ctx, String resourceName,
				String typeName) {
			if (ctx == null) {
				// FIXME 这里请使用 {@link IllegalArgumentException}
				throw new IllegalArgumentException();
			}
			return ctx.getResources().getIdentifier(resourceName, typeName,
					ctx.getApplicationContext().getPackageName());
		}

		public static int getAnimId(Context ctx, String animResourceName) {
			return reverseResourceId(ctx, animResourceName, "anim");
		}

		public static int getArrayId(Context ctx, String arrayResourceName) {
			return reverseResourceId(ctx, arrayResourceName, "array");
		}

		public static int getColorId(Context ctx, String colorResourceName) {
			return reverseResourceId(ctx, colorResourceName, "color");
		}

		public static int getDrawableId(Context ctx, String drawableResourceName) {
			return reverseResourceId(ctx, drawableResourceName, "drawable");
		}

		public static int getDimenId(Context ctx, String dimenResourceName) {
			return reverseResourceId(ctx, dimenResourceName, "dimen");
		}

		public static int getLayoutId(Context ctx, String layoutResourceName) {
			return reverseResourceId(ctx, layoutResourceName, "layout");
		}

		public static int getStringId(Context ctx, String stringResourceName) {
			return reverseResourceId(ctx, stringResourceName, "string");
		}

		public static int getStyleId(Context ctx, String styleResourceName) {
			return reverseResourceId(ctx, styleResourceName, "style");
		}

	}

	public static class EncryptUtils {

		private static final String PASSWORD_SECRET = DES_KEY;

		public static String encrypt(String clearText) {
			try {
				DESKeySpec keySpec = new DESKeySpec(
						PASSWORD_SECRET.getBytes("UTF-8"));
				SecretKeyFactory keyFactory = SecretKeyFactory
						.getInstance("DES");
				SecretKey key = keyFactory.generateSecret(keySpec);

				Cipher cipher = Cipher.getInstance("DES");
				cipher.init(Cipher.ENCRYPT_MODE, key);
				String encrypedPwd = Base64.encodeToString(
						cipher.doFinal(clearText.getBytes("UTF-8")),
						Base64.DEFAULT);
				return encrypedPwd;
			} catch (Exception e) {
			}
			return clearText;
		}

		public static String decrypt(String encryptedPwd) {
			try {
				DESKeySpec keySpec = new DESKeySpec(
						PASSWORD_SECRET.getBytes("UTF-8"));
				SecretKeyFactory keyFactory = SecretKeyFactory
						.getInstance("DES");
				SecretKey key = keyFactory.generateSecret(keySpec);

				byte[] encryptedWithoutB64 = Base64.decode(encryptedPwd,
						Base64.DEFAULT);
				Cipher cipher = Cipher.getInstance("DES");
				cipher.init(Cipher.DECRYPT_MODE, key);
				byte[] plainTextPwdBytes = cipher.doFinal(encryptedWithoutB64);
				return new String(plainTextPwdBytes);
			} catch (Exception e) {
			}
			return encryptedPwd;
		}
	}

	public static class CollectionUtils {

		public static boolean inRange(int index, List list) {

			if (list == null) {
				/*SimpleLogger.log_e("CollectionUtils.inRange(index:" + index
						+ ",count:" + list + ")", "集合为空");*/
				return false;
			} else {
				return inRange(index, list.size());
			}
		}

		public static boolean inRange(int index, int[] array) {

			if (array == null) {
				/*SimpleLogger.log_e("CollectionUtils.inRange(index:" + index
						+ ",count:" + array + ")", "数组为空");*/
				return false;
			} else {
				return inRange(index, array.length);
			}
		}

		private static boolean inRange(int index, int count) {
			// TODO Auto-generated method stub
			if (index < 0) {
				/*SimpleLogger.log_e("CollectionUtils.inRange(index:" + index
						+ ",count:" + count + ")", "索引无效");*/
				return false;
			} else if (index < count) {
				return true;
			}
			return false;
		}

		public static String getElementByIndex(String[] array, int index) {
			if (array != null && array.length > 0) {
				for (int i = 0; i < array.length; i++) {
					if (i == index) {
						return array[i];
					}
				}
			}
			return null;
		}

		/**
		 * 
		 * @Title: isEmpty
		 * @Description: 判断数组是否为空
		 * @param array
		 * @return: boolean
		 */
		public static boolean isEmpty(String[] array) {
			if (array == null)
				return true;
			if (array != null && array.length > 0) {
				for (String string : array) {
					if (StringUtils.hazAvailableContent(string)) {
						return false;
					}
				}
				return true;
			} else {
				return true;
			}
		}

		/**
		 * Put. 过滤空值
		 * 
		 * @param cv
		 *            the cv
		 * @param key
		 *            the key
		 * @param Value
		 *            the value
		 * @return the content values
		 */
		public static ContentValues putString(ContentValues cv, String key,
				String Value) {
			if (cv == null) {
				cv = new ContentValues();
			}
			if (StringUtils.hazAvailableContent(Value)) {// 一定value不为空才put
				cv.put(key, Value);
			}
			return cv;
		}

		/**
		 * Put encrypt string. 同{@link putString}，加密数据库字段
		 * 
		 * @param cv
		 *            the cv
		 * @param key
		 *            the key
		 * @param Value
		 *            the value
		 * @return the content values
		 */
		public static ContentValues putEncryptString(ContentValues cv,
				String key, String Value) {
			if (cv == null) {
				cv = new ContentValues();
			}
			if (!TextUtils.isEmpty(Value)) {// 恰大亏了
				cv.put(key, EncryptUtils.encrypt(Value));
			}
			return cv;
		}

		public static class BroadcastUtils {

			public static void registerReceiver(Context ctx,
					BroadcastReceiver receiver, IntentFilter filter) {
				ctx.registerReceiver(receiver, filter);
			}

			public static void unRegisterReceiver(Context ctx,
					BroadcastReceiver receiver) {
				try {
					ctx.unregisterReceiver(receiver);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}

		public static class OperationUtils {
			private static long lastClickTime;
			private static long span = 500;
			private static long exitSpan = 1200;

			/**
			 * Checks if is frequently operation. 判断操作是否过快
			 * 
			 * @return true, if is frequently operation
			 */
			public static boolean isFrequentlyOperation() {
				long time = System.currentTimeMillis();
				if (time - lastClickTime < span) {
					return true;
				}
				lastClickTime = time;
				return false;
			}

			public static boolean isNeedExit() {
				long time = System.currentTimeMillis();
				if (time - lastClickTime < exitSpan) {
					return true;
				}
				lastClickTime = time;
				return false;
			}

			public static void cancleKeyBoardIfNecessary(Activity activity) {
				if (activity == null)
					return;
				View view = activity.getWindow().peekDecorView();
				if (view != null) {
					InputMethodManager inputmanger = (InputMethodManager) activity
							.getSystemService(Context.INPUT_METHOD_SERVICE);
					if (inputmanger.isActive()) {
						inputmanger.hideSoftInputFromWindow(
								view.getWindowToken(), 0);
					}
				}
			}

			/**
			 * @param ctx
			 * @param editText
			 * @description 隐藏软键盘
			 */
			public static void hideSoftInput(Context ctx, View v) {
				InputMethodManager imm = (InputMethodManager) ctx
						.getSystemService(Context.INPUT_METHOD_SERVICE);
				imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
			}

			/**
			 * @Title: exec
			 * @Description: linux 终端
			 * @param @param args
			 * @param @return
			 * @return String
			 * @throws
			 */
			public static String exec(String[] args) {
				String result = "";
				ProcessBuilder processBuilder = new ProcessBuilder(args);
				Process process = null;
				InputStream errIs = null;
				InputStream inIs = null;
				try {
					ByteArrayOutputStream baos = new ByteArrayOutputStream();
					int read = -1;
					process = processBuilder.start();
					errIs = process.getErrorStream();
					while ((read = errIs.read()) != -1) {
						baos.write(read);
					}
					baos.write('n');
					inIs = process.getInputStream();
					while ((read = inIs.read()) != -1) {
						baos.write(read);
					}
					byte[] data = baos.toByteArray();
					result = new String(data);
				} catch (IOException e) {
					e.printStackTrace();
				} catch (Exception e) {
					e.printStackTrace();
				} finally {
					try {
						if (errIs != null) {
							errIs.close();
						}
						if (inIs != null) {
							inIs.close();
						}
					} catch (IOException e) {
						e.printStackTrace();
					}
					if (process != null) {
						process.destroy();
					}
				}
				return result;
			}

			@SuppressWarnings("deprecation")
			@SuppressLint("NewApi")
			public static void copyToClipboard(Context context, String content) {
				if (VersionUtils.isAPICompatible(11)) {
					android.content.ClipboardManager clipboardManager = (android.content.ClipboardManager) context
							.getSystemService(Context.CLIPBOARD_SERVICE);
					clipboardManager.setPrimaryClip(ClipData.newPlainText(
							"Label", content.trim()));
				} else {
					ClipboardManager cmb = (ClipboardManager) context
							.getSystemService(Context.CLIPBOARD_SERVICE);
					cmb.setText(content.trim());
				}

			}
		}

	}
	/**
	 * The Class AnimUtils. 动画工具类
	 */
	public static class AnimUtils {

		
	}
	
	public static class VersionUtils {
		/**
		 * Checks if is API compatible. 判断请求的APIlevel是否兼容
		 * 
		 * @param requiresAPILevel
		 *            the requires api level
		 * @return true, if is aPI compatible
		 */
		public static boolean isAPICompatible(int requiresAPILevel) {
			return Build.VERSION.SDK_INT > requiresAPILevel ? true : false;
		}

		/**
		 * Gets the system version.
		 * 
		 * @return the system version
		 */
		public static String getSystemVersion() {
			String osVersion = Build.VERSION.RELEASE;
			if (!TextUtils.isEmpty(osVersion)) {
				return osVersion;
			}
			return "此设备无版本信息";

		}

		/**
		 * 获取当前版本号
		 * 
		 * @param mContext
		 * @return
		 */
		public static int getCurrentVersionCode(Context mContext) {
			try {
				return mContext.getPackageManager().getPackageInfo(
						mContext.getPackageName(), 0).versionCode;
			} catch (NameNotFoundException e) {
				return 0;
			}
		}
	}
}
