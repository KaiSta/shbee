package examples.bufwriter;

/**
 * Title:
 * Description:
 * Copyright:    Copyright (c) 2003
 * Company:
 * @author
 * @version 1.0
 */

public class Writer extends Thread  {
  private int _writeVal;
  private int _writeCount;
  private static Buffer _buf;
  private static boolean flag = true;
  public void stopWriter(){
	  flag = false;
  }
  public Writer(Buffer buf,int val) {
    _writeVal = val;
    _writeCount = 0;
    _buf = buf;
  }

  public void run()
  {
    while (flag)
    {
      if (_buf._pos >= _buf._size) continue;
      _buf._array[_buf._pos] = _writeVal;
      synchronized(_buf)
      {
        _buf._count += 1;
      }
    }
  }

  public int getCounter ()
  {
    return _writeCount;
  }
}