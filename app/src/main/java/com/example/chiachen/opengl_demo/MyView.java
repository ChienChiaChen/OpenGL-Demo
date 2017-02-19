package com.example.chiachen.opengl_demo;

import android.content.Context;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 * Created by chiachen on 2017/2/19.
 */

public class MyView extends GLSurfaceView{
	final float ANGLE_SPAN = 0.375f;
	RotateThread rotateThread;
	SceneRender mSceneRender;

	public MyView(Context context) {
		super(context);
		this.setEGLContextClientVersion(2);
		mSceneRender = new SceneRender();
		this.setRenderer(mSceneRender);
		this.setRenderMode(GLSurfaceView.RENDERMODE_CONTINUOUSLY);//不間斷
	}

	private class SceneRender implements GLSurfaceView.Renderer {
		Triangle triangle;
		@Override
		public void onSurfaceCreated(GL10 gl, EGLConfig config) {
			GLES20.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);//red green blue alpha
			triangle = new Triangle(MyView.this);//
			GLES20.glEnable(GLES20.GL_DEPTH_TEST);//
			rotateThread = new RotateThread();//
			rotateThread.start();//
		}

		@Override
		public void onSurfaceChanged(GL10 gl, int width, int height) {
			GLES20.glViewport(0, 0, width, height);//创建窗口，第一二个参数为窗口坐标，三四参数宽高
			float ratio = (float) width / height;//銀幕寬高比例
			Matrix.frustumM(Triangle.mProjectMatrix, 0, -ratio, ratio, -1, 1, 1, 10);//設定透視投影
			Matrix.setLookAtM(Triangle.mVMatrix, 0, 0, 0, 3, 0f, 0f, 0f, 0f, 1.0f, 0.0f);//設定攝影機
		}

		@Override
		public void onDrawFrame(GL10 gl) {
			GLES20.glClear(GLES20.GL_DEPTH_BUFFER_BIT | GLES20.GL_COLOR_BUFFER_BIT);
			triangle.drawSelf();
		}
	}

	public class RotateThread extends Thread {
		private boolean flag = true;

		@Override
		public void run() {
			while (flag) {
				mSceneRender.triangle.xAngle = mSceneRender.triangle.xAngle + ANGLE_SPAN;
				try {
					Thread.sleep(20);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}
}
