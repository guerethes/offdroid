/**
 * I thank GOD for the insatiable desire to acquire knowledge that was given to
 * me. The search for knowledge must be one of our main purposes as human beings.
 * I sincerely hope that this simple tool is in any way useful to the community
 * in general.
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy 
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 * 
 */
package br.com.guerethes.orm.util.log.i;

import br.com.guerethes.orm.util.log.i.ILog;

/**
 * @author <a
 *         href="mailto:carlostimoshenkorodrigueslopes@gmail.com">Timoshenko</
 *         a>.
 * @version $Revision: 0.0.0.1 $
 */
public class Log implements ILog {

	private static final String PACKAGE_LOG_L = "br.com.guerethes.orm.util.log.i.Log";

	private static ILog iLog;

	public Log(int type) {

		try {
			Class<?> classLog = Class.forName(PACKAGE_LOG_L + type);
			iLog = (ILog) classLog.newInstance();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see br.com.softctrl.h4android.orm.util.log.i.ILog#d(java.lang.String,
	 * java.lang.String)
	 */
	@Override
	public int d(String tag, String msg) {
		return iLog.d(tag, msg);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see br.com.softctrl.h4android.orm.util.log.i.ILog#e(java.lang.String,
	 * java.lang.String)
	 */
	@Override
	public int e(String tag, String msg) {
		return iLog.e(tag, msg);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see br.com.softctrl.h4android.orm.util.log.i.ILog#i(java.lang.String,
	 * java.lang.String)
	 */
	@Override
	public int i(String tag, String msg) {
		return iLog.i(tag, msg);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see br.com.softctrl.h4android.orm.util.log.i.ILog#v(java.lang.String,
	 * java.lang.String)
	 */
	@Override
	public int v(String tag, String msg) {
		return iLog.v(tag, msg);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see br.com.softctrl.h4android.orm.util.log.i.ILog#wtf(java.lang.String,
	 * java.lang.String)
	 */
	@Override
	public int wtf(String tag, String msg) {
		return iLog.wtf(tag, msg);
	}

}
