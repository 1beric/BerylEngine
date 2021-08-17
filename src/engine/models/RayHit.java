package engine.models;

import org.joml.Vector3f;

import engine.components.renderable.Collider;
import engine.entities.Entity;
import engine.util.string.StringTools;
import engine.util.string.Stringable;

public class RayHit implements Stringable {

	private float m_Distance;
	private Vector3f m_Point;
	private Vector3f m_Origin;
	private Vector3f m_Direction;
	private Vector3f m_Normal;
	private Entity m_Hit;
	
	public RayHit(Entity hit, float dist, Vector3f p, Vector3f o, Vector3f dir, Vector3f n) {
		hit(hit);
		distance(dist);
		point(p);
		origin(o);
		direction(dir);
		normal(n);
	}

	/**
	 * @return m_Distance
	 */
	public float distance() {
		return m_Distance;
	}


	/**
	 * @param m_Distance m_Distance to set
	 */
	public void distance(float m_Distance) {
		this.m_Distance = m_Distance;
	}


	/**
	 * @return m_Origin
	 */
	public Vector3f origin() {
		return m_Origin;
	}


	/**
	 * @param m_Origin m_Origin to set
	 */
	public void origin(Vector3f m_Origin) {
		this.m_Origin = m_Origin;
	}


	/**
	 * @return m_Direction
	 */
	public Vector3f direction() {
		return m_Direction;
	}


	/**
	 * @param m_Direction m_Direction to set
	 */
	public void direction(Vector3f m_Direction) {
		this.m_Direction = m_Direction;
	}


	/**
	 * @return m_Normal
	 */
	public Vector3f normal() {
		return m_Normal;
	}


	/**
	 * @param m_Normal m_Normal to set
	 */
	public void normal(Vector3f m_Normal) {
		this.m_Normal = m_Normal;
	}

	/**
	 * @return m_Point
	 */
	public Vector3f point() {
		return m_Point;
	}

	/**
	 * @param m_Point m_Point to set
	 */
	public void point(Vector3f m_Point) {
		this.m_Point = m_Point;
	}

	/**
	 * @return m_Hit
	 */
	public Entity hit() {
		return m_Hit;
	}

	/**
	 * @param m_Hit m_Hit to set
	 */
	public void hit(Entity m_Hit) {
		this.m_Hit = m_Hit;
	}

	
	
	@Override
	public String string(int indentAmt) {
		return StringTools.buildString(
				StringTools.indent(indentAmt), "RayHit: {",
				
				
				StringTools.indentl(indentAmt), "}"
				);
	}

	@Override
	public String toString() {
		return string(0);
	}

}
