package engine.models;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;

import org.joml.Vector2f;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL14;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.opengl.GL32;

public class FrameBuffer {

	private static final int SAMPLES = 4;

	public enum DepthType {
		NONE, DEPTH_TEXTURE, DEPTH_RENDER_BUFFER
	}

	private static final List<FrameBuffer> s_FBOs = new ArrayList<>();

	public static void resizeAll(Vector2f resolution) {
		for (FrameBuffer fbo : s_FBOs) {
			fbo.resize(resolution);
		}
	}

	public static void unload() {
		for (FrameBuffer fbo : s_FBOs)
			fbo.cleanUp();
	}

	private Texture m_Texture1;
	private Texture m_Texture2;
	private Texture m_DepthTexture;
	private int m_FrameBuffer;
	private DepthType m_DepthType;

	private int m_Width;
	private int m_Height;
	/** multi-sample and multi-target */
	private boolean m_MSMT;
	private float m_Scale;

	public FrameBuffer(Vector2f resolution, DepthType depthType, boolean msmt) {
		s_FBOs.add(this);
		m_Width = (int) resolution.x;
		m_Height = (int) resolution.y;
		m_Scale = 1;
		m_DepthType = depthType;
		m_MSMT = msmt;
		create();
	}

	public FrameBuffer(Vector2f resolution, float scale, DepthType depthType) {
		m_Width = (int) (resolution.x * scale);
		m_Height = (int) (resolution.y * scale);
		m_Scale = scale;
		m_DepthType = depthType;
		m_MSMT = false;
		create();
	}

	public void resize(Vector2f resolution) {
		cleanUp();
		m_Width = (int) (resolution.x * m_Scale);
		m_Height = (int) (resolution.y * m_Scale);
		create();
	}

	private void create() {
		m_FrameBuffer = cFrameBuffer();
		if (m_MSMT) {
			m_Texture1 = cMSTextureAttachment(GL30.GL_COLOR_ATTACHMENT0);
			m_Texture2 = cMSTextureAttachment(GL30.GL_COLOR_ATTACHMENT1);
		} else
			m_Texture1 = cTextureAttachment();
		switch (m_DepthType) {
		case DEPTH_RENDER_BUFFER:
			m_DepthTexture = cDepthBufferAttachment();
			break;
		case DEPTH_TEXTURE:
			m_DepthTexture = cDepthTextureAttachment();
			break;
		case NONE:
			break;
		}
		unbind();
	}

	public void resolve(int readBuffer, FrameBuffer resolveTo) {
		GL30.glBindFramebuffer(GL30.GL_DRAW_FRAMEBUFFER, resolveTo.m_FrameBuffer);
		GL30.glBindFramebuffer(GL30.GL_READ_FRAMEBUFFER, m_FrameBuffer);
		GL11.glReadBuffer(readBuffer);
		GL30.glBlitFramebuffer(0, 0, m_Width, m_Height, 0, 0, resolveTo.width(), resolveTo.height(),
				GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT, GL11.GL_NEAREST);
		unbind();
	}

	public void resolve(FrameBuffer resolveTo) {
		GL30.glBindFramebuffer(GL30.GL_DRAW_FRAMEBUFFER, resolveTo.m_FrameBuffer);
		GL30.glBindFramebuffer(GL30.GL_READ_FRAMEBUFFER, m_FrameBuffer);
		GL30.glBlitFramebuffer(0, 0, m_Width, m_Height, 0, 0, resolveTo.width(), resolveTo.height(),
				GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT, GL11.GL_NEAREST);
		unbind();
	}

	public void resolve(Vector2f resolution) {
		GL30.glBindFramebuffer(GL30.GL_DRAW_FRAMEBUFFER, 0); // default
		GL30.glBindFramebuffer(GL30.GL_READ_FRAMEBUFFER, m_FrameBuffer);
		GL11.glDrawBuffer(GL11.GL_BACK);
		GL30.glBlitFramebuffer(0, 0, m_Width, m_Height, 0, 0, (int) resolution.x, (int) resolution.y,
				GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT, GL11.GL_NEAREST);
		unbind();
	}

	public void bind() {
		bindFrameBuffer(m_FrameBuffer);
	}

	public void unbind() {
		int status = GL30.glCheckFramebufferStatus(GL30.GL_FRAMEBUFFER);
		if (status != GL30.GL_FRAMEBUFFER_COMPLETE) {
			System.out.println(status);
		}
//		BerylGL.glErrorHandling();
		GL30.glBindFramebuffer(GL30.GL_FRAMEBUFFER, 0);
//		BerylGL.resetViewport();
	}

	private void bindFrameBuffer(int frameBuffer) {
		GL30.glBindFramebuffer(GL30.GL_FRAMEBUFFER, frameBuffer);
		GL11.glViewport(0, 0, m_Width, m_Height);
	}

	private int cFrameBuffer() {
		int buffer = GL30.glGenFramebuffers();
		GL30.glBindFramebuffer(GL30.GL_FRAMEBUFFER, buffer);
		determineDrawBuffers();
		return buffer;
	}

	public void determineDrawBuffers() {
		IntBuffer buffer = BufferUtils.createIntBuffer(2);
		buffer.put(GL30.GL_COLOR_ATTACHMENT0);
		if (m_MSMT)
			buffer.put(GL30.GL_COLOR_ATTACHMENT1);
		buffer.flip();
		GL20.glDrawBuffers(buffer);
	}

	private Texture cTextureAttachment() {
		int texture = GL11.glGenTextures();
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, texture);
		GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGB, m_Width, m_Height, 0, GL11.GL_RGB, GL11.GL_UNSIGNED_BYTE,
				(ByteBuffer) null);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR);
		GL32.glFramebufferTexture(GL30.GL_FRAMEBUFFER, GL30.GL_COLOR_ATTACHMENT0, texture, 0);
		return new Texture("FBO" + texture, texture);
	}

	private Texture cMSTextureAttachment(int attachment) {
		int buffer = GL30.glGenRenderbuffers();
		GL30.glBindRenderbuffer(GL30.GL_RENDERBUFFER, buffer);
		GL30.glRenderbufferStorageMultisample(GL30.GL_RENDERBUFFER, SAMPLES, GL11.GL_RGBA8, m_Width, m_Height);
		GL30.glFramebufferRenderbuffer(GL30.GL_FRAMEBUFFER, attachment, GL30.GL_RENDERBUFFER, buffer);
		return new Texture("msTexture" + buffer, buffer);
	}

	private Texture cDepthTextureAttachment() {
		int texture = GL11.glGenTextures();
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, texture);
		GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL14.GL_DEPTH_COMPONENT32, m_Width, m_Height, 0,
				GL11.GL_DEPTH_COMPONENT, GL11.GL_FLOAT, (ByteBuffer) null);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR);
		GL32.glFramebufferTexture(GL30.GL_FRAMEBUFFER, GL30.GL_DEPTH_ATTACHMENT, texture, 0);
		return new Texture("dTexture" + texture, texture);
	}

	private Texture cDepthBufferAttachment() {
		int depthBuffer = GL30.glGenRenderbuffers();
		GL30.glBindRenderbuffer(GL30.GL_RENDERBUFFER, depthBuffer);
		if (m_MSMT)
			GL30.glRenderbufferStorageMultisample(GL30.GL_RENDERBUFFER, SAMPLES, GL11.GL_DEPTH_COMPONENT, m_Width,
					m_Height);
		else
			GL30.glRenderbufferStorage(GL30.GL_RENDERBUFFER, GL11.GL_DEPTH_COMPONENT, m_Width, m_Height);
		GL30.glFramebufferRenderbuffer(GL30.GL_FRAMEBUFFER, GL30.GL_DEPTH_ATTACHMENT, GL30.GL_RENDERBUFFER,
				depthBuffer);
		return new Texture("dBuffer" + depthBuffer, depthBuffer);
	}

	public void cleanUp() {// call when closing the game
		GL30.glDeleteFramebuffers(m_FrameBuffer);
		GL11.glDeleteTextures(m_Texture1.textureID());
		if (m_MSMT)
			GL11.glDeleteTextures(m_Texture2.textureID());
		switch (m_DepthType) {
		case DEPTH_RENDER_BUFFER:
			GL30.glDeleteRenderbuffers(m_DepthTexture.textureID());
			break;
		case DEPTH_TEXTURE:
			GL30.glDeleteTextures(m_DepthTexture.textureID());
			break;
		case NONE:
			break;
		}
	}

	/**
	 * @return m_Texture1
	 */
	public Texture texture1() {
		return m_Texture1;
	}

	/**
	 * @param texture1 m_Texture1 to set
	 */
	public void texture1(Texture texture1) {
		this.m_Texture1 = texture1;
	}

	/**
	 * @return m_Texture2
	 */
	public Texture texture2() {
		return m_Texture2;
	}

	/**
	 * @param texture2 m_Texture2 to set
	 */
	public void texture2(Texture texture2) {
		this.m_Texture2 = texture2;
	}

	/**
	 * @return m_DepthTexture
	 */
	public Texture depthTexture() {
		return m_DepthTexture;
	}

	/**
	 * @param depthTexture m_DepthTexture to set
	 */
	public void depthTexture(Texture depthTexture) {
		this.m_DepthTexture = depthTexture;
	}

	/**
	 * @return m_Width
	 */
	public int width() {
		return m_Width;
	}

	/**
	 * @param width m_Width to set
	 */
	public void width(int width) {
		this.m_Width = width;
	}

	/**
	 * @return m_Height
	 */
	public int height() {
		return m_Height;
	}

	/**
	 * @param height m_Height to set
	 */
	public void height(int height) {
		this.m_Height = height;
	}

}
