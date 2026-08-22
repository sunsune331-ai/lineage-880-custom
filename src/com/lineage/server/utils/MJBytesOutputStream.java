package com.lineage.server.utils;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import com.lineage.config.Config;

public class MJBytesOutputStream extends OutputStream{
    private static final String CLIENT_LANGUAGE_CODE = Config.CLIENT_LANGUAGE_CODE;
	private byte[] 	_buf;		// ??
	private int		_idx;		// ?? ??? ???? ?? ???
	private int		_capacity;	// ??? ?? / ??? ??
	private boolean _isClosed;	// ???? ?????
	private boolean	_isShared;	// ???? ???? ??????
	
	public MJBytesOutputStream(){
		this(4096);
	}
	
	public MJBytesOutputStream(int capacity){
		_isShared	= false;
		_isClosed	= false;
		_capacity 	= capacity;
		_buf		= new byte[_capacity];
	}
	
	/** ???? ??? ?????. **/
	private void realloc(int capacity){
		_capacity 	= capacity;
		byte[] tmp 	= new byte[_capacity];
		System.arraycopy(_buf, 0, tmp, 0, _idx);
		_buf 		= tmp;
		_isShared 	= false;
	}
	
	/** ???? ??. **/
	@Override
	public void write(int i) throws IOException {
		if(_isClosed)
			throw new IOException("BytesOutputStream Closed...");
		
		if(_idx >= _capacity)
			realloc(_capacity*2+1);
		
		_buf[_idx++] = (byte)(i & 0xff);
	}
	
	/** ???? ??. **/
	public void write(byte[] data, int offset, int length) throws IOException{
		if(data == null)
			throw new NullPointerException();
		
		if(offset < 0 || offset + length > data.length || length < 0)
			throw new IndexOutOfBoundsException();
		
		if(_isClosed)
			throw new IOException("BytesOutputStream Closed...");
		
		int capacity = _capacity;
		while(_idx + length > capacity)
			capacity = capacity*2+1;
		if(capacity > _capacity)
			realloc(capacity);
		
		System.arraycopy(data, offset, _buf, _idx, length);
		_idx += length;
	}
	
	/** short?(2byte) ???? ??. **/
	public void writeH(int i) throws IOException{
		write(i 		& 0xFF);
	    write(i >> 8 	& 0xFF);
	}
	
	/** int?(4byte) ???? ??. **/
	public void writeD(int i) throws IOException{
		write(i 		& 0xFF);
	    write(i >> 8 	& 0xFF);
	    write(i >> 16 	& 0xFF);
	    write(i >> 24 	& 0xFF);
	}
	
	public void writeBit(long value) throws Exception
	{
		if (value < 0L) {
			String str = Integer.toBinaryString((int)value);
			value = Long.valueOf(str, 2).longValue();
		}
		int i = 0;
		while (value >> 7 * (i + 1) > 0L)
			write((int)((value >> 7 * i++) % 128L | 0x80));
		write((int)((value >> 7 * i) % 128L));
	}
	
	public void writeS(String text) throws IOException{
		writeS(text, CLIENT_LANGUAGE_CODE);
	}
	
	public void writeS(String text, String encoding) throws IOException{
		if(text != null){
			byte[] b = text.getBytes(encoding);
			write(b, 0, b.length);
		}
		write(0);
	}
	
	public void writeSForMultiBytes(String text) throws IOException{
		writeSForMultiBytes(text, CLIENT_LANGUAGE_CODE);
	}
	
	public void writeSForMultiBytes(String text, String encoding) throws IOException{
		if(text != null){
			byte[] b = text.getBytes(encoding);
			int i = 0;
			while(i < b.length){
				if((b[i] & 0xff) >= 0x7f){
					write(b[i + 1]);
					write(b[i]);
					i += 2;
				}else{
					write(b[i]);
					write(0);
					i += 1;
				}
			}
		}
		write(0);
		write(0);
	}
	
	/** ??? outputStream? ??. **/
	public void writeTo(OutputStream out) throws IOException{
		out.write(_buf, 0, _idx);
	}
	
	/** InputStream?? ???. **/
	public InputStream toInputStream(){
		_isShared = true;
		return new MJBytesInputStream(_buf, 0, _idx);
	}
	
	/** ??? **/
	public void reset() throws IOException{
		if(_isClosed)
			_isClosed = false;
		
		if(_isShared){
			_buf = new byte[_capacity];
			_isShared = false;
		}
		
		_idx = 0;
	}
	
	/** ???? ???. **/
	public void close(){
		_isClosed = true;
	}
	
	/** ???? ??? ??? ????. **/
	public byte[] toArray(){
		byte[] result = new byte[_idx];
		System.arraycopy(_buf, 0, result, 0, _idx);
		return result;
	}
}

