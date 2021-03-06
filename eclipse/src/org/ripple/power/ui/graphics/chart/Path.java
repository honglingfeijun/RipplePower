package org.ripple.power.ui.graphics.chart;

import java.awt.geom.Path2D;

class Path {
	private static int getWindingRule(FillRule fillRule) {
		switch (fillRule) {
		case EVEN_ODD:
			return Path2D.WIND_EVEN_ODD;
		case NON_ZERO:
			return Path2D.WIND_NON_ZERO;
		}

		throw new IllegalArgumentException("unknown fill rule:" + fillRule);
	}

	Path2D path2D;

	public Path() {
		this.path2D = new Path2D.Float();
	}

	public Path(Path p) {
		this.path2D = p.path2D;
	}

	public void close() {
		this.path2D.closePath();
	}

	public void clear() {
		this.path2D.reset();
	}

	public void reset() {
		this.path2D.reset();
	}

	public void lineTo(float x, float y) {
		this.path2D.lineTo(x, y);
	}

	public void moveTo(float x, float y) {
		this.path2D.moveTo(x, y);
	}

	public void setFillRule(FillRule fillRule) {
		this.path2D.setWindingRule(getWindingRule(fillRule));
	}
}
