package com.example.chiachen.opengl_demo;

import android.opengl.GLES20;
import android.opengl.Matrix;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

/**
 * Created by chiachen on 2017/2/19.
 */

public class Triangle {
	public static float[] mProjectMatrix = new float[16];//4x4 投射矩陣
	public static float[] mVMatrix = new float[16];
	public static float[] mMVPMatrix = new float[16];
	int mProgram;
	int muMVPMatrixHandle;
	int maPositionHandle;
	int maColorHandle;
	String mVertexShader;
	String mFragmentShader;
	static float mMMatrix[] = new float[16];//具體物體的d變換矩陣（旋轉、平移、縮放

	FloatBuffer mVertexBuffer;
	FloatBuffer mColorBuffer;

	int vCount = 0;//頂點數量
	float xAngle = 0;//繞x軸的角度

	public Triangle(MyView view) {
		initVertexData();
		initShader(view);
	}

	public void initVertexData() {
		vCount = 3;
		final float UNIT_SIZE = 0.2f;

		float vertices[] = new float[]{//座標組
				-4 * UNIT_SIZE, 0, 0, 0, -4 * UNIT_SIZE, 0, 4 * UNIT_SIZE, 0, 0
		};
		ByteBuffer vbb = ByteBuffer.allocateDirect(vertices.length * 4);
		vbb.order(ByteOrder.nativeOrder());
		mVertexBuffer = vbb.asFloatBuffer();
		mVertexBuffer.put(vertices);
		mVertexBuffer.position(0);


		float colors[] = new float[]{//座標組
				1, 1, 1, 0, 0, 0, 1, 0, 0, 1, 0, 0
		};
		ByteBuffer cbb = ByteBuffer.allocateDirect(colors.length * 4);
		cbb.order(ByteOrder.nativeOrder());
		mColorBuffer = cbb.asFloatBuffer();
		mColorBuffer.put(colors);
		mColorBuffer.position(0);//設置緩衝區的起始位置

	}

	public static float[] getFinalMatrix(float spec[]) {
		mMVPMatrix = new float[16];
		Matrix.multiplyMM(mMVPMatrix, 0, mVMatrix, 0, spec, 0);
		Matrix.multiplyMM(mMVPMatrix, 0, mProjectMatrix, 0, mMVPMatrix, 0);
		return mMVPMatrix;
	}

	public void initShader(MyView view){
		mVertexShader = ShaderUtil.loadFromAssetsFile(view.getContext(),R.raw.vertex );//
		mFragmentShader = ShaderUtil.loadFromAssetsFile(view.getContext(), R.raw.frag);//
		mProgram = ShaderUtil.createProgram(mVertexShader, mFragmentShader);
		maPositionHandle = GLES20.glGetAttribLocation(mProgram, "aPosition");
		maColorHandle = GLES20.glGetAttribLocation(mProgram, "aColor");
		muMVPMatrixHandle = GLES20.glGetUniformLocation(mProgram, "uMVPMatrix");
	}

	public void drawSelf(){
		GLES20.glUseProgram(mProgram);
		Matrix.setRotateM(mMMatrix, 0, 0, 0, 1, 0);//初始化變換矩陣
		Matrix.translateM(mMMatrix, 0, 0, 0, 1);//設置延Ｚ軸正向位移
		Matrix.rotateM(mMMatrix, 0, xAngle, 1, 0, 0);//設置繞Ｘ軸旋轉

		GLES20.glUniformMatrix4fv(muMVPMatrixHandle, 1, false, Triangle.getFinalMatrix(mMMatrix), 0);

		GLES20.glVertexAttribPointer(maPositionHandle, 3, GLES20.GL_FLOAT, false, 3 * 4, mVertexBuffer);//
		GLES20.glVertexAttribPointer(maColorHandle, 4, GLES20.GL_FLOAT, false, 4 * 4, mColorBuffer);//

		GLES20.glEnableVertexAttribArray(maPositionHandle);//使用頂點位置資料
		GLES20.glEnableVertexAttribArray(maColorHandle);//使用頂點著色資料
		GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, vCount);//執行

	}
}
